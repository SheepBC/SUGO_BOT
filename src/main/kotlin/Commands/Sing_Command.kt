package Commands

import dev.kord.core.event.message.MessageCreateEvent

class Sing_Command {

    companion object{
        @Command(commands = ["노래","play"])
        suspend fun sing(event: MessageCreateEvent, msg_list: List<String>){
            TODO("노래 작동 기능")
        }
    }

}