/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emk.migration.custom;

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
public class OrderMigration extends MfGenericMigration {

    public OrderMigration(Class sourceClass, Class targetClass) {
        super(sourceClass, targetClass);
    }

    // It´s possible to customises the saveAllTargetInstances method to save all the target instances directly into NoSQL database.
    @Override
    public void saveAllTargetInstances() {
        HibernateConnection.emNoSQL.getTransaction().begin();
        for (var item : this.getTargetInstances()){
            HibernateConnection.emNoSQL.persist(item);
        }
        HibernateConnection.emNoSQL.getTransaction().commit();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        OrderMigration migrator = new OrderMigration(
                Orders.class, 
                DocOrders.class
        );
        
        migrator.getModelMapper().addMappings(
            new PropertyMap<Orderlines, DocOrderlines>() {
                @Override
                protected void configure() {
                    map().setOrderid( source.getOrderid().getId() );
                    map().setProdid( source.getProdid().getId() );
                }
            }
        );
        
        HibernateConnection.openConnections();
        migrator.setSourceInstances( HibernateConnection.emRDB.createQuery("Select o From Orders o").getResultList() );
        migrator.saveAllTargetInstances();
        HibernateConnection.closeConnections();
    }
    
}