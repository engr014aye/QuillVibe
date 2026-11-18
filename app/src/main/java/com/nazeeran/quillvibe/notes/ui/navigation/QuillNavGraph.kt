package com.nazeeran.quillvibe.notes.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nazeeran.quillvibe.notes.QuillVibeApp
import com.nazeeran.quillvibe.notes.ui.screens.checklist.ChecklistEditorScreen
import com.nazeeran.quillvibe.notes.ui.screens.checklist.ChecklistEditorViewModel
import com.nazeeran.quillvibe.notes.ui.screens.dashboard.DashboardScreen
import com.nazeeran.quillvibe.notes.ui.screens.dashboard.DashboardViewModel
import com.nazeeran.quillvibe.notes.ui.screens.editor.NoteEditorScreen
import com.nazeeran.quillvibe.notes.ui.screens.editor.NoteEditorViewModel
import com.nazeeran.quillvibe.notes.ui.screens.settings.SettingsScreen
import com.nazeeran.quillvibe.notes.ui.screens.settings.SettingsViewModel
import com.nazeeran.quillvibe.notes.ui.screens.trash.TrashScreen
import com.nazeeran.quillvibe.notes.ui.screens.trash.TrashViewModel

@Composable
fun QuillNavGraph(
    app: QuillVibeApp,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(220)) },
        exitTransition = { fadeOut(animationSpec = tween(220)) },
        popEnterTransition = { fadeIn(animationSpec = tween(220)) },
        popExitTransition = { fadeOut(animationSpec = tween(220)) }
    ) {
        // Dashboard
        composable(route = Screen.Dashboard.route) {
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModel.Factory(
                    repository = app.repository,
                    preferencesManager = app.preferencesManager
                )
            )
            DashboardScreen(
                viewModel = dashboardViewModel,
                onNavigateToNoteEditor = { noteId ->
                    navController.navigate(Screen.NoteEditor.createRoute(noteId))
                },
                onNavigateToChecklistEditor = { noteId ->
                    navController.navigate(Screen.ChecklistEditor.createRoute(noteId))
                },
                onNavigateToTrash = {
                    navController.navigate(Screen.Trash.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // Note Editor
        composable(
            route = Screen.NoteEditor.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
            val noteEditorViewModel: NoteEditorViewModel = viewModel(
                factory = NoteEditorViewModel.Factory(
                    noteId = noteId,
                    repository = app.repository
                )
            )
            NoteEditorScreen(
                viewModel = noteEditorViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Checklist Editor
        composable(
            route = Screen.ChecklistEditor.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
            val checklistViewModel: ChecklistEditorViewModel = viewModel(
                factory = ChecklistEditorViewModel.Factory(
                    noteId = noteId,
                    repository = app.repository
                )
            )
            ChecklistEditorScreen(
                viewModel = checklistViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Trash Screen
        composable(route = Screen.Trash.route) {
            val trashViewModel: TrashViewModel = viewModel(
                factory = TrashViewModel.Factory(repository = app.repository)
            )
            TrashScreen(
                viewModel = trashViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Settings Screen
        composable(route = Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.Factory(
                    preferencesManager = app.preferencesManager,
                    backupManager = app.backupManager
                )
            )
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
