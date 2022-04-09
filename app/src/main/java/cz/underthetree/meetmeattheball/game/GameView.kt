package cz.underthetree.meetmeattheball.game

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.SensorManager
import android.util.Log
import android.view.SurfaceView
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

    private val background: Background //pouze obrázek co stojí
    private var player: GameObject //objekt s kterým se pohybuje
    private var table: GameObject //objekt s kterým se pohybuje

    //Controls
    private lateinit var accelerometer: Accelerometer
    private var ax = 0f
    private var ay = 0f

//    private lateinit var objectPlacer: ObjectPlacer
    init
    {
        paint = Paint()
        //AKCELEROMETR CODE
        accelerometer = Accelerometer(context)

        // create a listener for accelerometer
        accelerometer.setListener { tx, ty, tz ->
            //landscape mód osy přehozené tedy
            ay = tx
            ax = ty

            //Log.i("ay",ay.toString())
            //Log.i("ax",ax.toString())
        }

        //Log.i("ratioX", screenRatioX.toString())
        //Log.i("ratioY", screenRatioY.toString())

        //Instantiate objects
        background = Background(windowSizeX, windowSizeY, resources)
        player = GameObject(Point(windowSizeX,windowSizeY), resources, R.drawable.sadbg, .1f, paint)
        table = GameObject(Point(windowSizeX,windowSizeY), resources, R.drawable.sadbg, .2f ,paint)

        //SET POSITIONS
        player.setPosition(500*screenRatioX,200*screenRatioY)
        table.setPosition(500*screenRatioX, 500*screenRatioY)

//        objectPlacer = ObjectPlacer(table , 3)

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


            canvas.drawBitmap(background.background, background.x.toFloat(), background.y.toFloat(), paint)
            table.draw(canvas)

/*
            objectPlacer.objects[0].draw(canvas)
            objectPlacer.objects[1].draw(canvas)
            objectPlacer.objects[2].draw(canvas)

*/
            player.draw(canvas)
            Log.i("playerPosY", player.transform.y.toString())
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun update()
    {
        movement()
        borderCollision(player)
    }

    fun sleep()
    {
        Thread.sleep(17)
    }

    fun movement() {

        Log.i( "aBeforeX", (ax).toString())
        Log.i( "aBeforeY", (ay).toString())

        //PŘEVEDENÍ NA 1 A -1
        if(ax > 1f)
            ax = 1f
        else if (ax < -1f)
            ax = -1f

        if(ay > 1f)
            ay = 1f
        else if (ay < -1f)
            ay = -1f

        //POKUD SE NEHÝBE TAK ZPOMALUJE
        if(ax < 1.5f && ax > -1.5f)
        {
            ax = (player.movement.x*-.03f)
        }
        if(ay < 1.5f && ay > -1.5f)
        {
            ay = (player.movement.y*-.03f)
        }
        Log.i( "aX", (ax).toString())
        Log.i( "aY", (ay).toString())

        player.movement.addValues(ax,ay, 15f)
        Log.i( "movementX", (player.movement.x).toString())
        Log.i( "movementY", (player.movement.y).toString())


        player.addPosition(player.movement.x *  screenRatioX, player.movement.y *  screenRatioY)
    }


    private fun collisions(): Boolean
    {
        return (player.checkColision(table))
    }

    private fun borderCollision(obj: GameObject)
    {
        if(obj.transform.x < 0 || obj.transform.x > windowSizeX) {
            obj.setPosition(obj.transform.x + (obj.movement.x*-1), obj.transform.y)
            obj.movement.x *= -1
        }
        if(obj.transform.y < 0 || obj.transform.y > windowSizeY) {
            obj.setPosition(obj.transform.x, obj.transform.y + (obj.movement.y*-1))
            obj.movement.y *= -1
        }
    }

    fun showQuestion() {
        val i = Intent(activity, QuestionActivity::class.java)
        i.putExtra("characterIndex", 2)

        activity.startActivity(i)
        activity.finish()
    }

}