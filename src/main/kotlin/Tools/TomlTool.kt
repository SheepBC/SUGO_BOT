package Bc.Sheep_BC.Tools

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.io.File

fun Toml.getToml(file_path: String): Toml{
    val file = File(file_path)
    try {
        return Toml().read(file)
    }catch (e: Exception) {
        file.createNewFile()
        val reFile = File(file_path)
        return Toml().read(reFile)
    }
}

fun Toml.setString(key: String,value: String){
    val map = this.toMap()
    map[key] = value
    val newTomlString = TomlWriter().write(map)

    this.read(newTomlString)

}

fun <T> Toml.setType(key: String, value: T){
    val map = this.toMap()
    map[key] = value
    val newTomlString = TomlWriter().write(map)

    this.read(newTomlString)

}

fun Toml.setBoolean(key: String,value: Boolean){
    val map = this.toMap()
    map[key] = value
    val newTomlString = TomlWriter().write(map)

    this.read(newTomlString)

}

fun Toml.save(file_path: String) {
    TomlWriter().write(this.toMap(),File(file_path))
}