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
    private var alcohol: FlyingObject //objekt s kterým se pohybuje
    private var time: FlyingObject //objekt s kterým se pohybuje

    //Controls
    private var accelerometer: Accelerometer
    private var ax = 0f
    private var ay = 0f

    //GAME OBJECTS
    private var objectManager: ObjectManager
    private var alcoholObjectManager: ObjectManager
    private lateinit var timeObjectManager: ObjectManager

    //GAME VALUES
    private var drunkness = 0f
    private val drunknessLimit = .7f
    private val drunkMovement = 8

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
        player =
            GameObject(Point(windowSizeX, windowSizeY), resources, R.drawable.greendot, .1f, paint)
        table = GameObject(
            Point(windowSizeX, windowSizeY),
            resources,
            R.drawable.tablewhite,
            .2f,
            paint
        )
        alcohol = FlyingObject(
            Point(windowSizeX, windowSizeY), resources, R.drawable.alcohol, .15f, paint,
            Vector2()
        )
        time = FlyingObject(
            Point(windowSizeX, windowSizeY), resources, R.drawable.bluedot, .075f, paint,
            Vector2()
        )

        //SET POSITIONS
        player.setPosition(500 * screenRatioX, 200 * screenRatioY)
//        table.setPosition(500 * screenRatioX, 500 * screenRatioY)

        objectManager = ObjectManager(table, 2, Vector2(screenRatioX, screenRatioY),false)
        alcoholObjectManager = ObjectManager(alcohol, 8, Vector2(screenRatioX, screenRatioY),true)
        timeObjectManager = ObjectManager(time, 8, Vector2(screenRatioX, screenRatioY),true)

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

            if (tableCollisions()) {
                canvas.drawColor(Color.RED)   //clearing screen
//                showQuestion()
            } else
                canvas.drawColor(Color.BLUE)   //clearing screen

            canvas.drawBitmap(
                background.background,
                background.x.toFloat(),
                background.y.toFloat(),
                paint
            )

            objectManager.drawObjects(canvas)
            alcoholObjectManager.drawObjects(canvas)
            timeObjectManager.drawObjects(canvas)
            player.draw(canvas)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun update() {
        alcoholObjectManager.updateObjects()    //pohyb objektů alkoholu
        alcoholBorderCollision()    //narazil alkohol do hrany obrazovky?
        alcoholCollisions() //je alkohol v okruhu kolize?
        resetAlcoholCollisions()    //vyskočil alkohol z okruhu kolize?

        timeObjectManager.updateObjects()    //pohyb objektů alkoholu
        timeBorderCollision()    //narazil alkohol do hrany obrazovky?


        movement()  //ovládání pohybu hráče
        borderCollision(player) //narazil objekt hráče do hrany obrazovky?
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

        drunkInfluence()
        player.movement.addValues(ax, ay, 15f)

        player.addPosition(player.movement.x * screenRatioX, player.movement.y * screenRatioY)
    }

    private fun tableCollisions(): Boolean {
        var col = false

        for (GameObject in objectManager.objects) {
            if (player.checkColision(GameObject as GameObject)) {
                col = true
                showQuestion(GameObject)
                return col  //stačí jeden stůl nemusíme projíždět jestli tu je další
            }
        }
        return col
    }

    private fun alcoholCollisions() {
        for (GameObject in alcoholObjectManager.objects) {

            //je object v kolizi?
            if (player.checkColision(GameObject as GameObject)) {

                //přetypování na flying object a nastavení kolize na true
                val obj = GameObject as FlyingObject

                //pokud ještě necollidoval přidat opilost a nastavit kolizi na true
                if (!obj.collided) {
                    if (drunkness < drunknessLimit) {
                        drunkness += 0.05f
                        obj.collided = true
                        Log.i("collision", obj.collided.toString());

                    } else
                        drunkness = drunknessLimit
                }
                Log.i("alcohol", drunkness.toString());
            }
        }

    }

    private fun resetAlcoholCollisions() {
        for (GameObject in alcoholObjectManager.objects) {
            //je object mimo kolizi?
            if (!player.checkColision(GameObject as GameObject)) {

                //přetypování na flying object a nastavení kolize na true
                val obj = GameObject as FlyingObject

                //pokud byl v kolizi
                if (obj.collided) {
                    obj.collided = false
                    Log.i("collision", obj.collided.toString());
                }

            }
        }
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

    fun showQuestion(obj: GameObject) {
        val i = Intent(activity, QuestionActivity::class.java)
        i.putExtra("characterIndex", obj.extrasValue)

        activity.startActivity(i)
        activity.finish()
    }

    //TODO udělat pro time a alcohol to samé
    private fun alcoholBorderCollision() {
        for (FlyingObject in alcoholObjectManager.objects) {
            borderCollision(FlyingObject as GameObject)
        }
    }

    private fun timeBorderCollision() {
        for (FlyingObject in timeObjectManager.objects) {
            borderCollision(FlyingObject as GameObject)
        }
    }

    private fun drunkInfluence() {
        var dx = (-drunkMovement..drunkMovement).random().toFloat()
        dx *= drunkness

        var dy = (-drunkMovement..drunkMovement).random().toFloat()
        dy *= drunkness

        ax += dx
        ay += dy
    }
}