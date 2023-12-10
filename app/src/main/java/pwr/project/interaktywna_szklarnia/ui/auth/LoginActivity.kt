package pwr.project.interaktywna_szklarnia.ui.auth


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pwr.project.interaktywna_szklarnia.MainActivity
import pwr.project.interaktywna_szklarnia.R
import pwr.project.interaktywna_szklarnia.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth;
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setupRegisterLink(view)
        binding.btnLogin.setOnClickListener { view -> login(view) }
    }


    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    } // Jezeli uzytkownik jest zalogowany, zostanie przeniesiony do głównej części programu

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupRegisterLink(view: View?) {
        val tvRegister : TextView = binding.tvRegisterLink
        tvRegister.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(view: View) {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        updateUI(null)
                        Toast.makeText(baseContext, "Błedny login lub/i hasło.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else { Toast.makeText(this, "Pole email lub/i hasło są puste",
            Toast.LENGTH_SHORT).show() }
    }


    private fun applyTheme() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkTheme = sharedPref.getBoolean("DARK_THEME", false)
        val themeId = if (isDarkTheme) {
            R.style.Theme_Dark
        } else {
            R.style.Theme_Light
        }
        setTheme(themeId)
    }


}