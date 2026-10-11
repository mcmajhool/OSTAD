package com.example.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

enum class Gender {
    MALE,
    FEMALE
}

@IgnoreExtraProperties
data class Student(
    @DocumentId val id: String = "",
    val classroomId: String = "",
    val classroomName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: Gender = Gender.MALE,
    val registrationNumber: String = "",
    val parentPhone: String = "",
    val termAverage: Double? = null,
    val totalAbsences: Int = 0,
    val participationStars: Int = 3,
    val notes: String = ""
) {
    val fullName: String
        get() = "$lastName $firstName".trim()
}
