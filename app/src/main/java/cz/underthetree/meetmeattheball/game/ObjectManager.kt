package cz.underthetree.meetmeattheball.game

import android.graphics.Canvas
import android.util.Log

//Vytváří objekty a pokládá je do prostoru
class ObjectManager(val objectPrefab: GameObject, val objectCount: Int) {

    val objects: MutableList<GameObject> = arrayListOf()

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

        //přidání kopií prefabu (-2 protože první -1 je prefab a začínáme od nuly takže to je druhý -1)
        for (i in 0..objectCount - 2) {
            objects.add(duplicateObject())
        }
    }

    fun drawObjects(canvas: Canvas) {
        for (GameObject in objects) {
            GameObject.draw(canvas)
        }
    }

    fun updateObjects()
    {
        var i = 0
        for (GameObject in objects) {
            GameObject.update()
            Log.i("objectIndex:", i++.toString())
        }
    }

    private fun duplicateObject(): GameObject {
        //vytvoří nový objekt se stejnými hodnotami jako prefabObject
        val obj = GameObject(
            objectPrefab.windowSize,
            objectPrefab.res,
            objectPrefab.drawable,
            objectPrefab.sizeRelativeToScreen,
            objectPrefab.paint
        )
        obj.movement = objectPrefab.movement
        return obj
    }

}