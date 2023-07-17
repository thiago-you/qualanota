package you.thiago.qualanota.ui.itemreview

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.qualanota.R
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemReview

class NewItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_review_item)
        setupInterface()
    }

    private fun setupInterface() {
        val title = findViewById<EditText>(R.id.edtTitle)
        val owner = findViewById<EditText>(R.id.edtOwner)
        val rating = findViewById<EditText>(R.id.edtRating)
        val review = findViewById<EditText>(R.id.edtReview)

        findViewById<FloatingActionButton>(R.id.fabSaveAction)?.setOnClickListener {
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
    }
}
