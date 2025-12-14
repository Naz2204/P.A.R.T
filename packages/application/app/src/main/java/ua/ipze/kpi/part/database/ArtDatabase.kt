package ua.ipze.kpi.part.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.database.layer.LayerDao
import ua.ipze.kpi.part.database.project.Project
import ua.ipze.kpi.part.database.project.ProjectDao


@Database(
    entities = [Project::class, Layer::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArtDatabase: RoomDatabase() {

    abstract val layerDao: LayerDao

    abstract val projectDao: ProjectDao
}