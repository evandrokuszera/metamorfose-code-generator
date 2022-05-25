package emk.springdata.nosqlrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import emk.collection.orders.DocOrders;

public interface OrdersNoSQLRepository extends MongoRepository<DocOrders, String> {

}
