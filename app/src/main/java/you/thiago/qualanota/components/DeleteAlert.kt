package you.thiago.qualanota.components

import android.content.Context
import ir.androidexception.andexalertdialog.AndExAlertDialog
import ir.androidexception.andexalertdialog.AndExAlertDialogListener

object DeleteAlert {
    fun show(context: Context, clickListener: AndExAlertDialogListener) {
        AndExAlertDialog.Builder(context)
            .setMessage("Deseja mesmo deletar este item?")
            .setPositiveBtnText("Deletar")
            .setNegativeBtnText("Cancelar")
            .setCancelableOnTouchOutside(true)
            .OnPositiveClicked(clickListener)
            .OnNegativeClicked {}
            .build()
    }
}
