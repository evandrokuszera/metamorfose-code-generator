/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.repositories;

import java.util.List;
import mf.sd.model.nosql.products.Products;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author evand
 */
public interface ProductRepository extends MongoRepository<Products, String> {
    
    @Query("{'id_prod': ?0}")
    public List<Products> findProductById(int id);
    
    @Query("{$or: [{'actor': ?0}, {'actor': ?1}]}")
    public List<Products> findProductByActor1OrActor2(String actor1, String actor2);
    
    
    
    
    
    @Query("{'categories.id_category': ?0}")
    public List<Products> findByCategoryID(int id);
    
    @Query("{'price': {$gte: ?0}}")
    public List<Products> findByPriceGreaterThan(double price);
    
    @Query("{'inventory.quan_in_stock': ?0}")
    public List<Products> findByQuanInStock(int qtde);
    
}
