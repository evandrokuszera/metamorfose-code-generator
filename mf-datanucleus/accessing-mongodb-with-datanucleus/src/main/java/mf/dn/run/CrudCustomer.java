/***************************************************************************
*
* entity: Customers
*
***************************************************************************/
package mf.dn.run;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import mf.dn.model.nosql.customers.Customers;
import static mf.dn.run.DNucleusConnection.closeConn;
import static mf.dn.run.DNucleusConnection.openConn;
import mf.dn.utils.CustomersObjToString;

/**
 *
 * @author evand
 */
public class CrudCustomer {
    private static EntityManager em;
    private static final boolean printDocumentAfterSave = RunCrudQueries.printDocumentAfterSave;
    
    public static void testInsertCustomer() throws Exception {
        em = openConn();
        
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
        
        
        Customers customer = em.find(Customers.class, 2000000);
        if (customer != null){
            if (customer.getFirstname().equals("Customer") && customer.getLastname().equals("Test")){
                System.out.println("*INFO: DNucleusCRUDQueries.testInsertCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customer) );
            }
        }
        closeConn(em);
    }
    
    public static void testUpdateCustomer() throws Exception {
        em = openConn();
        
        Customers c = em.find(Customers.class, 2000000);
        if (c == null){
            closeConn(em);
            System.out.println("*ERROR: DNucleusCRUDQueries.testUpdateCustomer(): customer not found!");
            return;
        }
        
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

        Customers customer = em.find(Customers.class, 2000000);
        if (customer != null){
            if (customer.getFirstname().equals("Customer2") && customer.getLastname().equals("Test2")){
                System.out.println("*INFO: DNucleusCRUDQueries.testUpdateCustomer(): Ok");
                if (printDocumentAfterSave) System.out.println( CustomersObjToString.get(customer) );
            }
        }
        closeConn(em);
    }
    
    public static void testRemoveCustomer() throws Exception {
        em = openConn();
        EntityTransaction tx = em.getTransaction(); // https://www.datanucleus.org/products/accessplatform_6_0/jpa/persistence.html#transaction_local
        
        try{
            tx.begin();
            Customers c = em.find(Customers.class, 2000000);
            if (c == null){
                System.out.println("*ERROR: DNucleusCRUDQueries.testRemoveCustomer(): customer not found!");
                tx.rollback();
                closeConn(em);
                return;
            } else {
                em.remove(c);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
        }

        Customers customer = em.find(Customers.class, 2000000);
        if (customer == null){
            System.out.println("*INFO: DNucleusCRUDQueries.testRemoveCustomer(): Ok");
        }
        closeConn(em);
    }
}