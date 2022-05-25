/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emk.migration.generic.test;

import emk.HibernateConnection;
import emk.collection.orders.DocOrderlines;
import emk.collection.orders.DocOrders;
import emk.rdb.Orderlines;
import emk.rdb.Orders;
import mf.migration.MfGenericMigration;
import org.modelmapper.PropertyMap;

/**
 *
 * @author evand
 */
public class OrderMigration {    
    
    public static void main(String[] args) {
        
        MfGenericMigration migration = new MfGenericMigration(
                Orders.class, 
                DocOrders.class
        );
        
        migration.getModelMapper().addMappings(
            new PropertyMap<Orderlines, DocOrderlines>() {
                @Override
                protected void configure() {
                    map().setOrderid( source.getOrderid().getId() );
                    map().setProdid( source.getProdid().getId() );
                }
            }
        );
        
        HibernateConnection.openConnections();
        
        migration.setSourceInstances( HibernateConnection.emRDB.createQuery("select o from Orders o").getResultList() );
        
        HibernateConnection.emNoSQL.getTransaction().begin();
        for (var item : migration.getTargetInstances()){
            HibernateConnection.emNoSQL.persist(item);
        }
        HibernateConnection.emNoSQL.getTransaction().commit();
        
        HibernateConnection.closeConnections();
    }
}
