package pwr.project.interaktywna_szklarnia.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import pwr.project.interaktywna_szklarnia.MainActivity
import pwr.project.interaktywna_szklarnia.R
import pwr.project.interaktywna_szklarnia.databinding.FragmentSettingsBinding
import kotlin.math.log


class SettingsFragment : Fragment() {

    val TAG = "SettFG"
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonLogout.setOnClickListener { view -> logoutFun(view)}
        binding.buttonLanguage.setOnClickListener { view -> chooseLanguage(view)}

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity ?: throw IllegalStateException("Activity cannot be null"))
        val isDarkTheme = sharedPref.getBoolean("DARK_THEME", false)

        binding.switchTheme.isChecked = isDarkTheme
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            // Zapisz stan przełącznika do preferencji
            sharedPref.edit().putBoolean("DARK_THEME", isChecked).apply()

            // Powiadom aktywność o zmianie motywu
            (activity as? MainActivity)?.changeTheme(isChecked)
        }


    // TODO kolory trybu ciemnego
        // TODO part2 na ciemnym tle wyswietla sie bialy popout - moze to ktorys kolor i tekst jest na bialo xpp
        // TODO mb feedback?
        // TODO opcja recznego wlaczania actywow
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun logoutFun(view: View) {
        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Logout function")
    }

    fun chooseLanguage(view: View) {
        // Tworzenie nowego PopupMenu
        val popup = PopupMenu(context, view)
        // Inflating menu z pliku XML
        popup.menuInflater.inflate(R.menu.settings_menu, popup.menu)

        // Ustawienie OnClickListener dla elementów menu
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.item_lang_pl -> {
                    Toast.makeText(context, "Wybrano Polski", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item_lang_eng -> {
                    Toast.makeText(context, "Wybrano English", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Wyświetlanie PopupMenu
        popup.show()
    }

    // TODO popout menu
    // TODO popout activity o braku internetu
}

// Zdefiniuj motywy w pliku styles.xml:
//Upewnij się, że masz zdefiniowane dwa motywy w res/values/styles.xml - jeden dla jasnego motywu i jeden dla ciemnego.
//Dodaj przełącznik do interfejsu użytkownika:
//Umieść przełącznik (Switch) w layoutcie swojej aktywności, gdzie użytkownicy będą mogli zmieniać motywy.
//Obsłuż zdarzenie zmiany przełącznika:
//W metodzie onCreate() Twojej aktywności ustaw OnCheckedChangeListener na przełączniku, aby reagować na zmiany jego stanu.
//Zapisz stan przełącznika do preferencji:
//Gdy stan przełącznika się zmienia, zapisz nowy stan do preferencji dzielonych (SharedPreferences), a następnie zastosuj odpowiedni motyw.
//Stosuj motyw przy uruchomieniu aplikacji:
//W onCreate() Twojej aktywności lub w innej metodzie inicjalizacji, wczytaj zapisany motyw z preferencji i ustaw go przed wywołaniem setContentView().
//Oto przykładowy kod, który pokazuje, jak to zrobić:
//
//kotlin
//Copy code
//class SettingsActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Stosuj motyw przed setContentView()
//        applyTheme(getSavedTheme())
//
//        setContentView(R.layout.activity_settings)
//
//        // Znajdź przełącznik w layoutcie
//        val themeSwitch: Switch = findViewById(R.id.themeSwitch)
//
//        // Ustaw stan przełącznika na podstawie zapisanych preferencji
//        themeSwitch.isChecked = getSavedTheme() == R.style.DarkTheme
//
//        // Ustaw listener na zmianę stanu przełącznika
//        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                saveTheme(R.style.DarkTheme)
//            } else {
//                saveTheme(R.style.LightTheme)
//            }
//            // Ponowne uruchomienie aktywności, aby zastosować zmianę motywu
//            recreate()
//        }
//    }
//
//    private fun applyTheme(themeId: Int) {
//        setTheme(themeId)
//    }
//
//    private fun saveTheme(themeId: Int) {
//        val prefs = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
//        val editor = prefs.edit()
//        editor.putInt("AppTheme", themeId)
//        editor.apply()
//    }
//
//    private fun getSavedTheme(): Int {
//        val prefs = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
//        return prefs.getInt("AppTheme", R.style.LightTheme) // Domyślnie jasny motyw
//    }
//}