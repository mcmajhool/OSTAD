package com.example

import com.example.data.model.AcademicTerm
import com.example.data.model.Gender
import com.example.data.model.GradeRecord
import com.example.data.model.Student
import com.example.ui.viewmodel.OstadiUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun algerianGradeCalculation_isAccurate() {
        // Continuous: 18.0, Test: 16.0 -> Devoir Avg = 17.0
        // Exam: 17.0 * 2 = 34.0
        // Weighted = (17.0 + 34.0) / 3 = 51.0 / 3 = 17.0
        val grade = GradeRecord(
            id = "g_test",
            studentId = "s_1",
            studentName = "ياسين بوزيد",
            classroomId = "c_1",
            term = AcademicTerm.TERM_1,
            continuousAssessment = 18.0,
            testScore = 16.0,
            examScore = 17.0
        )
        assertNotNull(grade.calculatedAverage)
        assertEquals(17.0, grade.calculatedAverage!!, 0.01)
        assertEquals("ممتاز", grade.mention)
    }

    @Test
    fun uiStateStatistics_calculatesAccurately() {
        val students = listOf(
            Student("1", "c1", "4AM1", "أحمد", "بن علي", Gender.MALE, "01", "0600", 15.0),
            Student("2", "c1", "4AM1", "فاطمة", "سالم", Gender.FEMALE, "02", "0600", 17.0),
            Student("3", "c1", "4AM1", "كريم", "دحمان", Gender.MALE, "03", "0600", 8.0)
        )
        val state = OstadiUiState(students = students)

        assertEquals(3, state.totalStudentsCount)
        assertEquals(2, state.maleStudentsCount)
        assertEquals(1, state.femaleStudentsCount)
        assertEquals(66, state.successRatePercentage) // 2 out of 3 passed >= 10
    }
}
