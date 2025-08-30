package com.github.squirrelgrip.meter;

import com.github.squirrelgrip.meter.database.hibernate.HibernateDatabaseSession;
import com.github.squirrelgrip.meter.database.hibernate.HibernateDatabaseSessionFactory;
import com.github.squirrelgrip.meter.service.DefaultErrorHandler;
import com.github.squirrelgrip.meter.service.DefaultLineHandler;
import com.github.squirrelgrip.meter.service.MeterReaderService;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1 || args[0].equals("--help")) {
            System.out.println("Usage: com.github.squirrelgrip.meter.Main <file>");
        } else {
            File file = new File(args[0]);
            if (file.exists()) {
                HibernateDatabaseSessionFactory databaseSessionFactory = new HibernateDatabaseSessionFactory();
                new MeterReaderService(
                        new DefaultLineHandler(),
                        new DefaultErrorHandler(),
                        databaseSessionFactory
                ).process(file);

                try(HibernateDatabaseSession hibernateDatabaseSession = databaseSessionFactory.create() ){
                    System.out.println(hibernateDatabaseSession.count() + " records created.");
                }
            } else {
                System.out.println(file + " does not exist.");
            }
        }
    }
}