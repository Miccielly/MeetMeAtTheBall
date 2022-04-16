package cz.underthetree.meetmeattheball.game

import android.util.Log
import cz.underthetree.meetmeattheball.utils.GameObjectPredecesor
import cz.underthetree.meetmeattheball.utils.Vector2

class TableObjectManager(
    objectPrefab: GameObjectPredecesor,
    objectCount: Int,
    screenRatio: Vector2,
    flyingObjectType: Boolean,
    private val player: GameObject
) : ObjectManager(objectPrefab, objectCount, screenRatio, flyingObjectType) {

    private val borderDistance = 100f

    //dá pozici stolu
    override fun placeObjects()
    {
        var dist = 0f

        var pos = Vector2()
        do
        {
            val minX = borderDistance*screenRatio.x
            val maxX = objectPrefab.windowSize.x.toFloat() - borderDistance*screenRatio.x
            pos.x = ((minX.toInt()..maxX.toInt()).random()).toFloat()

            val minY = 3*borderDistance*screenRatio.y
            val maxY = objectPrefab.windowSize.y.toFloat() - borderDistance*screenRatio.y
            pos.y = ((minY.toInt()..maxY.toInt()).random()).toFloat()

            dist = pos.dist(player.transform)
    }
        while (dist < (player.scale.x*3f))  //je potřeba aby byl položen hráč dřív než table (hráč je souměrný, tedy čtverec)

        objectPrefab.setPosition(pos.x, pos.y)
//        Log.i("tablePlacement", dist.toString())
//        Log.i("tablePlacementSize", (player.scale.x*2f).toString())
    }


}


