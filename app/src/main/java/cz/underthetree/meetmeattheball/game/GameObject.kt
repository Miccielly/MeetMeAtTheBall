package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.*
import android.util.Log
import cz.underthetree.meetmeattheball.utils.Vector2

class GameObject (var windowSize:Point, val res:Resources, val drawable:Int, val sizeRelativeToScreen:Float, val paint: Paint){

    var bitmap:Bitmap   //obrázek objektu
    var transform:Vector2 = Vector2()   //pozice objektu
    var origin:Vector2 = Vector2()   //pozice objektu
    var scale:Vector2 = Vector2()   //velikost objektu

    init {
        bitmap = BitmapFactory.decodeResource(res, drawable)
        scale.x = windowSize.x*sizeRelativeToScreen
        scale.y = windowSize.y*sizeRelativeToScreen
        bitmap = Bitmap.createScaledBitmap(bitmap, scale.x.toInt(), scale.y.toInt(), false)




        setPosition(0f,0f)
    }

    fun setPosition(x:Float, y:Float)
    {
        transform.x = x
        transform.y = y

        //přepočet pozice na střed objektu
        origin.x = transform.x + (scale.x/2)
        origin.y = transform.y + (scale.y/2)


        if(x > 0)
        Log.i("transformX:", transform.x.toString())
        Log.i("transformY:", transform.y.toString())

        Log.i("originX:", origin.x.toString())
        Log.i("originY:", origin.y.toString())
    }

    fun addPosition(x:Float, y:Float)
    {
        //přepočet pozice na střed objektu
        transform.x += x
        transform.y += y

        //přepočet pozice na střed objektu
        origin.x = transform.x + (scale.x/2)
        origin.y = transform.y + (scale.y/2)
    }


    fun draw(canvas: Canvas)
    {
        canvas.drawBitmap(bitmap, transform.x - (scale.x/2), transform.y - (scale.y/2), paint)
    }

    fun  checkColision(obj: GameObject):Boolean
    {
        val d = transform.dist(obj.transform)

        Log.i("collision", (d).toString() )

        return d < 100

    }
}