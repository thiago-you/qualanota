package you.thiago.qualanota.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import you.thiago.qualanota.data.model.ItemOwner

@Dao
interface ItemOwnerDao {
    @Query("SELECT * FROM itemOwner")
    fun getAll(): List<ItemOwner>

    @Query("SELECT * FROM itemOwner WHERE id IN (:itemOwnerIds)")
    fun loadAllByIds(itemOwnerIds: IntArray): List<ItemOwner>

    @Query("SELECT * FROM itemOwner WHERE name LIKE :name AND location LIKE :location LIMIT 1")
    fun findByName(name: String, location: String): ItemOwner

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(itemOwner: ItemOwner): Long

    @Update
    suspend fun update(itemOwner: ItemOwner)

    @Insert
    fun insertAll(vararg itemOwners: ItemOwner)

    @Delete
    fun delete(itemOwner: ItemOwner)
}
