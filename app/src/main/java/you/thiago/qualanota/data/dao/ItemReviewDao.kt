package you.thiago.qualanota.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import you.thiago.qualanota.data.model.ItemReview

@Dao
interface ItemReviewDao {
    @Query("SELECT * FROM itemReview")
    fun getAll(): List<ItemReview>

    @Query("SELECT * FROM itemReview WHERE id IN (:itemReviewIds)")
    fun loadAllByIds(itemReviewIds: IntArray): List<ItemReview>

    @Query("SELECT * FROM itemReview WHERE title LIKE :title AND owner LIKE :owner LIMIT 1")
    fun findByTitle(title: String, owner: String): ItemReview

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(itemReview: ItemReview)

    @Update
    suspend fun update(itemReview: ItemReview)

    @Insert
    fun insertAll(vararg itemReviews: ItemReview)

    @Delete
    fun delete(itemReview: ItemReview)
}
