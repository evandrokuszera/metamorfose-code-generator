/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.utils;

import java.util.List;
import mf.sd.model.nosql.orders.Orderlines;
import mf.sd.model.nosql.products.Products;

/**
 *
 * @author evand
 */
public class ProductsObjToString {
    
    public static String get(Object obj){
        String prod = "", category = "null", inventory = "null";
        
        if (obj == null) return "null";
        if (obj instanceof Products == false) return "ERROR: it is not a Products.";
            
        Products p = (Products) obj;
        
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
                ", \"orderlines\":" + getOrderlinessToString(p.getOrderlines()) + 
                "}";
        return prod;
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
                "},";
        }
        msg += "]";
        return msg;
    }
}