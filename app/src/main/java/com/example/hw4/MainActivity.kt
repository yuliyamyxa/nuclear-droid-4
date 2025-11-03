package com.example.hw4

import MyWorker
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableIntStateOf
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.hw4.ui.theme.Hw4Theme
import kotlin.Int
import kotlin.collections.listOf
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hw4Theme {

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("biches")}
                        )
                    },

                    //modifier = Modifier.fillMaxSize(),

                ) { innerPadding ->
                    Column (
                        modifier = Modifier.padding(innerPadding).padding(horizontal = 50.dp, vertical = 50.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                        Greeting(
                            name = "Android",
                        )
                        ReminderText()

                    }
                }
            }
        }
    }

    @Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ReminderText(){
    var textNotificationMsg by remember { mutableStateOf("default text") }
    var mExpanded by remember { mutableStateOf(false) }
    var timerTime by remember { mutableIntStateOf(1) }
    val timeValues = listOf<Int>(1, 2, 3, 4, 5, 10)
    //val timeValues = listOf<String>("1", "2", "3", "4", "5", "10")
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val inputData = workDataOf(
        "notification_text" to textNotificationMsg
    )
    val workReq = OneTimeWorkRequestBuilder<MyWorker>()
        .setInitialDelay(
            timerTime.toLong() * 10,
            TimeUnit.SECONDS
        )
        .setInputData(inputData)
        .build()

    TextField(
        value = textNotificationMsg,
        onValueChange = { textNotificationMsg = it },
    )
    OutlinedTextField(
        readOnly = true,
        value = timerTime.toString(),
        //placeholder = Text("123"),
        onValueChange = {
            //Log.d("TextField", "text: $textNotificationMsg, it: $it" )
        },
        //fontSize = 24.sp,
        //modifier = Modifier.padding(innerPadding)
        label = {Text("Label")},
        trailingIcon = {
            Icon(icon,"contentDescription",
                Modifier.clickable { mExpanded = !mExpanded })
        }
    )

    DropdownMenu(
        expanded = mExpanded,
        onDismissRequest = {mExpanded = false}
    ) {
        timeValues.forEachIndexed { index, label ->
            DropdownMenuItem(
                text = { Text(label.toString()) },
                onClick = {
                    timerTime = index + 1
                    mExpanded = false
                }

            )
        }
    }

    Button(onClick = {
        Log.d("Button", "pressed, text is $textNotificationMsg, time is $timerTime")
        //workReq = makeWorkReq(timerTime.toLong(), textNotificationMsg)
        WorkManager.getInstance(this).enqueue(workReq)


    }) {
        Text (
            text = "Запланировать",
            fontSize = 24.sp,
            //modifier = Modifier.padding(innerPadding),
        )

    }

    Button(onClick = {
        Log.d("Button", "pressed, text is $textNotificationMsg, time is $timerTime")

        val workM = WorkManager.getInstance(this)
        workM.cancelWorkById(workReq.id)

    }) {
        Text (
            text = "Отминет",
            fontSize = 24.sp,
            //modifier = Modifier.padding(innerPadding),
        )

    }
}
}

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Hw4Theme {
        Greeting("Android")
    }
}*/