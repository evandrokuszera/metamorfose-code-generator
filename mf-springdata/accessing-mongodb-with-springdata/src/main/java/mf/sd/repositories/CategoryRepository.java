/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.repositories;

import java.util.List;
import mf.sd.model.nosql.categories.Categories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author evand
 */
public interface CategoryRepository extends MongoRepository<Categories, Integer> {
    @Query("{ 'id_category': ?0 }")
    List<Categories> findByCategoryId(int id);
}
