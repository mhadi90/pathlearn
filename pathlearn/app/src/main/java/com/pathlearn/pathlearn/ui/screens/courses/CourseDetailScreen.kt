package com.pathlearn.pathlearn.ui.screens.courses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pathlearn.pathlearn.ui.components.ErrorState
import com.pathlearn.pathlearn.ui.components.LoadingIndicator
import com.pathlearn.pathlearn.ui.components.ModuleCard
import com.pathlearn.pathlearn.ui.components.PrimaryButton
import com.pathlearn.pathlearn.utils.UiState
import com.pathlearn.pathlearn.viewmodel.CourseViewModel
import com.pathlearn.pathlearn.viewmodel.EnrollmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: Int,
    onBackClick: () -> Unit,
    courseViewModel: CourseViewModel = viewModel(),
    enrollmentViewModel: EnrollmentViewModel = viewModel()
) {
    val courseState by courseViewModel.courseDetailState.collectAsState()
    val modulesState by courseViewModel.modulesState.collectAsState()
    val enrollmentState by enrollmentViewModel.enrollmentCreateState.collectAsState()

    var showEnrollDialog by remember { mutableStateOf(false) }

    LaunchedEffect(courseId) {
        courseViewModel.loadCourseDetail(courseId)
        courseViewModel.loadModules(courseId)
    }

    LaunchedEffect(enrollmentState) {
        if (enrollmentState is UiState.Success) {
            showEnrollDialog = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails de la formation") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        when (val state = courseState) {
            is UiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.padding(paddingValues),
                    message = "Chargement..."
                )
            }

            is UiState.Success -> {
                val course = state.data

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = course.titre,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            InfoChip(
                                icon = Icons.Default.Category,
                                label = course.domaine
                            )
                            InfoChip(
                                icon = Icons.Default.TrendingUp,
                                label = when (course.niveau) {
                                    1 -> "Débutant"
                                    2 -> "Intermédiaire"
                                    3 -> "Avancé"
                                    else -> "N/A"
                                }
                            )
                            InfoChip(
                                icon = Icons.Default.Schedule,
                                label = "${course.duree / 60}h"
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "%.2f €".format(course.prix),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (course.note != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "%.1f / 5".format(course.note),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    item {
                        PrimaryButton(
                            text = "S'inscrire à cette formation",
                            onClick = { showEnrollDialog = true },
                            icon = Icons.Default.Add,
                            isLoading = enrollmentState is UiState.Loading
                        )
                    }

                    item {
                        Card {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Description",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = course.description,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Contenu du cours",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    when (val modulesUiState = modulesState) {
                        is UiState.Success -> {
                            val modules = modulesUiState.data

                            if (modules.isEmpty()) {
                                item {
                                    Text(
                                        text = "Aucun module disponible pour le moment",
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            } else {
                                items(modules) { module ->
                                    ModuleCard(module)
                                }
                            }
                        }
                        is UiState.Loading -> {
                            item {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                        else -> {}
                    }
                }

                if (showEnrollDialog) {
                    AlertDialog(
                        onDismissRequest = { showEnrollDialog = false },
                        title = { Text("Confirmer l'inscription") },
                        text = {
                            Text("Voulez-vous vous inscrire à la formation \"${course.titre}\" pour ${course.prix}€ ?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    enrollmentViewModel.enrollInCourse(courseId)
                                }
                            ) {
                                Text("Confirmer")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showEnrollDialog = false }) {
                                Text("Annuler")
                            }
                        }
                    )
                }
            }

            is UiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { courseViewModel.loadCourseDetail(courseId) },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {}
        }
    }
}

@Composable
fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}