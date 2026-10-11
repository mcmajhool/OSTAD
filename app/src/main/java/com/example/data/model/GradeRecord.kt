package com.example.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

enum class AcademicTerm {
    TERM_1,
    TERM_2,
    TERM_3
}

@IgnoreExtraProperties
data class GradeRecord(
    @DocumentId val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val classroomId: String = "",
    val term: AcademicTerm = AcademicTerm.TERM_1,
    val continuousAssessment: Double? = null, // التقويم المستمر (من 20)
    val testScore: Double? = null,           // الفرض المحروس (من 20)
    val examScore: Double? = null,           // الاختبار الفصلي (من 20، المعامل 2)
    val remarks: String = ""
) {
    /**
     * Official Algerian Ministry of National Education Grading Formula:
     * - Continuous Assessment (التقويم المستمر): weight 1 (averaged with test)
     * - Scheduled Test (الفرض المحروس): weight 1 (averaged with continuous)
     * - Term Exam (الاختبار الفصلي): weight 2
     *
     * General Formula when all 3 present:
     * DevoirAvg = (Continuous + Test) / 2.0
     * WeightedAverage = (DevoirAvg + Exam * 2.0) / 3.0
     *
     * Crucial QA Rule: Missing grades (null) MUST NOT be converted to zero.
     * Denominators adjust dynamically based on completed components only.
     */
    val calculatedAverage: Double?
        get() {
            val hasC = continuousAssessment != null
            val hasT = testScore != null
            val hasE = examScore != null

            if (!hasC && !hasT && !hasE) return null

            val weighted = when {
                // All 3 components present
                hasC && hasT && hasE -> {
                    val devoirAvg = (continuousAssessment!! + testScore!!) / 2.0
                    (devoirAvg + (examScore!! * 2.0)) / 3.0
                }
                // Continuous + Test present, Exam missing (pending exam period)
                hasC && hasT && !hasE -> {
                    (continuousAssessment!! + testScore!!) / 2.0
                }
                // Test + Exam present, Continuous missing
                !hasC && hasT && hasE -> {
                    (testScore!! + (examScore!! * 2.0)) / 3.0
                }
                // Continuous + Exam present, Test missing (e.g. exempt from test)
                hasC && !hasT && hasE -> {
                    (continuousAssessment!! + (examScore!! * 2.0)) / 3.0
                }
                // Only Exam present
                !hasC && !hasT && hasE -> examScore!!
                // Only Continuous present
                hasC && !hasT && !hasE -> continuousAssessment!!
                // Only Test present
                !hasC && hasT && !hasE -> testScore!!
                else -> return null
            }
            return Math.round(weighted * 100.0) / 100.0
        }

    val mention: String
        get() {
            val avg = calculatedAverage ?: return "غير مرصود"
            return when {
                avg >= 16.0 -> "ممتاز"
                avg >= 14.0 -> "جيد جداً"
                avg >= 12.0 -> "جيد"
                avg >= 10.0 -> "مقبول"
                else -> "دون المتوسط"
            }
        }
}
