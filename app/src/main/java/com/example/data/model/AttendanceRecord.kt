package com.example.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE,
    EXCUSED
}

@IgnoreExtraProperties
data class AttendanceRecord(
    @DocumentId val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val classroomId: String = "",
    val date: String = "",
    val sessionName: String = "", // e.g. "الحصة 1 (08:00 - 09:00)"
    val status: AttendanceStatus = AttendanceStatus.PRESENT,
    val note: String = ""
)
