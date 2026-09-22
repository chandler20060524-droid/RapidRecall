package com.deming1.rapidrecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.deming1.rapidrecall.ui.theme.RapidRecallTheme

/*
* Description:
* The MainActivity class is the default entry point of the app.
*
* Design Rationale:
* I only call the RapidRecallApp() method inside onCreate() method to support reusability and
* modularity, since I stored the essential UI functions in other files.
*
* Outstanding Issue: None
* */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RapidRecallTheme {
                RapidRecallApp()
            }
        }
    }
}