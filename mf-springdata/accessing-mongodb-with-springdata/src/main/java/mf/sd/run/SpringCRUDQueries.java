/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.run;

import java.util.ArrayList;
import java.util.Date;
import mf.sd.model.nosql.customers.Customers;
import mf.sd.model.nosql.orders.Orderlines;
import mf.sd.model.nosql.orders.Orders;
import mf.sd.model.nosql.products.Categories;
import mf.sd.model.nosql.products.Inventory;
import mf.sd.model.nosql.products.Products;
import mf.sd.repositories.CategoryRepository;
import mf.sd.repositories.CustomerRepository;
import mf.sd.repositories.OrderRepository;
import mf.sd.repositories.ProductRepository;
import mf.sd.utils.CustomersObjToString;
import mf.sd.utils.OrdersObjToString;
import mf.sd.utils.ProductsObjToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author evand
 */
@Component
public class SpringCRUDQueries {
    private static boolean printDocumentAfterSave;
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
        testInsertCategory();
//        testUpdateCategory();
//        testRemoveCategory();
        
        testInsertProduct();
//        testUpdateProduct();
//        testRemoveProduct();
//
        testInsertCustomer();
//        testUpdateCustomer();
//        testRemoveCustomer();
//
        testInsertOrder();
//        testUpdateOrder();
//        testRemoveOrder();

