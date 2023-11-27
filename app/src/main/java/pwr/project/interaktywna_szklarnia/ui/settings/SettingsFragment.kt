package pwr.project.interaktywna_szklarnia.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        binding.buttonLogout.setOnClickListener { view ->
            testFun(view)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun testFun(view: View) {
        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Invisible button")
    }

//    fun popUP(view: View) {
//        PopupMenu popup = new PopupMenu(context, view)
//    }
    // TODO popout menu
    // TODO popout activity o braku internetu
}