package com.nazeeran.quillvibe.notes.ui.screens.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nazeeran.quillvibe.notes.data.local.entity.NoteWithChecklist
import com.nazeeran.quillvibe.notes.data.model.AppThemeMode
import com.nazeeran.quillvibe.notes.ui.components.CategoryFilterChips
import com.nazeeran.quillvibe.notes.ui.components.ChecklistCard
import com.nazeeran.quillvibe.notes.ui.components.ConfirmationDialog
import com.nazeeran.quillvibe.notes.ui.components.NoteCard
import com.nazeeran.quillvibe.notes.ui.components.QuillSearchBar
import com.nazeeran.quillvibe.notes.ui.components.QuillSpeedDialFab
import com.nazeeran.quillvibe.notes.ui.theme.QuillMotion

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToNoteEditor: (Long) -> Unit,
    onNavigateToChecklistEditor: (Long) -> Unit,
    onNavigateToTrash: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var noteToTrash by remember { mutableStateOf<NoteWithChecklist?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // Header Bar: App Name & Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo & App Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = androidx.compose.foundation.shape.CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "QuillVibe",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Action Icons: Quick Theme, Layout View, Trash, Settings
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Quick Theme Toggle
                        IconButton(onClick = { viewModel.cycleTheme() }) {
                            Icon(
                                imageVector = when (uiState.themeMode) {
                                    AppThemeMode.LIGHT -> Icons.Outlined.LightMode
                                    AppThemeMode.DARK -> Icons.Outlined.DarkMode
                                    AppThemeMode.SYSTEM -> Icons.Rounded.AutoAwesome
                                },
                                contentDescription = "Toggle Theme",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Layout Toggle (Masonry Grid <-> List)
                        IconButton(onClick = { viewModel.toggleLayoutMode() }) {
                            Icon(
                                imageVector = if (uiState.isGridLayout) Icons.Outlined.ViewAgenda else Icons.Outlined.GridView,
                                contentDescription = if (uiState.isGridLayout) "Switch to List View" else "Switch to Grid View",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Trash Screen Shortcut
                        IconButton(onClick = onNavigateToTrash) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteOutline,
                                contentDescription = "Trash",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Settings Shortcut
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar
                QuillSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Filter Chips
                CategoryFilterChips(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = viewModel::onCategorySelect
                )
            }
        },
        floatingActionButton = {
            QuillSpeedDialFab(
                onNewNote = { onNavigateToNoteEditor(0L) },
                onNewChecklist = { onNavigateToChecklistEditor(0L) },
                hapticsEnabled = uiState.hapticsEnabled
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.items.isEmpty()) {
                // Clean Empty State
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    shape = androidx.compose.foundation.shape.CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Text(
                            text = if (uiState.searchQuery.isNotBlank()) "No matching results" else "No notes or checklists yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = if (uiState.searchQuery.isNotBlank()) {
                                "Try searching for a different keyword or clearing filters."
                            } else {
                                "Tap the '+' button below to jot down your thoughts or start a to-do list."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Animated view switch between Grid and List
                AnimatedContent(
                    targetState = uiState.isGridLayout,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                    },
                    label = "layout_transition"
                ) { isGrid ->
                    if (isGrid) {
                        // 2-Column Staggered Masonry Grid
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalItemSpacing = 10.dp,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = uiState.items,
                                key = { it.note.id }
                            ) { item ->
                                if (item.note.isChecklist) {
                                    ChecklistCard(
                                        noteWithChecklist = item,
                                        onClick = { onNavigateToChecklistEditor(item.note.id) },
                                        onPinToggle = { viewModel.togglePin(item) },
                                        onItemToggle = { viewModel.toggleChecklistItem(it) },
                                        onLongClick = { noteToTrash = item },
                                        hapticsEnabled = uiState.hapticsEnabled
                                    )
                                } else {
                                    NoteCard(
                                        note = item.note,
                                        onClick = { onNavigateToNoteEditor(item.note.id) },
                                        onPinToggle = { viewModel.togglePin(item) },
                                        onLongClick = { noteToTrash = item },
                                        hapticsEnabled = uiState.hapticsEnabled
                                    )
                                }
                            }
                        }
                    } else {
                        // 1-Column Clean List View
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = uiState.items,
                                key = { it.note.id }
                            ) { item ->
                                if (item.note.isChecklist) {
                                    ChecklistCard(
                                        noteWithChecklist = item,
                                        onClick = { onNavigateToChecklistEditor(item.note.id) },
                                        onPinToggle = { viewModel.togglePin(item) },
                                        onItemToggle = { viewModel.toggleChecklistItem(it) },
                                        onLongClick = { noteToTrash = item },
                                        hapticsEnabled = uiState.hapticsEnabled
                                    )
                                } else {
                                    NoteCard(
                                        note = item.note,
                                        onClick = { onNavigateToNoteEditor(item.note.id) },
                                        onPinToggle = { viewModel.togglePin(item) },
                                        onLongClick = { noteToTrash = item },
                                        hapticsEnabled = uiState.hapticsEnabled
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Move to Trash Confirmation Dialog on long press
    noteToTrash?.let { item ->
        val title = if (item.note.title.isNotBlank()) item.note.title else if (item.note.isChecklist) "Checklist" else "Note"
        ConfirmationDialog(
            title = "Move to Trash?",
            message = "Move \"$title\" to Trash? You can restore it anytime from the Trash bin.",
            confirmText = "Move to Trash",
            isDestructive = true,
            onConfirm = {
                viewModel.moveToTrash(item.note.id)
                noteToTrash = null
            },
            onDismiss = {
                noteToTrash = null
            }
        )
    }
}
