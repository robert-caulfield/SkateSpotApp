package com.example.skatespotapp

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class UserPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}