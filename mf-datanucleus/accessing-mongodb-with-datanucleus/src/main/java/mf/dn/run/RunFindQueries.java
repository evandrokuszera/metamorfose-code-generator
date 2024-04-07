/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.dn.run;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import mf.dn.model.nosql.orders.Orderlines;
import mf.dn.model.nosql.orders.Orders;
import mf.dn.utils.CustomersObjToString;
import mf.dn.utils.OrdersObjToString;
import mf.dn.utils.ProductsObjToString;
import org.datanucleus.api.jpa.JPAEntityManagerFactory;

/**
 *
 * @author evand
 */
public class RunFindQueries {
    private static long init = 0, end = 0;
    private static void startTime(){ init = System.currentTimeMillis(); }
    private static long stopTime(boolean print){ 
        end = System.currentTimeMillis(); 
        long time = end - init;
        if (print) System.out.printf("Time: %dms (%.3fs) \n", time, (double)time / 1000);
        return time;
    }
           
    // Reading queries. Uncomment the code below to run the test.
    public static void main(String[] args) {
        EntityManagerFactory emfMongo = new JPAEntityManagerFactory(DNucleusConnection.getMongoPersistenceUnit(), null);
        EntityManager emMongo = emfMongo.createEntityManager();
        
        //q1
        q1FindCustomerById(emMongo);
        q1FindOrderById(emMongo);
        q1FindProductById(emMongo);
        
        q2FindCustomerByFirstnameAndLastname(emMongo);
        q3FindProductByActor1OrActor2(emMongo);
        q4FindCustomerByFirstnameStartingWith(emMongo);
        q5FindOrderBetweenDate1AndDate2(emMongo);
        q6FindOrdersJoinCustomersByTotalamountAndState(emMongo);
//        q7FindOrdersByOrderlineProductCategory(emMongo); // this query is not working! We think is about that Orderlines is not an @Entity.
        q7FindOrdersByOrderlineProductCategory_Manually(emMongo); // an alternative of the query above.
        
        emMongo.close();
        emfMongo.close();
    }
    
    // Q1 - Customers
    public static void q1FindCustomerById(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT c FROM Customers c WHERE c.id_customer = :id");
        query.setParameter("id", 15000);
        List result = query.getResultList();
        System.out.printf("--> q1:findCustomerById: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( CustomersObjToString.get(o) );
    }
    
    // Q1 - Orders
    public static void q1FindOrderById(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT c FROM Orders c WHERE c.id_order = :id");
        query.setParameter("id", 6001);
        List result = query.getResultList();
        System.out.printf("--> q1:q1FindOrderById: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( OrdersObjToString.get(o) );
    }
    
    // Q1 - Products
    public static void q1FindProductById(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT p FROM Products p WHERE p.id_prod = :id   ");
        query.setParameter("id", 6001);
        List result = query.getResultList();
        System.out.printf("--> q1:q1FindProductById: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( ProductsObjToString.get(o) );
    }
    
    // Q2 - findCustomerByFirstnameAndLastname
    public static void q2FindCustomerByFirstnameAndLastname(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT c FROM Customers c WHERE c.firstname = :firstname and c.lastname = :lastname");
        query.setParameter("firstname", "SRIMVH");
        query.setParameter("lastname", "SLZCISTEWL");
        List result = query.getResultList();
        System.out.printf("--> q2:findCustomerByFirstnameAndLastname: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( CustomersObjToString.get(o) );
    }
    
    // Q3 - findProductByActor1OrActor2
    public static void q3FindProductByActor1OrActor2(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT p FROM Products p WHERE p.actor = :actor1 OR p.actor = :actor2");
        query.setParameter("actor1", "PENELOPE GUINESS");
        query.setParameter("actor2", "ADAM HOFFMAN");
        List result = query.getResultList();
        System.out.printf("--> q3:findProductByActor1OrActor2: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( ProductsObjToString.get(o) );
    }
    
    // Q4 - findCustomerByFirstnameStartingWith
    public static void q4FindCustomerByFirstnameStartingWith(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT c FROM Customers c WHERE c.firstname LIKE :name");
//        Query query = emMongo.createQuery("SELECT c FROM Customers c WHERE c.firstname LIKE '^AA'");
        query.setParameter("name", "^AA");
        List result = query.getResultList();
        System.out.printf("--> q4:findCustomerByFirstnameStartingWith: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( CustomersObjToString.get(o) );
    }
    
    // Q5 - findOrderBetweenDate1AndDate2
    public static void q5FindOrderBetweenDate1AndDate2(EntityManager emMongo){
        LocalDate ini = LocalDate.parse("02/11/2009 00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm"));
        LocalDate fim = LocalDate.parse("03/11/2009 00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm"));
        Date ini1 = Date.from(ini.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fim1 = Date.from(fim.atStartOfDay(ZoneId.systemDefault()).toInstant());
        startTime();
        Query query = emMongo.createQuery("SELECT o FROM Orders o WHERE o.orderdate BETWEEN :date1 AND :date2");
        query.setParameter("date1", ini1);
        query.setParameter("date2", fim1);
        List result = query.getResultList();
        System.out.printf("--> q5:findOrderBetweenDate1AndDate2: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( OrdersObjToString.get(o) );
    }
    
    
    // Q6 - findOrdersJoinCustomersByTotalamountAndState
    public static void q6FindOrdersJoinCustomersByTotalamountAndState(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT o FROM Orders o JOIN o.customers "
                + "WHERE o.totalamount >= :totalamount AND o.customers.state = :state");
        // The query below also works!
        //Query query = emMongo.createQuery("SELECT o FROM Orders o "
        //        + "WHERE o.totalamount >= :totalamount AND o.customers.state = :state");
        query.setParameter("state", "ME");
        query.setParameter("totalamount", 400);
        List result =  query.getResultList();
        System.out.printf("--> q6:findOrdersJoinCustomersByTotalamountAndState: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( OrdersObjToString.get(o) );
    }
    
    /// Q7 - findOrdersByOrderlineProductCategory
    // It's not working! It seems Data Nucleus can't support JOIN between entities that aren't tagged as @Entity (root level).
    // Below, the join condititon between Orders and Products is through Orderlines embedded field, what is not possible.
    // An alternative is implement the query manually, loading all the Orders, then filter only the Orders with products.category == 2.
    public static void q7FindOrdersByOrderlineProductCategory(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT o FROM Orders o "
                + "WHERE o.orderlines.products.category = :category");
        query.setParameter("category", 2);
        List result =  query.getResultList();
        System.out.printf("--> q7:findOrdersByOrderlineProductCategory: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( OrdersObjToString.get(o) );
    }
    
    // An alternative is implement the query manually, loading all the Orders, then filter only the Orders with products.category == 2.
    public static void q7FindOrdersByOrderlineProductCategory_Manually(EntityManager emMongo){
        startTime();
        Query query = emMongo.createQuery("SELECT o FROM Orders o");
        int count = 0;
        for (Object o : query.getResultList()){
            Orders order = (Orders) o;
            for (Orderlines oline : order.getOrderlines()){
                if (oline.getProducts().getCategories().getId_category() == 2){
//                    System.out.println( OrdersObjToString.get(o) );
                    count++;
                    continue;
                }
            }
        }
        System.out.printf("--> q7:findOrdersByOrderlineProductCategory: documents(%d) | ", count);
        stopTime(true);
    }
    
}