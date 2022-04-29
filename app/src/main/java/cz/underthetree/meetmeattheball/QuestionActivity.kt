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

class QuestionActivity : AppCompatActivity() {

    //TODO PŘEDĚLAT OBRÁZEK NEXT TLAČÍTKA
    private lateinit var myApp: MyApp

    private lateinit var textView: TextView
    private lateinit var nextBtn: Button

    private lateinit var imageView: ImageView
    private lateinit var chosenCharacter: Character

    private var characterIndex = (0..2).random(); //výběr z listu charakterů
    private var questionsAskedCount = 0 //po X otázkách konec tázací fáze
    private var characterQuestionsCount = 2
    private var questionIndex = 0  //výběr otázky z listu otázek
    private var askedIndexes = mutableListOf<Int>()

    private val fileReader: FileReader = FileReader(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        myApp = applicationContext as MyApp

        characterQuestionsCount = myApp.characterQuestionsCount

        if (intent.extras != null) {
            characterIndex = intent.extras!!.getInt("characterIndex")
        }

        if(characterIndex != 3 || myApp.includeToilet)
            myApp.characterPhaseCounter++  //přičtení počítadla zobrazených obrazovek otázky

        Log.i("globalClass",myApp.characterPhaseCounter.toString())

        //TODO nevytvářet vždy novou scénu jen změnit index charakteru na pozadí
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_activity0)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        nextBtn = findViewById(R.id.nextBtn)
        initCharacters()

        imageView.setBackgroundColor(Color.CYAN)

        imageView.background = chosenCharacter.characterImage
        //PŘIDÁNÍ LISTENERŮ
        val btn = findViewById(R.id.nextBtn) as Button
        btn.findViewById<Button>(R.id.nextBtn).setOnClickListener {
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

        //už byly položeny tři otázky?
        if (questionsAskedCount > characterQuestionsCount) {
            //pokud už jsme u maximálního počtu tázací fáze tak vypnout hru
            if(myApp.characterPhaseCounter >= myApp.characterPhaseCount)
            {
                //TODO přidat obrazovku ukončení
                this.finish()
                return
            }
            startActivity(Intent(this, WalkingActivity::class.java))
            this.finish()   //jako mohli bychom to nechat otevřené, ale přepínat pak mezi už otevřenými aktivitami zatím neumím
            return
        }
        val questions = chosenCharacter.questions    //výběr charakteru, z kterého vybíráme otázky
        imageView.background = chosenCharacter.characterImage

        //vybrání nového indexu
        var newIndex = 0;

        //TODO udělat seznam už použitých indexů, aby se neobjevila stejná otázka v jednom povídání (možná bude stačit mít hodně otázek a pak bude ta psta menší)
        //loop dokud se nenajde jiný index než co tam byl minule
        do {
            newIndex = (0..questions.size - 1).random()
        } while (alreadyUsedQuestion(newIndex))
        questionIndex = newIndex
        askedIndexes.add(questionIndex) // přidat index mezi použité indexy

        textView.text = questions[questionIndex]    //přiřazení strigu do textového pole
        Log.i("nextBtnAfter", questionIndex.toString())
        Log.i("nextBtnAfter", textView.text.toString())
        questionsAskedCount++
        updateNextButtonText()
    }

    private fun alreadyUsedQuestion(newIndex: Int): Boolean
    {
        var alreadyUsed = false
        for(index in askedIndexes)
        {
            alreadyUsed = index == newIndex

            if(alreadyUsed)
                return true //pokud je jeden true ukončíme metodu
        }
        return false    //nenašel se index se stejnou hodnotou mezi použitými
    }

    private fun backToMenu() {
//        val switchActivityIntent = Intent(this, MainMenuActivity::class.java)
//        startActivity(switchActivityIntent)
        this.finish()
    }

    private fun updateNextButtonText() {
        if (questionsAskedCount > characterQuestionsCount) {
            nextBtn.text = "See you soon"
            return
        }
        nextBtn.text = ("Next (${3-questionsAskedCount})")
    }

    private fun initCharacters() {
        when(characterIndex){
            0 -> chosenCharacter = (Character(
                fileReader.readFromAsset("psycholog.txt"),
                "Jan",
                "Typical psychologist, nothing special nor extra. Still quite chill to chill with him.",
                ResourcesCompat.getDrawable(resources, R.drawable.c_psycholog, null)
            ))
            1 -> chosenCharacter =( Character(
                fileReader.readFromAsset("kuchar.txt"),
                "Giovani",
                "Would eat anything and post review of it on his instagram story.",
                ResourcesCompat.getDrawable(resources, R.drawable.c_kuchar, null)
            ))
            2 -> chosenCharacter =(Character(
                fileReader.readFromAsset("cestovatel.txt"),
                "Terrence",
                "His instagram looks like one man travel agency, he's on his way to another country you didn't even know exists.",
                ResourcesCompat.getDrawable(resources, R.drawable.c_cestovatel, null)
            ))
            3 -> chosenCharacter =(Character(
                fileReader.readFromAsset("zachod.txt"),
                "Tony Seen It All",
                "The one guy you don't think about much but meeting him always makes you feel at home.",
                ResourcesCompat.getDrawable(resources, R.drawable.c_zachod, null)
            ))
        }
    }
}