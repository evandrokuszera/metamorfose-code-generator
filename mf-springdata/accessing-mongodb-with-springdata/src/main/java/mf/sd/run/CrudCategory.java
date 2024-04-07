/***************************************************************************
*
* entity: Categories
*
***************************************************************************/
package mf.sd.run;

import mf.sd.repositories.CategoryRepository;

/**
 *
 * @author evand
 */
public class CrudCategory {
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;

    public static void testInsertCategory(CategoryRepository categoryRepo){
        mf.sd.model.nosql.categories.Categories cat = new mf.sd.model.nosql.categories.Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        
        categoryRepo.save(cat);

        if (!categoryRepo.findByCategoryId(2000).isEmpty()){
            mf.sd.model.nosql.categories.Categories catFromDB = categoryRepo.findByCategoryId(2000).get(0);
            if (catFromDB.getCategoryname().equals("Category Test")){
                System.out.println("*INFO: SpringCRUDQueries.testInsertCategory(): Ok");
                if (printDocumentAfterSave) System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
    }
    
    public static void testUpdateCategory(CategoryRepository categoryRepo) {
        
        if (categoryRepo.findByCategoryId(2000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testUpdateCategory(): category not found!");
            return;
        }
        
        mf.sd.model.nosql.categories.Categories cat = categoryRepo.findByCategoryId(2000).get(0);
        cat.setId_category(2000);
        cat.setCategoryname("Category Test Updated");
        
        categoryRepo.save(cat);

        if (!categoryRepo.findByCategoryId(2000).isEmpty()){
            mf.sd.model.nosql.categories.Categories catFromDB = categoryRepo.findByCategoryId(2000).get(0);
            if (catFromDB.getCategoryname().equals("Category Test Updated")){
                System.out.println("*INFO: SpringCRUDQueries.testUpdateCategory(): Ok");
                if (printDocumentAfterSave) System.out.println( "{\"id_category\":"+catFromDB.getId_category()+"}" );
            }
        }
    }
    
    public static void testRemoveCategory(CategoryRepository categoryRepo) {
        if (categoryRepo.findByCategoryId(2000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testRemoveCategory(): category not found!");
            return;
        }
        
        mf.sd.model.nosql.categories.Categories cat = categoryRepo.findByCategoryId(2000).get(0);
        categoryRepo.delete(cat);

        if (categoryRepo.findByCategoryId(2000).isEmpty()){
            System.out.println("*INFO: SpringCRUDQueries.testRemoveCategory(): Ok");
        }
    }
}
