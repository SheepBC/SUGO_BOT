package DataBases

import Bc.Sheep_BC.Tools.*
import Configs.GameConfig
import com.moandjiezana.toml.Toml
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.api.gather
import org.jetbrains.kotlinx.dataframe.api.into
import org.jetbrains.kotlinx.kandy.dsl.categorical
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.layers.bars
import org.jetbrains.kotlinx.kandy.util.color.Color

object GameHistory {

    fun getGuildHistoryAccept(guildId: ULong): Boolean{
        val gameToml = Toml().getToml(GameConfig.GAME_HISTORY_ACCEPT)
        val textGuildId = guildId.toString()
        if (gameToml.contains(textGuildId)) {
            val isAccept = gameToml.getBoolean(textGuildId)
            return isAccept
        }
        println(gameToml.contains(textGuildId))
        gameToml.setBoolean(textGuildId,false)
        gameToml.save(GameConfig.GAME_HISTORY_ACCEPT)
        return false
    }

    fun setGuildHistoryAccept(guildId: ULong,isAccept: Boolean){
        val gameToml = Toml().getToml(GameConfig.GAME_HISTORY_ACCEPT)
        val textGuildId = guildId.toString()
        gameToml.setBoolean(textGuildId,isAccept)
        gameToml.save(GameConfig.GAME_HISTORY_ACCEPT)

    }

    fun setGameHistoryUser(userId: ULong,list: List<Int>){
        val gameToml = Toml().getToml(GameConfig.GAME_HISTORY_USER_DB)
        gameToml.setType(userId.toString(),list)
        gameToml.save(GameConfig.GAME_HISTORY_USER_DB)
    }

    fun getGameHistoryUser(userId: ULong): MutableList<Int> {
        val gameToml = Toml().getToml(GameConfig.GAME_HISTORY_USER_DB)
        var list = gameToml.getList<Int>(userId.toString())
        if (list.isNullOrEmpty()) {
            list = mutableListOf()
            list.add(0)
            list.add(0)
        }
        return list
    }

    fun setGameHistoryRegion(region: String,list: List<Int>){
        val gameToml = Toml().getToml(GameConfig.GAME_HISTORY_REGION_DB)
        gameToml.setType(region,list)
        gameToml.save(GameConfig.GAME_HISTORY_REGION_DB)
    }

    fun getGameHistoryRegion(region: String): MutableList<Int> {
        val gameToml = Toml().getToml(GameConfig.GAME_HISTORY_REGION_DB)
        var list = gameToml.getList<Int>(region)
        if (list.isNullOrEmpty()) {
            list = mutableListOf()
            list.add(0)
            list.add(0)
        }
        return list
    }

    fun getRegionBarsFile(): String{
        val gameToml = Toml().getToml(GameConfig.GAME_HISTORY_REGION_DB)
        val names = gameToml.toMap().keys.toList()
        val collects = List(names.size) { gameToml.getList<Int>(names[it])[0].toInt() }
        val wrongs = List(names.size) {gameToml.getList<Int>(names[it])[1].toInt()}

        val datas = dataFrameOf(
            "names" to names,
            "correct" to collects,
            "wrong" to wrongs,
        ).gather("correct","wrong").into("Answer","answer")
        plot(datas) {
            layout {
                title = "모든 유저 대륙별 기록"
            }
            bars {
                x("names")
                y("answer")
                fillColor("Answer") {
                    scale = categorical(
                        "correct" to Color.BLUE,
                        "wrong" to Color.RED
                    )
                }
            }

        }.save(
            filename = "test.png",
            path = ""
        )
        return "test.png"
    }

}