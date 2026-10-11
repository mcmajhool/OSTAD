package com.example.data.repository

import com.example.data.model.AttendanceRecord
import com.example.data.model.Classroom
import com.example.data.model.ExamEvent
import com.example.data.model.GradeRecord
import com.example.data.model.ParticipationRecord
import com.example.data.model.Student
import com.example.data.model.TeacherProfile
import kotlinx.coroutines.flow.Flow

interface TeacherRepository {
    fun getTeacherProfile(): Flow<TeacherProfile>
    suspend fun updateTeacherProfile(profile: TeacherProfile)

    fun getClassrooms(): Flow<List<Classroom>>
    suspend fun addClassroom(classroom: Classroom)

    fun getStudents(): Flow<List<Student>>
    suspend fun addStudent(student: Student)

    fun getAttendanceRecords(): Flow<List<AttendanceRecord>>
    suspend fun saveAttendance(records: List<AttendanceRecord>)

    fun getGradeRecords(): Flow<List<GradeRecord>>
    suspend fun saveGrade(record: GradeRecord)

    fun getParticipationRecords(): Flow<List<ParticipationRecord>>
    suspend fun updateParticipation(studentId: String, starsDelta: Int, note: String)

    fun getUpcomingExams(): Flow<List<ExamEvent>>
}
