/***************************************************************************
*
* entity: Customer
*
***************************************************************************/
package mf.ku.run;

import javax.persistence.EntityManager;
import mf.ku.model.nosql.customers.Customers;
import mf.ku.utils.CustomersObjToString;

/**
 *
 * @author evand
 */
public class CrudCustomer {
    private static EntityManager em = KunderaConnection.em;
    
    public static void testInsertCustomer() throws Exception {
        em.getTransaction().begin();
        
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
        em.persist(c);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testInsertCustomer(): Ok");
    }
    
    public static void testReadCustomer() throws Exception {
        Customers customer = em.find(Customers.class, 2000000);
        if (customer != null){
            if (customer.getId_customer() == 2000000){
                System.out.println("*INFO: KunderaCRUDQueries.testReadCustomer(): Ok");
                System.out.println( CustomersObjToString.get(customer) );
            }
        }
    }
    
    public static void testUpdateCustomer() throws Exception {
        Customers c = em.find(Customers.class, 2000000);
        if (c == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testUpdateCustomer(): customer not found!");
            return;
        }
        
        em.getTransaction().begin();
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
        em.merge(c);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testUpdateCustomer(): Ok");
    }
    
    public static void testRemoveCustomer() throws Exception {
        Customers c = em.find(Customers.class, 2000000);
        if (c == null){
            System.out.println("*ERROR: KunderaCRUDQueries.testRemoveCustomer(): customer not found!");
            return;
        }
        
        em.getTransaction().begin();
        em.remove(c);
        em.getTransaction().commit();
        System.out.println("*INFO: KunderaCRUDQueries.testRemoveCustomer(): Ok");
    }
}
