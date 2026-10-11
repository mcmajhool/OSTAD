package com.example.ui.viewmodel

import com.example.data.model.AcademicTerm
import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceStatus
import com.example.data.model.Classroom
import com.example.data.model.ExamEvent
import com.example.data.model.Gender
import com.example.data.model.GradeRecord
import com.example.data.model.ParticipationRecord
import com.example.data.model.Student
import com.example.data.model.TeacherProfile
import com.example.localization.AppLanguage
import com.example.ui.navigation.OstadiScreen

data class OstadiUiState(
    val currentLanguage: AppLanguage = AppLanguage.ARABIC,
    val currentScreen: OstadiScreen = OstadiScreen.WELCOME,
    val isDemoMode: Boolean = false,
    val teacherUid: String? = null,
    val teacherProfile: TeacherProfile? = null,
    val classrooms: List<Classroom> = emptyList(),
    val students: List<Student> = emptyList(),
    val attendanceRecords: List<AttendanceRecord> = emptyList(),
    val gradeRecords: List<GradeRecord> = emptyList(),
    val participationRecords: List<ParticipationRecord> = emptyList(),
    val upcomingExams: List<ExamEvent> = emptyList(),
    val selectedClassroomId: String? = null,
    val searchQuery: String = "",
    val selectedTerm: AcademicTerm = AcademicTerm.TERM_1,
    val selectedAttendanceDate: String = "2024-10-11",
    val selectedSessionName: String = "الحصة 1 (08:00 - 09:00)",
    val showFirebaseInfoDialog: Boolean = false,
    val showAddClassroomDialog: Boolean = false,
    val showAddStudentDialog: Boolean = false,
    val showAddGradeDialog: Boolean = false,
    val selectedStudentForGrade: Student? = null,
    val selectedStudentForDetails: Student? = null,
    val isLoggedIn: Boolean = false,
    val isAuthLoading: Boolean = false,
    val authErrorMessage: String? = null,
    val statusNotification: String? = null
) {
    val totalClassroomsCount: Int
        get() = classrooms.size

    val totalStudentsCount: Int
        get() = students.size

    val maleStudentsCount: Int
        get() = students.count { it.gender == Gender.MALE }

    val femaleStudentsCount: Int
        get() = students.count { it.gender == Gender.FEMALE }

    val todayAbsencesCount: Int
        get() = attendanceRecords.count { it.date == selectedAttendanceDate && it.status == AttendanceStatus.ABSENT }

    /**
     * Resolves the actual calculated average for a student for the currently selected term.
     * Checks gradeRecords first; falls back to student's static termAverage only if no record exists.
     */
    fun getStudentAverage(studentId: String): Double? {
        val record = gradeRecords.firstOrNull { it.studentId == studentId && it.term == selectedTerm }
        if (record != null) {
            return record.calculatedAverage
        }
        val student = students.firstOrNull { it.id == studentId }
        return student?.termAverage
    }

    fun getStudentGradeRecord(studentId: String): GradeRecord? {
        return gradeRecords.firstOrNull { it.studentId == studentId && it.term == selectedTerm }
    }

    /**
     * General average across all students with actual recorded grades for the current term.
     * Students with missing/unentered grades are NOT counted as 0.0 and do not deflated the average.
     */
    val generalAverageScore: Double
        get() {
            val gradedAverages = students.mapNotNull { getStudentAverage(it.id) }
            if (gradedAverages.isEmpty()) return 0.0
            val sum = gradedAverages.sum()
            return (Math.round((sum / gradedAverages.size) * 100.0) / 100.0)
        }

    /**
     * Success rate percentage (scores >= 10.0) out of evaluated students only.
     */
    val successRatePercentage: Int
        get() {
            val gradedAverages = students.mapNotNull { getStudentAverage(it.id) }
            if (gradedAverages.isEmpty()) return 0
            val passed = gradedAverages.count { it >= 10.0 }
            return ((passed.toDouble() / gradedAverages.size.toDouble()) * 100).toInt()
        }

    /**
     * Dynamically computes the student count for a specific classroom.
     */
    fun getClassroomStudentCount(classroomId: String): Int {
        return students.count { it.classroomId == classroomId }
    }

    /**
     * Dynamically computes the average score for a specific classroom.
     */
    fun getClassroomAverage(classroomId: String): Double {
        val classStudents = students.filter { it.classroomId == classroomId }
        val classGrades = classStudents.mapNotNull { getStudentAverage(it.id) }
        if (classGrades.isEmpty()) {
            val classObj = classrooms.firstOrNull { it.id == classroomId }
            return classObj?.averageScore ?: 0.0
        }
        val sum = classGrades.sum()
        return (Math.round((sum / classGrades.size) * 100.0) / 100.0)
    }

    /**
     * Individual attendance rate for a student:
     * Rate = (Present + Late) / (Present + Late + Absent) * 100.
     * Excused absences are excluded from penalty.
     * Returns 100.0 if no sessions are recorded.
     */
    fun getStudentAttendanceRate(studentId: String): Double {
        val studentRecords = attendanceRecords.filter { it.studentId == studentId }
        val presentOrLate = studentRecords.count { it.status == AttendanceStatus.PRESENT || it.status == AttendanceStatus.LATE }
        val absent = studentRecords.count { it.status == AttendanceStatus.ABSENT }
        val evaluatedTotal = presentOrLate + absent
        if (evaluatedTotal == 0) return 100.0
        val rate = (presentOrLate.toDouble() / evaluatedTotal.toDouble()) * 100.0
        return Math.round(rate * 10.0) / 10.0
    }

    val filteredStudents: List<Student>
        get() {
            return students.filter { student ->
                val matchesClass = selectedClassroomId == null || student.classroomId == selectedClassroomId
                val matchesQuery = searchQuery.isBlank() ||
                        student.fullName.contains(searchQuery, ignoreCase = true) ||
                        student.registrationNumber.contains(searchQuery, ignoreCase = true)
                matchesClass && matchesQuery
            }
        }
}
