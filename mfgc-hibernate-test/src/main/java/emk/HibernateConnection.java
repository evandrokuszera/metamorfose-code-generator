/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author evand
 */
public class HibernateConnection {
    public static EntityManagerFactory emfRDB;
    public static EntityManagerFactory emfNoSQL;

    public static EntityManager emRDB;
    public static EntityManager emNoSQL;
    
    public static void openConnections(){
        emfRDB = Persistence.createEntityManagerFactory("postgres-pu");
        emfNoSQL = Persistence.createEntityManagerFactory("mongo-pu");

        emRDB = emfRDB.createEntityManager();
        emNoSQL = emfNoSQL.createEntityManager();
    }
    
    public static void closeConnections(){
        emRDB.close();
        emNoSQL.close();
        emfNoSQL.close();
        emfRDB.close();
    }
}
