package ua.ipze.kpi.part.utils.filesystem

import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import ua.ipze.kpi.part.providers.static.GlobalApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID


fun writePngToGallery(name: String, pngAsByteArray: ByteArray) {
    Log.d("aaa", "mew2")
    val safeName = name + "__" + UUID.randomUUID()
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        writeToGalleryOld(safeName, pngAsByteArray)
    } else {
        writeToGalleryNew(safeName, pngAsByteArray)
    }
}

/** @param safeName - name with uuid in format name_UUID4 */
@Suppress("DEPRECATION")
private fun writeToGalleryOld(safeName: String, pngAsByteArray: ByteArray) {
    val picturesDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    if (!picturesDir.exists()) picturesDir.mkdirs()

    val file = File(picturesDir, "$safeName.png")
    FileOutputStream(file).use { it.write(pngAsByteArray) }

    // Notify gallery
    val uri = android.net.Uri.fromFile(file)
    val intent =
        android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
    android.content.ContextWrapper(null).sendBroadcast(intent)

}

/** @param safeName - name with uuid in format name_UUID4 */
private fun writeToGalleryNew(safeName: String, pngAsByteArray: ByteArray) {
    val mimeType = "image/png"

    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$safeName.png")
        put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val resolver = GlobalApplicationContext.context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        resolver.openOutputStream(it).use { outStream: OutputStream? ->
            outStream?.write(pngAsByteArray)
        }

        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }
}


