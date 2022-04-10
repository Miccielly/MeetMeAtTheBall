package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.*
import android.util.Log
import cz.underthetree.meetmeattheball.utils.Vector2

//TODO udělat variaci gameobjectu jen na sbírací věci, kde bude proměnná na uložení co načíst?(jen pokud bychom dělali i další třídy)
open class GameObject(
    var windowSize: Point,
    val res: Resources,
    val drawable: Int,
    val sizeRelativeToScreen: Float,
    val paint: Paint
) {
    var bitmap: Bitmap   //obrázek objektu
    var transform: Vector2 = Vector2()   //pozice objektu
    var origin: Vector2 = Vector2()   //pozice objektu
    var scale: Vector2 = Vector2()   //velikost objektu (od levé strany do pravé strany)
    var movement: Vector2 = Vector2()

    init {
        bitmap = BitmapFactory.decodeResource(res, drawable)
        scale.x =
            windowSize.y * sizeRelativeToScreen //oboje je Y protože je to menší strana a chceeme stejně spíš kulaté kolize
        scale.y = windowSize.y * sizeRelativeToScreen //
        bitmap = Bitmap.createScaledBitmap(bitmap, scale.x.toInt(), scale.y.toInt(), false)

        setPosition(0f, 0f)  //kvůli přepočtu souřadnic
    }

    fun setPosition(x: Float, y: Float) {
        transform.x = x
        transform.y = y

        //přepočet pozice na střed objektu
        origin.x = transform.x + (scale.x / 2)
        origin.y = transform.y + (scale.y / 2)
    }

    fun addPosition(x: Float, y: Float) {
        //přepočet pozice na střed objektu
        transform.x += x
        transform.y += y

        //přepočet pozice na střed objektu
        origin.x = transform.x + (scale.x / 2)
        origin.y = transform.y + (scale.y / 2)

//        Log.i("transformX:", transform.x.toString())
//        Log.i("transformY:", transform.y.toString())
    }


    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, transform.x - (scale.x / 2), transform.y - (scale.y / 2), paint)
    }

    fun checkColision(obj: GameObject): Boolean {
        val d = transform.dist(obj.transform) //vzdálenost mezi středy objektů

        if (scale.x > obj.scale.x) {
            return d < scale.x / 2  // dělíme dvouma jelikož jde o rádius poloměru
        } else
            return d < obj.scale.x / 2  // dělíme dvouma jelikož jde o rádius poloměru

    }

    open fun update()
    {
        Log.i("update:", "old")

    }


}