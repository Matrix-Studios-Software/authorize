package ltd.matrixstudios.authorize.license.controller

import ltd.matrixstudios.authorize.controller.Controller
import ltd.matrixstudios.authorize.controller.route.Path
import ltd.matrixstudios.authorize.controller.route.methods.GET
import ltd.matrixstudios.authorize.controller.route.methods.POST
import ltd.matrixstudios.authorize.license.mongo.LicenseContainer
import ltd.matrixstudios.authorize.license.`object`.License
import ltd.matrixstudios.authorize.util.logger.APILogger
import spark.Route
import spark.kotlin.halt

@Path("/license")
object LicenseController : Controller {
    override fun load() {}

    @GET
    @Path("/verify/:id")
    fun verifyLicense(): Route {
        return Route { request, response ->
            val pathId = request.params(":id") ?: return@Route false
            val startLookup = System.currentTimeMillis()
            val item = LicenseContainer.getFromCache(pathId)

            if (item == null) {
                return@Route false
            }

            APILogger.info("[METRIC] Fetch from mongo took ${System.currentTimeMillis().minus(startLookup)}ms")

            return@Route true
        }
    }

    @POST
    @Path("/create/:id")
    fun create() : Route {
        return Route { request, response ->
            val pathId = request.params(":id") ?: return@Route halt(404)
            val license = License(pathId, "Change", mutableListOf(), Long.MAX_VALUE, false, System.currentTimeMillis())

            return@Route license
        }
    }
}