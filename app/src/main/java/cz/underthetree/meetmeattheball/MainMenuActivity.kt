package cz.underthetree.meetmeattheball

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainMenuActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenu_activity)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        val playBtn = findViewById(R.id.playBtn) as Button
        playBtn.findViewById<Button>(R.id.playBtn).setOnClickListener {
            play()
        }

        val quitBtn = findViewById(R.id.quitBtn) as Button
        quitBtn.findViewById<Button>(R.id.quitBtn).setOnClickListener {
            quit()
        }
    }

    private fun play()
    {
        //val switchActivityIntent = Intent(this, QuestionActivity::class.java)
        val switchActivityIntent = Intent(this, QuestionActivity::class.java)
        startActivity(switchActivityIntent)
    }

    private fun quit()
    {
        finish()
        System.exit(0)
    }
}