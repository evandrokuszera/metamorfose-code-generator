/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.ku.run;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import mf.ku.model.nosql.customers.Customers;
import mf.ku.model.nosql.orders.Orderlines;
import mf.ku.model.nosql.orders.Orders;
import mf.ku.model.nosql.categories.Categories;
import mf.ku.model.nosql.products.Inventory;
import mf.ku.model.nosql.products.Products;
import mf.ku.utils.CustomersObjToString;
import mf.ku.utils.OrdersObjToString;
import mf.ku.utils.ProductsObjToString;

/**
 *
 * @author evand
 */
public class KunderaCRUDQueries {
    private static final String PU = "mongo_pu";
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    public static void openConn() throws Exception {
        emf = Persistence.createEntityManagerFactory(PU);
        em = emf.createEntityManager();
    }
    
    public static void closeConn() throws Exception {
        em.close();
        if (emf != null){
            emf.close();
            emf = null;
        }
        em = null;
    }
    
    // CRUD operations test. Uncomment the code below to run the test.
    public static void main(String args[]) throws Exception{
        openConn();
        
        // WARNING:
        // There are an instability to execute the tests for the Customers and Orders entities, that have @OneToMany relationships.
        // When we loaded a Customer from database, sometimes the Impetus Kundera can't load the associated Orders and throws an exception.
        // But sometimes, Impetus Kundera load all the entities and their associated data.
        
        testInsertCategory();
        testInsertProduct();
        testInsertCustomer();
        testInsertOrder();

//        testUpdateCategory(); 
//        testUpdateProduct();
//        testUpdateCustomer();
//        testUpdateOrder();
        
        testReadCategory();
        testReadProduct();
        testReadCustomer();
        testReadOrder();
        
        // Kundera verifies referential integrity, so we remove the document only after the tests above.
//        testRemoveOrder();
//        testRemoveCategory();
//        testRemoveProduct();
//        testRemoveCustomer();
        
        closeConn();
    }
    
    
    
    /***************************************************************************
    *
    * entity: Categories
    *
    ***************************************************************************/
    
    private static void testInsertCategory() throws Exception {
        em.getTransaction().begin();
        
        Categories cat = new Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        
        em.persist(cat);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testInsertCategory(): Ok");
    }
    
    private static void testReadCategory() throws Exception {
        Categories catFromDB = em.find(Categories.class, 2000);
        if (catFromDB != null){
            if (catFromDB.getId_category() == 2000){
                System.out.println("*INFO: KunderaCRUDQueries.testReadCategory(): Ok");
                System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
    }
    
    private static void testUpdateCategory() throws Exception {
        Categories cat = em.find(Categories.class, 2000);
        if (cat == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testUpdateCategory(): category not found!");
            return;
        }
        
        em.getTransaction().begin();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test Test");        
        em.merge(cat);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testUpdateCategory(): Ok");
    }
    
    private static void testRemoveCategory() throws Exception {
        Categories c = em.find(Categories.class, 2000);
        if (c == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testRemoveCategory(): category not found!");
            return;
        }
        
        em.getTransaction().begin();        
        em.remove(c);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testRemoveCategory(): Ok");
    }
    
    
    
    
    /***************************************************************************
    *
    * entity: Products
    *
    ***************************************************************************/
    
    private static void testInsertProduct() throws Exception {
        em.getTransaction().begin();
        
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
        
        mf.ku.model.nosql.products.Categories cat = new mf.ku.model.nosql.products.Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        p.setCategories(cat);
        
        em.persist(p);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testInsertProduct(): Ok");
    }
    
    private static void testReadProduct() throws Exception {
        Products product = em.find(Products.class, 2000000);
        if (product != null){
            if (product.getId_prod() == 2000000){
                System.out.println("*INFO: KunderaCRUDQueries.testReadProduct(): Ok");
                System.out.println( ProductsObjToString.get(product) );
            }
        }
    }
    
    private static void testUpdateProduct() throws Exception {
        Products p = em.find(Products.class, 2000000);
        if (p == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testUpdateProduct(): product not found!");
            return;
        }
        
        em.getTransaction().begin();
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
        
        mf.ku.model.nosql.products.Categories cat = new mf.ku.model.nosql.products.Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        p.setCategories(cat);        
        em.merge(p);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testUpdateProduct(): Ok");
    }
    
    private static void testRemoveProduct() throws Exception {
        Products p = em.find(Products.class, 2000000);
        if (p == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testRemoveProduct(): product not found!");
            return;
        }
        
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testRemoveProduct(): Ok");
    }
    
    
    
    
    /***************************************************************************
    *
    * entity: Customer
    *
    ***************************************************************************/
    
    private static void testInsertCustomer() throws Exception {
        em.getTransaction().begin();
        
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
        em.persist(c);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testInsertCustomer(): Ok");
    }
    
    private static void testReadCustomer() throws Exception {
        Customers customer = em.find(Customers.class, 2000000);
        if (customer != null){
            if (customer.getId_customer() == 2000000){
                System.out.println("*INFO: KunderaCRUDQueries.testReadCustomer(): Ok");
                System.out.println( CustomersObjToString.get(customer) );
            }
        }
    }
    
    private static void testUpdateCustomer() throws Exception {
        Customers c = em.find(Customers.class, 2000000);
        if (c == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testUpdateCustomer(): customer not found!");
            return;
        }
        
        em.getTransaction().begin();
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
        em.merge(c);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testUpdateCustomer(): Ok");
    }
    
    private static void testRemoveCustomer() throws Exception {
        Customers c = em.find(Customers.class, 2000000);
        if (c == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testRemoveCustomer(): customer not found!");
            return;
        }
        
        em.getTransaction().begin();
        em.remove(c);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testRemoveCustomer(): Ok");
    }
    
    
    
    
    /***************************************************************************
    *
    * entity: Orders
    *
    ***************************************************************************/
    
    private static void testInsertOrder() throws Exception {
        em.getTransaction().begin();
        
        Orders order = new Orders();
        order.setCustomerid(2000000);
        order.setId_order(2000000);
        order.setNetamount(100.0);
        order.setOrderdate(new Date());
        order.setTax(1.1);
        order.setTotalamount(101.0);
                
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
        
        em.persist(order);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testInsertOrder(): Ok");
    }
    
    private static void testReadOrder() throws Exception {
        Orders o = em.find(Orders.class, 2000000);
        if (o != null){
            if (o.getCustomerid() == 2000000){
                System.out.println("*INFO: KunderaCRUDQueries.testReadOrder(): Ok");
                System.out.println( OrdersObjToString.get(o) );
            }
        }
    }
    
    private static void testUpdateOrder() throws Exception {
        Orders order = em.find(Orders.class, 2000000);
        if (order == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testUpdateOrder(): order not found!");
            return;
        }
        
        em.getTransaction().begin();
        order.setCustomerid(2000000);
        order.setId_order(2000000);
        order.setNetamount(500.0);
        order.setOrderdate(new Date());
        order.setTax(5.1);
        order.setTotalamount(501.0);
                
        for (Orderlines ol : order.getOrderlines()){
            ol.setQuantity( ol.getQuantity() * 4 );
        }
        
        em.merge(order);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testUpdateOrder(): Ok");
    }
    
    private static void testRemoveOrder() throws Exception {
        Orders p = em.find(Orders.class, 2000000);
        if (p == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testRemoveOrder(): order not found!");
            return;
        }

        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testRemoveOrder(): Ok");
    }
    
}