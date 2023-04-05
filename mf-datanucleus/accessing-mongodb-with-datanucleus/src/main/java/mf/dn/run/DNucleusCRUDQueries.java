/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.dn.run;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import mf.dn.model.nosql.customers.Customers;
import mf.dn.model.nosql.orders.Orderlines;
import mf.dn.model.nosql.orders.Orders;
import mf.dn.model.nosql.products.Categories;
import mf.dn.model.nosql.products.Inventory;
import mf.dn.model.nosql.products.Products;
import mf.dn.utils.CustomersObjToString;
import mf.dn.utils.OrdersObjToString;
import mf.dn.utils.ProductsObjToString;
import org.datanucleus.api.jpa.JPAEntityManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;

/**
 *
 * @author evand
 */
public class DNucleusCRUDQueries {
    private static EntityManagerFactory emfMongo;
    private static EntityManager em;
    private static boolean printDocumentAfterSave;
    
    private static PersistenceUnitMetaData getMongoPersistenceUnit(){
        PersistenceUnitMetaData pumd = new PersistenceUnitMetaData("dynamic-mongo-unit", "RESOURCE_LOCAL", null);
        pumd.addClassName("mf.dn.model.nosql.customers.Customers");
        pumd.addClassName("mf.dn.model.nosql.orders.Orders");
        pumd.addClassName("mf.dn.model.nosql.products.Products");
        pumd.addClassName("mf.dn.model.nosql.categories.Categories");
        pumd.setExcludeUnlistedClasses(true);
        pumd.addProperty("javax.persistence.jdbc.url", "mongodb://localhost/dvd-store-dn-temp");
        pumd.addProperty("datanucleus.schema.autoCreateAll", "true");
        pumd.addProperty("datanucleus.schema.validateTables", "false");  // https://www.datanucleus.org/products/accessplatform_4_1/jpa/guides/tutorial_mongodb.html
        pumd.addProperty("datanucleus.schema.validateConstraints", "false");
        pumd.addProperty("datanucleus.storeManagerType", "mongodb");
        pumd.addProperty("datanucleus.identifier.case", "MixedCase");
        return pumd;
    }
    
    public static void openConn() throws Exception {
        emfMongo = new JPAEntityManagerFactory(getMongoPersistenceUnit(), null);
        em = emfMongo.createEntityManager();
    }
    
    public static void closeConn() throws Exception {
        em.close();
        if (emfMongo != null){
            emfMongo.close();
            emfMongo = null;
        }
    }
    
    // CRUD operations test. Uncomment the code below to run the test.
    public static void main(String args[]) throws Exception{
        printDocumentAfterSave = true;
        
        testInsertCategory();
        testUpdateCategory();
//        testRemoveCategory();
        
        testInsertProduct();
        testUpdateProduct();
//        testRemoveProduct();
//
        testInsertCustomer();
        testUpdateCustomer();
//        testRemoveCustomer();
//
        testInsertOrder();
        testUpdateOrder();
//        testRemoveOrder();
        
        // The test below write the objects into MongoDB using the Data Nucleus format
        //   to stablish reference relationships, where the relationship field must store
        //   the full qualified class name and the value of the related object as a String.
        //   e.g. mf.dn.model.nosql.customers.Customers:1
        // Warning: Metamorfose doesn't use this format to write objects, and
        //   as a result the related data cannot be loaded by Data Nucleus.
        testInsertProductCustomerOrders_withReferences();
    }
    
    /***************************************************************************
    *
    * entity: Categories
    *
    ***************************************************************************/
    
