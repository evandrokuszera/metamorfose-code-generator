/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.utils;

import java.util.List;
import mf.sd.model.nosql.customers.Customers;
import mf.sd.model.nosql.orders.Orderlines;
import mf.sd.model.nosql.orders.Orders;
import mf.sd.model.nosql.products.Products;

/**
 *
 * @author evand
 */
public class CustomersObjToString {
    public static String get(Object obj){
        if (obj== null) return "null";
        if (obj instanceof Customers == false) return "ERROR: it is not a Customers.";
            
        Customers c = (Customers) obj;
        
        return
            "{\"id_customer\":" + c.getId_customer() + 
            ", \"firstname\":\"" + c.getFirstname() + 
            "\", \"lastname\":\"" + c.getLastname() + 
            "\", \"address1\":\"" + c.getAddress1() + 
            "\", \"address2\":\"" + c.getAddress2() + 
            "\", \"city\":\"" + c.getCity() + 
            "\", \"state\":\"" + c.getState() + 
            "\", \"zip\":" + c.getZip() + 
            ", \"country\":\"" + c.getCountry() + 
            "\", \"region\":" + c.getRegion() + 
            ", \"email\":\"" + c.getEmail() + 
            "\", \"phone\":" + c.getPhone() + 
            ", \"creditcardtype\":" + c.getCreditcardtype() + 
            ", \"creditcard\":" + c.getCreditcard() + 
            ", \"creditcardexpiration\":\"" + c.getCreditcardexpiration() + 
            "\", \"username\":\"" + c.getUsername() + 
            "\", \"password\":\"" + c.getPassword() + 
            "\", \"age\":" + c.getAge() + 
            ", \"income\":" + c.getIncome() + 
            ", \"gender\":\"" + c.getGender() + 
            "\", \"orders\":" + getOrdersToString(c.getOrders()) + 
            "}"
        ;
    }
    
    private static String getOrdersToString(List<Orders> listOrders){
        if (listOrders == null) return "null";
        String ordersString = "[";
        for (Orders o : listOrders){
            ordersString += "{\"id_order\":" + o.getId_order() + 
                    ", \"orderdate\":\"" + o.getOrderdate() + 
                    "\", \"customerid\":" + o.getCustomerid() + 
                    ", \"netamount\":" + o.getNetamount() + 
                    ", \"tax\":" + o.getTax() + 
                    ", \"totalamount\":" + o.getTotalamount() + 
                    ", \"orderlines\":" + getOrderlinessToString(o.getOrderlines()) + 
//                    ", \"customers\":" + customers + 
                "}, ";
        }
        ordersString += "]";
        return ordersString;
    }
    
    private static String getOrderlinessToString(List<Orderlines> listOrderlines){
        if (listOrderlines == null) return "null";
        String msg = "[";
        for (Orderlines l : listOrderlines){
            msg += "{\"orderlineid\":" + l.getOrderlineid() + 
                ", \"orderid\":" + l.getOrderid() + 
                ", \"prod_id\":" + l.getProd_id() + 
                ", \"quantity\":" + l.getQuantity() + 
                ", \"orderlinedate\":\"" + l.getOrderlinedate() + 
                "\", \"products\":" + getProductToString(l.getProducts()) + 
                "},";
        }
        msg += "]";
        return msg;
    }
    
    private static String getProductToString(Products p){
        String prod = "", category = "null", inventory = "null";
        
        if (p == null) return "null";
        
        if (p.getCategories() != null){
            category = "{\"id_category\":" + p.getCategories().getId_category() + 
                        ", \"categoryname\":\"" + p.getCategories().getCategoryname() + "\"}";
        }
        
        if (p.getInventory() != null){
            inventory = "{\"prod_id\":" + p.getInventory().getProd_id() + 
                        ", \"quan_in_stock\":" + p.getInventory().getQuan_in_stock() + 
                        ", \"sales\":" + p.getInventory().getSales() + '}';
        }
        
        prod =  "{\"id_prod\":" + p.getId_prod() + 
                ", \"category\":" + p.getCategory() + 
                ", \"title\":\"" + p.getTitle() + 
                "\", \"actor\":\"" + p.getActor() + 
                "\", \"price\":" + p.getPrice() + 
                ", \"special\":" + p.getSpecial() + 
                ", \"common_prod_id\":" + p.getCommon_prod_id() + 
                ", \"inventory\":" + inventory + 
                ", \"categories\":" + category + 
                "}";
        return prod;
    }
}