package cz.underthetree.meetmeattheball.game

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.SensorManager
import android.util.Log
import android.view.SurfaceView
import cz.underthetree.meetmeattheball.MainMenuActivity
import cz.underthetree.meetmeattheball.QuestionActivity
import cz.underthetree.meetmeattheball.R
import cz.underthetree.meetmeattheball.WalkingActivity
import cz.underthetree.meetmeattheball.utils.Accelerometer

class GameView(context: Context?, val activity: WalkingActivity, val windowSizeX:Int, val windowSizeY:Int) : SurfaceView(context), Runnable {

    private lateinit var sensorManager:SensorManager
    private lateinit var thread:Thread
    private var isPlaying = false

    private val paint: Paint

    //TODO ačkoliv násobíme tímto, vypadá to že pohyb objektů není nezávislý na rozlišení obrazovky (možná je to frameratem?)
    private val screenRatioX:Float = windowSizeX/1920f
    private val screenRatioY:Float = windowSizeY/1080f

    private val bg1: Background //pouze obrázek co stojí
    private var obj: GameObject //objekt s kterým se pohybuje
    private var obj2: GameObject //objekt s kterým se pohybuje


    //Controls
    private lateinit var accelerometer: Accelerometer
    private var ax = 0f
    private var ay = 0f

    init
    {
        paint = Paint()

        //Log.i("ratioX", screenRatioX.toString())
        //Log.i("ratioY", screenRatioY.toString())

        bg1 = Background(windowSizeX, windowSizeY, resources)

        obj = GameObject(Point(windowSizeX,windowSizeY), resources, R.drawable.sadbg, .1f, paint)


        obj2 = GameObject(Point(windowSizeX,windowSizeY), resources, R.drawable.sadbg, .2f ,paint)

        //SET POSITIONS
        obj.setPosition(500*screenRatioX,200*screenRatioY)
        obj2.setPosition(500*screenRatioX, 500*screenRatioY)

        //AKCELEROMETR CODE
        accelerometer = Accelerometer(context)

        // create a listener for accelerometer
        accelerometer.setListener { tx, ty, tz ->
            //landscape mód osy přehozené tedy
            ay = tx
            ax = ty

            Log.i("ay",ay.toString())
            Log.i("ax",ax.toString())
        }
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

        accelerometer.register()
    }

    fun pause()
    {
        isPlaying = false
        //pravděpodobně tady ani join nemá být?
        thread.join()   //v javě tohle bylo obalené try catchem

        accelerometer.unregister()
    }

    fun draw()
    {
        //je možné že to nepustí přes tento if
        if(holder.surface.isValid()) {
            val canvas: Canvas = holder.lockCanvas()

            if(collisions()) {
                canvas.drawColor(Color.RED)   //clearing screen
                showQuestion()
            }
            else
                canvas.drawColor(Color.BLUE)   //clearing screen


            canvas.drawBitmap(bg1.background, bg1.x.toFloat(), bg1.y.toFloat(), paint)
            obj2.draw(canvas)
            obj.draw(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun update()
    {
        movement()

    }

    fun sleep()
    {
        Thread.sleep(17)
    }

    fun movement() {

        Log.i( "X", ax.toString())
        Log.i( "Z", ay.toString())

        if(ax > 1f)
            ax = 1f
        else if (ax < -1f)
            ax = -1f

        if(ay > 1f)
            ay = 1f
        else if (ay < -1f)
            ay = -1f



//        obj.transform.x += ax.toInt() * screenRatioX.toInt()   //pohyb objektem do leva
//        obj.transform.y += ay.toInt() * screenRatioX.toInt()   //pohyb objektem do leva

        Log.i("screenRatioX", screenRatioX.toString())
        Log.i("screenRatioY", screenRatioY.toString())


        obj.addPosition(ax *  screenRatioX, ay *  screenRatioY)

    }


    private fun collisions(): Boolean
    {
        return (obj.checkColision(obj2))
    }

    fun showQuestion() {
        activity.startActivity(Intent(activity, QuestionActivity::class.java))
        activity.finish()
        Log.i("end", screenRatioY.toString())

    }

}