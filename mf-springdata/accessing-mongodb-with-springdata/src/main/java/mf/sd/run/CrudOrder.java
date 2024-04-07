/***************************************************************************
*
* entity: Order
*
***************************************************************************/
package mf.sd.run;

import java.util.ArrayList;
import java.util.Date;
import mf.sd.model.nosql.orders.Orderlines;
import mf.sd.model.nosql.orders.Orders;
import mf.sd.repositories.OrderRepository;
import mf.sd.utils.OrdersObjToString;

/**
 *
 * @author evand
 */
public class CrudOrder {
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;

    public static void testInsertOrder(OrderRepository orderRepo) {
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
    
    public static void testUpdateOrder(OrderRepository orderRepo) {
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
    
    public static void testRemoveOrder(OrderRepository orderRepo) {
        
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
}
