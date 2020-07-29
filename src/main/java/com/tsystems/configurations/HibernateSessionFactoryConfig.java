package com.tsystems.configurations;

import com.tsystems.entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryConfig {

    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryConfig() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Cargo.class);
                configuration.addAnnotatedClass(City.class);
                configuration.addAnnotatedClass(Driver.class);
                configuration.addAnnotatedClass(Order.class);
                configuration.addAnnotatedClass(Truck.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Exception!" + e);
            }
        }
        return sessionFactory;
    }
}

