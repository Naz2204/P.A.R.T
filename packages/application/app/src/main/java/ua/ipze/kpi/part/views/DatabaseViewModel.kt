package ua.ipze.kpi.part.views

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ua.ipze.kpi.part.database.ArtDatabase
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.database.project.LayersList
import ua.ipze.kpi.part.database.project.Project
import java.util.concurrent.atomic.AtomicBoolean

val Tag = DatabaseViewModel::class.simpleName ?: ""

class DatabaseViewModel : ViewModel() {

    private val initialized = AtomicBoolean(false)

    fun initialize(dao: ArtDatabase) {
        if (!initialized.compareAndSet(false, true)) {
            Log.e(Tag, "Got second init")
            return
        }
        this.artDao = dao
    }

    lateinit var artDao: ArtDatabase

    // -------------------
    // get

    suspend fun getAllProjectsCollected(): List<Project> {
        return artDao.projectDao.getAllProjects().first()
    }

    fun getAllProjects(): Flow<List<Project>> {
        return artDao.projectDao.getAllProjects()
    }

    suspend fun getProjectWithLayers(id: Long): DatabaseProjectWithLayers? {
        val project = artDao.projectDao.getProject(id)
        if (project == null) {
            Log.e(Tag, "Failed to get project with layer (id: $id)")
            return null
        }

        val layers = artDao.layerDao.getLayers(project.layers.layersList).first()
        if (layers.size != project.layers.layersList.size) {
            Log.e(Tag, "Failed to get project with layer (id: $id)")
            return null
        }

        return DatabaseProjectWithLayers(project, layers)
    }

    // -------------------
    // insert

    fun saveProject(project: Project, layers: List<Layer>) {
        viewModelScope.launch {
            artDao.withTransaction {
                val layerIds = layers.map { layer -> artDao.layerDao.upsertLayer(layer) }
                artDao.projectDao.upsertProject(
                    project.copy(layers = LayersList(layersList = layerIds))
                )
            }
        }
    }


    // -------------------
    // delete
    suspend fun deleteProject(id: Long) {
        artDao.withTransaction {
            val oldValues = getProjectWithLayers(id)
            if (oldValues == null) {
                Log.d(Tag, "Failed to delete project (id: $id)")
                return@withTransaction
            }
            oldValues.layers.map { layer -> artDao.layerDao.deleteLayer(layer) }
            artDao.projectDao.deleteProject(oldValues.project)
        }
    }


    // -------------------
    // update

    suspend fun renameProject(id: Long, newName: String) {
        val project = artDao.projectDao.getProject(id)
        if (project == null) {
            Log.e(Tag, "Failed to rename project (id: $id, newName: $newName)")
            return
        }
        artDao.projectDao.updateProject(project.copy(name = newName))

    }
}

data class DatabaseProjectWithLayers(
    var project: Project,
    var layers: List<Layer>
)