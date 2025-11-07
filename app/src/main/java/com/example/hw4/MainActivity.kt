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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.hw4.ui.theme.Hw4Theme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            var test by remember { mutableStateOf(false)}
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
                            name = "bitches",
                        )
                        ReminderText()

                        CheckBoxWithText(test, "test") { test = !test}
                    }
                }
            }
        }
    }


@Composable
fun CheckBoxWithText(state : Boolean, msg: String, onChange: (Boolean) -> Unit){
    //var isChecked by remember {mutableStateOf(state)}
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Left,
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = {
                onChange(state)
            },
        )
        Text (
           msg
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

/* как правильно уничтожить - в простом варианте и во втором
TODO
не всегда появляются уведомления
констрейнт на полный заряд

как обновить сообщенеи в periodic
 */

@Composable
fun ReminderText(){
    val tag = "shit"
    var textNotificationMsg by remember { mutableStateOf("default text") }
    var mExpanded by remember { mutableStateOf(false) }
    var timerTime by remember { mutableIntStateOf(6) }
    val timeValues = listOf<Int>(1, 2, 3, 4, 5, 10, 15, 20, 25, 30)
    //val timeValues = listOf<String>("1", "2", "3", "4", "5", "10")
    var chargingOnly by remember { mutableStateOf(false)}
    var fullChargeOnly by remember { mutableStateOf(false)}
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val inputData = workDataOf(
        "notification_text" to textNotificationMsg
    )
    //val workReq = OneTimeWorkRequestBuilder<MyWorker>()
    val constraints = Constraints.Builder()
        .setRequiresCharging(chargingOnly) // Только при зарядке
        //.setRequiresBatteryNotLow(true) // Батарея не на низком уровне
        .build()
    val workReq = PeriodicWorkRequestBuilder<MyWorker>(
        timerTime.toLong(),
        TimeUnit.MINUTES,
        timerTime.toLong() - 1,
        TimeUnit.MINUTES
    )//TimeUnit.SECONDS)
        .setInitialDelay(
            timerTime.toLong() * 5,
            TimeUnit.SECONDS
        )
        .setInputData(inputData)
        .setConstraints(constraints)
        .addTag(tag)
        .build()

    TextField(
        value = textNotificationMsg,
        onValueChange = { textNotificationMsg = it },
    )
    Column (
        horizontalAlignment = Alignment.Start,
    ) {
        CheckBoxWithText(chargingOnly, "Все на зарядочку") { chargingOnly = !chargingOnly}
        CheckBoxWithText(fullChargeOnly, "Только на полном заряде") { fullChargeOnly = !fullChargeOnly}
    }
    OutlinedTextField(
        readOnly = true,
        value = timeValues[timerTime].toString(),
        //value = timerTime.toString(),
        //placeholder = Text("123"),
        onValueChange = {
            //Log.d("TextField", "text: $textNotificationMsg, it: $it" )
        },
        //fontSize = 24.sp,
        //modifier = Modifier.padding(innerPadding)
        label = {Text("Период повтора, минут")},
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
                    timerTime = index
                    mExpanded = false
                }

            )
        }
    }


   // PLAN
    Button(onClick = {
        Log.d("Button", "pressed, text is $textNotificationMsg, time is $timerTime")
        //workReq = makeWorkReq(timerTime.toLong(), textNotificationMsg)
        WorkManager.getInstance(this).enqueue(workReq)
    }) {
        Text (
            text = "Старт",
            fontSize = 24.sp,
            //modifier = Modifier.padding(innerPadding),
        )
    }
    Button(onClick = {
        Log.d("Button", "pressed, text is $textNotificationMsg, time is $timerTime")

        val workM = WorkManager.getInstance(this)
        //workM.cancelWorkById(workReq.id)
        workM.cancelAllWorkByTag(tag)


    }) {
        Text (
            text = "Стоп",
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