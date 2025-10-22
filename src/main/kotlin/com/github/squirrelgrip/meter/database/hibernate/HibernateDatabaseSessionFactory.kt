package com.github.squirrelgrip.meter.database.hibernate

import com.github.squirrelgrip.meter.database.DatabaseSessionFactory
import com.github.squirrelgrip.meter.entity.MeterReading
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder

class HibernateDatabaseSessionFactory : DatabaseSessionFactory {
    private val sessionFactory: SessionFactory

    init {
        val registry = StandardServiceRegistryBuilder().build()
        try {
            sessionFactory = MetadataSources(registry)
                .addAnnotatedClass(MeterReading::class.java)
                .buildMetadata()
                .buildSessionFactory()
        } catch (e: Exception) {
            StandardServiceRegistryBuilder.destroy(registry)
            throw e
        }
    }

    override fun create(): HibernateDatabaseSession =
        HibernateDatabaseSession(sessionFactory.createEntityManager())
}
