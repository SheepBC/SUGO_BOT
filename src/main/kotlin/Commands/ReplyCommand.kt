package Commands

import dev.kord.core.event.message.MessageCreateEvent
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

class Reply_Command {

    var command_funcion_instances = mapOf<KFunction<*>, KClass<*>>()
    var command_funcions = listOf<KFunction<*>>()

    init {
        val class_list = mutableListOf<KClass<*>>()
        class_list.add(GameCommand.Companion::class)

        command_funcion_instances = getCommand_funs(class_list)
        command_funcions = command_funcion_instances.keys.toList()
    }

    suspend fun run(event: MessageCreateEvent){
        val command = event.message.referencedMessage!!.actionRows.first()
            .components.first().data.customId.value!!

        if (!command.startsWith("숩")) return

        val replyCommandList = command.split(" ")

        val function = getFunction(replyCommandList[0].substring(1))!!

        function.callSuspendBy(
            mapOf(
                function.parameters[0] to command_funcion_instances[function]!!.objectInstance,
                function.parameters[1] to event,
                function.parameters[2] to replyCommandList,
            )
        )

    }

    private fun getCommand_funs(class_list: MutableList<KClass<*>>): Map<KFunction<*>,KClass<*>> {
        val list = mutableMapOf<KFunction<*>,KClass<*>>()

        for (command_class in class_list){
            for ( member in command_class.memberFunctions){
                val annotation = member.findAnnotation<ReplyCommand>()
                if (annotation != null){
                    list[member] = command_class
                }
            }
        }

        return list
    }

    private fun getFunction(command: String): KFunction<*>? {
        for( function in command_funcions){
            if (function.findAnnotation<ReplyCommand>()!!.msgCommand == command){
                return function
            }
        }
        return null;
    }

}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ReplyCommand(
    val msgCommand: String
)