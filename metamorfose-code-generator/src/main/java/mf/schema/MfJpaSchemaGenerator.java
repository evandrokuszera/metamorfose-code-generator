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
import dag.nosql_schema.NoSQLSchema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.customization.MfClassCustomization;
import mf.generator.RdbTypeEnum;
import static mf.generator.MfCodeGenerator.getStringWithFirstCapitalLetter;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 *
 * @author evand
 */
public class MfJpaSchemaGenerator extends MfSchemaGenerator {

    public MfJpaSchemaGenerator(NoSQLSchema noSQLschema, MfClassCustomization classCustomization, RdbTypeEnum typeRDBDictionary) {
        super(noSQLschema, classCustomization, typeRDBDictionary);
    }

    public void generate(String packageBaseName) {
        this.newMfSchema();
        
        // For each entity in the DAG we will extract only the distinct entities.
        for (DirectedAcyclicGraph<TableVertex, RelationshipEdge> graphEntity : this.getNoSQLschema().getEntities()) {

            Iterator<TableVertex> vertexIterator = graphEntity.vertexSet().iterator();
            while (vertexIterator.hasNext()) {
                TableVertex vertex = vertexIterator.next();
                ClassMetadata classMetadata = new ClassMetadata();
                classMetadata.setPackageName(packageBaseName); // deve ser setado externamente
                classMetadata.setName(getStringWithFirstCapitalLetter(vertex.getTableName()));
                // Read each field from leafVertex and create a ClassField of classMetadata
                for (TableColumnVertex column : vertex.getTypeFields()) {
                    classMetadata.getFields().add(
                            new ClassField(
                                    "private",
                                    this.getTypeDictionary().getJavaDataType(column.getColumnType()),
                                    column.getColumnName(),
                                    ClassRelationshipFieldType.NONE,
                                    column.isPk(),
                                    column.isFk()
                            )
                    );
                }
                // if entity is not in schema entities, then we add it.
                if (findClassByNameInSchema(classMetadata.getName()) == null) {
                    // create the entity using our model.
                    MfEntity entity = new MfEntity(classMetadata.getName());
                    entity.addClassMetadata(classMetadata);
                    // add the entity in the schema.
                    this.getMfSchema().addEntity(entity);
                }
            } // end while vertices
        } // end for Schema
        
        
        for (RelationshipEdge edge : getOnlyDistinctsEdges(this.getNoSQLschema())) {
            ClassField newField = null;

            ClassMetadata oneSideEntity = findClassByNameInSchema(edge.getOneSideEntity());
            newField = new ClassField();
            newField.setModifier("private");
            newField.setType("java.util.List<" + getStringWithFirstCapitalLetter(edge.getManySideEntity()) + ">");
            newField.setName(edge.getManySideEntity());
            newField.setRelationshipType(ClassRelationshipFieldType.ARRAY_OF_OBJECTS);
            newField.setRefLocalFieldName(edge.getPkOneSide());
            newField.setRefForeignFieldName(edge.getFkManySide());
            oneSideEntity.getFields().add(newField);

            ClassMetadata manySideEntity = findClassByNameInSchema(edge.getManySideEntity());
            newField = new ClassField();
            newField.setModifier("private");
            newField.setType(getStringWithFirstCapitalLetter(edge.getOneSideEntity()));
            newField.setName(edge.getOneSideEntity());
            newField.setRelationshipType(ClassRelationshipFieldType.OBJECT);
            newField.setRefLocalFieldName(edge.getFkManySide());
            newField.setRefForeignFieldName(edge.getPkOneSide());
            manySideEntity.getFields().add(newField);
        } // end for edges
        

        // If there is a customization object, then we apply the customization for all the class metadata objects.
        if (this.getClassODMCustomization() != null){
            for (MfEntity entity : this.getMfSchema().getEntities()){
                this.getClassODMCustomization().applyCustomizationsTo( entity );
            }
        }
        
        System.out.println("MfJpaSchemaGenerator.generate(): OK");
    }

    private ClassMetadata findClassByNameInSchema(String className){
        for (MfEntity entity : this.getMfSchema().getEntities()){
            for (ClassMetadata c : entity.getClassMetadataList()){
                if (c.getName().equalsIgnoreCase(className)){
                    return c;
                }
            }
        }
        return null;
    }
    
    private List<RelationshipEdge> getOnlyDistinctsEdges(NoSQLSchema schema){
        List<RelationshipEdge> edges = new ArrayList<>();
        Map<String,String> edgesMap = new HashMap<>();
        // For each schema entity...
        for (DirectedAcyclicGraph<TableVertex, RelationshipEdge> graphEntity : this.getNoSQLschema().getEntities()) {
            // Get all distincts edges...
            Iterator<RelationshipEdge> edgeIterator = graphEntity.edgeSet().iterator();
            while (edgeIterator.hasNext()) {
                RelationshipEdge edge = edgeIterator.next();
                // testing if the edge was already add in the Map.
                if ( edgesMap.get( edge.getSource().getName() + "-->" + edge.getTarget().getName() ) == null){
                    if ( edgesMap.get( edge.getTarget().getName() + "-->" + edge.getSource().getName() ) == null){
                        // if the edge wasn´t add, then we add the edge in the map.
                        edges.add(edge);
                        // updating the Map.
                        edgesMap.put( edge.getSource().getName() + "-->" + edge.getTarget().getName(), "has" );
                        edgesMap.put( edge.getTarget().getName() + "-->" + edge.getSource().getName(), "has" );
                    }
                }
            }
        }
        // Get all distinct relationship edges.
        for (RelationshipEdge edge : schema.getRefEntities()){
            if ( edgesMap.get( edge.getOneSideEntity() + "-->" + edge.getManySideEntity() ) == null  ||
                  edgesMap.get( edge.getManySideEntity() + "-->" + edge.getOneSideEntity() ) == null ) {
                
                edges.add(edge);
                edgesMap.put( edge.getOneSideEntity() + "-->" + edge.getManySideEntity(), "has" );
                edgesMap.put( edge.getManySideEntity() + "-->" + edge.getOneSideEntity(), "has" );
            }
        }
        
        // returning only the distincts edges from DAG.
        return edges;
    }
}