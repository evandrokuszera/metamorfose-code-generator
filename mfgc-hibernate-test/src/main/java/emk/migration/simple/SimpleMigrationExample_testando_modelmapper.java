/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emk.migration.simple;

import emk.HibernateConnection;
import emk.collection.orders.DocOrderlines;
import emk.collection.orders.DocOrders;
import emk.collection.products.DocProducts;
import emk.rdb.Orderlines;
import emk.rdb.Orders;
import emk.rdb.Products;
import java.util.ArrayList;
import java.util.Date;
import mf.migration.MfGenericMigration;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.Mapping;

/**
 *
 * @author evand
 */
public class SimpleMigrationExample_testando_modelmapper {
    public static void main(String[] args) {
        //HibernateConnection.openConnections();
        orderMigration();
        //orderlineMigration();
        //HibernateConnection.closeConnections();
    }
    
    // example with custom mappings.
    public static void orderMigration(){
        MfGenericMigration migrator = new MfGenericMigration(
                Orders.class, 
                DocOrders.class
        );
        
        migrator.getModelMapper().getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        
//        migrator.getModelMapper().addMappings(
//            new PropertyMap<Orders, emk.collection.orders.DocOrders>() {
//                protected void configure() {
//                    map().set_id(null);
//                }
//            }
//        );
//        
//        migrator.getModelMapper().addMappings(
//            new PropertyMap<Orderlines, DocOrderlines>() {
//                protected void configure() {
//                    map().setOrderid( source.getOrderid().getId() );
//                    map().setProdid( source.getProdid().getId() );
//                }
//            }
//        );

        Products p = new Products();
        p.setId(55);
        p.setDescription("DVD");
        p.setPrice(500.0);
        
        Orderlines ol = new Orderlines();
        ol.setId(1);
        ol.setOrderlinedate(new Date());
        ol.setPrice(500.0);
        ol.setProdid(p);
        ol.setQuantity(1.0);
        
        ArrayList<Orderlines> lista = new ArrayList<>();
        lista.add(ol);

        Orders o = new Orders();
        o.setId(1);
        o.setOrderdate(new Date());
        o.setTotal(1000.0);
        o.setOrderlinesList(lista);
        
        
        
        
        
        
        
        migrator.getModelMapper().createTypeMap(Orders.class, DocOrders.class);
        migrator.getModelMapper().createTypeMap(Orderlines.class, DocOrderlines.class);
        migrator.getModelMapper().createTypeMap(Products.class, DocProducts.class);
        
        migrator.getModelMapper().addMappings(
            new PropertyMap<Orders, DocOrders>() {
                @Override
                protected void configure() {
                    skip().set_id(null);
                }
            }
        );
        
        migrator.getModelMapper().addMappings(
            new PropertyMap<Orderlines, DocOrderlines>() {
                @Override
                protected void configure() {
                    map().setOrderid(source.getOrderid().getId());
                    map().setProdid(source.getProdid().getId());
                }
            }
        );
        
        migrator.getModelMapper().addMappings(
            new PropertyMap<Products, DocProducts>() {
                @Override
                protected void configure() {
                    skip().set_id(null);
                }
            }        
        );
        
        
        
        
        DocOrders teste = migrator.getModelMapper().map(o, DocOrders.class);

        migrator.getModelMapper().validate();
        
        for (TypeMap typeMap : migrator.getModelMapper().getTypeMaps()){
            System.out.println(typeMap);
            
            for (Object mapping : typeMap.getMappings()){
                Mapping m = (Mapping) mapping;
                System.out.println(m);
            }
            
            System.out.println("UNMAPPED_PROPERTIES...");
            
            for (Object info : typeMap.getUnmappedProperties()){
                System.out.println(info);
            }
            
            System.out.println("");
        }
        
//        migrator.setSourceInstances( HibernateConnection.emRDB.createQuery("select o from Orders o").getResultList() );
//        
//        HibernateConnection.emNoSQL.getTransaction().begin();
//        for (var item : migrator.getTargetInstances()){
//            HibernateConnection.emNoSQL.persist(item);
//        }
//        HibernateConnection.emNoSQL.getTransaction().commit();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // example without custom mappings
    public static void orderlineMigration(){
        MfGenericMigration migrator = new MfGenericMigration(
                Orderlines.class, 
                emk.collection.orderlines.DocOrderlines.class
        );
        
        migrator.getModelMapper().getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        migrator.getModelMapper().addMappings(
            new PropertyMap<Orderlines, emk.collection.orderlines.DocOrderlines>() {
                protected void configure() {
                    map().set_id( null );
                    map().setProdid( source.getProdid().getId() );
                    map().setOrderid( source.getOrderid().getId() );
                }
            }
        );
        
        migrator.getModelMapper().validate();
        
        migrator.setSourceInstances( HibernateConnection.emRDB.createQuery("select ol from Orderlines ol").getResultList() );
        
        HibernateConnection.emNoSQL.getTransaction().begin();
        for (var item : migrator.getTargetInstances()){
            HibernateConnection.emNoSQL.persist(item);
        }
        HibernateConnection.emNoSQL.getTransaction().commit();
    }
}
