/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.repositories;

import java.time.LocalDate;
import java.util.List;
import mf.sd.model.nosql.orders.Orders;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author evand
 */
public interface OrderRepository extends MongoRepository<Orders, String> {
    
    @Query("{ 'id_order': ?0}")
    public List<Orders> findOrderById(int id);
    
    @Query("{ 'orderdate': {$gte: ?0, $lte: ?1} }")
    public List<Orders> findOrderBetweenDate1AndDate2(LocalDate ini, LocalDate fim);
    
    @Aggregation(pipeline = {
        "{$match: { totalamount: {$gte: ?0} } }",
        "{$lookup: { from: \"Customers\", localField: \"customerid\", foreignField: \"id_customer\", as: \"CustomersObj\"}}", 
        "{$match: { 'CustomersObj.state': ?1 }}",
    })
    public List<Orders> findOrdersByTotalAmountAndCustomerState(double totalamount, String customerState);
    
    @Aggregation(pipeline = {
        "{$lookup: { from: \"Products\", localField: \"orderlines.products.prod_id\", foreignField: \"id_prod\", as: \"ProductsObj\"}}", 
        "{$match: { 'ProductsObj.categories.id_category': ?0 }}",
    })
    public List<Orders> findOrdersByOrderlineProductCategory(int i);
    
    
    
    @Query("{ 'orderlines.prod_id': ?0 }")
    public List<Orders> getOrdersByProdId(int id);        
}
