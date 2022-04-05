package cz.underthetree.meetmeattheball

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cz.underthetree.meetmeattheball.game.GameView

class WalkingActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displaySize:Point = Point()
        //TODO na mobilu to nevezme asi správnou size a je to 0 0 tím pádem při násobení pozice objektů se nepohne (násobení 0)
        windowManager.defaultDisplay.getSize(displaySize)


        gameView = GameView(this, displaySize.x, displaySize.y)

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