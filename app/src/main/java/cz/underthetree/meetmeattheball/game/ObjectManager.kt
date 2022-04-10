package cz.underthetree.meetmeattheball.game

import android.graphics.Canvas

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

/*
        objects[0].setPosition((0..objectPrefab.windowSize.x).random().toFloat(), (0..objectPrefab.windowSize.y).random().toFloat())
        objects[1].setPosition((0..objectPrefab.windowSize.x).random().toFloat(), (800f))
        objects[1].setPosition(10f, (0..objectPrefab.windowSize.y).random().toFloat())

        Log.i("objPos",objects[0].transform.x.toString())
        Log.i("objPos",objects[1].transform.x.toString())
        Log.i("objPos",objects[2].transform.x.toString())

 */
    }

    fun makeObjects() {
        objects.add(objectPrefab)   //přidání prefabu

        //přidání kopií prefabu (-2 protože první -1 je prefab a začínáme od nuly takže to je druhý -1)
        for (i in 0..objectCount - 2) {
            objects.add(duplicateObject())
        }
    }

    private fun duplicateObject(): GameObject {
        //vytvoří nový objekt se stejnými hodnotami jako prefabObject
        return GameObject(
            objectPrefab.windowSize,
            objectPrefab.res,
            objectPrefab.drawable,
            objectPrefab.sizeRelativeToScreen,
            objectPrefab.paint
        )
    }

    fun drawObjects(canvas: Canvas) {
        for (GameObject in objects) {
            GameObject.draw(canvas)
        }
    }

    fun updateObjects()
    {
        for (GameObject in objects) {
            GameObject.update()
        }
    }

}