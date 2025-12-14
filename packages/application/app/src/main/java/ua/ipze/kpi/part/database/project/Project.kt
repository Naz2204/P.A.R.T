package ua.ipze.kpi.part.database.project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val layers : List<Int>,
    val width: Int,
    val height: Int,
    val name: String,
    val lastGeolocation: String,
    val lastSettlement: String,
    val palette: List<Long>
)