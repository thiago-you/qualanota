package you.thiago.qualanota.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import you.thiago.qualanota.data.dao.ItemOwnerDao
import you.thiago.qualanota.data.dao.ItemReviewDao
import you.thiago.qualanota.data.model.ItemOwner
import you.thiago.qualanota.data.model.ItemReview

@Database(entities = [ItemReview::class, ItemOwner::class], version = 3, exportSchema = false)
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

    abstract fun itemReviewDao(): ItemReviewDao

    abstract fun itemOwnerDao(): ItemOwnerDao
}
