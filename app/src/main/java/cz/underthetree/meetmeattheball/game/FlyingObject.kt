package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.Log

import cz.underthetree.meetmeattheball.utils.Vector2

class FlyingObject(
    windowSize: Point?,
    res: Resources?,
    drawable: Int,
    sizeRelativeToScreen: Vector2,
    paint: Paint?,
    mov: Vector2,
) : GameObject(windowSize!!, res!!, drawable, sizeRelativeToScreen, paint!!) {


    init {
        movement.setValues(mov.x, mov.y)
    }

    override fun update(screenRatio: Vector2) {
        Log.i("update:", "new")
        addPosition(movement.x * screenRatio.x, movement.y * screenRatio.y)
    }

    fun drawUncollided(canvas: Canvas)
    {
        if(!collided)
            super.draw(canvas)
    }

}