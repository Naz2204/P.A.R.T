package ua.ipze.kpi.part.database.layer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Layer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val visibility: Boolean,
    val lock: Boolean
)
