package Bc.Sheep_BC.Tools

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.io.File

fun Toml.getToml(file_path: String): Toml{
    val file = File(file_path)

    return Toml().read(file)
}

fun Toml.setString(key: String,value: String){
    val map = this.toMap()
    map[key] = value
    val new_toml_string = TomlWriter().write(map)

    this.read(new_toml_string)

}

fun Toml.save(file_path: String) {
    TomlWriter().write(this.toMap(),File(file_path))
}