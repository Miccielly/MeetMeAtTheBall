package cz.underthetree.meetmeattheball

import android.app.Application

class MyApp : Application() {

    var characterQuestionsCount = 3   //počet otázek na charakter

    var characterPhaseCounter = 0  //kolik už proběhlo fází tázání?
    var characterPhaseCount = 3  //maximální počet hovorů
    var includeToilet = false   //připočítává mezi počet hovorů i záchod

    var maxTimeObjects = 12 //maximální počet objektů času
    var minTimeObjects = 8 //maximální počet objektů času

    override fun onCreate() {
        super.onCreate()
    }
}