package you.thiago.qualanota.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemOwner(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") var name: String? = "",
    @ColumnInfo(name = "location") var location: String? = "",
    @ColumnInfo(name = "description") var description: String? = "",
    @ColumnInfo(name = "rating") var rating: String? = "",
)
