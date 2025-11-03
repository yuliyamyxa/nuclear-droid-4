package com.example.hw4

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
import com.example.hw4.ui.theme.Hw4Theme
import kotlin.Int
import kotlin.collections.listOf

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
    var textTimerTime by remember { mutableStateOf("")}
    val timeValues = listOf<Int>(1, 2, 3, 4, 5, 10)
    //val timeValues = listOf<String>("1", "2", "3", "4", "5", "10")
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    TextField(
        value = textNotificationMsg,
        onValueChange = { textNotificationMsg = it },
    )
    OutlinedTextField(

        value = textTimerTime,
        //placeholder = Text("123"),
        onValueChange = {
            textTimerTime = it
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
        timeValues.forEach { label ->
            DropdownMenuItem(
                text = { Text(label.toString()) },
                onClick = {
                    textTimerTime = label.toString()
                    mExpanded = false
                }

            )
        }
    }

    Button(onClick = {
        Log.d("Button", "pressed, text is $textNotificationMsg, time is $textTimerTime")
    }) {
        Text (
            text = "Press me bitch",
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