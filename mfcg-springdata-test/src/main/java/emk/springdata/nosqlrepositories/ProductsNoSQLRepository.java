package emk.springdata.nosqlrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import emk.collection.products.DocProducts;

public interface ProductsNoSQLRepository extends MongoRepository<DocProducts, String> {

}
