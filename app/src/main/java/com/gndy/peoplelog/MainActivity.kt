package com.gndy.peoplelog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gndy.peoplelog.presentation.navigation.AppNavigation
import com.gndy.peoplelog.ui.theme.PeopleLogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeopleLogTheme {
                AppNavigation()
            }
        }
    }
}
