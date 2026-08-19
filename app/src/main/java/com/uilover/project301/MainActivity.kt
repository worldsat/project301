package com.uilover.project301

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uilover.project301.ui.AppNavHost
import com.uilover.project301.ui.theme.Project301Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project301Theme {
                AppNavHost()
            }
        }
    }
}