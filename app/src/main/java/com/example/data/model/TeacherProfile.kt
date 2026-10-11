package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class TeacherProfile(
    @DocumentId val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val schoolName: String = "",
    val educationalLevel: String = "التعليم المتوسط (CEM)",
    @ServerTimestamp val accountCreationDate: Timestamp? = null,
    val wilaya: String = "الجزائر العاصمة",
    val subject: String = "مادة الرياضيات",
    val academicYear: String = "2024 / 2025"
) {
    val fullName: String
        get() {
            val combined = "$lastName $firstName".trim()
            return if (combined.isNotBlank()) combined else "الأستاذ"
        }

    val cycle: String
        get() = educationalLevel
}
