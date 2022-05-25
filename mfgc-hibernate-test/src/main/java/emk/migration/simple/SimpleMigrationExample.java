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
import mf.migration.MfGenericMigration;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

/**
 *
 * @author evand
 */
public class SimpleMigrationExample {
    public static void main(String[] args) {
        HibernateConnection.openConnections();
        productMigration();
        orderMigration();
        orderlineMigration();
        HibernateConnection.closeConnections();
    }
    
    // example without custom mappings
    public static void productMigration(){
        MfGenericMigration migrator = new MfGenericMigration(
                Products.class, 
                DocProducts.class
        );
        
        migrator.setSourceInstances( HibernateConnection.emRDB.createQuery("select p from Products p").getResultList() );
        
        HibernateConnection.emNoSQL.getTransaction().begin();
        for (var item : migrator.getTargetInstances()){
            HibernateConnection.emNoSQL.persist(item);
        }
        HibernateConnection.emNoSQL.getTransaction().commit();
    }
    
    // example with custom mappings.
    public static void orderMigration(){
        MfGenericMigration migrator = new MfGenericMigration(
                Orders.class, 
                DocOrders.class
        );
        
        migrator.getModelMapper().getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        
        migrator.getModelMapper().addMappings(
            new PropertyMap<Orders, emk.collection.orders.DocOrders>() {
                protected void configure() {
                    map().set_id(null);
                }
            }
        );
        
        migrator.getModelMapper().addMappings(
            new PropertyMap<Orderlines, DocOrderlines>() {
                protected void configure() {
                    map().setOrderid( source.getOrderid().getId() );
                    map().setProdid( source.getProdid().getId() );
                }
            }
        );

        migrator.getModelMapper().validate();
        
        migrator.setSourceInstances( HibernateConnection.emRDB.createQuery("select o from Orders o").getResultList() );
        
        HibernateConnection.emNoSQL.getTransaction().begin();
        for (var item : migrator.getTargetInstances()){
            HibernateConnection.emNoSQL.persist(item);
        }
        HibernateConnection.emNoSQL.getTransaction().commit();
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
