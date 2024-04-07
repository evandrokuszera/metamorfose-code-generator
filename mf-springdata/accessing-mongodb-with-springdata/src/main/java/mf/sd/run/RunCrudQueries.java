/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.run;

import mf.sd.repositories.CategoryRepository;
import mf.sd.repositories.CustomerRepository;
import mf.sd.repositories.OrderRepository;
import mf.sd.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author evand
 */
@Component
public class RunCrudQueries {
    public static boolean printDocumentAfterSave;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    
    // CRUD operations test. Uncomment the code below to run the test.
    public void run(){
        printDocumentAfterSave = true;
        boolean insert = false;
        boolean update = false;
        boolean remove = false;
        
        if (insert) CrudCategory.testInsertCategory(categoryRepo);
        if (update) CrudCategory.testUpdateCategory(categoryRepo);
        if (remove) CrudCategory.testRemoveCategory(categoryRepo);
        
        if (insert) CrudProduct.testInsertProduct(productRepo);
        if (update) CrudProduct.testUpdateProduct(productRepo);
        if (remove) CrudProduct.testRemoveProduct(productRepo);
        
        if (insert) CrudCustomer.testInsertCustomer(customerRepo);
        if (update) CrudCustomer.testUpdateCustomer(customerRepo);
        if (remove) CrudCustomer.testRemoveCustomer(customerRepo);
        
        if (insert) CrudOrder.testInsertOrder(orderRepo);
        if (update) CrudOrder.testUpdateOrder(orderRepo);
        if (remove) CrudOrder.testRemoveOrder(orderRepo);
        
        CrudAllEntitiesWithReferences.testInsert(customerRepo, orderRepo, productRepo);
        CrudAllEntitiesWithReferences.testRead(customerRepo, orderRepo, productRepo);
       
    }
    
    
}