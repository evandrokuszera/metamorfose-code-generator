package emk.springdata.nosqlrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import emk.collection.orderlines.DocOrderlines;

public interface OrderlinesNoSQLRepository extends MongoRepository<DocOrderlines, String> {

}
