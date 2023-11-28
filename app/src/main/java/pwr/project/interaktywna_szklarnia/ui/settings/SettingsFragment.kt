package pwr.project.interaktywna_szklarnia.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pwr.project.interaktywna_szklarnia.R
import pwr.project.interaktywna_szklarnia.databinding.FragmentSettingsBinding


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

//        binding.buttonLanguage.setOnClickListener { view -> chooseLanguage(view) }
//        binding.buttonLogout.setOnClickListener { view -> logoutFun(view) }
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