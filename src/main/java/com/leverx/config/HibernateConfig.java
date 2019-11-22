package com.leverx.config;

import com.leverx.cat.entity.Cat;
import com.leverx.user.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateConfig {
    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Cat.class);
        configuration.addAnnotatedClass(User.class);

        Properties properties = configuration.getProperties();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(properties)
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}
