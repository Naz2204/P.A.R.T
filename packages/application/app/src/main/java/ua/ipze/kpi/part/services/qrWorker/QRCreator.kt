package ua.ipze.kpi.part.services.qrWorker

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONArray
import ua.ipze.kpi.part.services.paletteApi.colorSchemeToJson
import ua.ipze.kpi.part.ui.theme.pixelBorder
import ua.ipze.kpi.part.ui.theme.topBottomBorder


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

@Composable
fun QRDialog(colorScheme: List<List<Int>>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .pixelBorder(backgroundColor = Color(0xff232323), borderWidth = 4.dp)
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Color Scheme QR Code",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Image(
                    bitmap = generateQrCode(colorSchemeToJson(colorScheme)),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(300.dp)
                )
                Box(
                    modifier = Modifier
                        .topBottomBorder(4.dp, Color(0xff53565a), Color(0xff53565A))
                        .clickable { onDismiss() }
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "Close",
                        color = Color(0xffffffff),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}