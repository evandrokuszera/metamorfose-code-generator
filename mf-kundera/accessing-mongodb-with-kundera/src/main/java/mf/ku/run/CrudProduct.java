/***************************************************************************
*
* entity: Product
*
***************************************************************************/
package mf.ku.run;

import javax.persistence.EntityManager;
import mf.ku.model.nosql.products.Inventory;
import mf.ku.model.nosql.products.Products;
import mf.ku.utils.ProductsObjToString;

/**
 *
 * @author evand
 */
public class CrudProduct {
    private static EntityManager em = KunderaConnection.em;
    
    public static void testInsertProduct() throws Exception {
        em.getTransaction().begin();
        
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
        
        mf.ku.model.nosql.products.Categories cat = new mf.ku.model.nosql.products.Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        p.setCategories(cat);
        
        em.persist(p);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testInsertProduct(): Ok");
    }
    
    public static void testReadProduct() throws Exception {
        Products product = em.find(Products.class, 2000000);
        if (product != null){
            if (product.getId_prod() == 2000000){
                System.out.println("*INFO: KunderaCRUDQueries.testReadProduct(): Ok");
                System.out.println( ProductsObjToString.get(product) );
            }
        }
    }
    
    public static void testUpdateProduct() throws Exception {
        Products p = em.find(Products.class, 2000000);
        if (p == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testUpdateProduct(): product not found!");
            return;
        }
        
        em.getTransaction().begin();
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
        
        mf.ku.model.nosql.products.Categories cat = new mf.ku.model.nosql.products.Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        p.setCategories(cat);        
        em.merge(p);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testUpdateProduct(): Ok");
    }
    
    public static void testRemoveProduct() throws Exception {
        Products p = em.find(Products.class, 2000000);
        if (p == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testRemoveProduct(): product not found!");
            return;
        }
        
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testRemoveProduct(): Ok");
    }
}
