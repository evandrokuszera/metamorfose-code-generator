/***************************************************************************
*
* entity: Customer
*
***************************************************************************/
package mf.sd.run;

import mf.sd.model.nosql.customers.Customers;
import mf.sd.repositories.CustomerRepository;
import mf.sd.utils.CustomersObjToString;

/**
 *
 * @author evand
 */
public class CrudCustomer {
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;

    public static void testInsertCustomer(CustomerRepository customerRepo) {
        Customers c = new Customers();
        c.setId_customer(2000000);
        c.setFirstname("Customer");
        c.setLastname("Test");        
        c.setAddress1("Street 1");
        c.setAddress2("Street 2");
        c.setAge(41);
        c.setCity("City Test");
        c.setCountry("Brazil");
        c.setCreditcard("VISA");
        c.setCreditcardexpiration("07");
        c.setCreditcardtype(1);
        c.setEmail("customer@test.com");
        c.setGender("M");
        c.setUsername("customertest");
        c.setPassword("1234");
        c.setPhone("554699991234");
        c.setRegion(22);
        c.setState("PR");
        c.setZip("85660000");
        c.setIncome(50000);        
        customerRepo.save(c);
        
        
        if (!customerRepo.findCustomerById(2000000).isEmpty()){
            Customers customer = customerRepo.findCustomerById(2000000).get(0);
            if (customer.getFirstname().equals("Customer") && customer.getLastname().equals("Test")){
                System.out.println("*INFO: SpringCRUDQueries.testInsertCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customer) );
            }
        }
        
    }
    
    public static void testUpdateCustomer(CustomerRepository customerRepo) {
        
        if (customerRepo.findCustomerById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testUpdateCustomer(): customer not found!");
            return;
        }
        
        Customers c = customerRepo.findCustomerById(2000000).get(0);
        c.setId_customer(2000000);
        c.setFirstname("Customer2");
        c.setLastname("Test2");        
        c.setAddress1("Street 12");
        c.setAddress2("Street 22");
        c.setAge(41);
        c.setIncome(100000);
        c.setCity("City Test2");
        c.setCountry("Brazil2");
        c.setCreditcard("VISA");
        c.setCreditcardexpiration("08");
        c.setCreditcardtype(2);
        c.setEmail("customer2@test.com");
        c.setGender("F");
        c.setUsername("customertest2");
        c.setPassword("12345");
        c.setPhone("554699992222");
        c.setRegion(33);
        c.setState("SC");
        c.setZip("95660000");
        
        customerRepo.save(c);

        if (!customerRepo.findCustomerById(2000000).isEmpty()){
            Customers customer = customerRepo.findCustomerById(2000000).get(0);
            if (customer.getFirstname().equals("Customer2") && customer.getLastname().equals("Test2")){
                System.out.println("*INFO: SpringCRUDQueries.testUpdateCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customer) );
            }
        }
    }
    
    public static void testRemoveCustomer(CustomerRepository customerRepo) {
        if (customerRepo.findCustomerById(2000000).isEmpty()){
            System.out.println("*ERROR: SpringCRUDQueries.testRemoveCustomer(): customer not found!");
            return;
        }
        
        Customers c = customerRepo.findCustomerById(2000000).get(0);
        customerRepo.delete(c);

        if (customerRepo.findCustomerById(2000000).isEmpty()){
            System.out.println("*INFO: SpringCRUDQueries.testRemoveCustomer(): Ok");
        }
        
    }
}
