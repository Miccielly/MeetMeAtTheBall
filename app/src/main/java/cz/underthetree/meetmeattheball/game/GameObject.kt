package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.*
import android.util.Log

class GameObject (var windowSize:Point, val res:Resources, val drawable:Int, val sizeRelativeToScreen:Float, val paint: Paint){

    //přidat point na pozici která bude ve středu obrázku (defaultně je to levý horní roh)
    var bitmap:Bitmap
    var transform:Point = Point()
    var absoluteSize:Point = Point()

    var sizeX:Float
    var sizeY:Float

    init {
        bitmap = BitmapFactory.decodeResource(res, drawable)
        sizeX = windowSize.x*sizeRelativeToScreen
        sizeY = windowSize.y*sizeRelativeToScreen
        bitmap = Bitmap.createScaledBitmap(bitmap, sizeX.toInt(), sizeY.toInt(), false)

        Log.i("absSizeX:", sizeX.toString())
        Log.i("absSizeY:", sizeY.toString())

    }

    public fun setPosition()
    {


    }

    public fun draw(canvas: Canvas)
    {
        canvas.drawBitmap(bitmap, transform.x.toFloat(), transform.y.toFloat(), paint)
    }

    public fun  checkColision(obj: GameObject):Boolean
    {
        val d = Math.sqrt(
            Math.pow(transform.x.toDouble() - +obj.transform.x, 2.0)
                    + Math.pow(transform.y.toDouble() - obj.transform.y, 2.0)
        )

        Log.i("collision", (d).toString() )

        return d < 100

//
//        val dx = Math.abs(transform.x - obj.transform.x);
//        val dy = Math.abs(transform.y - obj.transform.y);
//
//        Log.i("distanceX", dx.toString() )
//        Log.i("distanceY", dy.toString() )

    }
}