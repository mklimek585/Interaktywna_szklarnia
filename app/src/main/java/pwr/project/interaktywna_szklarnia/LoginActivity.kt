package pwr.project.interaktywna_szklarnia


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import pwr.project.interaktywna_szklarnia.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupRegisterLink(view)
    }

    fun setupRegisterLink(view: View?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvRegister : TextView = binding.tvRegisterLink
        tvRegister.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
    fun loginMainActivity(view: View) {
        //TODO logowanie
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}