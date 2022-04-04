package cz.underthetree.meetmeattheball

import android.graphics.Point
import android.hardware.display.DisplayManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cz.underthetree.meetmeattheball.game.GameView

class WalkingActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val point:Point = Point()
        windowManager.defaultDisplay.getSize(point)


        gameView = GameView(this, point.x, point.y)

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