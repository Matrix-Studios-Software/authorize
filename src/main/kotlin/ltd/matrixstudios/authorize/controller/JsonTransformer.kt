package ltd.matrixstudios.authorize.controller

import com.google.gson.JsonElement
import ltd.matrixstudios.authorize.Authorize
import spark.ResponseTransformer

object JsonTransformer : ResponseTransformer {

    override fun render(model: Any?): String {
        if (model is JsonElement)
            return model.toString()

        return Authorize.gson.toJson(model)
    }
}