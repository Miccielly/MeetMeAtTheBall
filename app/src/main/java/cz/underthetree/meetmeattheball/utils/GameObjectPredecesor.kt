package cz.underthetree.meetmeattheball.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import cz.underthetree.meetmeattheball.game.GameObject

interface GameObjectPredecesor {
    val windowSize: Point
    val res: Resources
    val drawable: Int
    val sizeRelativeToScreen: Vector2
    val paint: Paint
    var bitmap: Bitmap   //obrázek objektu
    var transform: Vector2
    var origin: Vector2
    var scale: Vector2
    var movement: Vector2
    var extrasValue: Int
    var collided: Boolean
    fun setPosition(x: Float, y: Float)
    fun setPosition(position: Vector2)

    fun addPosition(x: Float, y: Float)

    fun setScale(x: Float, y: Float)

    open fun update(screenRatio: Vector2)
    fun draw(canvas: Canvas)
    fun checkColision(obj: GameObject): Boolean
    }