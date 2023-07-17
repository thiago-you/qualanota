package you.thiago.qualanota.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "owner") val owner: String?,
    @ColumnInfo(name = "rating") val rating: Int?,
    @ColumnInfo(name = "review") val review: String?,
    @ColumnInfo(name = "type") val type: Int? = 0,
)
