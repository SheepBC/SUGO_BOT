package Commands

import DataBases.GameHistory
import Tools.*
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.edit
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.addFile
import dev.kord.rest.builder.message.embed
import kotlinx.coroutines.delay
import kotlinx.serialization.json.*
import kotlin.io.path.Path

class GameCommand {

    companion object{

        private val countryTldList = mutableListOf<String>()

        init {
            setCountryTld()
        }

        private fun setCountryTld() {
            val url = "https://restcountries.com/v3.1/independent?status=true&fields=tld"
            val json = OtherTool.getJson(url)
            for(jsonTld in json.jsonArray){
                if(jsonTld.jsonObject["tld"]!!.jsonArray.isEmpty()) continue
                var tld = jsonTld.jsonObject["tld"]!!.jsonArray.get(0).toString()
                tld = tld.replace(".","")
                tld = tld.replace("\"","")
                countryTldList.add(tld)
            }

        }

        @Command(["나라","ㄴㄹ","국기","ㄱㄱ","country"])
        suspend fun game(event: MessageCreateEvent, msg_list: List<String>){
            val msg = event.sendMessage("퀴즈 제작중...")

            val tld = countryTldList.random()
            val fakeTld1 = countryTldList.random()
            val fakeTld2 = countryTldList.random()
            val fakeTld3 = countryTldList.random()

            val randomList = mutableListOf(tld)
            randomList.add(fakeTld1)
            randomList.add(fakeTld2)
            randomList.add(fakeTld3)

            randomList.shuffle()

            val nameJson = OtherTool.getJson("https://restcountries.com/v3.1/alpha/$tld?fields=translations")
            msg.edit {
                content = ""
                embed {
                    title = "이 나라는?"
                    image = "https://flagcdn.com/w160/$tld.png"
                    footer = EmbedBuilder.Footer(text = "CountryQuiz")
                }

                actionRow {
                    randomList.forEachIndexed { index, code ->
                        val name = getKorName(code)
                        interactionButton(
                            style = ButtonStyle.Primary,
                            customId = "CountryQuiz $code",
                            builder = {
                                label = "${index + 1}. $name"
                            }
                        )
                    }
                }
            }

            delay(15000)

            msg.edit {
                components = mutableListOf()

                val tldNameList = nameJson.jsonObject["translations"]!!.jsonObject["kor"]!!
                val official = tldNameList.jsonObject["official"]!!.toString().replace("\"","")
                val common = tldNameList.jsonObject["common"]!!.toString().replace("\"","")

                embed {
                    title = official
                    description = common
                    image = "https://flagcdn.com/w160/$tld.png"
                    footer = EmbedBuilder.Footer(text = "CountryQuiz")
                }
            }

        }

        @Deprecated("예시")
        @ReplyCommand("CountryQuiz")
        suspend fun checkAnswer(event: MessageCreateEvent, msg_list: List<String>){

        }

        private fun getKorName(tld: String): String{
            val json = OtherTool.getJson("https://restcountries.com/v3.1/alpha/$tld?fields=translations")

            val list = json.jsonObject["translations"]!!.jsonObject["kor"]!!.jsonObject.toList()

            val name = list.random().second

            return name.toString().replace("\"","")
        }

        @Command(["나라기록","ㄴㄹㄱㄹ","countryhistory"])
        suspend fun countryHistory(event: MessageCreateEvent, msg_list: List<String>) {
            val path = GameHistory.getRegionBarsFile()
            val userDate = GameHistory.getGameHistoryUser(event.member?.id?.value!!)
            var answerPercent = 0
            if (userDate[0]+userDate[1] != 0){
                answerPercent = userDate[0]*100/(userDate[0]+userDate[1])
            }
            event.message.channel.createMessage {
                embed {
                    title = "Country Quiz History with User Data"
                    image = "attachment://$path"
                    fields = mutableListOf(
                        EmbedBuilder.Field(
                            name = "${event.member?.nickname!!}의 기록",
                            value = "정답: ${userDate[0]}개 오답: ${userDate[1]}개 " +
                                    "정답률: ${answerPercent}%"
                        )
                    )
                }
                addFile(Path(path))
            }
        }

    }

}