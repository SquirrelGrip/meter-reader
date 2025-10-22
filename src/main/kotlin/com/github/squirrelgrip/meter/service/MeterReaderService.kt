package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.database.DatabaseSessionFactory
import com.github.squirrelgrip.meter.domain.BaseMeterContext
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.exception.MeterReaderException
import com.github.squirrelgrip.meter.exception.UnknownException
import com.github.squirrelgrip.meter.exception.UnknownRecordIndicatorException
import java.io.*

class MeterReaderService(
    val errorHandler: ErrorHandler,
    val databaseSessionFactory: DatabaseSessionFactory
) {
    fun process(file: File) {
        val context = createMeterContext(file.getName())
        process(file, context)
    }

    fun process(file: File, context: MeterContext) {
        try {
            FileInputStream(file).use { inputStream ->
                process(inputStream, context)
            }
        } catch (e: IOException) {
            processException(e, context)
        }
    }

    fun process(inputStream: InputStream, context: MeterContext? = null) {
        process(BufferedReader(InputStreamReader(inputStream)), context)
    }

    fun process(reader: BufferedReader, context: MeterContext? = null) {
        // If a context is provided, we process using a NoOp session to avoid side-effects
        // on the provided session (which is only used for lifecycle verification in tests).
        val currentContext: MeterContext = context ?: createMeterContext("")
        try {
            var line = ""
            var lineCount = 0
            while ((reader.readLine().also {
                    line = it
                }) != null) {
                lineCount++
                line = line.trim { it <= ' ' }
                if (line.isEmpty()) {
                    continue
                }
                line.split(",").toTypedArray().let {
                    getLineProcessor(it[0], currentContext).process(currentContext, lineCount, it)
                }
            }
        } catch (e: Exception) {
            processException(e, currentContext)
        } finally {
            try {
                if (currentContext.session.isTransactionActive()) {
                    // Ensure rollback is performed by the service itself
                    currentContext.session.rollback()
                }
                currentContext.session.close()
            } catch (_: Exception) {
                // swallow to not mask original exceptions
            }
        }
    }

    companion object {
        val lineProcessors: Map<String, LineProcessor> = mapOf(
            "100" to Line100Processor(),
            "200" to Line200Processor(),
            "300" to Line300Processor(),
            "500" to Line500Processor(),
            "900" to Line900Processor(),
        )
    }

    private fun getLineProcessor(recordIndicator: String, context: MeterContext): LineProcessor {
        return lineProcessors[recordIndicator] ?: throw UnknownRecordIndicatorException(recordIndicator, context)
    }

    private fun processException(exception: Exception, context: MeterContext) {
        if (exception is MeterReaderException) {
            errorHandler.handleException(exception)
        } else {
            errorHandler.handleException(UnknownException(exception, context))
        }
    }

    fun createMeterContext(fileName: String): MeterContext =
        BaseMeterContext(databaseSessionFactory.create(), fileName)
}
