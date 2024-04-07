/***************************************************************************
*
* entity: Categories
*
***************************************************************************/
package mf.dn.run;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import static mf.dn.run.DNucleusConnection.closeConn;
import static mf.dn.run.DNucleusConnection.openConn;

/**
 *
 * @author evand
 */
public class CrudCategory {
    private static EntityManager em;
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;
    
    public static void testInsertCategory() throws Exception {
        em = openConn();
        
        mf.dn.model.nosql.categories.Categories cat = new mf.dn.model.nosql.categories.Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        
        em.persist(cat);

        mf.dn.model.nosql.categories.Categories catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
        if (catFromDB != null){
            if (catFromDB.getCategoryname().equals("Category Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertCategory(): Ok");
                if (printDocumentAfterSave) System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
        closeConn(em);
    }
    
    public static void testUpdateCategory() throws Exception {
        em = openConn();
        
        mf.dn.model.nosql.categories.Categories catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
        if (catFromDB == null){
            closeConn(em);
            System.out.println("*ERROR: DNucleusCRUDQueries.testUpdateCategory(): category not found!");
            return;
        }
        
        catFromDB.setId_category(2000);
        catFromDB.setCategoryname("Category Test Updated");
        
        em.merge(catFromDB);

        catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
        if (catFromDB != null){
            if (catFromDB.getCategoryname().equals("Category Test Updated")){
                System.out.println("*INFO: DNucleusCRUDQueries.testUpdateCategory(): Ok");
                if (printDocumentAfterSave) System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
        closeConn(em);
    }
    
    public static void testRemoveCategory() throws Exception {
        em = openConn();
        EntityTransaction tx = em.getTransaction(); // https://www.datanucleus.org/products/accessplatform_6_0/jpa/persistence.html#transaction_local
        
        try{
            tx.begin();
            mf.dn.model.nosql.categories.Categories catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
            if (catFromDB == null){
                System.out.println("*ERROR: DNucleusCRUDQueries.testRemoveCategory(): category not found!");
                tx.rollback();
                closeConn(em);
                return;
            } else {
                em.remove(catFromDB);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
        }

        mf.dn.model.nosql.categories.Categories catFromDB = em.find(mf.dn.model.nosql.categories.Categories.class, 2000);
        if (catFromDB == null){
            System.out.println("*INFO: DNucleusCRUDQueries.testRemoveCategory(): Ok");
        }
        closeConn(em);
    }
}
