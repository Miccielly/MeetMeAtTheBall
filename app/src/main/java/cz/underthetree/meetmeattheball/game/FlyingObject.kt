package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Point

import cz.underthetree.meetmeattheball.utils.Vector2

class FlyingObject(
    windowSize: Point?,
    res: Resources?,
    drawable: Int,
    sizeRelativeToScreen: Float,
    paint: Paint?,
    mov: Vector2,
    limit: Float
) : GameObject(windowSize!!, res!!, drawable, sizeRelativeToScreen, paint!!) {

    init {
        movement.addValues(mov.x, mov.y, limit)
    }

    override fun update() {
        addPosition(movement.x, movement.y)
    }
}