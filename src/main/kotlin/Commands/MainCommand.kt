package Commands

import dev.kord.core.event.message.MessageCreateEvent
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions


class Main_Command {

    var command_funcion_instances = mapOf<KFunction<*>,KClass<*>>()
    var command_funcions = listOf<KFunction<*>>()

    init {
        val class_list = mutableListOf<KClass<*>>()
        class_list.add(MusicCommand.Companion::class)
        class_list.add(GameCommand.Companion::class)

        command_funcion_instances = getCommand_funs(class_list)
        command_funcions = command_funcion_instances.keys.toList()
    }

    suspend fun run(event: MessageCreateEvent){
        val msg_list = event.message.content.split(" ")

        val function = getFunction(msg_list[0].substring(1))!!

        function.callSuspendBy(
            mapOf(
                function.parameters[0] to command_funcion_instances[function]!!.objectInstance,
                function.parameters[1] to event,
                function.parameters[2] to msg_list,
            )
        )

    }

    private fun getCommand_funs(class_list: MutableList<KClass<*>>): Map<KFunction<*>,KClass<*>> {
        val list = mutableMapOf<KFunction<*>,KClass<*>>()

        for (command_class in class_list){
            for ( member in command_class.memberFunctions){
                val annotation = member.findAnnotation<Command>()
                if (annotation != null){
                    list[member] = command_class
                }
            }
        }

        return list
    }

    private fun getFunction(command: String): KFunction<*>? {
        for( function in command_funcions){
            if (function.findAnnotation<Command>()!!.commands.contains(command.lowercase())){
                return function
            }
        }
        return null;
    }

}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
    val commands: Array<String>,
)