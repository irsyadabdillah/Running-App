package com.irzstudio.runningapp.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.irzstudio.runningapp.R

class CancelRunDialog : DialogFragment() {

    private var yesListener: (() -> Unit)? = null

    fun setYesListener(listener: (() -> Unit)) {
        yesListener = listener
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        return super.onCreateDialog(savedInstanceState)
            builder.setTitle("Cancel the Run")
            builder.setMessage("Are you sure that you want to cancel the current run and delete it's data?")
            builder.setIcon(R.drawable.ic_delete)
            builder.setPositiveButton("Yes") { _, _ ->
                yesListener?.let { yes ->
                    yes()
                }
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create().show()

    }
}