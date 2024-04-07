/***************************************************************************
*
* entity: Categories
*
***************************************************************************/
package mf.ku.run;

import javax.persistence.EntityManager;
import mf.ku.model.nosql.categories.Categories;

/**
 *
 * @author evand
 */
public class CrudCategory {
    private static EntityManager em = KunderaConnection.em;
    
    public static void testInsertCategory() throws Exception {
        em.getTransaction().begin();
        
        Categories cat = new Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        
        em.persist(cat);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testInsertCategory(): Ok");
    }
    
    public static void testReadCategory() throws Exception {
        Categories catFromDB = em.find(Categories.class, 2000);
        if (catFromDB != null){
            if (catFromDB.getId_category() == 2000){
                System.out.println("*INFO: KunderaCRUDQueries.testReadCategory(): Ok");
                System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
    }
    
    public static void testUpdateCategory() throws Exception {
        Categories cat = em.find(Categories.class, 2000);
        if (cat == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testUpdateCategory(): category not found!");
            return;
        }
        
        em.getTransaction().begin();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test Updated");        
        em.merge(cat);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testUpdateCategory(): Ok");
    }
    
    public static void testRemoveCategory() throws Exception {
        Categories c = em.find(Categories.class, 2000);
        if (c == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testRemoveCategory(): category not found!");
            return;
        }
        
        em.getTransaction().begin();        
        em.remove(c);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testRemoveCategory(): Ok");
    }
}
