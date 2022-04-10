package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.*
import android.util.Log
import cz.underthetree.meetmeattheball.utils.GameObjectPredecesor
import cz.underthetree.meetmeattheball.utils.Vector2

open class GameObject
    (
    override val windowSize: Point,
    override val res: Resources,
    override val drawable: Int,
    override val sizeRelativeToScreen: Float,
    override val paint: Paint
) : GameObjectPredecesor {

    final override lateinit var bitmap: Bitmap   //obrázek objektu
    override var transform: Vector2 = Vector2()   //pozice objektu
    override var origin: Vector2 = Vector2()   //pozice objektu
    override var scale: Vector2 = Vector2()   //velikost objektu (od levé strany do pravé strany)
    override var movement: Vector2 = Vector2()

    init {
        scale.x =
            windowSize.y * sizeRelativeToScreen //oboje je Y protože je to menší strana a chceeme stejně spíš kulaté kolize
        scale.y = windowSize.y * sizeRelativeToScreen //

        bitmap = BitmapFactory.decodeResource(res, drawable)
        bitmap = Bitmap.createScaledBitmap(bitmap, scale.x.toInt(), scale.y.toInt(), false)

        setPosition(0f, 0f)  //kvůli přepočtu souřadnic
    }

    override fun setPosition(x: Float, y: Float) {
        transform.x = x
        transform.y = y

        //přepočet pozice na střed objektu
        origin.x = transform.x + (scale.x / 2)
        origin.y = transform.y + (scale.y / 2)
    }

    override fun addPosition(x: Float, y: Float) {
        //přepočet pozice na střed objektu
        transform.x += x
        transform.y += y

        //přepočet pozice na střed objektu
        origin.x = transform.x + (scale.x / 2)
        origin.y = transform.y + (scale.y / 2)

//        Log.i("transformX:", transform.x.toString())
//        Log.i("transformY:", transform.y.toString())
    }


    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, transform.x - (scale.x / 2), transform.y - (scale.y / 2), paint)
    }

    override fun checkColision(obj: GameObject): Boolean {
        val d = transform.dist(obj.transform) //vzdálenost mezi středy objektů

        if (scale.x > obj.scale.x) {
            return d < scale.x / 2  // dělíme dvouma jelikož jde o rádius poloměru
        } else
            return d < obj.scale.x / 2  // dělíme dvouma jelikož jde o rádius poloměru

    }

    override fun update() {
        Log.i("update:", "old")
    }


}