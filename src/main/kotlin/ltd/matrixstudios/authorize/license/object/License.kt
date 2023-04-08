package ltd.matrixstudios.authorize.license.`object`

import ltd.matrixstudios.jasper.annotation.DataObject

@DataObject("licenses")
data class License(
    var id: String,
    var discordId: String,
    var ip: MutableList<String>,
    var duration: Long,
    var admin: Boolean,
    var addedAt: Long,
    var product: String
)