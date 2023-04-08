package ltd.matrixstudios.authorize.filters

import spark.Request
import spark.Response
import spark.kotlin.halt

object RequestFilter {

    //make this whatever u want
    private const val key = "AuthorizeKey12345"

    fun handleRequest(request: Request, response: Response) {
        response.type("application/json")

        val apiKey = request.headers("Authorize-API-Key")
        if (apiKey == null)
        {
            halt(404)
        }

        if (apiKey != key) { halt(404, "Invalid APIKey") }


        if (request.contentType() != null && request.contentType() != "application/json" && request.contentType() != "application/json; charset=UTF-8")
        {
            println("Malformed content-type")
            halt(400, "Bad request")
        }
    }

}