/***************************************************************************
*
* entity: Products
*
***************************************************************************/
package mf.dn.run;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import mf.dn.model.nosql.customers.Customers;
import mf.dn.model.nosql.orders.Orderlines;
import mf.dn.model.nosql.orders.Orders;
import mf.dn.model.nosql.products.Categories;
import mf.dn.model.nosql.products.Inventory;
import mf.dn.model.nosql.products.Products;
import static mf.dn.run.DNucleusConnection.closeConn;
import static mf.dn.run.DNucleusConnection.openConn;
import mf.dn.utils.CustomersObjToString;
import mf.dn.utils.OrdersObjToString;
import mf.dn.utils.ProductsObjToString;

/**
 *
 * @author evand
 */
public class CrudAllEntitiesWithReferences {
    private static EntityManager em;
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;
    
    public static void testInsert() throws Exception {
        
        System.out.println("\n testInsertProductCustomerOrders_withReferences:");
        
        em = openConn();
        
        // 1 - Persisting the objects into the NoSQL.
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
        olines1.setProducts(product); // Setting the reference to orderline-product
        
        Orderlines olines2 = new Orderlines();
        olines2.setOrderid(2000001);
        olines2.setOrderlinedate(new Date());
        olines2.setOrderlineid(2000002);
        olines2.setQuantity(2);
        olines2.setProd_id(2000001);
        olines2.setProducts(product); // Setting the reference to orderline-product
        
        order.setOrderlines(new ArrayList<Orderlines>());
        order.getOrderlines().add(olines1);
        order.getOrderlines().add(olines2);
        
        //////////////////////////////////////
        // Setting the reference to order-customer
        order.setCustomers(customer);
        em.persist(order);
        
        //////////////////////////////////////
        // Bidirectional relationships need adding the new Order to Customer object.
        customer.setOrders(new ArrayList<Orders>());
        customer.getOrders().add(order);
        em.persist(customer);
        
        //////////////////////////////////////
        // Bidirectional relationships need adding the new Orderlines to Products object.
        product.setOrderlines(new ArrayList<Orderlines>());
        product.getOrderlines().add(olines1);
        product.getOrderlines().add(olines2);
        em.persist(product);
        
        closeConn(em);
    }
    
    public static void testRead() throws Exception {
        
        // 2 - Reading the objects from the NoSQL.
        
        em = openConn();
        
        // Printing the documents
        Customers customerFromDB = em.find(Customers.class, 2000001);
        if (customerFromDB != null){
            if (customerFromDB.getFirstname().equals("Customer") && customerFromDB.getLastname().equals("Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testReadCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customerFromDB) );
            }
        }
        System.out.println("");
        
        Products productFromDB = em.find(Products.class, 2000001);
        if (productFromDB != null){
            if (productFromDB.getActor().equals("Actor Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testReadProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(productFromDB) );
            }
        }
        System.out.println("");
        
        Orders orderFromDB = em.find(Orders.class, 2000001);
        if (orderFromDB != null){
            if (orderFromDB.getCustomerid() == 2000001){
                System.out.println("*INFO: DNucleusCRUDQueries.testReadOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(orderFromDB) );
            }
        }
        System.out.println("");
        
        closeConn(em);
    }
    
}