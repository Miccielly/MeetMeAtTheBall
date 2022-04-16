package cz.underthetree.meetmeattheball.utils

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class FileReader {

    fun read() {

        val inputStream: InputStream = File("example.txt").inputStream()
        val lineList = mutableListOf<String>()

//        val is = assets.open

        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        lineList.forEach { println(">  " + it) }
    }

    fun write() {
        val outputStream: OutputStream = File("bruh.txt").outputStream()

        outputStream.bufferedWriter().use { out ->
            out.write("sheesh")

        }
    }

    //read file myText.txt from assets folder


}