package you.thiago.qualanota.ui.itemreview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.qualanota.R
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemReview
import you.thiago.qualanota.ui.itemowner.NewItemOwnerActivity

class NewItemReviewActivity : AppCompatActivity() {

    private val title: EditText by lazy { findViewById(R.id.edtTitle) }
    private val owner: EditText by lazy { findViewById(R.id.edtOwner) }
    private val rating: EditText by lazy { findViewById(R.id.edtRating) }
    private val review: EditText by lazy { findViewById(R.id.edtReview) }

    private val fabSaveAction: FloatingActionButton by lazy { findViewById(R.id.fabSaveAction) }
    private val actionNewOwner: TextView by lazy { findViewById(R.id.actionAddOwner) }

    private val newOwnerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_review_item)
        setupInterface()
    }

    private fun setupInterface() {
        fabSaveAction.setOnClickListener {
            val itemReview = ItemReview(
                title = title.text.toString(),
                owner = owner.text.toString(),
                rating = rating.text.toString().toInt(),
                review = review.text.toString(),
            )

            lifecycleScope.launch(Dispatchers.IO) {
                Database.get().itemReviewDao().insert(itemReview)

                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        actionNewOwner.setOnClickListener {
            newOwnerResult.launch(Intent(this, NewItemOwnerActivity::class.java))
        }
    }
}