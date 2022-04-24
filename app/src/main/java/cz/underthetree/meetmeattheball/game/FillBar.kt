package cz.underthetree.meetmeattheball.game

import android.graphics.Canvas
import android.util.Log
import cz.underthetree.meetmeattheball.utils.Vector2
import kotlinx.coroutines.flow.callbackFlow

class FillBar(private val sliderObj: GameObject, val backgroundImage: Int, val maxValue: Float) {

    private val sliderBackground = GameObject(
        sliderObj.windowSize,
        sliderObj.res,
        backgroundImage,
        sliderObj.sizeRelativeToScreen,
        sliderObj.paint
    )

    private var value = 0f


    init {
        setup()
        update()
    }

    fun draw(canvas: Canvas) {
        sliderBackground.draw(canvas)
        sliderObj.draw(canvas)
    }

    fun setPosition(pos: Vector2) {
        sliderObj.setPosition(pos)
    }

    fun setPosition(x: Float, y: Float) {
        sliderObj.setPosition(x, y)
        sliderBackground.setPosition(x, y)
    }

    fun setValue(v: Float) {
        if (value >= maxValue)
            return

        value = v
        update()
    }

    fun update() {
        val sizeX = (sliderBackground.scale.x * .8f) * (value / maxValue)   //velikost * procento naplnění
        sliderObj.setScale(sizeX, sliderBackground.scale.y * .8f)
    }

    private fun setup() {
        sliderObj.setScale(sliderBackground.scale.x * .8f, sliderBackground.scale.y * .8f)
    }
}