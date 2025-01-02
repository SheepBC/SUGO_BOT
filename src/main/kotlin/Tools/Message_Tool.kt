package Tools

class Message_Tool {

    fun prefix_check(prefixes: List<String>,message: String): Boolean{

        val prefix= message[0].toString()
        return prefixes.contains<String>(prefix)
    }

}