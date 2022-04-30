package cz.underthetree.meetmeattheball.utils

import android.content.Context
import android.content.Intent

class ActivityLoader {

    fun load(context: Context, c: Class<*>?)
    {
        val switchActivityIntent = Intent(context, c)
        context.startActivity(switchActivityIntent)
    }
}