/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package eu.de.bw.hn.ug.custos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import eu.de.bw.hn.ug.custos.R
import eu.de.bw.hn.ug.custos.presentation.theme.CustosTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp("Android")
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    CustosTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                ArrowButton(unicode = "\u2191", direction = "hoch", modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ArrowButton(unicode = "\u2190", direction = "links", modifier = Modifier.weight(1f))
                ArrowButton(unicode = "\u25A0", direction = "stop", modifier = Modifier.weight(1f))
                ArrowButton(unicode = "\u2192", direction = "rechts", modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ArrowButton(unicode = "\u2193", direction = "runter", modifier = Modifier.weight(1f))
            }
        }
    }
}


@Composable
fun ArrowButton(unicode: String, direction: String, modifier: Modifier = Modifier) {
    val pressed = remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (direction == "stop") {
                onButtonReleased("hoch")
                onButtonReleased("runter")
                onButtonReleased("links")
                onButtonReleased("rechts")
            } else {
                callRestEndpoint(direction)
            }
        },
        modifier = modifier.padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { pressed.value = true },
                    onLongPress = { pressed.value = false },
                    onTap = { pressed.value = false; onButtonReleased(direction) }
                )
            },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Text(
            text = unicode,
            color = Color.Black
        )
    }

    LaunchedEffect(pressed.value) {
        if (!pressed.value) {
            onButtonReleased(direction)
        }
    }
}



fun onButtonReleased(direction: String) {
    val baseUrl = "http://192.168.178.23:5000/move?"
    val leftSpeed = "0"
    val rightSpeed = "0"

    val url = "$baseUrl&left_speed=$leftSpeed&right_speed=$rightSpeed"
    println("URL: $url")

    GlobalScope.launch(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000 // Timeout-Wert anpassen, falls erforderlich

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Erfolgreicher REST-Call
                // Implementiere hier ggf. weitere Logik für den erfolgreichen Aufruf
            } else {
                // Fehler beim REST-Call
                // Implementiere hier ggf. weitere Logik für den fehlgeschlagenen Aufruf
            }
        } catch (e: IOException) {
            // Fehler beim Verbinden zum REST-Endpunkt
            e.printStackTrace()
            // Implementiere hier ggf. weitere Logik für den Verbindungsfehler
        }
    }
}

fun callRestEndpoint(direction: String) {
    val baseUrl = "http://192.168.178.23:5000/move?"
    val speedVal = "100" // Hier solltest du deinen eigenen Geschwindigkeitswert angeben

    val leftSpeed: String
    val rightSpeed: String

    when (direction) {
        "hoch" -> {
            leftSpeed = speedVal
            rightSpeed = speedVal
        }
        "runter" -> {
            leftSpeed = "-$speedVal"
            rightSpeed = "-$speedVal"
        }
        "links" -> {
            leftSpeed = "-$speedVal"
            rightSpeed = speedVal
        }
        "rechts" -> {
            leftSpeed = speedVal
            rightSpeed = "-$speedVal"
        }
        else -> return // Ungültige Richtung, Funktion beenden
    }

    val url = "$baseUrl&left_speed=$leftSpeed&right_speed=$rightSpeed"
    println("URL: $url")

    GlobalScope.launch(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000 // Timeout-Wert anpassen, falls erforderlich

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Erfolgreicher REST-Call
                // Implementiere hier ggf. weitere Logik für den erfolgreichen Aufruf
            } else {
                // Fehler beim REST-Call
                // Implementiere hier ggf. weitere Logik für den fehlgeschlagenen Aufruf
            }
        } catch (e: IOException) {
            // Fehler beim Verbinden zum REST-Endpunkt
            e.printStackTrace()
            // Implementiere hier ggf. weitere Logik für den Verbindungsfehler
        }
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}