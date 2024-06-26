/***************************************************************************
*
* entity: Categories
*
***************************************************************************/
package mf.sd.run;

import java.util.ArrayList;
import java.util.Date;
import mf.sd.model.nosql.customers.Customers;
import mf.sd.model.nosql.orders.Orderlines;
import mf.sd.model.nosql.orders.Orders;
import mf.sd.model.nosql.products.Categories;
import mf.sd.model.nosql.products.Inventory;
import mf.sd.model.nosql.products.Products;
import mf.sd.repositories.CustomerRepository;
import mf.sd.repositories.OrderRepository;
import mf.sd.repositories.ProductRepository;
import mf.sd.utils.CustomersObjToString;
import mf.sd.utils.OrdersObjToString;
import mf.sd.utils.ProductsObjToString;

/**
 *
 * @author evand
 */
public class CrudAllEntitiesWithReferences {
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;

    public static void testInsert(CustomerRepository customerRepo, OrderRepository orderRepo, ProductRepository productRepo){
        //////////////////////////////////////
        // Creating a Product
        Products product = new Products();
        product.setActor("Actor Test");
        product.setTitle("Title Test");
        product.setCategory(2001);
        product.setCommon_prod_id(1);
        product.setId_prod(2000001);
        product.setPrice(5555.0);
        product.setSpecial(1);
        
        Inventory in = new Inventory();
        in.setProd_id(2000001);
        in.setQuan_in_stock(1234);
        in.setSales(4321);
        product.setInventory(in);
        
        Categories cat = new Categories();
        cat.setId_category(2000);
        cat.setCategoryname("Category Test");
        product.setCategories(cat);
        
        productRepo.save(product);
        
        //////////////////////////////////////
        // Creating a Customer
        Customers customer = new Customers();
        customer.setId_customer(2000001);
        customer.setFirstname("Customer");
        customer.setLastname("Test");        
        customer.setAddress1("Street 1");
        customer.setAddress2("Street 2");
        customer.setAge(41);
        customer.setCity("City Test");
        customer.setCountry("Brazil");
        customer.setCreditcard("VISA");
        customer.setCreditcardexpiration("07");
        customer.setCreditcardtype(1);
        customer.setEmail("customer@test.com");
        customer.setGender("M");
        customer.setUsername("customertest");
        customer.setPassword("1234");
        customer.setPhone("554699991234");
        customer.setRegion(22);
        customer.setState("PR");
        customer.setZip("85660000");
        customer.setIncome(50000);        
        customerRepo.save(customer);
        
        //////////////////////////////////////
        // Creating an Order
        Orders order = new Orders();
        order.setCustomerid(2000001);
        order.setId_order(2000001);
        order.setNetamount(100.0);
        order.setOrderdate(new Date());
        order.setTax(1.1);
        order.setTotalamount(101.0);
        
        Orderlines olines1 = new Orderlines();
        olines1.setOrderid(2000001);
        olines1.setOrderlinedate(new Date());
        olines1.setOrderlineid(2000001);
        olines1.setQuantity(1);
        olines1.setProd_id(2000001);
        olines1.setProducts(product); // setting reference to product
        
        Orderlines olines2 = new Orderlines();
        olines2.setOrderid(2000001);
        olines2.setOrderlinedate(new Date());
        olines2.setOrderlineid(2000002);
        olines2.setQuantity(2);
        olines2.setProd_id(2000001);
        olines2.setProducts(product); // setting reference to product
        
        order.setOrderlines(new ArrayList<Orderlines>());
        order.getOrderlines().add(olines1);
        order.getOrderlines().add(olines2);
        
        //////////////////////////////////////
        // Setting the reference to order-customer
        order.setCustomers(customer); // setting reference to customer
        orderRepo.save(order);
        
        // Bidirectional relationship setting.
        // However, the three lines below don't make effect in the documents at MongoDB and could be removed. 
        // The Customer don't know about your Orders because we are using document references.
        customer.setOrders(new ArrayList<Orders>());
        customer.getOrders().add(order);
        customerRepo.save(customer);
        
        // Bidirectional relationship setting.
        // Same case as described above.
        product.setOrderlines(new ArrayList<Orderlines>());
        product.getOrderlines().add(olines1);
        product.getOrderlines().add(olines2);
        productRepo.save(product);
    }
    
    public static void testRead(CustomerRepository customerRepo, OrderRepository orderRepo, ProductRepository productRepo){
        // Printing the documents
        if (!customerRepo.findCustomerById(2000001).isEmpty()){
            Customers customerFromDB = customerRepo.findCustomerById(2000001).get(0);
            if (customerFromDB.getFirstname().equals("Customer") && customerFromDB.getLastname().equals("Test")){
                System.out.println("*INFO: SpringCRUDQueries.testReadCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customerFromDB) );
            }
        }

        if (!productRepo.findProductById(2000001).isEmpty()){
            Products productFromDB = productRepo.findProductById(2000001).get(0);
            if (productFromDB.getActor().equals("Actor Test")){
                System.out.println("*INFO: SpringCRUDQueries.testReadProduct(): Ok");
                if (printDocumentAfterSave) System.out.println( ProductsObjToString.get(productFromDB) );
            }
        }
        
        if (!orderRepo.findOrderById(2000001).isEmpty()){
            Orders orderFromDB = orderRepo.findOrderById(2000001).get(0);
            if (orderFromDB.getCustomerid() == 2000001){
                System.out.println("*INFO: SpringCRUDQueries.testReadOrder(): Ok");
                if (printDocumentAfterSave) System.out.println( OrdersObjToString.get(orderFromDB) );
            }
        }
    }
}
