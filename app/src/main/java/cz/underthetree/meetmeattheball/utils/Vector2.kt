package cz.underthetree.meetmeattheball.utils

import java.util.*

class Vector2 (var x:Float, var y:Float)
{
    constructor() : this(0f,0f) {

    }

    fun dist(v: Vector2) : Float
    {
        val d = Math.sqrt(
            Math.pow(x.toDouble() - v.x, 2.0)
                    + Math.pow(y.toDouble() - v.y, 2.0)
        )

        return d.toFloat()
    }
}