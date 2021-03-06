package cz.underthetree.meetmeattheball

import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cz.underthetree.meetmeattheball.game.GameView


class WalkingActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics) //obsolete, ale nenašel jsem nějaké novější řešení co by fungovalo
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val displaySize = Point(width, height)

        Log.i("height", height.toString())
        Log.i("width", width.toString())

        val context = applicationContext
        gameView = GameView(context, this, displaySize.x, displaySize.y)

        setContentView(gameView)
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}