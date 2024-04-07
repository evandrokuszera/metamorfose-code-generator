package mf.dn.run;

/**
 *
 * @author evand
 */
public class RunCrudQueries {
    public static boolean printDocumentAfterSave = true;
    
    // CRUD operations test. Uncomment the code below to run the test.
    public static void main(String args[]) throws Exception{
        boolean insert = false;
        boolean update = false;
        boolean remove = false;
        
        boolean categoryInsertRun = insert;
        boolean categoryUpdateRun = update;
        boolean categoryRemoveRun = remove;        
        if (categoryInsertRun) CrudCategory.testInsertCategory();
        if (categoryUpdateRun) CrudCategory.testUpdateCategory();
        if (categoryRemoveRun) CrudCategory.testRemoveCategory();
        
        /////////////////////////////////////////////////////////////////////
        boolean productInsertRun = insert;
        boolean productUpdateRun = update;
        boolean productRemoveRun = remove;        
        if (productInsertRun) CrudProduct.testInsertProduct();
        if (productUpdateRun) CrudProduct.testUpdateProduct();
        if (productRemoveRun) CrudProduct.testRemoveProduct();
        
        /////////////////////////////////////////////////////////////////////
        boolean customerInsertRun = insert;
        boolean customerUpdateRun = update;
        boolean customerRemoveRun = remove;        
        if (customerInsertRun) CrudCustomer.testInsertCustomer();
        if (customerUpdateRun) CrudCustomer.testUpdateCustomer();
        if (customerRemoveRun) CrudCustomer.testRemoveCustomer();
        
        /////////////////////////////////////////////////////////////////////
        boolean orderInsertRun = insert;
        boolean orderUpdateRun = update;
        boolean orderRemoveRun = remove;        
        if (orderInsertRun) CrudOrder.testInsertOrder();
        if (orderUpdateRun) CrudOrder.testUpdateOrder();
        if (orderRemoveRun) CrudOrder.testRemoveOrder();
        
        /////////////////////////////////////////////////////////////////////
        boolean allRefInsertRun = true;
        boolean allRefReadRun = true;        
        if (allRefInsertRun) CrudAllEntitiesWithReferences.testInsert();
        if (allRefReadRun) CrudAllEntitiesWithReferences.testRead();
        
        // The last two test above write the objects into MongoDB using the Data Nucleus format
        //   to stablish reference relationships, where the relationship field must store
        //   the full qualified class name and the value of the related object as a String.
        //   e.g. mf.dn.model.nosql.customers.Customers:1
        // Warning: Metamorfose doesn't use this format to write objects, and
        //   as a result the related data cannot be loaded by Data Nucleus.
    }
}
    
    