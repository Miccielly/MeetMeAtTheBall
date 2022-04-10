package cz.underthetree.meetmeattheball.game

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.SensorManager
import android.view.SurfaceView
import cz.underthetree.meetmeattheball.QuestionActivity
import cz.underthetree.meetmeattheball.R
import cz.underthetree.meetmeattheball.WalkingActivity
import cz.underthetree.meetmeattheball.utils.Accelerometer
import cz.underthetree.meetmeattheball.utils.Vector2

class GameView(
    context: Context?,
    val activity: WalkingActivity,
    val windowSizeX: Int,
    val windowSizeY: Int
) : SurfaceView(context), Runnable {

    private lateinit var sensorManager: SensorManager
    private lateinit var thread: Thread
    private var isPlaying = false

    private val paint: Paint

    //TODO ačkoliv násobíme tímto, vypadá to že pohyb objektů není nezávislý na rozlišení obrazovky (možná je to frameratem?)
    private val screenRatioX: Float = windowSizeX / 1920f
    private val screenRatioY: Float = windowSizeY / 1080f

    private val background: Background //pouze obrázek co stojí
    private var player: GameObject //objekt s kterým se pohybuje
    private var table: GameObject //objekt s kterým se pohybuje
    private var obstacle: FlyingObject //objekt s kterým se pohybuje

    //Controls
    private lateinit var accelerometer: Accelerometer
    private var ax = 0f
    private var ay = 0f

    private lateinit var objectManager: ObjectManager
    private lateinit var obstacleManager: ObjectManager

    init {
        paint = Paint()
        //AKCELEROMETR CODE
        accelerometer = Accelerometer(context)

        // create a listener for accelerometer
        accelerometer.setListener { tx, ty, tz ->
            //landscape mód osy přehozené tedy
            ay = tx
            ax = ty
        }

        //Log.i("ratioX", screenRatioX.toString())
        //Log.i("ratioY", screenRatioY.toString())

        //Instantiate objects
        background = Background(windowSizeX, windowSizeY, resources)
        player = GameObject(Point(windowSizeX, windowSizeY), resources, R.drawable.greendot, .1f, paint)
        table = GameObject(Point(windowSizeX, windowSizeY), resources, R.drawable.bluedot, .2f, paint)
        obstacle = FlyingObject(Point(windowSizeX, windowSizeY), resources, R.drawable.reddot, .05f, paint, Vector2(3f,5f))

        //SET POSITIONS
        player.setPosition(500 * screenRatioX, 200 * screenRatioY)
        table.setPosition(500 * screenRatioX, 500 * screenRatioY)

        objectManager = ObjectManager(table, 3)
        obstacleManager = ObjectManager(obstacle, 4)

    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread.start()

        accelerometer.register()
    }

    fun pause() {
        isPlaying = false
        //pravděpodobně tady ani join nemá být?
        thread.join()   //v javě tohle bylo obalené try catchem

        accelerometer.unregister()
    }

    fun draw() {
        //je možné že to nepustí přes tento if
        if (holder.surface.isValid()) {
            val canvas: Canvas = holder.lockCanvas()

            if (collisions()) {
                canvas.drawColor(Color.RED)   //clearing screen
            //    showQuestion()
            } else
                canvas.drawColor(Color.BLUE)   //clearing screen

            canvas.drawBitmap(
                background.background,
                background.x.toFloat(),
                background.y.toFloat(),
                paint
            )

            objectManager.drawObjects(canvas)
            obstacleManager.drawObjects(canvas)
            player.draw(canvas)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun update() {
        obstacleManager.updateObjects()
        obstacleBorderCollision()
        movement()
        borderCollision(player)
    }

    fun sleep() {
        Thread.sleep(17)
    }

    fun movement() {
        val tolerance = 1.5f

        //PŘEVEDENÍ NA 1 A -1
        if (ax > tolerance)
            ax = tolerance
        else if (ax < -tolerance)
            ax = -tolerance
        else
            ax = (player.movement.x * -.03f)  //pokud se telefon nenaklání vůbec

        if (ay > tolerance)
            ay = tolerance
        else if (ay < -tolerance)
            ay = -tolerance
        else
            ay = (player.movement.y * -.03f)  //pokud se telefon nenaklání vůbec

        player.movement.addValues(ax, ay, 15f)

        player.addPosition(player.movement.x * screenRatioX, player.movement.y * screenRatioY)
    }

    private fun collisions(): Boolean {
        var col = false

        for (GameObject in objectManager.objects) {
            if (player.checkColision(GameObject as GameObject))
                col = true
        }

        return col
    }

    private fun borderCollision(obj: GameObject) {
        if (obj.transform.x < 0 || obj.transform.x > windowSizeX) {
            obj.setPosition(obj.transform.x + (obj.movement.x * -1), obj.transform.y)
            obj.movement.x *= -1
        }
        if (obj.transform.y < 0 || obj.transform.y > windowSizeY) {
            obj.setPosition(obj.transform.x, obj.transform.y + (obj.movement.y * -1))
            obj.movement.y *= -1
        }
    }

    fun showQuestion() {
        val i = Intent(activity, QuestionActivity::class.java)
        i.putExtra("characterIndex", 2)

        activity.startActivity(i)
        activity.finish()
    }

    private fun obstacleBorderCollision()
    {
        for(FlyingObject in obstacleManager.objects) {
                borderCollision(FlyingObject as GameObject)
        }
    }
}