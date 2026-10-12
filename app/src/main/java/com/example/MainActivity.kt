package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.localization.AppLanguage
import com.example.ui.auth.GoogleAuthHelper
import com.example.ui.components.FirebaseReadinessDialog
import com.example.ui.components.OstadiBottomBar
import com.example.ui.components.OstadiTopAppBar
import com.example.ui.navigation.OstadiScreen
import com.example.ui.screens.AttendanceScreen
import com.example.ui.screens.ClassroomsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GradesScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ParticipationScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.StatisticsScreen
import com.example.ui.screens.StudentsScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.OstadiTheme
import com.example.ui.viewmodel.OstadiViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: OstadiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Attempt silent background auto sign-in for returning teachers
        val credentialManager = CredentialManager.create(this)
        GoogleAuthHelper.attemptAutoSignIn(
            context = this,
            credentialManager = credentialManager,
            onAuthSuccess = { viewModel.onAuthSuccess() },
            onUnauthenticated = { /* Keep on login / welcome screen */ },
            scope = lifecycleScope
        )

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(uiState.statusNotification) {
                uiState.statusNotification?.let { msg ->
                    snackbarHostState.showSnackbar(msg)
                    viewModel.clearNotification()
                }
            }

            OstadiTheme {
                CompositionLocalProvider(LocalLayoutDirection provides uiState.currentLanguage.layoutDirection) {
                    OstadiAppContent(
                        viewModel = viewModel,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
fun OstadiAppContent(
    viewModel: OstadiViewModel,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val currentScreen = uiState.currentScreen

    // Handle back button behavior for sub-screens
    BackHandler(enabled = currentScreen != OstadiScreen.WELCOME && currentScreen != OstadiScreen.DASHBOARD) {
        if (currentScreen == OstadiScreen.LOGIN || currentScreen == OstadiScreen.REGISTER) {
            viewModel.navigateTo(OstadiScreen.WELCOME)
        } else {
            viewModel.navigateTo(OstadiScreen.DASHBOARD)
        }
    }

    val showTopBar = currentScreen != OstadiScreen.WELCOME
    val showBottomBar = currentScreen.isTopLevel && uiState.isLoggedIn

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                OstadiTopAppBar(
                    currentScreen = currentScreen,
                    language = uiState.currentLanguage,
                    onNavigateBack = {
                        if (currentScreen == OstadiScreen.LOGIN || currentScreen == OstadiScreen.REGISTER) {
                            viewModel.navigateTo(OstadiScreen.WELCOME)
                        } else {
                            viewModel.navigateTo(OstadiScreen.DASHBOARD)
                        }
                    },
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onOpenFirebaseInfo = { viewModel.openFirebaseDialog(true) }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                OstadiBottomBar(
                    currentScreen = currentScreen,
                    language = uiState.currentLanguage,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Strict Auth Gate: If unauthenticated, only allow Welcome, Login, Register
            if (!uiState.isLoggedIn && currentScreen != OstadiScreen.WELCOME && currentScreen != OstadiScreen.REGISTER) {
                LoginScreen(
                    language = uiState.currentLanguage,
                    isLoading = uiState.isAuthLoading,
                    errorMessage = uiState.authErrorMessage,
                    onLoadingChange = { viewModel.setAuthLoading(it) },
                    onErrorChange = { viewModel.setAuthError(it) },
                    onAuthSuccess = { viewModel.onAuthSuccess() },
                    onDemoLogin = { viewModel.enterDemoMode() },
                    onNavigate = { viewModel.navigateTo(it) }
                )
            } else {
                when (currentScreen) {
                    OstadiScreen.WELCOME -> {
                        WelcomeScreen(
                            language = uiState.currentLanguage,
                            onNavigate = { viewModel.navigateTo(it) },
                            onDemoLogin = { viewModel.enterDemoMode() },
                            onToggleLanguage = { viewModel.toggleLanguage() }
                        )
                    }
                    OstadiScreen.LOGIN -> {
                        LoginScreen(
                            language = uiState.currentLanguage,
                            isLoading = uiState.isAuthLoading,
                            errorMessage = uiState.authErrorMessage,
                            onLoadingChange = { viewModel.setAuthLoading(it) },
                            onErrorChange = { viewModel.setAuthError(it) },
                            onAuthSuccess = { viewModel.onAuthSuccess() },
                            onDemoLogin = { viewModel.enterDemoMode() },
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                    OstadiScreen.REGISTER -> {
                        RegisterScreen(
                            language = uiState.currentLanguage,
                            isLoading = uiState.isAuthLoading,
                            onLoadingChange = { viewModel.setAuthLoading(it) },
                            onErrorChange = { viewModel.setAuthError(it) },
                            onAuthSuccess = { viewModel.onAuthSuccess() },
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                    OstadiScreen.DASHBOARD -> {
                        DashboardScreen(
                            uiState = uiState,
                            onNavigate = { viewModel.navigateTo(it) },
                            onOpenAddClassroom = { viewModel.openAddClassroomDialog(true) },
                            onOpenAddStudent = { viewModel.openAddStudentDialog(true) }
                        )
                    }
                    OstadiScreen.CLASSROOMS -> {
                        ClassroomsScreen(
                            uiState = uiState,
                            onNavigate = { viewModel.navigateTo(it) },
                            onSelectClassroom = { viewModel.setSelectedClassroom(it) },
                            onOpenAddDialog = { viewModel.openAddClassroomDialog(it) },
                            onAddClassroom = { name, level, room ->
                                viewModel.addClassroom(name, level, room)
                            }
                        )
                    }
                    OstadiScreen.STUDENTS -> {
                        StudentsScreen(
                            uiState = uiState,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            onSelectClassroom = { viewModel.setSelectedClassroom(it) },
                            onOpenAddStudentDialog = { viewModel.openAddStudentDialog(it) },
                            onOpenStudentDetails = { viewModel.openStudentDetailsDialog(it) },
                            onAddStudent = { firstName, lastName, gender, classId, phone, regNo ->
                                viewModel.addStudent(firstName, lastName, gender, classId, phone, regNo)
                            }
                        )
                    }
                    OstadiScreen.ATTENDANCE -> {
                        AttendanceScreen(
                            uiState = uiState,
                            onSelectClassroom = { viewModel.setSelectedClassroom(it) },
                            onRecordStatus = { studentId, status, note ->
                                viewModel.recordAttendanceForStudent(studentId, status, note)
                            },
                            onSaveFullRollCall = {
                                viewModel.saveFullRollCall()
                            }
                        )
                    }
                    OstadiScreen.GRADES -> {
                        GradesScreen(
                            uiState = uiState,
                            onSelectTerm = { viewModel.setSelectedTerm(it) },
                            onSelectClassroom = { viewModel.setSelectedClassroom(it) },
                            onOpenEditGrade = { viewModel.openAddGradeDialog(true, it) },
                            onCloseEditGrade = { viewModel.closeAddGradeDialog() },
                            onSaveGrade = { studentId, cont, test, exam, remarks ->
                                viewModel.saveGrade(studentId, cont, test, exam, remarks)
                            }
                        )
                    }
                    OstadiScreen.PARTICIPATION -> {
                        ParticipationScreen(
                            uiState = uiState,
                            onSelectClassroom = { viewModel.setSelectedClassroom(it) },
                            onUpdateParticipation = { studentId, delta, note ->
                                viewModel.updateParticipation(studentId, delta, note)
                            }
                        )
                    }
                    OstadiScreen.STATISTICS -> {
                        StatisticsScreen(uiState = uiState)
                    }
                    OstadiScreen.PROFILE_SETTINGS -> {
                        ProfileSettingsScreen(
                            uiState = uiState,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onOpenFirebaseDialog = { viewModel.openFirebaseDialog(true) },
                            onLogout = { viewModel.signOut(context) }
                        )
                    }
                }
            }
        }
    }

    // Firebase Architecture Readiness Dialog
    if (uiState.showFirebaseInfoDialog) {
        FirebaseReadinessDialog(
            language = uiState.currentLanguage,
            onDismiss = { viewModel.openFirebaseDialog(false) }
        )
    }
}
