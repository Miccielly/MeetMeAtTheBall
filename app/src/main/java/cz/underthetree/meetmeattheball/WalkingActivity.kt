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

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        //val displaySize:Point = Point()
//        val displaySize:Point = Point(2280, 1080) //Xiaomi Mi A2 lite
        //TODO na mobilu to nevezme asi správnou size a je to 0 0 tím pádem při násobení pozice objektů se nepohne (násobení 0)
        //windowManager.defaultDisplay.getSize(displaySize)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val displaySize = Point(width,height)

        Log.i("height", height.toString())
        Log.i("width", width.toString())



        gameView = GameView(this, this, displaySize.x, displaySize.y)

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