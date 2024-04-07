/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.run;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import mf.sd.model.nosql.orders.Orderlines;
import mf.sd.model.nosql.orders.Orders;
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
public class RunFindQueries {
    private static long init = 0, end = 0;
    private static void startTime(){ init = System.currentTimeMillis(); }
    private static long stopTime(boolean print){ 
        end = System.currentTimeMillis(); 
        long time = end - init;
        if (print) System.out.printf("Time: %dms (%.3fs) \n", time, (double)time / 1000);
        return time;
    }
    
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;
    
    // Reading queries. Uncomment the code below to run the test.
    public void run(){
        q1FindCustomerById();
        q1FindOrderById();
        q1FindProductById();
//        q2FindCustomerByFirstnameAndLastname();
//        q3FindProductByActor1OrActor2();
//        q4FindByCustomerFirstnameStartingWith();
//        q5FindOrderBetweenDate1AndDate2();
//        q6FindOrdersByTotalAmountAndCustomerState();
//        q7FindOrdersByOrderlineProductCategory();
    }
    
    // Q1 - Customers
    public void q1FindCustomerById(){
        startTime();
        List result = customerRepo.findCustomerById(15000);
        System.out.printf("---> q1:findCustomerById: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println(  CustomersObjToString.get(o)  );
    }
    
    // Q1 - Orders
    public void q1FindOrderById(){
        startTime();
        List result = orderRepo.findOrderById(6001);
        System.out.printf("---> q1:findOrderById: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println(  OrdersObjToString.get(o)  );
    }
    
    // Q1 - Products
    public void q1FindProductById(){
        startTime();
        List result = productRepo.findProductById(6001);
        System.out.printf("---> q1:q1FindProductById: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println(  ProductsObjToString.get(o)  );
    }
    ///////////////////////////////////////////////////////////
    
    // Q2 - findCustomerByFirstnameAndLastname
    public void q2FindCustomerByFirstnameAndLastname(){
        startTime();
        List result = customerRepo.findCustomerByFirstnameAndLastname("SRIMVH", "SLZCISTEWL");
        System.out.printf("---> q2:findCustomerByFirstnameAndLastname: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println(  CustomersObjToString.get(o)  );
    }
    
    // Q3 - findProductByActor1OrActor2
    public void q3FindProductByActor1OrActor2(){
        startTime();
        List result = productRepo.findProductByActor1OrActor2("PENELOPE GUINESS", "ADAM HOFFMAN");
        System.out.printf("---> q3:findProductByActor1OrActor2: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println(  ProductsObjToString.get(o)  );
    }
    
    // Q4 - findByCustomerFirstnameStartingWith
    public void q4FindByCustomerFirstnameStartingWith(){
        startTime();
        List result = customerRepo.findCustomerByFirstnameStartingWith("AA");
        System.out.printf("---> q4:findProductByActor1OrActor2: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println(  CustomersObjToString.get(o)  );
    }
    
    // Q5 - findOrderBetweenDate1AndDate2
    public void q5FindOrderBetweenDate1AndDate2(){
        LocalDate ini = LocalDate.parse("02/11/2009 00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm"));
        LocalDate fim = LocalDate.parse("03/11/2009 00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm"));
        startTime();
        List result =  orderRepo.findOrderBetweenDate1AndDate2(ini, fim);
        System.out.printf("---> q5:findOrderBetweenDate1AndDate2: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println(  OrdersObjToString.get(o)  );
    }
    
    // Q6 - findOrdersByTotalAmountAndCustomerState  (Join)
    public void q6FindOrdersByTotalAmountAndCustomerState(){
        startTime();
        List result =  orderRepo.findOrdersByTotalAmountAndCustomerState(400, "ME");
        System.out.printf("---> q6:findOrdersByTotalAmountAndCustomerState: documents(%d) | ", result.size());
        stopTime(true);
        for (Object o : result) System.out.println(  OrdersObjToString.get(o)  );
    }
    
    // Q7 - findOrdersByOrderlineProductCategory  (Join)
    public void q7FindOrdersByOrderlineProductCategory(){
        startTime();
        List result =  orderRepo.findOrdersByOrderlineProductCategory(2);
        System.out.printf("---> q7:findOrdersByOrderlineProductCategory: documents(%d) | ", result.size());
        stopTime(true);
//        for (Object o : result) System.out.println(o);
        int total = 0;
        boolean achou = false;
        for (Object o : result) {
            Orders order = (Orders)o;
            for (Orderlines ol : order.getOrderlines()){
                if (ol.getProducts().getCategory() == 2){
                    achou = true;
                    break;
                }
            }
            if (achou){
                total++;
                achou = false;
            }
            
            //System.out.println(o);
        }
        System.out.println("Orders com produto da categoria 2: " + total);
    }
}