package ua.ipze.kpi.part.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            DELETE FROM project WHERE layers LIKE "%-1%"
        """.trimIndent()
        )
    }
}