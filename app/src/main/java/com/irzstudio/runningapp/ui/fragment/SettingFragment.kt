package com.irzstudio.runningapp.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.util.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setting.*
import javax.inject.Inject


@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFieldFromSharedPref()

        btn_change.setOnClickListener {
            val success = applyChangeToSharedPref()
            if (success) {
                Snackbar.make(requireView(), "Saved Changes", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(
                    requireView(),
                    "Please fill out all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadFieldFromSharedPref() {
        val name = sharedPref.getString(Constant.KEY_NAME, "")
        val weight = sharedPref.getFloat(Constant.KEY_WEIGHT, 80f)
        et_name.setText(name)
        et_weight.setText(weight.toString())
    }

    private fun applyChangeToSharedPref(): Boolean {
        val nameText = et_name.text.toString()
        val weightText = et_weight.text.toString()
        if (nameText.isEmpty() || weightText.isEmpty()) {
            return false
        }

        sharedPref.edit()
            .putString(Constant.KEY_NAME, nameText)
            .putFloat(Constant.KEY_WEIGHT, weightText.toFloat())
            .apply()
        return true
    }

}