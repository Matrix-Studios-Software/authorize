package ltd.matrixstudios.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.LongSerializationPolicy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthorizeUsage {

    val gson: Gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:4223/license/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    lateinit var service: LicenseService

    fun load()
    {
        service = retrofit.create(LicenseService::class.java)

        val verified = service.verify("Test123", JsonObject().also { it.addProperty("product", "Test") }).execute()

        println(verified.body())
    }

    @JvmStatic
    fun main(args: Array<String>) {
        load()
    }
}