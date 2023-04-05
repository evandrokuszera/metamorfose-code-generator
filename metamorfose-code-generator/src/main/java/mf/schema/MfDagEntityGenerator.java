/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.schema;

import mf.schema.model.MfEntity;
import dag.model.RelationshipEdge;
import dag.model.TableColumnVertex;
import dag.model.TableVertex;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.customization.MfClassCustomization;
import mf.generator.MfCodeGenerator;
import mf.generator.RdbToJavaDatatypeDictionary;
import mf.generator.RdbTypeEnum;
import static mf.generator.MfCodeGenerator.getStringWithFirstCapitalLetter;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 *
 * @author evand
 */
public class MfDagEntityGenerator {
    private MfEntity entity;
    private MfClassCustomization classODMCustomization;
    private RdbToJavaDatatypeDictionary typeDictionary;
    private DirectedAcyclicGraph<TableVertex, RelationshipEdge> graphEntity;
    
    public MfDagEntityGenerator(
            DirectedAcyclicGraph<TableVertex, RelationshipEdge> graphEntity, 
            MfClassCustomization classODMCustomization, 
            RdbTypeEnum typeRDBDictionary) {
        
        this.graphEntity = graphEntity;
        this.classODMCustomization = classODMCustomization;
        this.typeDictionary = new RdbToJavaDatatypeDictionary(typeRDBDictionary);
    }

    public MfEntity getMfEntity() {
        return entity;
    }
    
    public void generate(String packageBaseName) {
        
        if (graphEntity == null){
            throw new NullPointerException("MfDagEntityGenerator: field 'graphEntity' is null. The attribute cannot be null.");
        }
        
        this.entity = new MfEntity( getStringWithFirstCapitalLetter( getRootVertex(graphEntity).getName() ) );
        
        // Clone the graph (DAG) 'graphEntity' to process each path.
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> cloneGraph = (DirectedAcyclicGraph<TableVertex, RelationshipEdge>) graphEntity.clone();
        
        // While 'cloneGraph' still has one or more vertices, the loop converts each Vertex to ClassMetadata object.
        while (cloneGraph.vertexSet().size() > 0) {
            // Get the leaf vertices set (leafVertexs)
            ArrayList<TableVertex> leafVertexs = getVertexsWithZeroInDegree(cloneGraph);
            
            // for each leafVertex create a ClassMetadata 
            for (TableVertex leafVertex : leafVertexs) {
                
                ClassMetadata classMetadata = new ClassMetadata();
                classMetadata.setId(leafVertex.getId());
                classMetadata.setPackageName(packageBaseName); // deve ser setado externamente
                classMetadata.setName( getStringWithFirstCapitalLetter( leafVertex.getTableName() ) );
                
                // Read each field from leafVertex and create a ClassField of classMetadata
                for (TableColumnVertex column : leafVertex.getTypeFields()){
                    classMetadata.getFields().add(
                            new ClassField(
                                    "private", 
                                    typeDictionary.getJavaDataType(column.getColumnType()), 
                                    column.getColumnName(), 
                                    ClassRelationshipFieldType.NONE,
                                    column.isPk(),
                                    column.isFk()
                            )
                    );
                }
                
                addNestingRelationshipFieldsToClass(classMetadata, graphEntity);
                
                this.entity.addClassMetadata(classMetadata);
                
                // Remove leafVertex that was processed (reducing one vertex from the cloneGraph).            
                cloneGraph.removeVertex(leafVertex);
            }
            
        }
        
        // If there is a customization object, then we apply the customization for all the class metadata objects.
        if (this.classODMCustomization != null){
            this.classODMCustomization.applyCustomizationsTo( this.entity );
        }
        //System.out.println("MfDagEntityGenerator.generate(): OK");
    }

    public void saveFiles() throws FileNotFoundException{
        for (ClassMetadata clazz : this.entity.getClassMetadataList()) {
            String classCode = MfCodeGenerator.createPojoCodeFrom(clazz);
            MfCodeGenerator.saveCodeToFile(clazz.getPackageName(), clazz.getName(), classCode);
        }
    }
    
    
    
    //########################################################################################################################################
    // MÉTODOS PRIVADOS DE APOIO.
    //########################################################################################################################################
    
