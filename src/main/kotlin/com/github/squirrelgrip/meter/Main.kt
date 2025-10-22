package com.github.squirrelgrip.meter

import com.github.squirrelgrip.meter.database.hibernate.HibernateDatabaseSessionFactory
import com.github.squirrelgrip.meter.service.DefaultErrorHandler
import com.github.squirrelgrip.meter.service.MeterReaderService
import java.io.File

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty() || args[0] == "--help") {
            println("Usage: com.github.squirrelgrip.meter.Main <file>")
        } else {
            val file = File(args[0])
            if (file.exists()) {
                MeterReaderService(
                    DefaultErrorHandler(),
                    HibernateDatabaseSessionFactory()
                ).process(file)
            } else {
                println("$file does not exist.")
            }
        }
    }
}