package pwr.project.interaktywna_szklarnia.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pwr.project.interaktywna_szklarnia.R
import pwr.project.interaktywna_szklarnia.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth;
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener { view -> register(view)}

        auth = Firebase.auth
    }

    fun register(view: View) {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextPassword2.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (password.length >= 8) {
                if(password == confirmPassword) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Podczas tworzenia konta wystąpił błąd",
                                    Toast.LENGTH_SHORT).show()
                                updateUI(null)
                            }
                        }
                } else { Toast.makeText(this, "Podane hasła się różnią",
                        Toast.LENGTH_SHORT).show() }
            } else { Toast.makeText(this, "Hasło musi składać się z conajmniej 8 znaków",
                Toast.LENGTH_LONG).show() }
        } else { Toast.makeText(this, "Pole email i/lub hasło są puste",
            Toast.LENGTH_SHORT).show() }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Użytkownik jest zarejestrowany i przekierowany do LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
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
