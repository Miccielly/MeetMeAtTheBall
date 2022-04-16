package cz.underthetree.meetmeattheball

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import cz.underthetree.meetmeattheball.game.Character
import cz.underthetree.meetmeattheball.utils.FileReader
import java.io.IOException
import java.io.InputStream

class QuestionActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    private lateinit var imageView: ImageView
    private lateinit var characters: MutableList<Character>
    private var characterIndex = (0..2).random(); //výběr z listu charakterů
    private var askedCount = 0; //po třech otázkách konec tázací fáze
    private var questionIndex = 0;  //výběr otázky z listu otázek

    private val fileReader: FileReader = FileReader()
    override fun onCreate(savedInstanceState: Bundle?) {

        readFromAsset()

        if (intent.extras != null) {
            characterIndex = intent.extras!!.getInt("characterIndex")
        }

        //TODO nevytvářet vždy novou scénu jen změnit index charakteru na pozadí
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_activity0)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        initCharacters()

        imageView.setBackgroundColor(Color.CYAN)

        imageView.background = characters[characterIndex].characterImage
        //PŘIDÁNÍ LISTENERŮ
        val btn = findViewById(R.id.nextBtn) as Button
        btn.findViewById<Button>(R.id.nextBtn).setOnClickListener {
            //countMe();
            newQuestion()
        }

        val pauseMenuBtn = findViewById(R.id.pauseMenuBtn) as Button
        pauseMenuBtn.findViewById<Button>(R.id.pauseMenuBtn).setOnClickListener {
            backToMenu()
        }

        newQuestion()
    }

    private fun newQuestion() {
        Log.i("nextBtnBefore", questionIndex.toString())
        Log.i("nextBtnBefore", textView.text.toString())

        if (askedCount > 2) {
            askedCount = 0
            var newCharacterIndex = 0
            do {
                newCharacterIndex = (0..2).random()
            } while (newCharacterIndex == characterIndex)

            characterIndex = newCharacterIndex

            print(characterIndex)

            //zavolat WalkingActivity

            startActivity(Intent(this, WalkingActivity::class.java))
            this.finish()   //jako mohli bychom to nechat otevřené, ale přepínat pak mezi už otevřenými aktivitami zatím neumím
            return
        }
        val questions =
            characters[characterIndex].questions    //výběr charakteru, z kterého vybíráme otázky
        imageView.background = characters[characterIndex].characterImage

        //vybrání nového indexu
        var newIndex = 0;

        //TODO udělat seznam už použitých indexů, aby se neobjevila stejná otázka v jednom povídání (možná bude stačit mít hodně otázek a pak bude ta psta menší)
        //loop dokud se nenajde jiný index než co tam byl minule
        do {
            newIndex = (0..questions.size - 1).random()
        } while (newIndex == questionIndex)
        questionIndex = newIndex;

        textView.text = questions[questionIndex]    //přiřazení strigu do textového pole
        Log.i("nextBtnAfter", questionIndex.toString())
        Log.i("nextBtnAfter", textView.text.toString())
        askedCount++
    }

    private fun backToMenu() {
//        textView.text = "BackToMenu"
        val switchActivityIntent = Intent(this, MainMenuActivity::class.java)
        startActivity(switchActivityIntent)
        this.finish()
    }


    private fun initCharacters() {
        val questionsGeneral = listOf<String>(
            "How are you today?",
            "What are your plans for this day?",
            "What major are you studying here?"
        )

        val questionsFood = listOf<String>(
            "What is best food you could recommend me?",
            "What did you have for breakfast?",
            "What food would you both eat and enjoy it?"
        )

        val questionsTravel = listOf<String>(
            "What unknown destiantions do you guys know?",
            "Have you ever been in other continents than Europe?",
            "What languages do you know?"
        )

        characters = mutableListOf(
            Character(
                questionsGeneral,
                "Jan",
                "Typical dude you see everywhere, nothing special nor extra. Still quite chill to chill with him.",
                ResourcesCompat.getDrawable(resources, R.drawable.c_psycholog, null)
            ),

            Character(
                questionsFood,
                "Giovani",
                "Would eat anything and post review of it on his instagram story.",
                ResourcesCompat.getDrawable(resources, R.drawable.c_kuchar, null)
            ),

            Character(
                questionsTravel,
                "Paul",
                "His instagram looks like one man travel agency, he's on his way to another country you didn't even know exists.",
                ResourcesCompat.getDrawable(resources, R.drawable.c_cestovatel, null)
            )
        )
    }

    private fun readFromAsset() {
        var string: String? = ""
        try {
            val inputStream: InputStream = assets.open("example.txt")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            string = String(buffer)
        } catch (e: IOException) {
            Log.i("file", string.toString())
            e.printStackTrace()
        }

        Log.i("file", string.toString())
    }
}