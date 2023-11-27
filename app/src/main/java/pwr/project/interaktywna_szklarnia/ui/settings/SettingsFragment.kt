package pwr.project.interaktywna_szklarnia.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pwr.project.interaktywna_szklarnia.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences_settings, rootKey)
    }
}
