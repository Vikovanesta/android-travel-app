package com.example.uts.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmationDialog(
    private val title: String,
    private val message: String,
    private val action: () -> Unit,
    ): DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = androidx.appcompat.app.AlertDialog.Builder(it)
            builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("Yes") { dialog, id ->
                    action()
                }
                .setNegativeButton("No") { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}