package DataBases

import Bc.Sheep_BC.Tools.*
import Configs.GameConfig
import com.moandjiezana.toml.Toml
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.CategoryChartBuilder
import org.knowm.xchart.style.Styler
import java.awt.Color

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

        val chart = CategoryChartBuilder()
            .width(600)
            .height(400)
            .title("모든 유저 대륙별 기록")
            .xAxisTitle("Name")
            .yAxisTitle("Answer")
            .build()

        chart.styler.legendPosition =Styler.LegendPosition.InsideNE
        chart.styler.chartBackgroundColor = Color.WHITE
        chart.styler.isStacked = false

        chart.addSeries("Correct",names,collects)
        chart.addSeries("Wrong",names,wrongs)
        val fileName = "region.png"
        BitmapEncoder.saveBitmap(chart,fileName,BitmapEncoder.BitmapFormat.PNG)
        return fileName
    }

}