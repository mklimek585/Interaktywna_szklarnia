package pwr.project.interaktywna_szklarnia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceManager

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun registerLoginActivity(view: View) {
        //TODO zwracanie parametrów konta
        this.finish()
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
