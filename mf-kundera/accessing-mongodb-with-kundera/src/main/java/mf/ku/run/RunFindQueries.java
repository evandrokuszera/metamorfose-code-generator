/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.ku.run;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mf.ku.utils.CustomersObjToString;
import mf.ku.utils.OrdersObjToString;
import mf.ku.utils.ProductsObjToString;

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
    public static void main(String args[]) throws Exception{
        EntityManager em = KunderaConnection.openConn();
        q1FindCustomerById(em);
        q1FindOrderById(em);
        q1FindProductById(em);
        
//        q2FindCustomerByFirstnameAndLastname(em);
//        
//        q3FindProductByActor1OrActor2(em);
//        
//        q4FindCustomerByFirstnameStartingWith(em);
//        
//        q5FindOrderBetweenDate1AndDate2(em);
//        
//        q6FindOrdersJoinCustomersByTotalamountAndState(em);
        
        KunderaConnection.closeConn();
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
//        Query query = emMongo.createQuery("SELECT o FROM Orders o JOIN o.customers "
//                + "WHERE o.totalamount >= :totalamount AND o.customers.state = :state");
        // The query below also works!
        Query query = emMongo.createQuery("SELECT o FROM Orders o "
                + "WHERE o.totalamount >= :totalamount AND o.customers.state = :state");
        query.setParameter("state", "ME");
        query.setParameter("totalamount", 400);
        List result =  query.getResultList();
        System.out.printf("--> q6:findOrdersJoinCustomersByTotalamountAndState: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println( OrdersObjToString.get(o) );
    }
}