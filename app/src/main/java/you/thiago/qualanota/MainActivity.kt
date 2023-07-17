package you.thiago.qualanota

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupInterface()
    }

    private fun setupInterface() {
        findViewById<FloatingActionButton>(R.id.fabNewItemAction)?.setOnClickListener {
            startActivity(Intent(this, NewItemActivity::class.java))
        }
    }
}
