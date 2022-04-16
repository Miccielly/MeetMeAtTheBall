package cz.underthetree.meetmeattheball.game

import android.content.Context
import cz.underthetree.meetmeattheball.utils.Accelerometer
import cz.underthetree.meetmeattheball.utils.Vector2

class Controls(context: Context) {
    var accelerometer: Accelerometer
    private var ax = 0f
    private var ay = 0f

    val drunkMovement = 8
    var drunkness = 0f


    init {
        accelerometer = Accelerometer(context)
        // create a listener for accelerometer
        accelerometer.setListener { tx, ty, tz ->
            //landscape mód osy přehozené tedy
            ay = tx
            ax = ty
        }
    }

    fun movement(player: GameObject) {
        val tolerance = 1.5f

        //PŘEVEDENÍ NA 1 A -1
        if (ax > tolerance)
            ax = tolerance
        else if (ax < -tolerance)
            ax = -tolerance
        else
            ax = (player.movement.x * -.03f)  //pokud se telefon nenaklání vůbec

        if (ay > tolerance)
            ay = tolerance
        else if (ay < -tolerance)
            ay = -tolerance
        else
            ay = (player.movement.y * -.03f)  //pokud se telefon nenaklání vůbec

        drunkInfluence()
        player.movement.addValues(ax, ay, 15f)
    }

    private fun drunkInfluence() {
        var dx = (-drunkMovement..drunkMovement).random().toFloat()
        dx *= drunkness

        var dy = (-drunkMovement..drunkMovement).random().toFloat()
        dy *= drunkness

        ax += dx
        ay += dy
    }

}