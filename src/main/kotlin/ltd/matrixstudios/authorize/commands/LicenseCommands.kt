package ltd.matrixstudios.authorize.commands

import ltd.matrixstudios.authorize.license.controller.LicenseController
import ltd.matrixstudios.authorize.license.mongo.LicenseContainer
import ltd.matrixstudios.authorize.license.`object`.License
import ltd.matrixstudios.authorize.util.logger.APILogger
import revxrsal.commands.annotation.Command
import revxrsal.commands.cli.core.CommandLineActor

class LicenseCommands {

    @Command("create")
    fun create(actor: CommandLineActor, name: String)
    {
        val model = License(name, "Change", mutableListOf(), Long.MAX_VALUE, false, System.currentTimeMillis())
        LicenseContainer.save(model)

        APILogger.info("[COMMAND] Created a new license and sent to mongo")
    }

    @Command("benchmark")
    fun benchmark(actor: CommandLineActor, amount: Int)
    {
        var index = 0
        for (int in 0 until amount) {
            val model = License(
                "Benchmark_Model_${index++}",
                "Change",
                mutableListOf(),
                Long.MAX_VALUE,
                false,
                System.currentTimeMillis()
            )
            LicenseContainer.save(model)

            APILogger.info("[COMMAND] Created a new license and sent to mongo")
        }
    }

    @Command("lookfor")
    fun lookfor(actor: CommandLineActor, id: String)
    {
        val start = System.currentTimeMillis()
        val item = LicenseContainer.rawMongoFetch(id)

        APILogger.info("[METRIC] Found existing license from mongo in ${System.currentTimeMillis().minus(start)}ms")
    }

}