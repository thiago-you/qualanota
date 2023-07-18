package you.thiago.qualanota.ui.itemowner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.qualanota.R
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemOwner

class NewItemOwnerActivity : AppCompatActivity() {

    private val name: EditText by lazy { findViewById(R.id.edtName) }
    private val location: EditText by lazy { findViewById(R.id.edtLocation) }
    private val description: EditText by lazy { findViewById(R.id.edtDescription) }

    private val fabSaveAction: FloatingActionButton by lazy { findViewById(R.id.fabSaveAction) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_item_owner)

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
        fabSaveAction.setOnClickListener {
            val itemOwner = ItemOwner(
                name = name.text.toString(),
                location = location.text.toString(),
                description = description.text.toString(),
            )

            lifecycleScope.launch(Dispatchers.IO) {
                Database.get().itemOwnerDao().insert(itemOwner)

                val data = Intent().apply {
                    putExtra("selected", itemOwner.name)
                    putExtra("inserted", true)
                }

                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }
}
