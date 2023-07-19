package you.thiago.qualanota.components

import android.content.Context
import ir.androidexception.andexalertdialog.AndExAlertDialog
import ir.androidexception.andexalertdialog.AndExAlertDialogListener
import you.thiago.qualanota.R

object ConfirmPopulateDataAlert {
    fun show(context: Context, clickListener: AndExAlertDialogListener) {
        AndExAlertDialog.Builder(context)
            .setMessage(context.getString(R.string.confirm_populate_action))
            .setPositiveBtnText(context.getString(R.string.btn_confirm))
            .setNegativeBtnText(context.getString(R.string.btn_cancel))
            .setCancelableOnTouchOutside(true)
            .OnPositiveClicked(clickListener)
            .OnNegativeClicked {}
            .build()
    }
}
