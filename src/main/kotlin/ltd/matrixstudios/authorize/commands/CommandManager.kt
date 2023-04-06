package ltd.matrixstudios.authorize.commands

import revxrsal.commands.cli.ConsoleCommandHandler

object CommandManager
{

    val COMMAND_HANDLER = ConsoleCommandHandler.create()

    fun register()
    {
        COMMAND_HANDLER.register(LicenseCommands())
    }
}