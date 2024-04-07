/***************************************************************************
*
* entity: Orders
*
***************************************************************************/
package mf.ku.run;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import mf.ku.model.nosql.orders.Orderlines;
import mf.ku.model.nosql.orders.Orders;
import mf.ku.utils.OrdersObjToString;

/**
 *
 * @author evand
 */
public class CrudOrder {
    private static EntityManager em = KunderaConnection.em;
        
    public static void testInsertOrder() throws Exception {
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
    
    public static void testReadOrder() throws Exception {
        Orders o = em.find(Orders.class, 2000000);
        if (o != null){
            if (o.getCustomerid() == 2000000){
                System.out.println("*INFO: KunderaCRUDQueries.testReadOrder(): Ok");
                System.out.println( OrdersObjToString.get(o) );
            }
        }
    }
    
    public static void testUpdateOrder() throws Exception {
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
    
    public static void testRemoveOrder() throws Exception {
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
