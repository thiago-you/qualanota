package you.thiago.qualanota.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import you.thiago.qualanota.data.dao.ItemDao
import you.thiago.qualanota.data.model.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "qualanota_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME,
                )

                return instance.fallbackToDestructiveMigration().build()
            }
        }
    }

    abstract fun itemDao(): ItemDao
}
