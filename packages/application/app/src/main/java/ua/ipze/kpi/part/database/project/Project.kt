package ua.ipze.kpi.part.database.project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val layers : LayersList,
    val width: Int,
    val height: Int,
    val name: String,
    val lastGeolocation: String,
    val lastSettlement: String,
    val palette: PaletteList,
    val timer: Long,
    val lastModified: Long
)

data class LayersList(
    val layersList : List<Int>,
)

data class PaletteList(
    val paletteList: List<Long>
)