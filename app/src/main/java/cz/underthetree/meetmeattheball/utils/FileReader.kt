package cz.underthetree.meetmeattheball.utils

import android.app.Activity
import android.util.Log
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class FileReader(val activity: Activity) {

    lateinit var questions: MutableList<String>

    fun readFromAsset(fileName: String): MutableList<String> {
        var string: String? = ""
        try {
            val inputStream: InputStream = activity.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            string = String(buffer)
        } catch (e: IOException) {
            Log.i("file", string.toString())
            e.printStackTrace()
        }
        if (string != null) {
            questions = string.split("/").toMutableList()

            for(question in questions)
                Log.i("file", question.toString())
        }

        return questions

    }

}