    private static void testInsertCategory() throws Exception {
        openConn();
        
        mf.dn.model.nosql.categories.Categories cat = new mf.dn.model.nosql.categories.Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        
        em.persist(cat);

        mf.dn.model.nosql.categories.Categories catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
        if (catFromDB != null){
            if (catFromDB.getCategoryname().equals("Category Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertCategory(): Ok");
                if (printDocumentAfterSave) System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
        closeConn();
    }
    
    private static void testUpdateCategory() throws Exception {
        openConn();
        
        mf.dn.model.nosql.categories.Categories catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
        if (catFromDB == null){
            closeConn();
            System.out.println("*ERROR: DNucleusCRUDQueries.testUpdateCategory(): category not found!");
            return;
        }
        
        catFromDB.setId_category(2000);
        catFromDB.setCategoryname("Category Test Test");
        
        em.merge(catFromDB);

        catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
        if (catFromDB != null){
            if (catFromDB.getCategoryname().equals("Category Test Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testUpdateCategory(): Ok");
                if (printDocumentAfterSave) System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
        closeConn();
    }
    
    private static void testRemoveCategory() throws Exception {
        openConn();
        EntityTransaction tx = em.getTransaction(); // https://www.datanucleus.org/products/accessplatform_6_0/jpa/persistence.html#transaction_local
        
        try{
            tx.begin();
            mf.dn.model.nosql.categories.Categories catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
            if (catFromDB == null){
                System.out.println("*ERROR: DNucleusCRUDQueries.testRemoveCategory(): category not found!");
                tx.rollback();
                closeConn();
                return;
            } else {
                em.remove(catFromDB);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
        }

        mf.dn.model.nosql.categories.Categories catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
        if (catFromDB == null){
            System.out.println("*INFO: DNucleusCRUDQueries.testRemoveCategory(): Ok");
        }
        closeConn();
    }
    
    
    /***************************************************************************
    *
    * entity: Products
    *
    ***************************************************************************/
    
    private static void testInsertProduct() throws Exception {
        openConn();
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
        
        em.persist(p);

        Products product = em.find(Products.class, 2000000);
        if (product != null){
            if (product.getActor().equals("Actor Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(product) );
            }
        }
        closeConn();
    }
    
    private static void testUpdateProduct() throws Exception {
        openConn();
        
        Products p = em.find(Products.class, 2000000);
        if (p == null){
            closeConn();
            System.out.println("*ERROR: DNucleusCRUDQueries.testUpdateProduct(): product not found!");
            return;
        }
        
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
        
        em.merge(p);

        Products product = em.find(Products.class, 2000000);
        if (product != null){
            if (product.getActor().equals("Actor Test Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testUpdateProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(product) );
            }
        }
        closeConn();
    }
    
    private static void testRemoveProduct() throws Exception {
        openConn();
        EntityTransaction tx = em.getTransaction(); // https://www.datanucleus.org/products/accessplatform_6_0/jpa/persistence.html#transaction_local
        
        try{
            tx.begin();
            Products p = em.find(Products.class, 2000000);
            if (p == null){
                System.out.println("*ERROR: DNucleusCRUDQueries.testRemoveProduct(): product not found!");
                tx.rollback();
                closeConn();
                return;
            } else {
                em.remove(p);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
        }

        Products product = em.find(Products.class, 2000000);
        if (product == null){
            System.out.println("*INFO: DNucleusCRUDQueries.testRemoveProduct(): Ok");
        }
        closeConn();
    }
    
    /***************************************************************************
    *
    * entity: Customer
    *
    ***************************************************************************/
    
    private static void testInsertCustomer() throws Exception {
        openConn();
        
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
        
        
        Customers customer = em.find(Customers.class, 2000000);
        if (customer != null){
            if (customer.getFirstname().equals("Customer") && customer.getLastname().equals("Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customer) );
            }
        }
        closeConn();
    }
    
    private static void testUpdateCustomer() throws Exception {
        openConn();
        
        Customers c = em.find(Customers.class, 2000000);
        if (c == null){
            closeConn();
            System.out.println("*ERROR: DNucleusCRUDQueries.testUpdateCustomer(): customer not found!");
            return;
        }
        
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

        Customers customer = em.find(Customers.class, 2000000);
        if (customer != null){
            if (customer.getFirstname().equals("Customer2") && customer.getLastname().equals("Test2")){
                System.out.println("*INFO: DNucleusCRUDQueries.testUpdateCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customer) );
            }
        }
        closeConn();
    }
    
    private static void testRemoveCustomer() throws Exception {
        openConn();
        EntityTransaction tx = em.getTransaction(); // https://www.datanucleus.org/products/accessplatform_6_0/jpa/persistence.html#transaction_local
        
        try{
            tx.begin();
            Customers c = em.find(Customers.class, 2000000);
            if (c == null){
                System.out.println("*ERROR: DNucleusCRUDQueries.testRemoveCustomer(): customer not found!");
                tx.rollback();
                closeConn();
                return;
            } else {
                em.remove(c);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
        }

        Customers customer = em.find(Customers.class, 2000000);
        if (customer == null){
            System.out.println("*INFO: DNucleusCRUDQueries.testRemoveCustomer(): Ok");
        }
        closeConn();
    }
    
    /***************************************************************************
    *
    * entity: Orders
    *
    ***************************************************************************/
    
    private static void testInsertOrder() throws Exception {
        openConn();
        
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
        
        em.persist(order);

        Orders o = em.find(Orders.class, 2000000);
        if (o != null){
            if (o.getCustomerid() == 2000000){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(o) );
            }
        }
        closeConn();
    }
    
    private static void testUpdateOrder() throws Exception {
        openConn();
        
        Orders order = em.find(Orders.class, 2000000);
        if (order == null){
            closeConn();
            System.out.println("*ERROR: DNucleusCRUDQueries.testUpdateOrder(): order not found!");
            return;
        }
        
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

        Orders o = em.find(Orders.class, 2000000);
        if (o != null){
            if (o.getCustomerid() == 2000000){
                System.out.println("*INFO: DNucleusCRUDQueries.testUpdateOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(o) );
            }
        }
        closeConn();
    }
    
    private static void testRemoveOrder() throws Exception {
        openConn();
        EntityTransaction tx = em.getTransaction(); // https://www.datanucleus.org/products/accessplatform_6_0/jpa/persistence.html#transaction_local
        
        try{
            tx.begin();
            Orders p = em.find(Orders.class, 2000000);
            if (p == null){
                System.out.println("*ERROR: DNucleusCRUDQueries.testRemoveOrder(): order not found!");
                tx.rollback();
                closeConn();
                return;
            } else {
                em.remove(p);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
        }

        Orders order = em.find(Orders.class, 2000000);
        if (order == null){
            System.out.println("*INFO: DNucleusCRUDQueries.testRemoveOrder(): Ok");
        }
        closeConn();
    }
    
    
    
    /***************************************************************************
    *
    * Insert Customer, Product and Order, setting the references between them.
    *
    ***************************************************************************/
    
    private static void testInsertProductCustomerOrders_withReferences() throws Exception {
        System.out.println("\n testInsertProductCustomerOrders_withReferences:");
        
        // 1 - Persisting the objects into the NoSQL.
        openConn();
        
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
        
        em.persist(product);
        
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
        em.persist(customer);
        
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
        
        Orderlines olines2 = new Orderlines();
        olines2.setOrderid(2000001);
        olines2.setOrderlinedate(new Date());
        olines2.setOrderlineid(2000002);
        olines2.setQuantity(2);
        olines2.setProd_id(2000001);
        
        order.setOrderlines(new ArrayList<Orderlines>());
        order.getOrderlines().add(olines1);
        order.getOrderlines().add(olines2);
        
        // Setting the reference to order-customer
        order.setCustomers(customer);
        em.persist(order);
        
        // Bidirectional relationships need adding the new Order to Customer object.
        customer.setOrders(new ArrayList<Orders>());
        customer.getOrders().add(order);
        em.persist(customer);
        
        // Bidirectional relationships need adding the new Orderlines to Products object.
        product.setOrderlines(new ArrayList<Orderlines>());
        product.getOrderlines().add(olines1);
        product.getOrderlines().add(olines2);
        em.persist(product);
        
        closeConn();
        
        // 2 - Reading the objects from the NoSQL.
        
        openConn();
        
        // Printing the documents
        Customers customerFromDB = em.find(Customers.class, 2000001);
        if (customerFromDB != null){
            if (customerFromDB.getFirstname().equals("Customer") && customerFromDB.getLastname().equals("Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customerFromDB) );
            }
        }

        Products productFromDB = em.find(Products.class, 2000001);
        if (productFromDB != null){
            if (productFromDB.getActor().equals("Actor Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(productFromDB) );
            }
        }
        
        Orders orderFromDB = em.find(Orders.class, 2000001);
        if (orderFromDB != null){
            if (orderFromDB.getCustomerid() == 2000001){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(orderFromDB) );
            }
        }
        
        closeConn();
    }
}