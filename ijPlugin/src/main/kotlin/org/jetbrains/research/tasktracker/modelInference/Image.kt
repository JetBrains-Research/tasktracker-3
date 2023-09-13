package org.jetbrains.research.tasktracker.modelInference

class Image(val content: List<Int>) {
    companion object {
        fun fromFile(path: String): Image {
            TODO()
//            return Image(Image::class.java.getResource(path).readBytes().asList())
        }
    }
}

fun main() {
    Image.fromFile("/img/img.png")
}