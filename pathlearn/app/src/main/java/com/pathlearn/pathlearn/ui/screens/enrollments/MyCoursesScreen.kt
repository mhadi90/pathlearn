package com.pathlearn.pathlearn.ui.screens.enrollments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pathlearn.pathlearn.ui.components.EmptyState
import com.pathlearn.pathlearn.ui.components.EnrollmentCard
import com.pathlearn.pathlearn.ui.components.ErrorState
import com.pathlearn.pathlearn.ui.components.LoadingIndicator
import com.pathlearn.pathlearn.utils.UiState
import com.pathlearn.pathlearn.viewmodel.EnrollmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesScreen(
    onBackClick: () -> Unit,
    onCourseClick: (Int) -> Unit,
    viewModel: EnrollmentViewModel = viewModel()
) {
    val enrollmentsState by viewModel.enrollmentsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyEnrollments()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mes formations") },
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
        when (val state = enrollmentsState) {
            is UiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.padding(paddingValues),
                    message = "Chargement de vos inscriptions..."
                )
            }

            is UiState.Success -> {
                val enrollments = state.data

                if (enrollments.isEmpty()) {
                    EmptyState(
                        icon = Icons.Default.School,
                        title = "Aucune inscription",
                        description = "Inscrivez-vous à une formation pour commencer",
                        modifier = Modifier.padding(paddingValues)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(enrollments) { enrollment ->
                            EnrollmentCard(
                                enrollment = enrollment,
                                onClick = { onCourseClick(enrollment.idFormation) },
                                onCancel = { viewModel.cancelEnrollment(enrollment.id) }
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { viewModel.loadMyEnrollments() },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {}
        }
    }
}