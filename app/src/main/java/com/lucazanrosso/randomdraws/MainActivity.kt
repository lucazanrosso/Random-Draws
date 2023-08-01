package com.lucazanrosso.randomdraws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.lucazanrosso.randomdraws.data.AppDatabase
import com.lucazanrosso.randomdraws.ui.SampleApp
import com.lucazanrosso.randomdraws.ui.theme.RandomDrawsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RandomDrawsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val db = Room.databaseBuilder(
//                        applicationContext,
//                        AppDatabase::class.java, "database-name"
//                    ).allowMainThreadQueries()
//                        .fallbackToDestructiveMigration()
//                        .build()
                    SampleApp()
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    RandomDrawsTheme {
//        Greeting("Android")
//    }
//}