        // This test set references between documents.
        testInsertProductCustomerOrders_withReferences();
    }
    
    /***************************************************************************
    *
    * entity: Categories
    *
    ***************************************************************************/
    
    private void testInsertCategory(){
        mf.sd.model.nosql.categories.Categories cat = new mf.sd.model.nosql.categories.Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        
        categoryRepo.save(cat);

        if (!categoryRepo.findByCategoryId(2000).isEmpty()){
            mf.sd.model.nosql.categories.Categories catFromDB = categoryRepo.findByCategoryId(2000).get(0);
            if (catFromDB.getCategoryname().equals("Category Test")){
                System.out.println("*INFO: SpringCRUDQueries.testInsertCategory(): Ok");
                if (printDocumentAfterSave) System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
    }
    
    private void testUpdateCategory() {
        
        if (categoryRepo.findByCategoryId(2000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testUpdateCategory(): category not found!");
            return;
        }
        
        mf.sd.model.nosql.categories.Categories cat = categoryRepo.findByCategoryId(2000).get(0);
        cat.setId_category(2000);
        cat.setCategoryname("Category Test Test");
        
        categoryRepo.save(cat);

        if (!categoryRepo.findByCategoryId(2000).isEmpty()){
            mf.sd.model.nosql.categories.Categories catFromDB = categoryRepo.findByCategoryId(2000).get(0);
            if (catFromDB.getCategoryname().equals("Category Test Test")){
                System.out.println("*INFO: SpringCRUDQueries.testUpdateCategory(): Ok");
                if (printDocumentAfterSave) System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
    }
    
    private void testRemoveCategory() {
        if (categoryRepo.findByCategoryId(2000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testRemoveCategory(): category not found!");
            return;
        }
        
        mf.sd.model.nosql.categories.Categories cat = categoryRepo.findByCategoryId(2000).get(0);
        categoryRepo.delete(cat);

        if (categoryRepo.findByCategoryId(2000).isEmpty()){
            System.out.println("*INFO: SpringCRUDQueries.testRemoveCategory(): Ok");
        }
    }
    
    /***************************************************************************
    *
    * entity: Products
    *
    ***************************************************************************/
    
    private void testInsertProduct(){
        Products p = new Products();
        p.setActor("Actor Test");
        p.setTitle("Title Test");
        p.setCategory(2000);
        p.setCommon_prod_id(1);
        p.setId_prod(2000000);
        p.setPrice(5555.0);
        p.setSpecial(1);
        
        Inventory in = new Inventory();
        in.setProd_id(2000000);
        in.setQuan_in_stock(1234);
        in.setSales(4321);
        p.setInventory(in);
        
        Categories cat = new Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        p.setCategories(cat);
        
        productRepo.save(p);

        if (!productRepo.findProductById(2000000).isEmpty()){
            Products product = productRepo.findProductById(2000000).get(0);
            if (product.getActor().equals("Actor Test")){
                System.out.println("*INFO: SpringCRUDQueries.testInsertProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(product) );
            }
        }
    }
    
    private void testUpdateProduct() {
        
        if (productRepo.findProductById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testUpdateProduct(): product not found!");
            return;
        }
        
        Products p = productRepo.findProductById(2000000).get(0);
        p.setActor("Actor Test Test");
        p.setTitle("Title Test Test");
        p.setCategory(2000);
        p.setCommon_prod_id(0);
        p.setId_prod(2000000);
        p.setPrice(10000.0);
        p.setSpecial(0);
        
        Inventory in = new Inventory();
        in.setProd_id(2000000);
        in.setQuan_in_stock(0);
        in.setSales(0);
        p.setInventory(in);
        
        Categories cat = new Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        p.setCategories(cat);
        
        productRepo.save(p);

        if (!productRepo.findProductById(2000000).isEmpty()){
            Products product = productRepo.findProductById(2000000).get(0);
            if (product.getActor().equals("Actor Test Test")){
                System.out.println("*INFO: SpringCRUDQueries.testUpdateProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(product) );
            }
        }
    }
    
    private void testRemoveProduct() {
        if (productRepo.findProductById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testRemoveProduct(): product not found!");
            return;
        }
        
        Products p = productRepo.findProductById(2000000).get(0);
        productRepo.delete(p);

        if (productRepo.findProductById(2000000).isEmpty()){
            System.out.println("*INFO: SpringCRUDQueries.testRemoveProduct(): Ok");
        }
    }
    
    /***************************************************************************
    *
    * entity: Customer
    *
    ***************************************************************************/
    
    private void testInsertCustomer() {
        Customers c = new Customers();
        c.setId_customer(2000000);
        c.setFirstname("Customer");
        c.setLastname("Test");        
        c.setAddress1("Street 1");
        c.setAddress2("Street 2");
        c.setAge(41);
        c.setCity("City Test");
        c.setCountry("Brazil");
        c.setCreditcard("VISA");
        c.setCreditcardexpiration("07");
        c.setCreditcardtype(1);
        c.setEmail("customer@test.com");
        c.setGender("M");
        c.setUsername("customertest");
        c.setPassword("1234");
        c.setPhone("554699991234");
        c.setRegion(22);
        c.setState("PR");
        c.setZip("85660000");
        c.setIncome(50000);        
        customerRepo.save(c);
        
        
        if (!customerRepo.findCustomerById(2000000).isEmpty()){
            Customers customer = customerRepo.findCustomerById(2000000).get(0);
            if (customer.getFirstname().equals("Customer") && customer.getLastname().equals("Test")){
                System.out.println("*INFO: SpringCRUDQueries.testInsertCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customer) );
            }
        }
        
    }
    
    private void testUpdateCustomer() {
        
        if (customerRepo.findCustomerById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testUpdateCustomer(): customer not found!");
            return;
        }
        
        Customers c = customerRepo.findCustomerById(2000000).get(0);
        c.setId_customer(2000000);
        c.setFirstname("Customer2");
        c.setLastname("Test2");        
        c.setAddress1("Street 12");
        c.setAddress2("Street 22");
        c.setAge(41);
        c.setIncome(100000);
        c.setCity("City Test2");
        c.setCountry("Brazil2");
        c.setCreditcard("VISA");
        c.setCreditcardexpiration("08");
        c.setCreditcardtype(2);
        c.setEmail("customer2@test.com");
        c.setGender("F");
        c.setUsername("customertest2");
        c.setPassword("12345");
        c.setPhone("554699992222");
        c.setRegion(33);
        c.setState("SC");
        c.setZip("95660000");
        
        customerRepo.save(c);

        if (!customerRepo.findCustomerById(2000000).isEmpty()){
            Customers customer = customerRepo.findCustomerById(2000000).get(0);
            if (customer.getFirstname().equals("Customer2") && customer.getLastname().equals("Test2")){
                System.out.println("*INFO: SpringCRUDQueries.testUpdateCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customer) );
            }
        }
    }
    
    private void testRemoveCustomer() {
        if (customerRepo.findCustomerById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testRemoveCustomer(): customer not found!");
            return;
        }
        
        Customers c = customerRepo.findCustomerById(2000000).get(0);
        customerRepo.delete(c);

        if (customerRepo.findCustomerById(2000000).isEmpty()){
            System.out.println("*INFO: SpringCRUDQueries.testRemoveCustomer(): Ok");
        }
        
    }
    
    /***************************************************************************
    *
    * entity: Orders
    *
    ***************************************************************************/
    
    private void testInsertOrder() {
        Orders order = new Orders();
        order.setCustomerid(2000000);
        order.setId_order(2000000);
        order.setNetamount(100.0);
        order.setOrderdate(new Date());
        order.setTax(1.1);
        order.setTotalamount(101.0);
        //o.setCustomers(c);
        
        Orderlines olines1 = new Orderlines();
        olines1.setOrderid(2000000);
        olines1.setOrderlinedate(new Date());
        olines1.setOrderlineid(2000001);
        olines1.setQuantity(1);
        olines1.setProd_id(2000000);
        
        Orderlines olines2 = new Orderlines();
        olines2.setOrderid(2000000);
        olines2.setOrderlinedate(new Date());
        olines2.setOrderlineid(2000002);
        olines2.setQuantity(2);
        olines2.setProd_id(2000000);
        
        order.setOrderlines(new ArrayList<Orderlines>());
        order.getOrderlines().add(olines1);
        order.getOrderlines().add(olines2);
        
        orderRepo.save(order);

        if (!orderRepo.findOrderById(2000000).isEmpty()){
            Orders o = orderRepo.findOrderById(2000000).get(0);
            if (o.getCustomerid() == 2000000){
                System.out.println("*INFO: SpringCRUDQueries.testInsertOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(o) );
            }
        }
    }
    
    private void testUpdateOrder() {
        if (orderRepo.findOrderById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testUpdateOrder(): order not found!");
            return;
        }
        
        Orders order = orderRepo.findOrderById(2000000).get(0);
        order.setCustomerid(2000000);
        order.setId_order(2000000);
        order.setNetamount(500.0);
        order.setOrderdate(new Date());
        order.setTax(5.1);
        order.setTotalamount(501.0);
        
        for (Orderlines ol : order.getOrderlines()){
            ol.setQuantity( ol.getQuantity() * 4 );
        }
        
        orderRepo.save(order);

        if (!orderRepo.findOrderById(2000000).isEmpty()){
            Orders o = orderRepo.findOrderById(2000000).get(0);
            if (o.getCustomerid() == 2000000){
                System.out.println("*INFO: SpringCRUDQueries.testUpdateOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(o) );
            }
        }
    }
    
    private void testRemoveOrder() {
        
        if (orderRepo.findOrderById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testRemoveOrder(): order not found!");
            return;
        }
        
        Orders p = orderRepo.findOrderById(2000000).get(0);
        orderRepo.delete(p);

        if (orderRepo.findOrderById(2000000).isEmpty()){
            System.out.println("*INFO: SpringCRUDQueries.testRemoveOrder(): Ok");
        }
    }
    
    /***************************************************************************
    *
    * Insert Customer, Product and Order, setting the references between them.
    *
    ***************************************************************************/
    
    private void testInsertProductCustomerOrders_withReferences() {
        // Creating a Product
        Products product = new Products();
        product.setActor("Actor Test");
        product.setTitle("Title Test");
        product.setCategory(2001);
        product.setCommon_prod_id(1);
        product.setId_prod(2000001);
        product.setPrice(5555.0);
        product.setSpecial(1);
        
        Inventory in = new Inventory();
        in.setProd_id(2000001);
        in.setQuan_in_stock(1234);
        in.setSales(4321);
        product.setInventory(in);
        
        Categories cat = new Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        product.setCategories(cat);
        
        productRepo.save(product);
        
        // Creating a Customer
        Customers customer = new Customers();
        customer.setId_customer(2000001);
        customer.setFirstname("Customer");
        customer.setLastname("Test");        
        customer.setAddress1("Street 1");
        customer.setAddress2("Street 2");
        customer.setAge(41);
        customer.setCity("City Test");
        customer.setCountry("Brazil");
        customer.setCreditcard("VISA");
        customer.setCreditcardexpiration("07");
        customer.setCreditcardtype(1);
        customer.setEmail("customer@test.com");
        customer.setGender("M");
        customer.setUsername("customertest");
        customer.setPassword("1234");
        customer.setPhone("554699991234");
        customer.setRegion(22);
        customer.setState("PR");
        customer.setZip("85660000");
        customer.setIncome(50000);        
        customerRepo.save(customer);
        
        // Creating an Order
        Orders order = new Orders();
        order.setCustomerid(2000001);
        order.setId_order(2000001);
        order.setNetamount(100.0);
        order.setOrderdate(new Date());
        order.setTax(1.1);
        order.setTotalamount(101.0);
        
        Orderlines olines1 = new Orderlines();
        olines1.setOrderid(2000001);
        olines1.setOrderlinedate(new Date());
        olines1.setOrderlineid(2000001);
        olines1.setQuantity(1);
        olines1.setProd_id(2000001);
        olines1.setProducts(product); // setting reference to product
        
        Orderlines olines2 = new Orderlines();
        olines2.setOrderid(2000001);
        olines2.setOrderlinedate(new Date());
        olines2.setOrderlineid(2000002);
        olines2.setQuantity(2);
        olines2.setProd_id(2000001);
        olines2.setProducts(product); // setting reference to product
        
        order.setOrderlines(new ArrayList<Orderlines>());
        order.getOrderlines().add(olines1);
        order.getOrderlines().add(olines2);
        
        // Setting the reference to order-customer
        order.setCustomers(customer); // setting reference to customer
        orderRepo.save(order);
        
        // Bidirectional relationship setting.
        // However, the three lines below don't make effect in the documents at MongoDB and could be removed. 
        // The Customer don't know about your Orders because we are using document references.
        customer.setOrders(new ArrayList<Orders>());
        customer.getOrders().add(order);
        customerRepo.save(customer);
        
        // Bidirectional relationship setting.
        // Same case as described above.
        product.setOrderlines(new ArrayList<Orderlines>());
        product.getOrderlines().add(olines1);
        product.getOrderlines().add(olines2);
        productRepo.save(product);
        
        // Printing the documents
        if (!customerRepo.findCustomerById(2000001).isEmpty()){
            Customers customerFromDB = customerRepo.findCustomerById(2000001).get(0);
            if (customerFromDB.getFirstname().equals("Customer") && customerFromDB.getLastname().equals("Test")){
                System.out.println("*INFO: SpringCRUDQueries.testInsertCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customerFromDB) );
            }
        }

        if (!productRepo.findProductById(2000001).isEmpty()){
            Products productFromDB = productRepo.findProductById(2000001).get(0);
            if (productFromDB.getActor().equals("Actor Test")){
                System.out.println("*INFO: SpringCRUDQueries.testInsertProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(productFromDB) );
            }
        }
        
        if (!orderRepo.findOrderById(2000001).isEmpty()){
            Orders orderFromDB = orderRepo.findOrderById(2000001).get(0);
            if (orderFromDB.getCustomerid() == 2000001){
                System.out.println("*INFO: SpringCRUDQueries.testInsertOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(orderFromDB) );
            }
        }
    }
}