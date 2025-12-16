package ua.ipze.kpi.part.database.layer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Layer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val visibility: Boolean,
    val lock: Boolean,
    val imageData: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Layer

        if (id != other.id) return false
        if (visibility != other.visibility) return false
        if (lock != other.lock) return false
        if (name != other.name) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.toInt()
        result = 31 * result + visibility.hashCode()
        result = 31 * result + lock.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}
