/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emk.migration.manual;

import emk.collection.orders.DocOrderlines;
import emk.collection.orders.DocOrders;
import emk.rdb.Orderlines;
import emk.rdb.Orders;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author evand
 */
public class Main {
    public static void main(String[] args) {
        // ler do RDB
        EntityManagerFactory emfRDB = Persistence.createEntityManagerFactory("hibernate-rdb");
        EntityManager emRDB = emfRDB.createEntityManager();
        
        Orders orderRDB = emRDB.find(Orders.class, 1);
        System.out.println(orderRDB.getId());
        System.out.println(orderRDB.getOrderdate());
        System.out.println(orderRDB.getTotal());
        
        emRDB.close();
        emfRDB.close();
        
        // persistir no NoSQL
        EntityManagerFactory emfMongo = Persistence.createEntityManagerFactory("mongo-pu");
        EntityManager emMongo = emfMongo.createEntityManager();
        
        DocOrders orderMongo = new DocOrders();
        orderMongo.setId( orderRDB.getId() );
        orderMongo.setOrderdate( orderRDB.getOrderdate() );
        orderMongo.setTotal( orderRDB.getTotal() );
        
        List<DocOrderlines> olines = new ArrayList<>();;
        for (Orderlines ol : orderRDB.getOrderlinesList()){
            DocOrderlines olMongo = new DocOrderlines();
            olMongo.setId( ol.getId() );
            olMongo.setOrderid( ol.getOrderid().getId() );
            olMongo.setOrderlinedate( ol.getOrderlinedate() );
            olMongo.setPrice( ol.getPrice() );
            olMongo.setProdid( ol.getProdid().getId() );
            olMongo.setQuantity( ol.getQuantity() );
            olines.add( olMongo );
        }
        orderMongo.setOrderlines(olines);
        
        emMongo.getTransaction().begin();
        emMongo.persist( orderMongo );
        emMongo.getTransaction().commit();
        
        emMongo.close();
        emfMongo.close();
    }
}
