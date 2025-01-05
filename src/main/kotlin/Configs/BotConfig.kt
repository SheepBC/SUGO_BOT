package Configs

import Bc.Sheep_BC.Tools.getToml
import com.moandjiezana.toml.Toml

class BotConfig {

    val Bot_Toml = Toml().getToml(BOT_INFORM_FILE_PATH)

    companion object {
        const val BOT_INFORM_FILE_PATH = "Informs/Bot_Inform.toml"

        const val BOT_TOKEN = "Bot_Token"
        const val BOT_PLAYING = "Bot_Playing"
        const val BOT_PREFIXES = "Bot_Prefixes"
    }

}