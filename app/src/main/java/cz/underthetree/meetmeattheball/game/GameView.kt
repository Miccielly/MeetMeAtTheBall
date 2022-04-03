package cz.underthetree.meetmeattheball.game

import android.content.Context
import android.graphics.*
import android.hardware.SensorManager
import android.util.Log
import android.view.SurfaceView
import cz.underthetree.meetmeattheball.R

class GameView(context: Context?, val sizeX:Int, val sizeY:Int) : SurfaceView(context), Runnable {

    private lateinit var sensorManager:SensorManager
    private lateinit var thread:Thread
    private var isPlaying = false

    private val paint: Paint

    private val screenRatioX:Float = 1920f/sizeX
    private val screenRatioY:Float = 1080f/sizeY

    private val bg1: Background //pouze obrázek co stojí
    private var obj: GameObject //objekt s kterým se pohybuje
    private var obj2: GameObject //objekt s kterým se pohybuje


    init
    {
        paint = Paint()

        Log.i("ratioX", screenRatioX.toString())
        Log.i("ratioY", screenRatioY.toString())

        bg1 = Background(sizeX, sizeY, resources)

        obj = GameObject(Point(0,0), Point(sizeX,sizeY), resources, R.drawable.sadbg, .1f)
        obj2 = GameObject(Point(0,0), Point(sizeX,sizeY), resources, R.drawable.sadbg, .1f)
        obj2.transform.x = 50*screenRatioX.toInt()
        obj2.transform.y = 600*screenRatioY.toInt()

    }

    override fun run() {
        while(isPlaying)
        {
            update()
            draw()
            sleep()
        }

    }

    fun resume()
    {
        isPlaying = true
        thread = Thread(this)
        thread.start()
    }

    fun pause()
    {
        isPlaying = false
        //pravděpodobně tady ani join nemá být?
        thread.join()   //v javě tohle bylo obalené try catchem
    }

    fun draw()
    {
        if(holder.surface.isValid())
        {
            val canvas: Canvas = holder.lockCanvas()
            canvas.drawColor(Color.BLUE)   //clearing screen
            canvas.drawBitmap(bg1.background, bg1.x.toFloat(), bg1.y.toFloat(), paint)
            canvas.drawBitmap(obj.bitmap, obj.transform.x.toFloat(),0f, paint)
            canvas.drawBitmap(obj2.bitmap, obj2.transform.x.toFloat(),obj2.transform.y.toFloat(), paint)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun update()
    {
//        sensorManager.get
        movement()
    }

    fun sleep()
    {
        Thread.sleep(17)
    }

    fun movement() {
        obj.transform.x += 1 * screenRatioX.toInt()   //pohyb objektem do leva
    }
}