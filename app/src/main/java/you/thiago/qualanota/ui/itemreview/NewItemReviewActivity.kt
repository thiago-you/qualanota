package you.thiago.qualanota.ui.itemreview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.koalaloadinglibrary.KoalaLoadingDialog
import you.thiago.qualanota.R
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemReview
import you.thiago.qualanota.ui.itemowner.NewItemOwnerActivity
import you.thiago.qualanota.validator.ItemReviewValidator

class NewItemReviewActivity : AppCompatActivity() {

    private val title: EditText by lazy { findViewById(R.id.edtTitle) }
    private val owner: Spinner by lazy { findViewById(R.id.spnOwner) }
    private val review: EditText by lazy { findViewById(R.id.edtReview) }

    private val fabSaveAction: FloatingActionButton by lazy { findViewById(R.id.fabSaveAction) }
    private val actionNewOwner: TextView by lazy { findViewById(R.id.actionAddOwner) }

    private val ratingStarDisabled1 by lazy { findViewById<ImageView>(R.id.star_disabled_1) }
    private val ratingStarDisabled2 by lazy { findViewById<ImageView>(R.id.star_disabled_2) }
    private val ratingStarDisabled3 by lazy { findViewById<ImageView>(R.id.star_disabled_3) }
    private val ratingStarDisabled4 by lazy { findViewById<ImageView>(R.id.star_disabled_4) }
    private val ratingStarDisabled5 by lazy { findViewById<ImageView>(R.id.star_disabled_5) }

    private val ratingStarEnabled1 by lazy { findViewById<ImageView>(R.id.star_enabled_1) }
    private val ratingStarEnabled2 by lazy { findViewById<ImageView>(R.id.star_enabled_2) }
    private val ratingStarEnabled3 by lazy { findViewById<ImageView>(R.id.star_enabled_3) }
    private val ratingStarEnabled4 by lazy { findViewById<ImageView>(R.id.star_enabled_4) }
    private val ratingStarEnabled5 by lazy { findViewById<ImageView>(R.id.star_enabled_5) }

    private var loadingDialog: KoalaLoadingDialog? = null

    private val newOwnerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selected = result.data?.getStringExtra("selected")

            if (!selected.isNullOrBlank()) {
                loadSpinner(selected)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_item_review)
        setupToolbar()
        setupInterface()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupInterface() {
        loadSpinner("Selecione")
        setupRatingToggle()

        fabSaveAction.setOnClickListener {
            runCatching {
                val itemReview = ItemReview(
                    title = title.text.toString(),
                    owner = owner.selectedItem.toString(),
                    rating = getRatingValue(),
                    review = review.text.toString(),
                )

                ItemReviewValidator.validate(itemReview)

                loadingDialog = KoalaLoadingDialog().also {
                    it.setText(getString(R.string.saving))
                    it.show(supportFragmentManager, "loading")
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    Database.get().itemReviewDao().insert(itemReview)

                    Thread.sleep(2000)

                    loadingDialog?.dismissAllowingStateLoss()

                    setResult(Activity.RESULT_OK)
                    finish()
                }

                return@runCatching
            }.onFailure {
                loadingDialog?.dismissAllowingStateLoss()
                Toast.makeText(this@NewItemReviewActivity, it.message, Toast.LENGTH_LONG).show()
            }
        }

        actionNewOwner.setOnClickListener {
            newOwnerResult.launch(Intent(this, NewItemOwnerActivity::class.java))
        }
    }

    private fun loadSpinner(selected: String = "") {
        runCatching {
            lifecycleScope.launch(Dispatchers.IO) {
                val list = Database.get().itemOwnerDao().getAll().map { it.name }.toMutableList()

                list.add(0, "Selecione")

                lifecycleScope.launch(Dispatchers.Main) {
                    val ownerAdapter = ArrayAdapter(
                        this@NewItemReviewActivity,
                        android.R.layout.simple_spinner_item,
                        list.toTypedArray(),
                    )

                    ownerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    with(owner) {
                        adapter = ownerAdapter
                        prompt = "Selecione"
                        gravity = Gravity.CENTER

                        if (selected.isNotBlank()) {
                            setSelection(list.indexOf(selected), false)
                        }
                    }
                }
            }

            return@runCatching
        }.onFailure {
            Toast.makeText(this@NewItemReviewActivity, it.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun getRatingValue(): Int {
        return when {
            ratingStarEnabled5.isVisible -> 5
            ratingStarEnabled4.isVisible -> 4
            ratingStarEnabled3.isVisible -> 3
            ratingStarEnabled2.isVisible -> 2
            ratingStarEnabled1.isVisible -> 1
            else -> 5
        }
    }

    private fun setupRatingToggle() {
        ratingStarDisabled1.setOnClickListener(toggle1StartClickListener)
        ratingStarDisabled2.setOnClickListener(toggle2StartClickListener)
        ratingStarDisabled3.setOnClickListener(toggle3StartClickListener)
        ratingStarDisabled4.setOnClickListener(toggle4StartClickListener)
        ratingStarDisabled5.setOnClickListener(toggle5StartClickListener)

        ratingStarEnabled1.setOnClickListener(toggle1StartClickListener)
        ratingStarEnabled2.setOnClickListener(toggle2StartClickListener)
        ratingStarEnabled3.setOnClickListener(toggle3StartClickListener)
        ratingStarEnabled4.setOnClickListener(toggle4StartClickListener)
        ratingStarEnabled5.setOnClickListener(toggle5StartClickListener)
    }

    private val toggle1StartClickListener = View.OnClickListener {
        ratingStarEnabled1.visibility = View.VISIBLE
        ratingStarEnabled2.visibility = View.INVISIBLE
        ratingStarEnabled3.visibility = View.INVISIBLE
        ratingStarEnabled4.visibility = View.INVISIBLE
        ratingStarEnabled5.visibility = View.INVISIBLE
    }

    private val toggle2StartClickListener = View.OnClickListener {
        ratingStarEnabled1.visibility = View.VISIBLE
        ratingStarEnabled2.visibility = View.VISIBLE
        ratingStarEnabled3.visibility = View.INVISIBLE
        ratingStarEnabled4.visibility = View.INVISIBLE
        ratingStarEnabled5.visibility = View.INVISIBLE
    }

    private val toggle3StartClickListener = View.OnClickListener {
        ratingStarEnabled1.visibility = View.VISIBLE
        ratingStarEnabled2.visibility = View.VISIBLE
        ratingStarEnabled3.visibility = View.VISIBLE
        ratingStarEnabled4.visibility = View.INVISIBLE
        ratingStarEnabled5.visibility = View.INVISIBLE
    }

    private val toggle4StartClickListener = View.OnClickListener {
        ratingStarEnabled1.visibility = View.VISIBLE
        ratingStarEnabled2.visibility = View.VISIBLE
        ratingStarEnabled3.visibility = View.VISIBLE
        ratingStarEnabled4.visibility = View.VISIBLE
        ratingStarEnabled5.visibility = View.INVISIBLE
    }

    private val toggle5StartClickListener = View.OnClickListener {
        ratingStarEnabled1.visibility = View.VISIBLE
        ratingStarEnabled2.visibility = View.VISIBLE
        ratingStarEnabled3.visibility = View.VISIBLE
        ratingStarEnabled4.visibility = View.VISIBLE
        ratingStarEnabled5.visibility = View.VISIBLE
    }
}
