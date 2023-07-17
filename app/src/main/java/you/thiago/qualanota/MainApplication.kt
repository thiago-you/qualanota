package you.thiago.qualanota

import android.app.Application
import you.thiago.qualanota.data.AppDatabase
import you.thiago.qualanota.data.Database

class MainApplication : Application() {

    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        Database.set(database)
    }
}
