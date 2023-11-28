package pwr.project.interaktywna_szklarnia

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.mikephil.charting.charts.BarChart
import com.google.android.material.bottomnavigation.BottomNavigationView
import pwr.project.interaktywna_szklarnia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_stats, R.id.navigation_settings
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun applyTheme() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val isDarkTheme = sharedPref.getBoolean("DARK_THEME", false)
        val themeId = if (isDarkTheme) {
            R.style.Theme_Dark
        } else {
            R.style.Theme_Light
        }
        setTheme(themeId)
    }

    fun changeTheme(isDark: Boolean) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("DARK_THEME", isDark).apply()

        // Ponownie uruchom aktywność, aby zastosować nowy motyw
        recreate()
    }

}