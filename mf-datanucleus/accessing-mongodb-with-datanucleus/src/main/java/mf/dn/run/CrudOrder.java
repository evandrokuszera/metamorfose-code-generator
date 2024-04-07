/***************************************************************************
*
* entity: Orders
*
***************************************************************************/
package mf.dn.run;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import mf.dn.model.nosql.orders.Orderlines;
import mf.dn.model.nosql.orders.Orders;
import static mf.dn.run.DNucleusConnection.closeConn;
import static mf.dn.run.DNucleusConnection.openConn;
import mf.dn.utils.OrdersObjToString;

/**
 *
 * @author evand
 */
public class CrudOrder {
    private static EntityManager em;
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;
    
    public static void testInsertOrder() throws Exception {
        em = openConn();
        
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
        closeConn(em);
    }
    
    public static void testUpdateOrder() throws Exception {
        em = openConn();
        
        Orders order = em.find(Orders.class, 2000000);
        if (order == null){
            closeConn(em);
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
        closeConn(em);
    }
    
    public static void testRemoveOrder() throws Exception {
        em = openConn();
        EntityTransaction tx = em.getTransaction(); // https://www.datanucleus.org/products/accessplatform_6_0/jpa/persistence.html#transaction_local
        
        try{
            tx.begin();
            Orders p = em.find(Orders.class, 2000000);
            if (p == null){
                System.out.println("*ERROR: DNucleusCRUDQueries.testRemoveOrder(): order not found!");
                tx.rollback();
                closeConn(em);
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
        closeConn(em);
    }
}