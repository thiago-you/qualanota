package you.thiago.qualanota.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.qualanota.R
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.ui.NewItemActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupInterface()
        setupList()
    }

    override fun onResume() {
        super.onResume()
        setupList()
    }

    private fun setupInterface() {
        findViewById<FloatingActionButton>(R.id.fabNewItemAction)?.setOnClickListener {
            startActivity(Intent(this, NewItemActivity::class.java))
        }
    }

    private fun setupList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = Database.get().itemDao().getAll()

            lifecycleScope.launch(Dispatchers.Main) {
                findViewById<RecyclerView>(R.id.recyclerview)?.apply {
                    layoutManager = LinearLayoutManager(this@HomeActivity)
                    adapter = ItemAdapter(list)
                }
            }
        }
    }
}
