package com.example.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Classroom(
    @DocumentId val id: String = "",
    val name: String = "",
    val level: String = "",
    val roomNumber: String = "القاعة 01",
    val studentCount: Int = 0,
    val averageScore: Double = 0.0
)
