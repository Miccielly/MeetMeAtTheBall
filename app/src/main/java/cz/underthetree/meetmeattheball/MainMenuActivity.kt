package cz.underthetree.meetmeattheball

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cz.underthetree.meetmeattheball.utils.Gyroscope


class MainMenuActivity: AppCompatActivity() {

    private lateinit var gyroscope: Gyroscope
    private lateinit var textView: TextView


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


        //GYROSCOPE CODE
        gyroscope = Gyroscope(this)
        textView = findViewById(R.id.gyroText)


        gyroscope.setListener { rx, ry, rz ->
            // on rotation method of gyroscope
            // set the color green if the device rotates on positive z axis
            textView.text = rx.toString() + " | "+ ry.toString() + " | "+ rz.toString()

            if (rz > 1.0f) {
                window.decorView.setBackgroundColor(Color.GREEN)
            } else if (rz < -1.0f) {
                window.decorView.setBackgroundColor(Color.YELLOW)
            }
        }
    }

    private fun play()
    {
        //val switchActivityIntent = Intent(this, QuestionActivity::class.java)
        val switchActivityIntent = Intent(this, WalkingActivity::class.java)
        startActivity(switchActivityIntent)
    }

    private fun quit()
    {
        finish()
        System.exit(0)
    }

    override fun onResume() {
        super.onResume()
        gyroscope.register()
    }

    override fun onPause() {
        super.onPause()
        gyroscope.unregister()
    }

}