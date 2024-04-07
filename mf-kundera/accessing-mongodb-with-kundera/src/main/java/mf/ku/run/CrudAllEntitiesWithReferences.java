/***************************************************************************
*
* entity: Products
*
***************************************************************************/
package mf.ku.run;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import mf.ku.model.nosql.customers.Customers;
import mf.ku.model.nosql.orders.Orderlines;
import mf.ku.model.nosql.orders.Orders;
import mf.ku.model.nosql.products.Categories;
import mf.ku.model.nosql.products.Inventory;
import mf.ku.model.nosql.products.Products;
import mf.ku.utils.CustomersObjToString;
import mf.ku.utils.OrdersObjToString;
import mf.ku.utils.ProductsObjToString;

/**
 *
 * @author evand
 */
public class CrudAllEntitiesWithReferences {
    private static EntityManager em = KunderaConnection.em;
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;
    
    public static void testInsert() throws Exception {
        
        System.out.println("\n testInsertProductCustomerOrders_withReferences:");
        
        // 1 - Persisting the objects into the NoSQL.
        
        em.getTransaction().begin();
        
        //////////////////////////////////////
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
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        //////////////////////////////////////
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
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        //////////////////////////////////////
        // Creating an Order with two orderlines
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
        //olines1.setProducts(product); // THIS LINE IS NOT SUPPORT (PRODUCT IS AN @ENTITY)
        
        Orderlines olines2 = new Orderlines();
        olines2.setOrderid(2000001);
        olines2.setOrderlinedate(new Date());
        olines2.setOrderlineid(2000002);
        olines2.setQuantity(2);
        olines2.setProd_id(2000001);
        //olines2.setProducts(product); // THIS LINE IS NOT SUPPORT (PRODUCT IS AN @ENTITY)
        
        order.setOrderlines(new ArrayList<Orderlines>());
        order.getOrderlines().add(olines1);
        order.getOrderlines().add(olines2);
        
        //////////////////////////////////////
        // Setting the reference to order-customer
        order.setCustomers(customer);
        em.persist(order);        
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        
        //////////////////////////////////////
        // Bidirectional relationships need adding the new Order to Customer object.
        customer.setOrders(new ArrayList<Orders>());
        customer.getOrders().add(order);
        em.merge(customer);        
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        
        // Bidirectional relationships need adding the new Orderlines to Products object.
        product.setOrderlines(new ArrayList<Orderlines>());
        product.getOrderlines().add(olines1);
        product.getOrderlines().add(olines2);
        em.merge(product);
        
        em.getTransaction().commit();
    }
    
    public static void testRead() throws Exception {
        
        // 2 - Reading the objects from the NoSQL.
        
        // Printing the documents
        Customers customerFromDB = em.find(Customers.class, 2000001);
        if (customerFromDB != null){
            if (customerFromDB.getFirstname().equals("Customer") && customerFromDB.getLastname().equals("Test")){
                System.out.println("*INFO: KunderaCRUDQueries.testReadCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customerFromDB) );
            }
        }
        System.out.println("");
        
        Products productFromDB = em.find(Products.class, 2000001);
        if (productFromDB != null){
            if (productFromDB.getActor().equals("Actor Test")){
                System.out.println("*INFO: KunderaCRUDQueries.testReadProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(productFromDB) );
            }
        }
        System.out.println("");
        
        Orders orderFromDB = em.find(Orders.class, 2000001);
        if (orderFromDB != null){
            if (orderFromDB.getCustomerid() == 2000001){
                System.out.println("*INFO: KunderaCRUDQueries.testReadOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(orderFromDB) );
            }
        }
        System.out.println("");
    }
    
}