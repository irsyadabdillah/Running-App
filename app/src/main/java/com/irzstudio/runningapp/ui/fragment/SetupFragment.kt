package com.irzstudio.runningapp.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import com.google.android.material.snackbar.Snackbar
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.util.Constant
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var firstTimeAppOpen: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!firstTimeAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment2, true)
                .build()
            startActivity(Intent(activity, RunFragment::class.java))
        }

        tv_continue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success){
                startActivity(Intent(activity, RunFragment::class.java))
            }else{
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = et_name.text.toString()
        val weight = et_weight.text.toString()

        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(Constant.KEY_NAME, name)
            .putFloat(Constant.KEY_WEIGHT, weight.toFloat())
            .putBoolean(Constant.KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        return true
    }

}