package mf.dn.run;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.datanucleus.api.jpa.JPAEntityManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;

/**
 *
 * @author evand
 */
public class DNucleusConnection {
        
    public static PersistenceUnitMetaData getMongoPersistenceUnit(){
        PersistenceUnitMetaData pumd = new PersistenceUnitMetaData("dynamic-mongo-unit", "RESOURCE_LOCAL", null);
        pumd.addClassName("mf.dn.model.nosql.customers.Customers");
        pumd.addClassName("mf.dn.model.nosql.orders.Orders");
        pumd.addClassName("mf.dn.model.nosql.products.Products");
        pumd.addClassName("mf.dn.model.nosql.categories.Categories");
        pumd.setExcludeUnlistedClasses(true);
        pumd.addProperty("javax.persistence.jdbc.url", "mongodb://localhost/dvd-store-dn-temp");
        pumd.addProperty("datanucleus.schema.autoCreateAll", "true");
        pumd.addProperty("datanucleus.schema.validateTables", "false");  // https://www.datanucleus.org/products/accessplatform_4_1/jpa/guides/tutorial_mongodb.html
        pumd.addProperty("datanucleus.schema.validateConstraints", "false");
        pumd.addProperty("datanucleus.storeManagerType", "mongodb");
        pumd.addProperty("datanucleus.identifier.case", "MixedCase");
        return pumd;
    }
    
    public static EntityManager openConn() throws Exception {
        EntityManagerFactory emfMongo = new JPAEntityManagerFactory(getMongoPersistenceUnit(), null);
        EntityManager em = emfMongo.createEntityManager();
        return em;
    }
    
    public static void closeConn(EntityManager em) throws Exception {
        em.close();
        em.getEntityManagerFactory();
        if (em.getEntityManagerFactory() != null){
            em.getEntityManagerFactory().close();
        }
    }   
}
    
    