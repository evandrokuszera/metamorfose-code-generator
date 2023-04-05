/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.sd.repositories;

import java.util.List;
import mf.sd.model.nosql.customers.Customers;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author evand
 */
public interface CustomerRepository extends MongoRepository<Customers, String> {
    
    @Query("{ 'id_customer': ?0 }")
    public List<Customers> findCustomerById(int id);
    
    @Query("{ $and: [ {'firstname': ?0}, {'lastname': ?1} ] }")
    public List<Customers> findCustomerByFirstnameAndLastname(String first, String last);
    
    @Query("{'firstname': {$regex: /^?0/}}")
    public List<Customers> findCustomerByFirstnameStartingWith(String name);
    
    
    
    
    @Query("{ 'firstname': ?0 }")
    public List<Customers> findByCustomerFirstname(String name);
    
    @Query("{ 'income' : {$gte : ?0, $lte : ?1} }")
    public List<Customers> findByCustomerIncomeBetween(int low, int high);
 
    @Query("{$and: [{'gender': ?0}, {$or: [ {'state': ?1}, {'state': ?2} ]} ]}")
    public List<Customers> findByCustomerGenderAndState1OrState2(String gender, String state1, String state2);
    
    
    
    // from Customers left join Orders on customerid = id_orders where Customers.age = ?0, Orders.totalamount >= ?1
    @Aggregation(pipeline = {
        "{$match: { age: {$gte: ?0} } }",
        "{$lookup: { from: \"Orders\", localField: \"id_customer\", foreignField: \"customerid\", as: \"OrdersList\"}}", 
        "{$match: { 'OrdersList.totalamount': {$gte: ?1} }}",
    })
    public List<Customers> findByCustomerJoinOrders(int age, double totalamount);
}