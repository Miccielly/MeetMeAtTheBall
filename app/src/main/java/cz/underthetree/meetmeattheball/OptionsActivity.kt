package cz.underthetree.meetmeattheball

import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class OptionsActivity : AppCompatActivity() {

    private lateinit var switch: Switch
    private lateinit var myApp: MyApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        myApp = applicationContext as MyApp

        val backBtn = findViewById(R.id.pauseMenuBtn) as Button
        backBtn.findViewById<Button>(R.id.pauseMenuBtn).setOnClickListener {
            backToMenu()
        }

        switch = findViewById(R.id.toiletIncludedSwitch)

        switch.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked)
                "ToiletIncluded:ON"
            else
                "ToiletIncluded:OFF"

            myApp.includeToilet = isChecked

            Toast.makeText(
                this@OptionsActivity, message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun backToMenu() {
        this.finish()
    }
}