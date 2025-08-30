package com.github.squirrelgrip.meter.database.hibernate;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.database.DatabaseSessionFactory;
import com.github.squirrelgrip.meter.domain.MeterReading;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateDatabaseSessionFactory implements DatabaseSessionFactory {
    private SessionFactory sessionFactory;

    public HibernateDatabaseSessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(MeterReading.class)
                    .buildMetadata()
                    .buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    @Override
    public HibernateDatabaseSession create() {
        return new HibernateDatabaseSession(sessionFactory.createEntityManager());
    }
}
