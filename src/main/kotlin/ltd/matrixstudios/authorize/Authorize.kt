package ltd.matrixstudios.authorize

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import ltd.matrixstudios.authorize.commands.CommandManager
import ltd.matrixstudios.authorize.controller.Controller
import ltd.matrixstudios.authorize.controller.JsonTransformer
import ltd.matrixstudios.authorize.controller.route.Path
import ltd.matrixstudios.authorize.controller.route.methods.*
import ltd.matrixstudios.authorize.filters.RequestFilter
import ltd.matrixstudios.authorize.license.controller.LicenseController
import ltd.matrixstudios.authorize.license.mongo.LicenseContainer
import ltd.matrixstudios.authorize.util.logger.APILogger
import ltd.matrixstudios.jasper.Jasper
import spark.Route
import spark.kotlin.ignite
import java.lang.Thread.sleep
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.starProjectedType

object Authorize {

    private val server = ignite()

    private val controllers = hashSetOf<KClass<out Controller>>(LicenseController::class)

    val gson: Gson = GsonBuilder().serializeNulls().setLongSerializationPolicy(LongSerializationPolicy.STRING).create()

    fun load() {
        val port = 4223
        val host = "127.0.0.1"

        server.service.port(port)
        server.ipAddress(host)

        APILogger.info("                _   _                _         \n" +
                "     /\\        | | | |              (_)        \n" +
                "    /  \\  _   _| |_| |__   ___  _ __ _ _______ \n" +
                "   / /\\ \\| | | | __| '_ \\ / _ \\| '__| |_  / _ \\\n" +
                "  / ____ \\ |_| | |_| | | | (_) | |  | |/ /  __/\n" +
                " /_/    \\_\\__,_|\\__|_| |_|\\___/|_|  |_/___\\___|\n" +
                "                                               ")

        APILogger.info("Using host $host with port $port")

        server.service.before { request, _ ->
            APILogger.info("[INBOUND] Incoming request")
            APILogger.info(request.ip())
            APILogger.info(request.pathInfo())
        }

       server.service.before("/*")
        { request, response ->
            RequestFilter.handleRequest(request, response)
        }


        Jasper.createClient("mongodb://localhost:27017", "Authorize")

        CommandManager.register()

        LicenseContainer.loadAll()

        for (controller in controllers) {
            val objectInstance = controller.objectInstance ?: continue

            objectInstance.load()

            val classPath = controller.findAnnotations<Path>().firstOrNull()?.path ?: ""

            for (member in controller.declaredMembers) {
                if (member.returnType != Route::class.starProjectedType) {
                    continue
                }

                val path = member.findAnnotations<Path>().firstOrNull()?.path ?: ""

                for (annotation in member.annotations) {
                    when(annotation.annotationClass) {
                        GET::class -> {
                            server.service.get("$classPath$path", member.call(objectInstance) as Route, JsonTransformer)
                            APILogger.info("[GET] $classPath$path")
                        }
                        POST::class -> {
                            server.service.post("$classPath$path", member.call(objectInstance) as Route, JsonTransformer)
                            APILogger.info("[POST] $classPath$path")
                        }
                        PUT::class -> {
                            server.service.put("$classPath$path", member.call(objectInstance) as Route, JsonTransformer)
                            APILogger.info("[PUT] $classPath$path")
                        }
                        PATCH::class -> {
                            server.service.patch("$classPath$path", member.call(objectInstance) as Route, JsonTransformer)
                            APILogger.info("[PATCH] $classPath$path")
                        }
                        DELETE::class -> {
                            server.service.delete("$classPath$path", member.call(objectInstance) as Route, JsonTransformer)
                            APILogger.info("[DELETE] $classPath$path")
                        }
                    }
                }
            }
        }

        CommandManager.COMMAND_HANDLER.pollInput()

        while (true) {
            sleep(100L)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        load()
    }
}