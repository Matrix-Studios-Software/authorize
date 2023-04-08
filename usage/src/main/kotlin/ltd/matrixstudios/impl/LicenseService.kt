package ltd.matrixstudios.impl

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LicenseService {

    @POST("verify/{id}")
    fun verify(@Path("id") license: String, @Body product: JsonObject) : Call<Boolean>

}