package com.tsystems;

import com.tsystems.configurations.HibernateSessionFactoryConfig;
import com.tsystems.entities.Driver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
;import static com.tsystems.entities.enums.DriverState.*;

public class LogisticsWebApp {
    public static void main(String[] args) {

        SessionFactory factory = HibernateSessionFactoryConfig.getSessionFactory();
        Session session = null;
        try {
//            session = factory.getCurrentSession();
//            session.beginTransaction();
//            Driver driver1 = new Driver();
//            session.save(driver1);
//            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
