/***************************************************************************
*
* entity: Product
*
***************************************************************************/
package mf.sd.run;

import mf.sd.model.nosql.products.Categories;
import mf.sd.model.nosql.products.Inventory;
import mf.sd.model.nosql.products.Products;
import mf.sd.repositories.ProductRepository;
import mf.sd.utils.ProductsObjToString;

/**
 *
 * @author evand
 */
public class CrudProduct {
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;

    public static void testInsertProduct(ProductRepository productRepo){
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
        
        productRepo.save(p);

        if (!productRepo.findProductById(2000000).isEmpty()){
            Products product = productRepo.findProductById(2000000).get(0);
            if (product.getActor().equals("Actor Test")){
                System.out.println("*INFO: SpringCRUDQueries.testInsertProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(product) );
            }
        }
    }
    
    public static void testUpdateProduct(ProductRepository productRepo) {
        
        if (productRepo.findProductById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testUpdateProduct(): product not found!");
            return;
        }
        
        Products p = productRepo.findProductById(2000000).get(0);
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
        
        productRepo.save(p);

        if (!productRepo.findProductById(2000000).isEmpty()){
            Products product = productRepo.findProductById(2000000).get(0);
            if (product.getActor().equals("Actor Test Updated")){
                System.out.println("*INFO: SpringCRUDQueries.testUpdateProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(product) );
            }
        }
    }
    
    public static void testRemoveProduct(ProductRepository productRepo) {
        if (productRepo.findProductById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testRemoveProduct(): product not found!");
            return;
        }
        
        Products p = productRepo.findProductById(2000000).get(0);
        productRepo.delete(p);

        if (productRepo.findProductById(2000000).isEmpty()){
            System.out.println("*INFO: SpringCRUDQueries.testRemoveProduct(): Ok");
        }
    }
}
