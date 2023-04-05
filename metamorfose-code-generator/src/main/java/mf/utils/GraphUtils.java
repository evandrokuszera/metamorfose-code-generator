/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.utils;

import dag.model.RelationshipEdge;
import dag.model.TableVertex;
import dag.nosql_schema.ConversionProcess;
import dag.nosql_schema.NoSQLSchema;
import dag.persistence.ConversionProcessJson;
import dag.persistence.JSONPersistence;
import dag.persistence.NoSQLSchemaJson;
import java.io.File;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.json.JSONObject;

/**
 *
 * @author evand
 */
public class GraphUtils {
    public static void showEntities(NoSQLSchema schema){
        for (DirectedAcyclicGraph<TableVertex, RelationshipEdge> entity : schema.getEntities()){
            for (TableVertex table : entity.vertexSet()){
                System.out.println( table.getTableName() );
                System.out.println( table.getPk() );
                System.out.println( table.getFields() );
                System.out.println("");
            }
            System.out.println("");
            System.out.println("Relationships");
            for (RelationshipEdge edge : entity.edgeSet()){
                System.out.printf("Source = %s --> Target = %s\n", edge.getSource().getName(), edge.getTarget().getName());
                System.out.printf("OneSide = %s, ManySide = %s\n", edge.getOneSideEntity(), edge.getManySideEntity());
            }
        }
    }
    
    public static NoSQLSchema loadNosqlSchema(String filePath){
        NoSQLSchema nosqlSchema = null;        
        File f = new File(filePath);
        JSONObject json = JSONPersistence.loadJSONfromFile(f.getAbsolutePath());
        nosqlSchema = NoSQLSchemaJson.fromJSON(json);
        return nosqlSchema;
    }
    
    public static ConversionProcess loadConversionProcess(){
        String conversionProcessFilePath = "..\\input-nosql-schema\\dvd-store.json";    
        ConversionProcess conversionProcess = null;        
        File f = new File(conversionProcessFilePath);
        JSONObject jsonConversionProcess = JSONPersistence.loadJSONfromFile(f.getAbsolutePath());
        conversionProcess = ConversionProcessJson.fromJSON(jsonConversionProcess);
        return conversionProcess;
    }
}
