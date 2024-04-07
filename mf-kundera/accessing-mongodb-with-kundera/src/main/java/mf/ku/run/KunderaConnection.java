/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.ku.run;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author evand
 */
public class KunderaConnection {
    private static final String PU = "mongo_pu";
    private static EntityManagerFactory emf;
    public static EntityManager em;
    
    public static EntityManager openConn() throws Exception {
        emf = Persistence.createEntityManagerFactory(PU);
        em = emf.createEntityManager();
        return em;
    }
    
    public static void closeConn() throws Exception {
        em.close();
        if (emf != null){
            emf.close();
            emf = null;
        }
        em = null;
    }
}
