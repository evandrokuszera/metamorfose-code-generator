package com.impetus.kundera;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mf.ku.model.nosql.customers.Customers;
import mf.ku.model.nosql.orders.Orders;
import mf.ku.model.nosql.products.Products;
import mf.ku.utils.CustomersObjToString;
import mf.ku.utils.OrdersObjToString;
import mf.ku.utils.ProductsObjToString;

/**
 * The Class CRUDTest.
 */
public class CRUDTest1
{

    /** The Constant PU. */
    private static final String PU = "mongo_pu";

    /** The emf. */
    private static EntityManagerFactory emf;

    /** The em. */
    private EntityManager em;

    /**
     * Sets the up before class.
     *
     * @throws Exception
     *             the exception
     */
    @BeforeClass
    public static void SetUpBeforeClass() throws Exception
    {
        emf = Persistence.createEntityManagerFactory(PU);
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception
    {
        em = emf.createEntityManager();
    }

    /**
     * Test crud operations.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testCRUDOperations() throws Exception
    {
//        testInsert();
        testMerge();
//        testRemove();
    }

    /**
     * Test insert.
     *
     * @throws Exception
     *             the exception
     */
    private void testInsert() throws Exception
    {
//        Products p = new Products();
//        p.setActor("Will");
//        p.setCategory(1);
//        p.setCommon_prod_id(1);
//        p.setId_prod(11);
//        p.setPrice(55.0);
//        p.setSpecial(1);
//        p.setTitle("Fogo Amigo");
//        em.persist(p);
//        
//        Customers c = new Customers();
//        c.setId_customer(1);
//        c.setFirstname("Evandro");
//        c.setLastname("Kuszera");
//        c.setIncome(50000);
//        em.persist(c);
//        
//        Orders o = new Orders();
//        o.setCustomerid(1);
//        o.setId_order(1);
//        o.setNetamount(100.0);
//        o.setOrderdate(new Date());
//        o.setTax(1.1);
//        o.setTotalamount(101.0);
//        o.setCustomers(c);
//        
//        Orderlines ol = new Orderlines();
//        ol.setOrderid(1);
//        ol.setOrderlinedate(new Date());
//        ol.setOrderlineid(1);
//        ol.setQuantity(1);
//        ol.setProd_id(11);
////        ol.setProducts(p);  // Não funciona se descomentar essa linha!
//        o.setOrderlines(new ArrayList<Orderlines>());
//        o.getOrderlines().add(ol);
//        
//        em.persist(o);
//        
//        c.setOrders(new ArrayList());
//        c.getOrders().add(o);
//        em.persist(c);
//        
//        p.setOrderlines(new ArrayList<Orderlines>());
//        p.getOrderlines().add(ol);
//        em.merge(p);
        
    }

    /**
     * Test merge.
     */
    private void testMerge()
    {
        // funcionou...
        Orders o = (Orders) em.createQuery("select o from Orders o").getResultList().get(0);
        System.out.println( OrdersObjToString.get(o) + "\n" );
        
        // funcionou...
        Customers c = (Customers) em.createQuery("select o from Customers o").getResultList().get(1);
        System.out.println( CustomersObjToString.get(c) + "\n" );
        
        // funcionou...
        Products p = (Products) em.createQuery("select o from Products o").getResultList().get(0);
        System.out.println( ProductsObjToString.get(p) + "\n" );
    }

    /**
     * Test remove.
     */
    private void testRemove()
    {
//        Person p = em.find(Person.class, "101");
//        em.remove(p);
//
//        Person p1 = em.find(Person.class, "101");
//        Assert.assertNull(p1);
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception
    {
        em.close();
    }

    /**
     * Tear down after class.
     *
     * @throws Exception
     *             the exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        if (emf != null)
        {
            emf.close();
            emf = null;
        }
    }
}
