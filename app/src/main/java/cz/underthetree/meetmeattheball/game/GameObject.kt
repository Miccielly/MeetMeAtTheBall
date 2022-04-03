package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point

class GameObject (var transform:Point, var size:Point, val res:Resources, val drawable:Int, val sizeRelativeToScreen:Float){

    //přidat point na pozici která bude ve středu obrázku (defaultně je to levý horní roh)
    public lateinit var bitmap:Bitmap

    init {
        bitmap = BitmapFactory.decodeResource(res, drawable)
        val sizeX = size.x*sizeRelativeToScreen
        val sizeY = size.y*sizeRelativeToScreen
        bitmap = Bitmap.createScaledBitmap(bitmap, sizeX.toInt(), sizeY.toInt(), false)
    }

    public fun setPosition()
    {


    }
}