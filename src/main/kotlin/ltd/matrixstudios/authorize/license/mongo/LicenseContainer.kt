package ltd.matrixstudios.authorize.license.mongo

import ltd.matrixstudios.authorize.license.`object`.License
import ltd.matrixstudios.authorize.util.logger.APILogger
import ltd.matrixstudios.jasper.container.JasperContainer
import java.util.UUID

object LicenseContainer : JasperContainer<String, License>(License::class.java)
{
    private val cache = hashMapOf<String, License>()

    fun loadAll() = this.getAllItems().forEach { item -> cache[item.id] = item }

    fun getFromCache(id: String) : License?
    {
        val isCached = cache.containsKey(id)

        return if (isCached) {
            cache[id]!!
        } else this.getByKey(id).firstOrNull()
    }

    fun rawMongoFetch(id: String) : License?
    {
        return this.getByKey(id).firstOrNull()
    }

    fun save(model: License)
    {
        val start = System.currentTimeMillis()
        this.update(model.id, model)
        cache[model.id] = model

        APILogger.info("[METRIC] Mongo save took ${System.currentTimeMillis().minus(start)}ms")
    }


}