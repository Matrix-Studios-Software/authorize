package ltd.matrixstudios.authorize.util.logger

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import java.util.logging.*
import java.util.logging.Formatter

object APILogger : Logger("API", null) {
    init {
        val handler = ConsoleHandler()
        handler.level = Level.ALL
        handler.formatter = APILoggerFormatter

        addHandler(handler)
        useParentHandlers = false
        level = Level.ALL
    }

    object APILoggerFormatter : Formatter() {
        private val DT_FORMAT: SimpleDateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")

        override fun format(record: LogRecord): String {
            return buildString {
                append("[" + formatTime() + "]")
                append(" ")
                append("(" + record.level.name + ")")
                append(": ")
                append("[API]")
                append(" ")
                append(record.message)
                append("\n")
            }
        }

        private fun formatTime(): String {
            return DT_FORMAT.format(Date.from(Instant.now()))
        }
    }
}