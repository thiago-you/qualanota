package you.thiago.qualanota.components

import android.content.Context
import ir.androidexception.andexalertdialog.AndExAlertDialog
import ir.androidexception.andexalertdialog.AndExAlertDialogListener
import you.thiago.qualanota.R

object DeleteAlert {
    fun show(context: Context, clickListener: AndExAlertDialogListener) {
        AndExAlertDialog.Builder(context)
            .setMessage(context.getString(R.string.confirm_delete_action))
            .setPositiveBtnText(context.getString(R.string.btn_delete))
            .setNegativeBtnText(context.getString(R.string.btn_cancel))
            .setCancelableOnTouchOutside(true)
            .OnPositiveClicked(clickListener)
            .OnNegativeClicked {}
            .build()
    }
}
