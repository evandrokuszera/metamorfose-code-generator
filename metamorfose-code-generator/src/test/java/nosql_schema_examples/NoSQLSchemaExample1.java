/*
 * Generating NoSQL Schema manually, without accessing the RDB metadata.
 * This class is used for testing purpuse.
 * Schema:
 *  Entities:
 *  - Orders/Orderlines
 *  - Products
 *  References:
 *  - Products(1) --- Orderlines(N)
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
public class NoSQLSchemaExample1 {
    private NoSQLSchema nosqlSchema;
    
    public NoSQLSchemaExample1(String schemaName){
        this.nosqlSchema = new NoSQLSchema(schemaName);
    }
    
    public NoSQLSchema gerar(){
        createOrderEntity();
        createProductEntity();
        createReferencesBetweenEntities();
        return this.nosqlSchema;
    }
    
    // **************************************************************************************************************************
    //  Creating the Order entity.
    //  This entity is composed of Orders and Orderlines tables, where Orders is the root and the Orderlines is the nested table.
    //  Representation: Orders <-- Orderlines : One To Many
    //
    private void createOrderEntity(){
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> entityGraph = new DirectedAcyclicGraph<>(RelationshipEdge.class);
        
        TableVertex orderTable = new TableVertex("orders", "orders", "id");        
        orderTable.setId(1);        
        orderTable.getTypeFields().add( new TableColumnVertex("id", "serial", true, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("orderdate", "date", false, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("total", "float8", false, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("customerid", "int4", false, true) );
        
        TableVertex orderlinesTable = new TableVertex("orderlines", "orderlines", "id");        
        orderlinesTable.setId(2);        
        orderlinesTable.getTypeFields().add( new TableColumnVertex("id", "serial", true, false) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("orderlinedate", "date", false, false) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("quantity", "float8", false, false) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("price", "float8", false, false) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("orderid", "int4", false, true) );
        orderlinesTable.getTypeFields().add( new TableColumnVertex("prodid", "int4", false, true) );
        
        RelationshipEdge nestingEdge = new RelationshipEdge(
                RelationshipEdgeType.EMBED_ONE_TO_MANY, 
                "orders", 
                "orderlines", 
                "id", 
                "orderid"
        );
        nestingEdge.setOneSideEntityId(1);
        nestingEdge.setManySideEntityId(2);
        
        entityGraph.addVertex(orderTable);
        entityGraph.addVertex(orderlinesTable);
        entityGraph.addEdge(orderlinesTable, orderTable, nestingEdge);
        
        addFieldsFromColumnField(orderTable);
        addFieldsFromColumnField(orderlinesTable);
        
        this.nosqlSchema.getEntities().add(entityGraph);
    }
    
    // **************************************************************************************************************************
    //  Creating the Product entity.
    //  This entity is composed only of Products table. There are no nested entities.
    //  Representation: Products
    //
    private void createProductEntity(){
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> entityGraph = new DirectedAcyclicGraph<>(RelationshipEdge.class);
        
        TableVertex productTable = new TableVertex("products", "products", "id");        
        productTable.setId(3);        
        productTable.getTypeFields().add( new TableColumnVertex("id", "serial", true, false) );
        productTable.getTypeFields().add( new TableColumnVertex("description", "varchar", false, false) );
        productTable.getTypeFields().add( new TableColumnVertex("price", "float8", false, false) );
        
        entityGraph.addVertex(productTable);
        
        addFieldsFromColumnField(productTable);
        
        this.nosqlSchema.getEntities().add(entityGraph);
    }
    
    // **************************************************************************************************************************
    //  Creating a relationship between entities Orders and Products.
    //  This is a reference relationship, like one used in RDBs. Here, one produt has one or more orderlines (One to Many).
    //  Representation: Products(1) --- Orderlines(N)
    //
    private void createReferencesBetweenEntities(){
        RelationshipEdge refEdge = new RelationshipEdge(
                RelationshipEdgeType.REF_ONE_TO_MANY, 
                "products", 
                "orderlines", 
                "id", 
                "prodid"
        );
        refEdge.setOneSideEntityId(3);
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
        NoSQLSchemaExample1 gen = new NoSQLSchemaExample1("Test");
        String path = "..\\input-nosql-schema\\";
        
        JSONPersistence.saveJSONtoFile(
                NoSQLSchemaJson.toJSON(gen.gerar()),
                path+"schema.json"
        );
        
        NoSQLSchema schema = NoSQLSchemaJson.fromJSON(JSONPersistence.loadJSONfromFile(path+"schema.json"));
        schema.printSchema();
    }
}