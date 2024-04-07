/***************************************************************************
*
* entity: Products
*
***************************************************************************/
package mf.dn.run;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import mf.dn.model.nosql.products.Categories;
import mf.dn.model.nosql.products.Inventory;
import mf.dn.model.nosql.products.Products;
import static mf.dn.run.DNucleusConnection.closeConn;
import static mf.dn.run.DNucleusConnection.openConn;
import mf.dn.utils.ProductsObjToString;

/**
 *
 * @author evand
 */
public class CrudProduct {
    private static EntityManager em;
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;
    
    public static void testInsertProduct() throws Exception {
        em = openConn();
        Products p = new Products();
        p.setActor("Actor Test");
        p.setTitle("Title Test");
        p.setCategory(2000);
        p.setCommon_prod_id(1);
        p.setId_prod(2000000);
        p.setPrice(5555.0);
        p.setSpecial(1);
        
        Inventory in = new Inventory();
        in.setProd_id(2000000);
        in.setQuan_in_stock(1234);
        in.setSales(4321);
        p.setInventory(in);
        
        Categories cat = new Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        p.setCategories(cat);
        
        em.persist(p);

        Products product = em.find(Products.class, 2000000);
        if (product != null){
            if (product.getActor().equals("Actor Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(product) );
            }
        }
        closeConn(em);
    }
    
    public static void testUpdateProduct() throws Exception {
        em = openConn();
        
        Products p = em.find(Products.class, 2000000);
        if (p == null){
            closeConn(em);
            System.out.println("*ERROR: DNucleusCRUDQueries.testUpdateProduct(): product not found!");
            return;
        }
        
        p.setActor("Actor Test Updated");
        p.setTitle("Title Test Updated");
        p.setCategory(2000);
        p.setCommon_prod_id(0);
        p.setId_prod(2000000);
        p.setPrice(10000.0);
        p.setSpecial(0);
        
        Inventory in = new Inventory();
        in.setProd_id(2000000);
        in.setQuan_in_stock(0);
        in.setSales(0);
        p.setInventory(in);
        
        Categories cat = new Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        p.setCategories(cat);
        
        em.merge(p);

        Products product = em.find(Products.class, 2000000);
        if (product != null){
            if (product.getActor().equals("Actor Test Updated")){
                System.out.println("*INFO: DNucleusCRUDQueries.testUpdateProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(product) );
            }
        }
        closeConn(em);
    }
    
    public static void testRemoveProduct() throws Exception {
        em = openConn();
        EntityTransaction tx = em.getTransaction(); // https://www.datanucleus.org/products/accessplatform_6_0/jpa/persistence.html#transaction_local
        
        try{
            tx.begin();
            Products p = em.find(Products.class, 2000000);
            if (p == null){
                System.out.println("*ERROR: DNucleusCRUDQueries.testRemoveProduct(): product not found!");
                tx.rollback();
                closeConn(em);
                return;
            } else {
                em.remove(p);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
        }

        Products product = em.find(Products.class, 2000000);
        if (product == null){
            System.out.println("*INFO: DNucleusCRUDQueries.testRemoveProduct(): Ok");
        }
        closeConn(em);
    }
}