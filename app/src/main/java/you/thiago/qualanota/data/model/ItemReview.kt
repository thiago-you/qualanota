package you.thiago.qualanota.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemReview(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") var title: String? = "",
    @ColumnInfo(name = "owner") var owner: String? = "",
    @ColumnInfo(name = "rating") var rating: Int? = 0,
    @ColumnInfo(name = "review") var review: String? = "",
    @ColumnInfo(name = "type") var type: Int? = 0,
)
