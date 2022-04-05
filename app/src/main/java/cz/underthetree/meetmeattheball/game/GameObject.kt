package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.*

class GameObject (var size:Point, val res:Resources, val drawable:Int, val sizeRelativeToScreen:Float, val paint: Paint){

    //přidat point na pozici která bude ve středu obrázku (defaultně je to levý horní roh)
    var bitmap:Bitmap
    var transform:Point = Point()
    init {
        bitmap = BitmapFactory.decodeResource(res, drawable)
        val sizeX = size.x*sizeRelativeToScreen
        val sizeY = size.y*sizeRelativeToScreen
        bitmap = Bitmap.createScaledBitmap(bitmap, sizeX.toInt(), sizeY.toInt(), false)
    }

    public fun setPosition()
    {


    }

    public fun draw(canvas: Canvas)
    {
        canvas.drawBitmap(bitmap, transform.x.toFloat(), transform.y.toFloat(), paint)
    }

    public fun checkColision()
    {

    }
}