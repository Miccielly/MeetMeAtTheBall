package cz.underthetree.meetmeattheball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EndingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ending)

        val endBtn = findViewById(R.id.endBtn) as Button
        endBtn.findViewById<Button>(R.id.endBtn).setOnClickListener {
            this.finish()
        }
    }
}