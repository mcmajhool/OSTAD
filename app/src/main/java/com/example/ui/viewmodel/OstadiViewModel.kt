package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AcademicTerm
import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceStatus
import com.example.data.model.Classroom
import com.example.data.model.Gender
import com.example.data.model.GradeRecord
import com.example.data.model.Student
import com.example.data.model.TeacherProfile
import com.example.data.repository.FirestoreTeacherRepository
import com.example.data.repository.SampleTeacherRepository
import com.example.data.repository.TeacherRepository
import com.example.localization.AppLanguage
import com.example.ui.auth.GoogleAuthHelper
import com.example.ui.navigation.OstadiScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OstadiViewModel(application: Application) : AndroidViewModel(application) {

    private val auth = Firebase.auth
    private var activeRepository: TeacherRepository? = null
    private val observationJobs = mutableListOf<Job>()

    private val _uiState = MutableStateFlow(OstadiUiState())
    val uiState: StateFlow<OstadiUiState> = _uiState.asStateFlow()

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isDemoMode = false,
                    teacherUid = user.uid,
                    isAuthLoading = false,
                    authErrorMessage = null,
                    currentScreen = if (it.currentScreen == OstadiScreen.LOGIN ||
                        it.currentScreen == OstadiScreen.WELCOME ||
                        it.currentScreen == OstadiScreen.REGISTER
                    ) {
                        OstadiScreen.DASHBOARD
                    } else it.currentScreen
                )
            }
            bindAuthenticatedRepository(user.uid)
        } else {
            // Only clear data if we are NOT in intentional demo mode
            if (!_uiState.value.isDemoMode) {
                clearAuthenticatedData()
                _uiState.update {
                    it.copy(
                        isLoggedIn = false,
                        teacherUid = null,
                        isAuthLoading = false,
                        currentScreen = if (it.currentScreen != OstadiScreen.WELCOME) {
                            OstadiScreen.LOGIN
                        } else OstadiScreen.WELCOME
                    )
                }
            }
        }
    }

    init {
        auth.addAuthStateListener(authListener)
    }

    /**
     * Activates Demo Preview mode for testing and reviewing the interface.
     * Uses SampleTeacherRepository in-memory without polluting Firebase.
     */
    fun enterDemoMode() {
        clearObservationJobs()
        val demoRepo = SampleTeacherRepository()
        activeRepository = demoRepo
        _uiState.update {
            it.copy(
                isDemoMode = true,
                isLoggedIn = true,
                currentScreen = OstadiScreen.DASHBOARD,
                statusNotification = "تم تفعيل المعاينة التجريبية (بيانات توضيحية فقط)"
            )
        }
        observeRepository(demoRepo)
    }

    fun exitDemoMode() {
        clearAuthenticatedData()
        _uiState.update {
            it.copy(
                isDemoMode = false,
                isLoggedIn = false,
                currentScreen = OstadiScreen.WELCOME,
                statusNotification = "تم الخروج من وضع المعاينة"
            )
        }
    }

    private fun bindAuthenticatedRepository(uid: String) {
        clearObservationJobs()
        val repo = FirestoreTeacherRepository(getApplication())
        activeRepository = repo
        observeRepository(repo)
    }

    private fun observeRepository(repo: TeacherRepository) {
        val job1 = viewModelScope.launch {
            repo.getTeacherProfile().collect { profile ->
                _uiState.update { it.copy(teacherProfile = profile) }
            }
        }
        val job2 = viewModelScope.launch {
            repo.getClassrooms().collect { classrooms ->
                _uiState.update { it.copy(classrooms = classrooms) }
            }
        }
        val job3 = viewModelScope.launch {
            repo.getStudents().collect { students ->
                _uiState.update { it.copy(students = students) }
            }
        }
        val job4 = viewModelScope.launch {
            repo.getAttendanceRecords().collect { attendance ->
                _uiState.update { it.copy(attendanceRecords = attendance) }
            }
        }
        val job5 = viewModelScope.launch {
            repo.getGradeRecords().collect { grades ->
                _uiState.update { it.copy(gradeRecords = grades) }
            }
        }
        val job6 = viewModelScope.launch {
            repo.getParticipationRecords().collect { participation ->
                _uiState.update { it.copy(participationRecords = participation) }
            }
        }
        val job7 = viewModelScope.launch {
            repo.getUpcomingExams().collect { exams ->
                _uiState.update { it.copy(upcomingExams = exams) }
            }
        }
        observationJobs.addAll(listOf(job1, job2, job3, job4, job5, job6, job7))
    }

    private fun clearObservationJobs() {
        observationJobs.forEach { it.cancel() }
        observationJobs.clear()
    }

    private fun clearAuthenticatedData() {
        clearObservationJobs()
        activeRepository = null
        _uiState.update {
            it.copy(
                teacherProfile = null,
                classrooms = emptyList(),
                students = emptyList(),
                attendanceRecords = emptyList(),
                gradeRecords = emptyList(),
                participationRecords = emptyList(),
                upcomingExams = emptyList()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
        clearObservationJobs()
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun toggleLanguage() {
        val next = if (_uiState.value.currentLanguage == AppLanguage.ARABIC) AppLanguage.FRENCH else AppLanguage.ARABIC
        setLanguage(next)
    }

    fun navigateTo(screen: OstadiScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setAuthLoading(loading: Boolean) {
        _uiState.update { it.copy(isAuthLoading = loading) }
    }

    fun setAuthError(message: String?) {
        _uiState.update { it.copy(authErrorMessage = message, isAuthLoading = false) }
    }

    fun onAuthSuccess() {
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                isDemoMode = false,
                isAuthLoading = false,
                authErrorMessage = null,
                currentScreen = OstadiScreen.DASHBOARD,
                statusNotification = "تم تسجيل الدخول بنجاح بحساب الأستاذ"
            )
        }
    }

    fun signOut(context: Context) {
        if (_uiState.value.isDemoMode) {
            exitDemoMode()
            return
        }
        val credentialManager = CredentialManager.create(context)
        GoogleAuthHelper.signOut(
            context = context,
            credentialManager = credentialManager,
            onSignOutComplete = {
                _uiState.update {
                    it.copy(
                        isLoggedIn = false,
                        isDemoMode = false,
                        teacherUid = null,
                        currentScreen = OstadiScreen.LOGIN,
                        statusNotification = "تم تسجيل الخروج بنجاح"
                    )
                }
            },
            scope = viewModelScope
        )
    }

    fun updateTeacherProfile(
        firstName: String,
        lastName: String,
        schoolName: String,
        wilaya: String,
        cycle: String,
        subject: String
    ) {
        viewModelScope.launch {
            val current = _uiState.value.teacherProfile ?: return@launch
            val updated = current.copy(
                firstName = firstName,
                lastName = lastName,
                schoolName = schoolName,
                wilaya = wilaya,
                educationalLevel = cycle,
                subject = subject
            )
            activeRepository?.updateTeacherProfile(updated)
            _uiState.update {
                it.copy(
                    teacherProfile = updated,
                    statusNotification = "تم تحديث الملف التربوي بنجاح"
                )
            }
        }
    }

    fun setSelectedClassroom(classroomId: String?) {
        _uiState.update { it.copy(selectedClassroomId = classroomId) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedTerm(term: AcademicTerm) {
        _uiState.update { it.copy(selectedTerm = term) }
    }

    fun setSelectedAttendanceDate(date: String) {
        _uiState.update { it.copy(selectedAttendanceDate = date) }
    }

    fun setSelectedSessionName(session: String) {
        _uiState.update { it.copy(selectedSessionName = session) }
    }

    fun recordAttendanceForStudent(studentId: String, status: AttendanceStatus, note: String = "") {
        viewModelScope.launch {
            val student = _uiState.value.students.firstOrNull { it.id == studentId } ?: return@launch
            val record = AttendanceRecord(
                id = "att_${System.currentTimeMillis()}_$studentId",
                studentId = studentId,
                studentName = student.fullName,
                classroomId = student.classroomId,
                date = _uiState.value.selectedAttendanceDate,
                sessionName = _uiState.value.selectedSessionName,
                status = status,
                note = note
            )
            activeRepository?.saveAttendance(listOf(record))
            _uiState.update {
                it.copy(statusNotification = "تم تسجيل حالة ${student.fullName} كـ (${status.name})")
            }
        }
    }

    fun saveFullRollCall() {
        val s = com.example.localization.AppStrings.get(_uiState.value.currentLanguage)
        _uiState.update {
            it.copy(statusNotification = if (it.currentLanguage == com.example.localization.AppLanguage.ARABIC) "تم حفظ ورقة المناداة الكاملة للحصة بنجاح" else "Feuille d'appel enregistrée avec succès")
        }
    }

    fun saveGrade(
        studentId: String,
        continuous: Double?,
        test: Double?,
        exam: Double?,
        remarks: String
    ) {
        viewModelScope.launch {
            val student = _uiState.value.students.firstOrNull { it.id == studentId } ?: return@launch
            val gradeRecord = GradeRecord(
                id = "grade_${studentId}_${_uiState.value.selectedTerm.name}",
                studentId = studentId,
                studentName = student.fullName,
                classroomId = student.classroomId,
                term = _uiState.value.selectedTerm,
                continuousAssessment = continuous,
                testScore = test,
                examScore = exam,
                remarks = remarks
            )
            activeRepository?.saveGrade(gradeRecord)
            _uiState.update {
                it.copy(
                    showAddGradeDialog = false,
                    selectedStudentForGrade = null,
                    statusNotification = "تم حفظ نقاط التلميذ ${student.fullName} بنجاح"
                )
            }
        }
    }

    fun updateParticipation(studentId: String, delta: Int, note: String = "") {
        viewModelScope.launch {
            activeRepository?.updateParticipation(studentId, delta, note)
            val student = _uiState.value.students.firstOrNull { it.id == studentId }
            _uiState.update {
                it.copy(statusNotification = "تم تحديث نقاط مشاركة التلميذ: ${student?.fullName ?: ""}")
            }
        }
    }

    fun addClassroom(name: String, level: String, room: String) {
        viewModelScope.launch {
            val newClass = Classroom(
                id = "c_${System.currentTimeMillis()}",
                name = name,
                level = level,
                roomNumber = room.ifBlank { "القاعة 01" },
                studentCount = 0,
                averageScore = 0.0
            )
            activeRepository?.addClassroom(newClass)
            _uiState.update {
                it.copy(
                    showAddClassroomDialog = false,
                    statusNotification = "تمت إضافة القسم $name بنجاح"
                )
            }
        }
    }

    fun addStudent(
        firstName: String,
        lastName: String,
        gender: Gender,
        classroomId: String,
        phone: String,
        regNumber: String
    ) {
        viewModelScope.launch {
            val targetClass = _uiState.value.classrooms.firstOrNull { it.id == classroomId }
            val className = targetClass?.name ?: "القسم"
            val newStudent = Student(
                id = "s_${System.currentTimeMillis()}",
                classroomId = classroomId,
                classroomName = className,
                firstName = firstName,
                lastName = lastName,
                gender = gender,
                registrationNumber = regNumber.ifBlank { "2024/${(100..999).random()}" },
                parentPhone = phone.ifBlank { "0600000000" },
                termAverage = null, // unentered grades are not converted to zero
                totalAbsences = 0,
                participationStars = 3
            )
            activeRepository?.addStudent(newStudent)
            _uiState.update {
                it.copy(
                    showAddStudentDialog = false,
                    statusNotification = "تمت إضافة التلميذ $lastName $firstName إلى $className"
                )
            }
        }
    }

    fun openFirebaseDialog(show: Boolean) {
        _uiState.update { it.copy(showFirebaseInfoDialog = show) }
    }

    fun openAddClassroomDialog(show: Boolean) {
        _uiState.update { it.copy(showAddClassroomDialog = show) }
    }

    fun openAddStudentDialog(show: Boolean) {
        _uiState.update { it.copy(showAddStudentDialog = show) }
    }

    fun openAddGradeDialog(show: Boolean, student: Student? = null) {
        _uiState.update {
            it.copy(
                showAddGradeDialog = show,
                selectedStudentForGrade = student
            )
        }
    }

    fun closeAddGradeDialog() {
        _uiState.update {
            it.copy(
                showAddGradeDialog = false,
                selectedStudentForGrade = null
            )
        }
    }

    fun openStudentDetailsDialog(student: Student?) {
        _uiState.update { it.copy(selectedStudentForDetails = student) }
    }

    fun clearNotification() {
        _uiState.update { it.copy(statusNotification = null) }
    }
}