    // Recebe um objeto classMetadata, encontra o vertex correspondente a ele no grafo e adiciona atributos de relacionamento 1:1 ou 1:N,
    //  com base nos predecessores do vertex. Relacionamento 1:1 são objetos embutidos e 1:N são listas de objetos embutidos.
    private void addNestingRelationshipFieldsToClass(ClassMetadata classMetadata, DirectedAcyclicGraph<TableVertex, RelationshipEdge> graph){
        TableVertex currentVertex = null;

        // Localizando o vertex relativo ao objeto classMetadata (comparação pelo nome da tabela).
        for (TableVertex tableVertex : graph.vertexSet()){
            if (tableVertex.getTableName().equalsIgnoreCase(classMetadata.getName())){
                currentVertex = tableVertex;
                break;
            }
        }

        // Recupera a lista de predecessores de currentVertex.
        //  Os predecessores correspondem com objetos ClassMetadata já gerados, de acordo com a lógica usada para caminhar no grafo.
        List<TableVertex> predecessorList = Graphs.predecessorListOf(graph, currentVertex);
        // Para cada predecessor, adicionar uma relacionamento (One ou Many = Object ou List) com o vértice corrente
        for (TableVertex predecessorVertex : predecessorList){
            RelationshipEdge edge = graph.getEdge(predecessorVertex, currentVertex);
            
            ClassField newField = new ClassField();
            newField.setModifier("private");
            newField.setName(predecessorVertex.getTableName());
            
            if (edge.getManySideEntity().equals(predecessorVertex.getTableName())){
                newField.setType("java.util.List<" + getStringWithFirstCapitalLetter(predecessorVertex.getTableName()) + ">");
                newField.setRelationshipType(ClassRelationshipFieldType.ARRAY_OF_OBJECTS);
                newField.setRefLocalFieldName(edge.getPkOneSide());
                newField.setRefForeignFieldName(edge.getFkManySide());
            } else if (edge.getOneSideEntity().equals(predecessorVertex.getTableName())) {
                newField.setType( getStringWithFirstCapitalLetter(predecessorVertex.getTableName()) );
                newField.setRelationshipType(ClassRelationshipFieldType.OBJECT);
                newField.setRefLocalFieldName(edge.getFkManySide());
                newField.setRefForeignFieldName(edge.getPkOneSide());
            }
            
            classMetadata.getFields().add(newField);
        }
    }
    
    // MÉTODOS PRIVADOS DE APOIO - MANIPULAÇÃO DO GRAFO.
    
    // Recupera todos os vértices folhas.
    // Vértice Folha = vertexs que NÃO possuem arestas de entrada.
    public static ArrayList<TableVertex> getVertexsWithZeroInDegree(DirectedAcyclicGraph<TableVertex, RelationshipEdge> g) {
        ArrayList<TableVertex> vertexs_with_zero_inDegree = new ArrayList<>();
        // Percorrendo conjunto de vértices do graph 'g'
        Iterator<TableVertex> vertexIterator = g.vertexSet().iterator();
        while (vertexIterator.hasNext()) {
            TableVertex vertex = vertexIterator.next();
            // Recuperando apenas vértices sem arestas de entrada (vértices folha).
            if (g.inDegreeOf(vertex) == 0) {
                vertexs_with_zero_inDegree.add(vertex);
            }
        }

        return vertexs_with_zero_inDegree;
    }
    
    // Recupera o vertex raiz. Esse vertex representa a entidade alvo da transformação.
    public static TableVertex getRootVertex(DirectedAcyclicGraph<TableVertex, RelationshipEdge> g) {
        TableVertex root = null;
        // Percorrendo conjunto de vértices do graph 'g'
        Iterator<TableVertex> vertexIterator = g.vertexSet().iterator();
        while (vertexIterator.hasNext()) {
            TableVertex vertex = vertexIterator.next();
            // Recuperando apenas o vértice sem arestas de saída. Esse vertice é raiz do grafo.
            if (g.outDegreeOf(vertex) == 0) {
                root = vertex;
            }
        }
        return root;
    }

    // Create a clone of the vertex object
    public static TableVertex cloneTableVertex(TableVertex vertex) {
        // Create the copy vertex...
        TableVertex newTableVertex = new TableVertex(vertex.getName(), vertex.getTableName(), vertex.getPk());
        // Copy all the field values from vertex object to newTableVertex object
        for (String field : vertex.getFields()) {
            newTableVertex.getFields().add(field);
        }
        return newTableVertex;
    }
    
    
}
