package com.example.lesson_roomdb_p1

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.lesson_roomdb_p1.ui.theme.LessonRoomDBP1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LessonRoomDBP1Theme {
                val name = remember { mutableStateOf("") }
                val number = remember { mutableStateOf("") }
                val db = DB(this)
                val activity = LocalContext.current as Activity

                val sharedPreferences = activity.getSharedPreferences("value", Context.MODE_PRIVATE)

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(value = name.value, onValueChange = { name.value = it })
                    TextField(
                        value = number.value,
                        onValueChange = { number.value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(onClick = {
                        if (name.value.isNotEmpty() && number.value.isNotEmpty()) {
                            val result = Result(name = name.value, number = number.value.toInt())
                            db.insert(result)
                            clearFields(name = name, number = number)
                        } else
                            Toast.makeText(
                                this@MainActivity,
                                "Please Fill All Data's",
                                Toast.LENGTH_SHORT
                            ).show()
                    }) { Text(text = "Write") }
                    LazyColumn(content = {
                        items(1) {
                            Log.d("SIZE", db.getAll("RESULT DESC").size.toString())
                            for (d in db.getAll("RESULT DESC")) {
                                Text(text = ("${d.name} ${d.number}\n"))
                            }
                        }
                        item {
                            Text(text = sharedPreferences.getString("value", "value1").toString())
                            Button(onClick = {
                                sharedPreferences.edit().putString("value", "value2").apply()
                                activity.finish()
                            }) {
                                Text(text = "Change value to value2")
                            }
                            Button(onClick = {
                                sharedPreferences.edit().putString("value", "value3").apply()
                                activity.finish()
                            }) {
                                Text(text = "Change value to value3")
                            }
                        }
                    })
                }
            }
        }
    }

    private fun clearFields(name: MutableState<String>, number: MutableState<String>) {
        name.value = ""
        number.value = ""
    }
}