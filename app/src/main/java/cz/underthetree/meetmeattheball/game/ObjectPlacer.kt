package cz.underthetree.meetmeattheball.game

import android.util.Log
import java.util.*

//Vytváří objekty a pokládá je do prostoru
class ObjectPlacer(val objectPrefab: GameObject, val objectCount: Int) {

    val objects : MutableList<GameObject> = arrayListOf()

    init
    {
        makeObjects()   //vytvoří list kopií prefabObject
        placeObjects()  //rozmístí objekty po obrazovce
    }

    fun placeObjects()
    {

        objects.forEach {
            gameObject -> gameObject.setPosition((0..objectPrefab.windowSize.x).random().toFloat(), (0..objectPrefab.windowSize.y).random().toFloat())
        }

/*
        objects[0].setPosition((0..objectPrefab.windowSize.x).random().toFloat(), (0..objectPrefab.windowSize.y).random().toFloat())
        objects[1].setPosition((0..objectPrefab.windowSize.x).random().toFloat(), (800f))
        objects[1].setPosition(10f, (0..objectPrefab.windowSize.y).random().toFloat())

        Log.i("objPos",objects[0].transform.x.toString())
        Log.i("objPos",objects[1].transform.x.toString())
        Log.i("objPos",objects[2].transform.x.toString())

 */
    }

    fun makeObjects()
    {
        objects.add(objectPrefab)   //přidání prefabu

        //přidání kopií prefabu (-2 protože první -1 je prefab a začínáme od nuly takže to je druhý -1)
        for(i in 0..objectCount-2) {
            objects.add(copyObject())
        }
    }

    private fun copyObject(): GameObject
    {
        //vytvoří nový objekt se stejnými hodnotami jako prefabObject
        return GameObject(objectPrefab.windowSize, objectPrefab.res, objectPrefab.drawable, objectPrefab.sizeRelativeToScreen, objectPrefab.paint)
    }

}