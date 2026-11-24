package com.wylhem.futurelockscreen

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent { FutureLockscreenApp() }
    }
}

@Composable
fun FutureLockscreenApp() {
    val context = LocalContext.current
    var dimMode by remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(false) }
    var timeoutMillis by remember { mutableLongStateOf(60 * 60 * 1000L) }
    var lastActivity by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var clock by remember { mutableStateOf("--:--") }

    DisposableEffect(Unit) {
        val window = (context as? ComponentActivity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }

    LaunchedEffect(Unit) {
        while (true) {
            clock = LocalTime.now().format(DateTimeFormatter.ofPattern("HH mm"))
            delay(1000)
        }
    }

    LaunchedEffect(active, lastActivity, timeoutMillis) {
        if (active) {
            val remaining = timeoutMillis - (System.currentTimeMillis() - lastActivity)
            if (remaining > 0) delay(remaining)
            if (System.currentTimeMillis() - lastActivity >= timeoutMillis) dimMode = true
        }
    }

    val background = Color(0xFF050B13)
    val cyan = Color(0xFFB9F4FF)
    val textColor = if (dimMode) Color(0xFF45616A) else cyan

    Surface(modifier = Modifier.fillMaxSize(), color = background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(dimMode, active) {
                    detectTapGestures(
                        onTap = {
                            if (dimMode) {
                                dimMode = false
                                lastActivity = System.currentTimeMillis()
                            }
                        },
                        onDoubleTap = {
                            dimMode = false
                            active = true
                            lastActivity = System.currentTimeMillis()
                        }
                    )
                }
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("FUTURE LOCKSCREEN", color = Color(0xFF7896A3), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(38.dp))
            Text(
                clock,
                color = textColor,
                fontSize = if (dimMode) 64.sp else 76.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(
                if (dimMode) "MODE VEILLE — DOUBLE-TOUCHE POUR RÉVEILLER"
                else if (active) "SURVEILLANCE DE L’INACTIVITÉ ACTIVÉE"
                else "PROTOYPE ANDROID — ÉCRAN MAINTENU ALLUMÉ",
                color = Color(0xFF6E8994),
                fontSize = 11.sp
            )
            Spacer(Modifier.height(42.dp))
            if (!dimMode) {
                Text("Délai avant le mode veille", color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { timeoutMillis = 30_000L; lastActivity = System.currentTimeMillis(); active = true }) {
                        Text("30 sec (test)")
                    }
                    TextButton(onClick = { timeoutMillis = 60 * 60 * 1000L; lastActivity = System.currentTimeMillis(); active = true }) {
                        Text("1 heure")
                    }
                    TextButton(onClick = { timeoutMillis = 2 * 60 * 60 * 1000L; lastActivity = System.currentTimeMillis(); active = true }) {
                        Text("2 heures")
                    }
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        active = !active
                        dimMode = false
                        lastActivity = System.currentTimeMillis()
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (active) "Désactiver la veille automatique" else "Activer la veille automatique")
                }
            } else {
                Text("Touchez deux fois l’écran pour revenir à l’affichage normal", color = Color(0xFF8CA7B2), fontSize = 13.sp)
            }
            Spacer(Modifier.height(24.dp))
            Text("Prototype 0.1 • L’écran reste allumé tant que l’application est au premier plan.", color = Color(0xFF526873), fontSize = 10.sp)
        }
    }
}
