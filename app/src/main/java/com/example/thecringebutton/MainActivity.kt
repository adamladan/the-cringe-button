package com.example.thecringebutton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class Joke(val setup: String, val punchline: String)

suspend fun fetchRandomJoke(): Joke = withContext(Dispatchers.IO) {
    val response = URL("https://official-joke-api.appspot.com/random_joke").readText()
    val json = JSONObject(response)
    Joke(
        setup = json.getString("setup"),
        punchline = json.getString("punchline")
    )
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvSetup = findViewById<TextView>(R.id.tvSetup)
        val tvPunchline = findViewById<TextView>(R.id.tvPunchline)
        val btnJoke = findViewById<Button>(R.id.btnJoke)

        btnJoke.setOnClickListener {
            btnJoke.isEnabled = false
            tvSetup.text = "Hämtar ett mästerverk..."
            tvPunchline.text = ""

            lifecycleScope.launch {
                try {
                    val joke = fetchRandomJoke()
                    tvSetup.text = joke.setup
                    tvPunchline.text = joke.punchline
                } catch (e: Exception) {
                    tvSetup.text = "Något gick fel :("
                    tvPunchline.text = "Har du slagit på internet?"
                } finally {
                    btnJoke.isEnabled = true
                }
            }
        }
    }
}