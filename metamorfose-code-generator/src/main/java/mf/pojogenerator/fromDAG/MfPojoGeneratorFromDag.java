/*
 * This class traverse a graph 'g' and generate a EntityMap collection according to types of edges (one_embedded, many_embedded, etc).
 */
package mf.pojogenerator.fromDAG;

import dag.model.RelationshipEdge;
import dag.model.TableColumnVertex;
import dag.model.TableVertex;
import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.classmetadata.odm.MfClassODMCustomization;
import mf.pojogenerator.MfPojoGenerator;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 *
 * @author Evandro
 */
public class MfPojoGeneratorFromDag extends MfPojoGenerator {
    private RdbToJavaDatatypeDictionary typeDictionary;
    private DirectedAcyclicGraph<TableVertex, RelationshipEdge> graphEntity = null;
    private NoSQLSchema noSQLschema = null;

    public MfPojoGeneratorFromDag(DirectedAcyclicGraph<TableVertex, RelationshipEdge> graphEntity, MfClassODMCustomization classODMCustomization, RdbTypeEnum typeRDBDictionary) {
        super(classODMCustomization);
        this.typeDictionary = new RdbToJavaDatatypeDictionary(typeRDBDictionary);
        this.graphEntity = graphEntity;
    }

    public MfPojoGeneratorFromDag(NoSQLSchema inputNoSQLschema, MfClassODMCustomization classODMCustomization, RdbTypeEnum typeRDBDictionary) {
        super(classODMCustomization);
        this.typeDictionary = new RdbToJavaDatatypeDictionary(typeRDBDictionary);
        this.noSQLschema = inputNoSQLschema;
    }

    // Go through each DAG path (leaf to root) to generate a ClassMetadata.
    public List<ClassMetadata> generateClassMetadataList(String packageBaseName) {
        
        if (graphEntity == null){
            throw new NullPointerException("Field 'graphEntity' is null. To use this method you should use the other class construtor.");
        }
        
        // Clone the graph (DAG) 'graphEntity' to process each path.
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> cloneGraph = (DirectedAcyclicGraph<TableVertex, RelationshipEdge>) graphEntity.clone();
                
        // Enquanto o grafo 'cloneGraph' ainda não foi reduzido a zero vertices, converte cada vértice para um objeto ClassMetadata.
        while (cloneGraph.vertexSet().size() > 0) {
            // Get the leaf vertices set (leafVertexs)
            ArrayList<TableVertex> leafVertexs = getVertexsWithZeroInDegree(cloneGraph);
            
            // for each leafVertex create a ClassMetadata 
            for (TableVertex leafVertex : leafVertexs) {
                
                ClassMetadata classMetadata = new ClassMetadata();
                classMetadata.setPackageName(packageBaseName); // deve ser setado externamente
                classMetadata.setName( getStringWithFirstCapitalLetter( leafVertex.getTableName() ) );
                
                // Ler campos da leaf e gerar ClassField para a classMetadata
                for (TableColumnVertex column : leafVertex.getTypeFields()){
                    classMetadata.getFields().add(
                            new ClassField(
                                    "private", 
                                    typeDictionary.getJavaDataType(column.getColumnType()), 
                                    column.getColumnName(), 
                                    ClassRelationshipFieldType.NONE));
                }
                
                addRelationshipFieldsToClass(classMetadata, graphEntity);
                
                this.getClassMetadataList().add(classMetadata);
                
                // Remove leafVertex that was processed (reducing one vertex from the cloneGraph).            
                cloneGraph.removeVertex(leafVertex);
            }
            
        }
        
        // After generating the ClassMetadataList we apply the Customizations that are specific by ODM.
        this.applyCustomizationsToClassMetadataList();
        
        return this.getClassMetadataList();
    }
    
    
    public void generateAndSavePojosByNoSQLSchema(String packageBaseName) throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException {
        if (noSQLschema == null){
            throw new NullPointerException("You must pass an NoSQLSchema object to execute generateClassMetadataListFromNoSQLSchema() method.");
        }
        
        // A partir de um esquema NoSQL, percorrer as entidades e gerar classes necessárias para representar a estrutura dos documentos de uma coleção.
        int i = 0;
        for (DirectedAcyclicGraph<TableVertex, RelationshipEdge> entity : noSQLschema.getEntities()){
            
            this.graphEntity = entity;
            
            // Gerar objetos ClassMetatada para uma entidade do schema. A última parte do pacote indica o nome da entidade (coleção de documentos)
            generateClassMetadataList(packageBaseName + "." + noSQLschema.getEntityName(i));
            
            // Salvar os objetos ClassMetadata no disco, como arquivos .java
            savePojoClassesToFile();
            
            getClassMetadataList().clear();
            
            i++;
        }
        
        this.graphEntity = null;
    }
    
    
    
    
    //########################################################################################################################################
    // MÉTODOS PRIVADOS DE APOIO.
    //########################################################################################################################################
    
    // Recebe um objeto classMetadata, encontra o vertex correspondente a ele no grafo e adiciona atributos de relacionamento 1:1 ou 1:N,
    //  com base nos predecessores do vertex. Relacionamento 1:1 são objetos embutidos e 1:N são listas de objetos embutidos.
    private void addRelationshipFieldsToClass(ClassMetadata classMetadata, DirectedAcyclicGraph<TableVertex, RelationshipEdge> graph){
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
            } else if (edge.getOneSideEntity().equals(predecessorVertex.getTableName())) {
                newField.setType( getStringWithFirstCapitalLetter(predecessorVertex.getTableName()) );
                newField.setRelationshipType(ClassRelationshipFieldType.OBJECT);
            }
            
            classMetadata.getFields().add(newField);
        }
    }
    
    // MÉTODOS PRIVADOS DE APOIO - MANIPULAÇÃO DO GRAFO.
    
    // Recupera todos os vértices folhas.
    // Vértice Folha = vertexs que NÃO possuem arestas de entrada.
    private ArrayList<TableVertex> getVertexsWithZeroInDegree(DirectedAcyclicGraph<TableVertex, RelationshipEdge> g) {
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
    private TableVertex getRootVertex(DirectedAcyclicGraph<TableVertex, RelationshipEdge> g) {
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

    // Faz um clone do vertex passado por parâmetro
    private TableVertex cloneTableVertex(TableVertex vertex) {
        // Cria vertex copia...
        TableVertex newTableVertex = new TableVertex(vertex.getName(), vertex.getTableName(), vertex.getPk());
        // Cria copia de cada filed do vertex
        for (String field : vertex.getFields()) {
            newTableVertex.getFields().add(field);
        }
        return newTableVertex;
    }

}
