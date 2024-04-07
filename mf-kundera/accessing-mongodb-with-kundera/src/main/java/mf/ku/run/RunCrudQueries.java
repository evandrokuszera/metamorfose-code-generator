/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.ku.run;

/**
 *
 * @author evand
 */
public class RunCrudQueries {
    public static boolean printDocumentAfterSave = true;
        
    // CRUD operations test. Uncomment the code below to run the test.
    public static void main(String args[]) throws Exception{
        boolean insert = false;
        boolean read = false;
        boolean update = false;
        boolean remove = false;
        
        KunderaConnection.openConn();
        
        if (insert) CrudCategory.testInsertCategory();
        if (read) CrudCategory.testReadCategory();
        if (update) CrudCategory.testUpdateCategory();
        if (remove) CrudCategory.testRemoveCategory();
        
        if (insert) CrudProduct.testInsertProduct();
        if (read) CrudProduct.testReadProduct();
        if (update) CrudProduct.testUpdateProduct();
        if (remove) CrudProduct.testRemoveProduct();
        
        if (insert) CrudCustomer.testInsertCustomer();
        if (read) CrudCustomer.testReadCustomer();
        if (update) CrudCustomer.testUpdateCustomer();
        if (remove) CrudCustomer.testRemoveCustomer();
        
        if (insert) CrudOrder.testInsertOrder();
        if (read) CrudOrder.testReadOrder();
        if (update) CrudOrder.testUpdateOrder();
        if (remove) CrudOrder.testRemoveOrder();
        
        CrudAllEntitiesWithReferences.testInsert();
        CrudAllEntitiesWithReferences.testRead();
        
        KunderaConnection.closeConn();
        
        // WARNING:
        // There are an instability to execute the tests for the Customers and Orders entities, that have @OneToMany relationships.
        // When we loaded a Customer from database, sometimes the Impetus Kundera can't load the associated Orders and throws an exception.
        // But sometimes, Impetus Kundera load all the entities and their associated data.
    }
}