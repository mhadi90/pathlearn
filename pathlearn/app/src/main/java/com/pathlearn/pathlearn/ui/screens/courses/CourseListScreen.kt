package com.pathlearn.pathlearn.ui.screens.courses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pathlearn.pathlearn.ui.components.CourseCard
import com.pathlearn.pathlearn.ui.components.EmptyState
import com.pathlearn.pathlearn.ui.components.ErrorState
import com.pathlearn.pathlearn.ui.components.LoadingIndicator
import com.pathlearn.pathlearn.utils.UiState
import com.pathlearn.pathlearn.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    onCourseClick: (Int) -> Unit,
    onMyCoursesClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: CourseViewModel = viewModel()
) {
    var showMenu by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDomaine by remember { mutableStateOf<String?>(null) }
    var selectedNiveau by remember { mutableStateOf<Int?>(null) }

    val coursesState by viewModel.coursesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCourses()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PathLearn - Formations") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Mes inscriptions") },
                            onClick = {
                                showMenu = false
                                onMyCoursesClick()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.School, contentDescription = null)
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Déconnexion") },
                            onClick = {
                                showMenu = false
                                onLogout()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Logout, contentDescription = null)
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchCourses(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Rechercher une formation...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Rechercher")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            viewModel.loadCourses()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Effacer")
                        }
                    }
                },
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedDomaine != null,
                    onClick = {
                        selectedDomaine = if (selectedDomaine == null) "Développement" else null
                        viewModel.filterByDomaine(selectedDomaine)
                    },
                    label = { Text(selectedDomaine ?: "Domaine") },
                    leadingIcon = {
                        Icon(Icons.Default.Category, contentDescription = null)
                    }
                )

                FilterChip(
                    selected = selectedNiveau != null,
                    onClick = {
                        selectedNiveau = if (selectedNiveau == null) 1 else null
                        viewModel.filterByNiveau(selectedNiveau)
                    },
                    label = {
                        Text(
                            when (selectedNiveau) {
                                1 -> "Débutant"
                                2 -> "Intermédiaire"
                                3 -> "Avancé"
                                else -> "Niveau"
                            }
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.TrendingUp, contentDescription = null)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = coursesState) {
                is UiState.Loading -> {
                    LoadingIndicator(message = "Chargement des formations...")
                }

                is UiState.Success -> {
                    val courses = state.data

                    if (courses.isEmpty()) {
                        EmptyState(
                            icon = Icons.Default.SearchOff,
                            title = "Aucune formation trouvée",
                            description = "Essayez de modifier vos filtres de recherche"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(courses) { course ->
                                CourseCard(
                                    course = course,
                                    onClick = { onCourseClick(course.id) }
                                )
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadCourses() }
                    )
                }

                is UiState.Idle -> {}
            }
        }
    }
}