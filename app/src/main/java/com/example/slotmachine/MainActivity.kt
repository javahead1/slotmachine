package com.example.slotmachine



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SlotMachineScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotMachineScreen() {
    // Counts for the 3 slots
    var count1 by remember { mutableIntStateOf(0) }
    var count2 by remember { mutableIntStateOf(0) }
    var count3 by remember { mutableIntStateOf(0) }

    // Coroutine Jobs
    var job1: Job? by remember { mutableStateOf(null) }
    var job2: Job? by remember { mutableStateOf(null) }
    var job3: Job? by remember { mutableStateOf(null) }

    // Game state
    var isRunning by remember { mutableStateOf(false) }
    var stoppedCount by remember { mutableIntStateOf(0) }
    var resultMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Who Wants to be a Millionaire Slot Machine!!!") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Win/Loss Message
            Text(
                text = resultMessage,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Three slot images in a centered row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SlotDisplay(count1)
                Spacer(modifier = Modifier.width(8.dp))
                SlotDisplay(count2)
                Spacer(modifier = Modifier.width(8.dp))
                SlotDisplay(count3)
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Control Logic
            if (!isRunning) {
                Button(onClick = {
                    // Reset game
                    resultMessage = ""
                    stoppedCount = 0
                    isRunning = true

                    // Cycle 0-3 with different delays
                    job1 = coroutineScope.launch(Dispatchers.Default) {
                        while (true) {
                            delay(100)
                            count1 = (count1 + 1) % 4
                        }
                    }
                    job2 = coroutineScope.launch(Dispatchers.Default) {
                        while (true) {
                            delay(130)
                            count2 = (count2 + 1) % 4
                        }
                    }
                    job3 = coroutineScope.launch(Dispatchers.Default) {
                        while (true) {
                            delay(160)
                            count3 = (count3 + 1) % 4
                        }
                    }
                }) {
                    Text("SPIN FOR MILLIONS!!!", fontSize = 20.sp)
                }
            } else {
                Button(onClick = {
                    // Stop one coroutine per click
                    when (stoppedCount) {
                        0 -> job1?.cancel()
                        1 -> job2?.cancel()
                        2 -> {
                            job3?.cancel()
                            isRunning = false

                            // Winning Logic
                            resultMessage = if (count1 == count2 && count2 == count3) {
                                "JACKPOT! 💰"
                            } else {
                                "Better luck next time!"
                            }
                        }
                    }
                    stoppedCount++
                }) {
                    // Button always says STOP
                    Text("STOP", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun SlotDisplay(value: Int) {
    // Map counts 0-3 to images
    val imageRes = when (value) {
        0 -> R.drawable.cherry
        1 -> R.drawable.grape
        2 -> R.drawable.pear
        else -> R.drawable.strawberry
    }

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier.size(80.dp)
    )
}