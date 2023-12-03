package pwr.project.interaktywna_szklarnia.ui.settings

import android.content.Context
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import pwr.project.interaktywna_szklarnia.LoginActivity
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

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonLogout.setOnClickListener { view -> logoutFun(view)}
        binding.buttonLanguage.setOnClickListener { view -> chooseLanguage(view)}
        binding.buttonDeleteAccount.setOnClickListener { view -> deleteAccount(view)}
        binding.buttonTimeRange.setOnClickListener { view -> chooseRangeTime(view) }
        initializeTimeRangeDisplay()

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
        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Logout function")
        FirebaseAuth.getInstance().signOut()

        // Przekierowanie do LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        activity?.finish()
    }

    fun deleteAccount(view: View) { // TODO usuwanie konta
        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User account deleted.")

                // Przekierowanie do LoginActivity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                activity?.finish()
            } else {
                // Nie udało się usunąć konta
                Log.w(TAG, "Failed to delete user account", task.exception)
            }
        }
    }

    // Definicje kluczy i wartości domyślnych
    companion object {
        const val KEY_TIME_RANGE = "TIME_RANGE"
        const val DEFAULT_TIME_RANGE = "DAY"
    }

    // W funkcji inicjalizującej fragment/aktywność
    fun initializeTimeRangeDisplay() {
        val tvTimeRangeDesc = binding.tvTimeRangeDesc

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val currentRange = sharedPref.getString(KEY_TIME_RANGE, DEFAULT_TIME_RANGE)

        tvTimeRangeDesc.text = when (currentRange) {
            "DAY" -> "Dzień"
            "WEEK" -> "Tydzień"
            else -> "Dzień"
        }
    }

    fun onTimeRangeSelected(selectedRange: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPref.edit().putString(KEY_TIME_RANGE, selectedRange).apply()

        val tvTimeRangeDesc = binding.tvTimeRangeDesc
        tvTimeRangeDesc.text = when (selectedRange) {
            "DAY" -> "Dzień"
            "WEEK" -> "Tydzień"
            else -> "Dzień"
        }
    }


    fun chooseRangeTime(view: View) {
        val sharedPref = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val currentRange = sharedPref?.getString(KEY_TIME_RANGE, DEFAULT_TIME_RANGE)

        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.settings_stats_menu, popup.menu)

        // Zaznacz aktualny zakres czasowy w menu
        val currentMenuItemId = when (currentRange) {
            "DAY" -> R.id.item_day
            "WEEK" -> R.id.item_week
            else -> R.id.item_day
        }
        popup.menu.findItem(currentMenuItemId).isChecked = true

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.item_day -> {
                    sharedPref?.edit()?.putString(KEY_TIME_RANGE, "DAY")?.apply()
                    Toast.makeText(context, "Wybrano dzień", Toast.LENGTH_SHORT).show()
                    onTimeRangeSelected("DAY")
                    true
                }
                R.id.item_week -> {
                    sharedPref?.edit()?.putString(KEY_TIME_RANGE, "WEEK")?.apply()
                    Toast.makeText(context, "Wybrano tydzień", Toast.LENGTH_SHORT).show()
                    onTimeRangeSelected("WEEK")
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    fun chooseLanguage(view: View) {
        // Tworzenie nowego PopupMenu
        val popup = PopupMenu(context, view)
        // Inflating menu z pliku XML
        popup.menuInflater.inflate(R.menu.settings_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.item_lang_pl -> {
                    Toast.makeText(context, "Zatwierdzono", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item_lang_eng -> {
                    Toast.makeText(context, "Zmiana języka nie jest zaimplementowana", Toast.LENGTH_SHORT).show()
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