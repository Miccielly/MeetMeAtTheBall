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
import cz.underthetree.meetmeattheball.utils.GameObjectPredecesor
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
    private var table: GameObject //objekt který reprezentuje stůl ke kterému se má hráč dostat
    private var character: GameObject //objekt který reprezentuje charakter u cílového stolu
    private var alcohol: FlyingObject //objekt s kterým se pohybuje
    private var time: FlyingObject //objekt s kterým se pohybuje

    //Controls
    private var controls: Controls = Controls(this.context) //hnedka instancujeme Controls

    //GAME OBJECTS
    private var tableManager: TableObjectManager
    private var alcoholObjectManager: ObjectManager
    private var timeObjectManager: ObjectManager

    //GAME OBJECTS VALUES
    private var charPicture: Int    //obrázek který se objeví u stolu

    //GAME VALUES
    private val drunknessLimit = .7f
    private var collectedTime = 0
    private val maxCollectedTime = (9..15).random()

    private var characterArrived = false
    private var tableCollision = false

    init {
        paint = Paint()

        //Instantiate objects
        background = Background(windowSizeX, windowSizeY, resources)
        player =
            GameObject(Point(windowSizeX, windowSizeY), resources, R.drawable.greendot, Vector2(.1f, .1f), paint)
        table = GameObject(
            Point(windowSizeX, windowSizeY),
            resources,
            R.drawable.tablewhite,
            Vector2(.2f,.2f),
            paint
        )

        when (table.extrasValue) {
            0 -> charPicture = R.drawable.c_psycholog
            1 -> charPicture = R.drawable.c_kuchar
            2 -> charPicture = R.drawable.c_cestovatel
            else -> charPicture = R.drawable.greendot
        }

        //TODO udělat charakter nečtvercový, aby nebyl spláclý
        character = GameObject(
            Point(windowSizeX, windowSizeY),
            resources,
            charPicture,
            Vector2(.175f, .325f),
            paint
        )
        alcohol = FlyingObject(
            Point(windowSizeX, windowSizeY), resources, R.drawable.alcohol, Vector2(.15f,.15f), paint,
            Vector2()
        )
        time = FlyingObject(
            Point(windowSizeX, windowSizeY), resources, R.drawable.bluedot, Vector2(.075f, .075f), paint,
            Vector2()
        )

        //SET POSITIONS
        player.setPosition(windowSizeX/2f, windowSizeY/2f)

        tableManager = TableObjectManager(table, 1, Vector2(screenRatioX, screenRatioY), false, player)
        tableManager.placeObjects()

        alcoholObjectManager = ObjectManager(alcohol, 8, Vector2(screenRatioX, screenRatioY), true)
        alcoholObjectManager.makeAndPlaceObjects()

        timeObjectManager = ObjectManager(time, 8, Vector2(screenRatioX, screenRatioY), true)
        timeObjectManager.makeAndPlaceObjects()

        character.setPosition(
            table.transform.x,
            table.transform.y - 130 * screenRatioY
        ) //ikdyž je table v objectManager má jen jeden objekt tudíž k němu patří jen jeden charakter

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

        controls.accelerometer.register()
    }

    fun pause() {
        isPlaying = false
        //pravděpodobně tady ani join nemá být?
        thread.join()   //v javě tohle bylo obalené try catchem

        controls.accelerometer.unregister()
    }

    fun draw() {
        //je možné že to nepustí přes tento if
        if (holder.surface.isValid()) {
            val canvas: Canvas = holder.lockCanvas()

            if (tableCollision) {
                canvas.drawColor(Color.RED)   //clearing screen
            } else
                canvas.drawColor(Color.BLUE)   //clearing screen

            canvas.drawBitmap(
                background.background,
                background.x.toFloat(),
                background.y.toFloat(),
                paint
            )

//            if (collectedTime >= maxCollectedTime)
                character.draw(canvas)

            table.draw(canvas)

            alcoholObjectManager.drawObjects(canvas)
            timeObjectManager.drawObjects(canvas)
            player.draw(canvas)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun update() {
        tableCollision = tableCollisions()

        alcoholObjectManager.updateObjects()    //pohyb objektů alkoholu
        flyingObjBorderCollision(alcoholObjectManager.objects)    //narazil alkohol do hrany obrazovky?
        alcoholCollisions() //je alkohol v okruhu kolize?
        resetFlyingObjectCollisions(alcoholObjectManager.objects)    //vyskočil alkohol z okruhu kolize?

        timeObjectManager.updateObjects()    //pohyb objektů času
        flyingObjBorderCollision(timeObjectManager.objects)    //narazil čas do hrany obrazovky?
        timeCollisions()    //střet s objektem času?
        resetFlyingObjectCollisions(timeObjectManager.objects)

        controls.movement(player)  //ovládání pohybu hráče
        player.addPosition(player.movement.x * screenRatioX, player.movement.y * screenRatioY)  //přidání pozice o aktuální movement hráče normovaný na velikost obrazovky

        borderCollision(player) //narazil objekt hráče do hrany obrazovky?
    }

    fun sleep() {
        Thread.sleep(17)
    }

    private fun tableCollisions(): Boolean {
        var col = false

        for (GameObject in tableManager.objects) {
            if (collectedTime >= maxCollectedTime && player.checkColision(GameObject as GameObject)) {
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
                    if (controls.drunkness < drunknessLimit) {
                        controls.drunkness += 0.05f
                        obj.collided = true
                        Log.i("collision", obj.collided.toString());

                    } else
                        controls.drunkness = drunknessLimit
                }
                Log.i("alcohol", controls.drunkness.toString());
            }
        }
    }

    private fun timeCollisions() {
        for (GameObject in timeObjectManager.objects) {

            //je object v kolizi?
            if (player.checkColision(GameObject as GameObject)) {

                //přetypování na flying object a nastavení kolize na true
                val obj = GameObject as FlyingObject

                //pokud ještě necollidoval přidat opilost a nastavit kolizi na true
                if (!obj.collided) {
                    obj.collided = true
                    collectedTime++

                    //posbírán všechen potřebný čas?
                    if (collectedTime >= maxCollectedTime)
                        characterArrived = true
                }
                Log.i("collectedTime", collectedTime.toString());
                Log.i("collidedReset", "false")

            }
        }
    }

    private fun resetFlyingObjectCollisions(list: MutableList<GameObjectPredecesor>) {
        for (GameObject in list) {
            //je object mimo kolizi?
            if (!player.checkColision(GameObject as GameObject)) {

                //přetypování na flying object a nastavení kolize na true
                val obj = GameObject as FlyingObject

                //pokud byl v kolizi
                if (obj.collided) {
                    obj.collided = false
                }
                Log.i("collidedReset", "true")
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

    private fun flyingObjBorderCollision(list: MutableList<GameObjectPredecesor>) {
        for (FlyingObject in list) {
            borderCollision(FlyingObject as GameObject)
        }
    }

}