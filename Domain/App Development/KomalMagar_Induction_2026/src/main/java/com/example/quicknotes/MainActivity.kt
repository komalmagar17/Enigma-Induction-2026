package com.example.quicknotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quicknotes.ui.screens.HomeScreen
import com.example.quicknotes.ui.screens.NoteEditorScreen
import com.example.quicknotes.ui.screens.SplashScreen
import com.example.quicknotes.ui.theme.QuickNotesTheme
import com.example.quicknotes.viewmodel.NoteViewModel
import com.example.quicknotes.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickNotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val noteApp = application as NoteApp
                    val viewModel: NoteViewModel = viewModel(
                        factory = NoteViewModelFactory(noteApp.repository)
                    )

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen(
                                onAnimationFinished = {
                                    navController.navigate("home") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onNoteClick = { note ->
                                    if (note == null) {
                                        navController.navigate("editor/null")
                                    } else {
                                        navController.navigate("editor/${note.id}")
                                    }
                                }
                            )
                        }
                        composable(
                            route = "editor/{noteId}",
                            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val noteId = backStackEntry.arguments?.getString("noteId")
                            val note = if (noteId == "null") null else {
                                var currentNote by remember { mutableStateOf<com.example.quicknotes.domain.model.Note?>(null) }
                                LaunchedEffect(noteId) {
                                    currentNote = viewModel.getNoteById(noteId!!)
                                }
                                currentNote
                            }
                            
                            if (noteId == "null" || note != null) {
                                NoteEditorScreen(
                                    note = note,
                                    viewModel = viewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
