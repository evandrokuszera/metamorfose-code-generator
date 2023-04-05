/*
 * Generating NoSQL Schema manually, without accessing the RDB metadata.
 * This class is used for testing purpuse.
 * Schema:
 *  Entities:
 *  - Customers
 *  - Orders <-- Orderlines
 *  - Products <-- Categories; <-- Inventory
 *  References:
 *  - Customers (1) <-- Orders(N)
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

public class NoSQLSchemaExample3DvdStore {
    private NoSQLSchema nosqlSchema;
    
    public NoSQLSchemaExample3DvdStore(String schemaName){
        this.nosqlSchema = new NoSQLSchema(schemaName);
    }
    
    public NoSQLSchema generate(){
        createCustomerEntity();
        createOrderEntity();
        createProductEntity();
        createCategoriesEntity();
        createReferences();
        return this.nosqlSchema;
    }
    
    // **************************************************************************************************************************
    //  Creating the Customers entity (tables): Customers   
    // **************************************************************************************************************************
    private void createCustomerEntity(){
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> entityGraph = new DirectedAcyclicGraph<>(RelationshipEdge.class);
        
        TableVertex customerTable = new TableVertex("customers", "customers", "id_customer");        
        customerTable.setId(1);                              //  ColumnName, ColumnType, isPK, isFK
        customerTable.getTypeFields().add( new TableColumnVertex("id_customer", "serial", true, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("firstname", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("lastname", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("address1", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("address2", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("city", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("state", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("zip", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("country", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("region", "int2", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("email", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("phone", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("creditcardtype", "int4", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("creditcard", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("creditcardexpiration", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("username", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("password", "varchar", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("age", "int2", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("income", "int4", false, false) );
        customerTable.getTypeFields().add( new TableColumnVertex("gender", "varchar", false, false) );
        entityGraph.addVertex(customerTable);
        
        addFieldsFromColumnField(customerTable);
        
        this.nosqlSchema.getEntities().add(entityGraph);
    }
    
    
    // **************************************************************************************************************************
    //  Creating the Order entity (tables): Orders <-- Orderlines    
    // **************************************************************************************************************************
    private void createOrderEntity(){
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> entityGraph = new DirectedAcyclicGraph<>(RelationshipEdge.class);
        
        TableVertex orderTable = new TableVertex("orders", "orders", "id_order");        
        orderTable.setId(2);        
        orderTable.getTypeFields().add( new TableColumnVertex("id_order", "serial", true, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("orderdate", "date", false, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("customerid", "int4", false, true) );
        orderTable.getTypeFields().add( new TableColumnVertex("netamount", "numeric", false, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("tax", "numeric", false, false) );
        orderTable.getTypeFields().add( new TableColumnVertex("totalamount", "numeric", false, false) );
        entityGraph.addVertex(orderTable);
        addFieldsFromColumnField(orderTable);
        
        TableVertex orderlineTable = new TableVertex("orderlines", "orderlines", "orderlineid");        
        orderlineTable.setId(3);        
        orderlineTable.getTypeFields().add( new TableColumnVertex("orderlineid", "int4", true, false) );
        orderlineTable.getTypeFields().add( new TableColumnVertex("orderid", "int4", false, true) );
        orderlineTable.getTypeFields().add( new TableColumnVertex("prod_id", "int4", false, true) );
        orderlineTable.getTypeFields().add( new TableColumnVertex("quantity", "int2", false, false) );
        orderlineTable.getTypeFields().add( new TableColumnVertex("orderlinedate", "date", false, false) );
        entityGraph.addVertex(orderlineTable);
        addFieldsFromColumnField(orderlineTable);
        
        RelationshipEdge nestingEdge = new RelationshipEdge(
                RelationshipEdgeType.EMBED_ONE_TO_MANY, 
                "orders", 
                "orderlines", 
                "id_order", 
                "orderid"
        );
        nestingEdge.setOneSideEntityId(2);
        nestingEdge.setManySideEntityId(3);
        
        entityGraph.addEdge(orderlineTable, orderTable, nestingEdge);
        
        this.nosqlSchema.getEntities().add(entityGraph);
    }
    
    // **************************************************************************************************************************
    //  Creating the Product entity (tables): Products <-- Inventory; Products <-- Categories     
    // **************************************************************************************************************************
    private void createProductEntity(){
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> entityGraph = new DirectedAcyclicGraph<>(RelationshipEdge.class);
        
        TableVertex productTable = new TableVertex("products", "products", "id_prod");        
        productTable.setId(4);        
        productTable.getTypeFields().add( new TableColumnVertex("id_prod", "serial", true, false) );
        productTable.getTypeFields().add( new TableColumnVertex("category", "int2", false, true) );
        productTable.getTypeFields().add( new TableColumnVertex("title", "text", false, false) );
        productTable.getTypeFields().add( new TableColumnVertex("actor", "text", false, false) );
        productTable.getTypeFields().add( new TableColumnVertex("price", "numeric", false, false) );
        productTable.getTypeFields().add( new TableColumnVertex("special", "int2", false, false) );
        productTable.getTypeFields().add( new TableColumnVertex("common_prod_id", "int4", false, false) );
        entityGraph.addVertex(productTable);
        addFieldsFromColumnField(productTable);
        
        TableVertex inventoryTable = new TableVertex("inventory", "inventory", "prod_id");        
        inventoryTable.setId(5);        
        inventoryTable.getTypeFields().add( new TableColumnVertex("prod_id", "int4", true, true) );
        inventoryTable.getTypeFields().add( new TableColumnVertex("quan_in_stock", "int4", false, false) );
        inventoryTable.getTypeFields().add( new TableColumnVertex("sales", "int4", false, false) );
        entityGraph.addVertex(inventoryTable);
        addFieldsFromColumnField(inventoryTable);
        
        TableVertex categoryTable = new TableVertex("categories", "categories", "id_category");        
        categoryTable.setId(6);        
        categoryTable.getTypeFields().add( new TableColumnVertex("id_category", "serial", true, false) );
        categoryTable.getTypeFields().add( new TableColumnVertex("categoryname", "varchar", false, false) );
        entityGraph.addVertex(categoryTable);
        addFieldsFromColumnField(categoryTable);
        
        // Creating the nesting between Inventory and Product
        RelationshipEdge nestingEdge1 = new RelationshipEdge(
                RelationshipEdgeType.EMBED_MANY_TO_ONE, 
                "inventory", 
                "products", 
                "prod_id", 
                "id_prod"
        );
        nestingEdge1.setOneSideEntityId( inventoryTable.getId() );
        nestingEdge1.setManySideEntityId( productTable.getId() );
        
        // Creating the nesting between Category and Product
        RelationshipEdge nestingEdge2 = new RelationshipEdge(
                RelationshipEdgeType.EMBED_MANY_TO_ONE, 
                "categories", 
                "products", 
                "id_category", 
                "category"
        );
        nestingEdge2.setOneSideEntityId( categoryTable.getId() );
        nestingEdge2.setManySideEntityId( productTable.getId() );
        
        // Adding the nesting edges into the Product entity
        entityGraph.addEdge(inventoryTable, productTable, nestingEdge1);
        entityGraph.addEdge(categoryTable, productTable, nestingEdge2);
        
        this.nosqlSchema.getEntities().add(entityGraph);
    }
    
    
    // **************************************************************************************************************************
    //  Creating the Categories entity (tables): Categories     
    // **************************************************************************************************************************
    private void createCategoriesEntity(){
        DirectedAcyclicGraph<TableVertex, RelationshipEdge> entityGraph = new DirectedAcyclicGraph<>(RelationshipEdge.class);
        
        TableVertex categoryTable = new TableVertex("categories", "categories", "id_category");        
        categoryTable.setId(7);        
        categoryTable.getTypeFields().add( new TableColumnVertex("id_category", "serial", true, false) );
        categoryTable.getTypeFields().add( new TableColumnVertex("categoryname", "varchar", false, false) );
        entityGraph.addVertex(categoryTable);
        addFieldsFromColumnField(categoryTable);
                
        this.nosqlSchema.getEntities().add(entityGraph);
    }
        

    
    // **************************************************************************************************************************
    //  Creating a relationship (Reference) between entities.
    //  Entities: Customers(1) --- Orders(N)
    //  Entities: Products(1) --- Orderlines(N)
    // **************************************************************************************************************************
    private void createReferences(){
        ///
        RelationshipEdge refEdge_Customers_Orders = new RelationshipEdge(
                RelationshipEdgeType.REF_ONE_TO_MANY, 
                "customers", 
                "orders", 
                "id_customer", 
                "customerid"
        );
        refEdge_Customers_Orders.setOneSideEntityId(1);
        refEdge_Customers_Orders.setManySideEntityId(2);        
        this.nosqlSchema.getRefEntities().add(refEdge_Customers_Orders);
        
        ///
        RelationshipEdge refEdge_Orderlines_Products = new RelationshipEdge(
                RelationshipEdgeType.REF_ONE_TO_MANY, 
                "products", 
                "orderlines", 
                "id_prod", 
                "prod_id"
        );
        refEdge_Orderlines_Products.setOneSideEntityId(4);
        refEdge_Orderlines_Products.setManySideEntityId(3);        
        this.nosqlSchema.getRefEntities().add(refEdge_Orderlines_Products);
    }
    
    // This method is used to copy all the column field to the fields array.
    private void addFieldsFromColumnField(TableVertex tableVertex){
        for (TableColumnVertex columnVertex : tableVertex.getTypeFields()){
            tableVertex.getFields().add( columnVertex.getColumnName() );
        }
    }
    
    // **************************************************************************************************************************
    // Persisting the schema to JSON, then load the schema from JSON.
    // **************************************************************************************************************************
    public static void main(String args[]){
        NoSQLSchemaExample3DvdStore gen = new NoSQLSchemaExample3DvdStore("dvd-store");
        String schema_file = "dvd-store.json";
        String path = "..\\input-nosql-schema\\" + schema_file;
        
        NoSQLSchema schema = gen.generate();
        
        // Persisting the NoSQL Schema to disk.
        JSONPersistence.saveJSONtoFile(
                NoSQLSchemaJson.toJSON(schema),
                path
        );
        
        // Loading the persisted NoSQL Schema from disk
        schema = NoSQLSchemaJson.fromJSON(JSONPersistence.loadJSONfromFile(path));
        
        // Printing...
        schema.printSchema();
    }
}