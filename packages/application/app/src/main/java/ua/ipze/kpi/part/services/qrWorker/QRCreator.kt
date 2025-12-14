package ua.ipze.kpi.part.services.qrWorker

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONArray


fun generateQrCode(json: String, size: Int = 256): ImageBitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(json, BarcodeFormat.QR_CODE, size, size)

    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(
                x,
                y,
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            )
        }
    }
    return bmp.asImageBitmap()
}

fun jsonToColors(json: String): List<List<Int>> {
    val jsonArray = JSONArray(json)
    return List(jsonArray.length()) { i ->
        val inner = jsonArray.getJSONArray(i)
        List(inner.length()) { j -> inner.getInt(j) }
    }
}