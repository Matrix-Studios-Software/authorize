package ltd.matrixstudios.authorize.license.controller

import com.google.gson.JsonObject
import ltd.matrixstudios.authorize.Authorize
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

    @POST
    @Path("/verify/:id")
    fun verifyLicense(): Route {
        return Route { request, response ->
            val pathId = request.params(":id") ?: return@Route false
            val startLookup = System.currentTimeMillis()
            val item = LicenseContainer.getFromCache(pathId)

            val body = Authorize.gson.fromJson(request.body(), JsonObject::class.java)

            if (item == null) return@Route false

            if (body == null || body.isEmpty) return@Route false

            val productKey: String = body["product"].asString ?: return@Route false

            if (productKey == item.product) return@Route true

            APILogger.info("[METRIC] Fetch from mongo took ${System.currentTimeMillis().minus(startLookup)}ms")

            return@Route false
        }
    }
}