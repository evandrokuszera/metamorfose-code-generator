/*
 * Generating NoSQL Schema manually, without accessing the RDB metadata.
 * This class is used for testing purpuse.
 * Schema:
 *  Entities:
 *  - Orders
 *  - Orderlines
 *  References:
 *  - Orders(1) --- Orderlines(N)
 */
package nosql_schema_examples;

import dag.model.RelationshipEdge;
import dag.model.RelationshipEdgeType;
import dag.model.TableColumnVertex;
import dag.model.TableVertex;
import dag.nosql_schema.NoSQLSchema;
import dag.persistence.JSONPersistence;
import dag.persistence.NoSQLSchemaJson;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 *
 * @author evand
 */
public class NoSQLSchemaExample2 {
    private NoSQLSchema nosqlSchema;
    
    public NoSQLSchemaExample2(String schemaName){
        this.nosqlSchema = new NoSQLSchema(schemaName);
    }
    
    public NoSQLSchema gerar(){
        createOrderEntity();
        createOrderlineEntity();
        createReferencesBetweenEntities();
        return this.nosqlSchema;
    }
    
    // **************************************************************************************************************************
    //  Creating the Order entity.    
    //
    private void createOrderEntity(){
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> entityGraph = new DirectedAcyclicGraph<>(RelationshipEdge.class);
        
        TableVertex orderTable = new TableVertex("orders", "orders", "id");        
        orderTable.setId(1);        
        orderTable.getTypeFields().add( new TableColumnVertex("id", "serial", true, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("orderdate", "date", false, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("total", "float8", false, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("customerid", "int4", false, true) );
        
        entityGraph.addVertex(orderTable);
        
        addFieldsFromColumnField(orderTable);
        
        this.nosqlSchema.getEntities().add(entityGraph);
    }
    
    // **************************************************************************************************************************
    //  Creating the Orderlines entity.    
    //
    private void createOrderlineEntity(){
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> entityGraph = new DirectedAcyclicGraph<>(RelationshipEdge.class);
        
        TableVertex orderlinesTable = new TableVertex("orderlines", "orderlines", "id");        
        orderlinesTable.setId(2);        
        orderlinesTable.getTypeFields().add( new TableColumnVertex("id", "serial", true, false) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("orderlinedate", "date", false, false) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("quantity", "float8", false, false) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("price", "float8", false, false) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("orderid", "int4", false, true) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("prodid", "int4", false, true) );
        
        entityGraph.addVertex(orderlinesTable);
        
        addFieldsFromColumnField(orderlinesTable);
        
        this.nosqlSchema.getEntities().add(entityGraph);
    }
    
    // **************************************************************************************************************************
    //  Creating a relationship between entities Orders and Orderlines.
    //  This is a reference relationship, like one used in RDBs. Here, one produt has one or more orderlines (One to Many).
    //  Representation: Orders(1) --- Orderlines(N)
    //
    private void createReferencesBetweenEntities(){
        RelationshipEdge refEdge = new RelationshipEdge(
                RelationshipEdgeType.REF_ONE_TO_MANY, 
                "orders", 
                "orderlines", 
                "id", 
                "orderid"
        );
        refEdge.setOneSideEntityId(1);
        refEdge.setManySideEntityId(2);
        
        this.nosqlSchema.getRefEntities().add(refEdge);
    }
    
    // This method is used to copy all the column field to the fields array.
    private void addFieldsFromColumnField(TableVertex tableVertex){
        for (TableColumnVertex columnVertex : tableVertex.getTypeFields()){
            tableVertex.getFields().add( columnVertex.getColumnName() );
        }
    }
    
    // **************************************************************************************************************************
    // Persisting the schema to JSON, then load the schema from JSON.
    public static void main(String args[]){
        NoSQLSchemaExample2 gen = new NoSQLSchemaExample2("Test");
        String path = "..\\input-nosql-schema\\";
        
        JSONPersistence.saveJSONtoFile(
                NoSQLSchemaJson.toJSON(gen.gerar()),
                path+"schema2.json"
        );
        
        NoSQLSchema schema = NoSQLSchemaJson.fromJSON(JSONPersistence.loadJSONfromFile(path+"schema2.json"));
        schema.printSchema();
    }
}