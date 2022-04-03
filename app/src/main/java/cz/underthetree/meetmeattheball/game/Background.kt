package cz.underthetree.meetmeattheball.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cz.underthetree.meetmeattheball.R

class Background(sizeX: Int, sizeY: Int, res: Resources?) {
    var x = 0
    var y = 0
    var background: Bitmap

    init
    {
        background = BitmapFactory.decodeResource(res, R.drawable.treemeter)
        background = Bitmap.createScaledBitmap(background, sizeX, sizeY, false)
    }
}