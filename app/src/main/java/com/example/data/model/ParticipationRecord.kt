package com.example.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class ParticipationRecord(
    @DocumentId val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val classroomId: String = "",
    val stars: Int = 3,
    val date: String = "",
    val note: String = "",
    val positiveRemark: Boolean = true
)

@IgnoreExtraProperties
data class ExamEvent(
    @DocumentId val id: String = "",
    val title: String = "",
    val classroomName: String = "",
    val date: String = "",
    val time: String = "",
    val term: AcademicTerm = AcademicTerm.TERM_1,
    val room: String = "",
    val classroomId: String = "",
    val coefficient: Int = 2
)
