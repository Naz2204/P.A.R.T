package ua.ipze.kpi.part.database.layer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LayerDao {

    @Upsert
    suspend fun upsertLayer(layer: Layer): Long

    @Delete
    suspend fun deleteLayer(layer: Layer)

    @Query("SELECT * FROM layer WHERE id IN(:layerIds)")
    fun getLayers(layerIds: List<Long>): Flow<List<Layer>>

}