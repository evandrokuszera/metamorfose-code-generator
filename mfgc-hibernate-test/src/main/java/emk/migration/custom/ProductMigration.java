/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emk.migration.custom;

import emk.HibernateConnection;
import emk.collection.products.DocProducts;
import emk.rdb.Products;
import java.util.List;
import mf.migration.MfGenericMigration;

/**
 *
 * @author evand
 */
public class ProductMigration extends MfGenericMigration {

    public ProductMigration(Class sourceClass, Class targetClass) {
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
    
    public static void main(String[] args) {
        ProductMigration migrator = new ProductMigration(
                Products.class, 
                DocProducts.class
        );
        
        HibernateConnection.openConnections();
        migrator.setSourceInstances( HibernateConnection.emRDB.createQuery("select p from Products p").getResultList() );
        migrator.saveAllTargetInstances();
        HibernateConnection.closeConnections();
    }
    
}