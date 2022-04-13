package cz.underthetree.meetmeattheball.game

import android.graphics.Canvas
import android.util.Log
import cz.underthetree.meetmeattheball.utils.GameObjectPredecesor
import cz.underthetree.meetmeattheball.utils.Vector2
import kotlin.reflect.typeOf

//Vytváří objekty a pokládá je do prostoru
class ObjectManager(
    val objectPrefab: GameObjectPredecesor,
    val objectCount: Int,
    val screenRatio: Vector2,
    val flyingObjectType: Boolean
) {

    val objects: MutableList<GameObjectPredecesor> = arrayListOf()

    //TODO podle počtu objektů rozdělit rovnoměrně počet objektů které volají jednotlivé charaktery + změnit obrázek k nim
    init {
        makeObjects()   //vytvoří list kopií prefabObject
        placeObjects()  //rozmístí objekty po obrazovce
    }

    fun placeObjects() {

        objects.forEach { gameObject ->
            gameObject.setPosition(
                (0..objectPrefab.windowSize.x).random().toFloat(),
                (0..objectPrefab.windowSize.y).random().toFloat()
            )
        }
    }

    fun makeObjects() {
        objects.add(objectPrefab)   //přidání prefabu

        if (flyingObjectType) {
            objectPrefab.movement = Vector2((-7..7).random().toFloat(), (-7..7).random().toFloat())
        } else {
            objectPrefab.extrasValue = (0..2).random()
        }

        //přidání kopií prefabu (-2 protože první -1 je prefab a začínáme od nuly takže to je druhý -1)
        for (i in 0..objectCount - 2) {

            if (flyingObjectType)
                objects.add(duplicateFlyingObject(objectPrefab))
            else {
                objects.add(duplicateObject(objectPrefab))
                Log.i("makeObj", "make")
            }
        }
    }

    fun drawObjects(canvas: Canvas) {
        for (GameObject in objects) {
            GameObject.draw(canvas)
        }
    }

    fun updateObjects() {
        for (GameObject in objects) {
            GameObject.update(screenRatio)
        }
    }

    private fun duplicateFlyingObject(obj: GameObjectPredecesor): FlyingObject {
        //vytvoří nový objekt se stejnými hodnotami jako prefabObject
        val obj = FlyingObject(
            objectPrefab.windowSize,
            objectPrefab.res,
            objectPrefab.drawable,
            objectPrefab.sizeRelativeToScreen,
            objectPrefab.paint, Vector2((-7..7).random().toFloat(), (-7..7).random().toFloat())
        )
        return obj
    }

    private fun duplicateObject(obj: GameObjectPredecesor): GameObject {
        //vytvoří nový objekt se stejnými hodnotami jako prefabObject
        val obj = GameObject(
            objectPrefab.windowSize,
            objectPrefab.res,
            objectPrefab.drawable,
            objectPrefab.sizeRelativeToScreen,
            objectPrefab.paint
        )
        obj.extrasValue = (0..2).random()
        Log.i("extrasValue", obj.extrasValue.toString())
        return obj
    }
}