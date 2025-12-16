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
    val lastModified: Long,
    val previewImageData: ByteArray,
    val drawingTime: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Project

        if (id != other.id) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (baseColor != other.baseColor) return false
        if (timer != other.timer) return false
        if (lastModified != other.lastModified) return false
        if (drawingTime != other.drawingTime) return false
        if (layers != other.layers) return false
        if (name != other.name) return false
        if (lastSettlement != other.lastSettlement) return false
        if (palette != other.palette) return false
        if (!previewImageData.contentEquals(other.previewImageData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + baseColor.hashCode()
        result = 31 * result + timer.hashCode()
        result = 31 * result + lastModified.hashCode()
        result = 31 * result + drawingTime.hashCode()
        result = 31 * result + layers.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + lastSettlement.hashCode()
        result = 31 * result + palette.hashCode()
        result = 31 * result + previewImageData.contentHashCode()
        return result
    }


}

data class LayersList(
    val layersList: List<Long>,
)

data class PaletteList(
    val paletteList: List<Long>
)