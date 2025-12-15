package ua.ipze.kpi.part.database.project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var layers: LayersList,
    val width: Int,
    val height: Int,
    val name: String,
    val lastSettlement: String,
    val palette: PaletteList,
    val baseColor: Long,
    val timer: Long,
    val lastModified: Long
)

data class LayersList(
    val layersList: List<Long>,
)

data class PaletteList(
    val paletteList: List<Long>
)