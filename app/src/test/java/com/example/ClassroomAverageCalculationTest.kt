package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AcademicTerm
import com.example.data.model.Classroom
import com.example.data.model.Gender
import com.example.data.model.GradeRecord
import com.example.data.model.Student
import com.example.data.repository.SampleTeacherRepository
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.viewmodel.OstadiUiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

/**
 * Comprehensive JUnit & Robolectric Test Suite for Ostadi (أستاذي) Classroom Average Calculations.
 *
 * Verifies that:
 * 1. Missing grades (null) in any grading component (continuousAssessment, testScore, examScore)
 *    are NEVER defaulted to zero.
 * 2. Missing students (null grade record or unentered term average) are completely excluded
 *    from classroom averages, both from the sum and the denominator count.
 * 3. Explicit zero scores (0.0 earned by a student) are strictly distinguished from missing grades (null),
 *    and properly included in averages and success rates.
 * 4. Classroom averages, general school averages, and success rates compute accurate results
 *    under mixed, empty, multi-classroom, and multi-term scenarios.
 * 5. Android resources and localized strings operate properly under Robolectric JVM runner.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ClassroomAverageCalculationTest {

    // =========================================================================
    // SECTION 1: INDIVIDUAL WEIGHTED AVERAGE FORMULAS (ALGERIAN MINISTRY)
    // =========================================================================

    @Test
    fun testAllThreeComponentsPresentFollowsOfficialFormula() {
        // Continuous = 16.0, Test = 14.0, Exam = 18.0 (coef 2)
        // DevoirAvg = (16.0 + 14.0) / 2 = 15.0
        // Weighted = (15.0 + 18.0 * 2) / 3 = (15.0 + 36.0) / 3 = 51.0 / 3 = 17.0
        val record = GradeRecord(
            id = "g1",
            studentId = "s1",
            continuousAssessment = 16.0,
            testScore = 14.0,
            examScore = 18.0
        )
        val avg = record.calculatedAverage
        assertNotNull("Average must not be null when all scores are provided", avg)
        assertEquals(17.0, avg!!, 0.001)
        assertEquals("ممتاز", record.mention)
    }

    @Test
    fun testMissingExamScoreIsExcludedFromWeightedAverageAndNotTreatedAsZero() {
        // Continuous = 15.0, Test = 13.0, Exam = null (not yet conducted/entered)
        // Expected Algerian formula without exam: DevoirAvg = (15.0 + 13.0) / 2 = 14.0
        //
        // Critical Anti-Regression:
        // If the missing exam were treated as 0.0:
        // DevoirAvg = 14.0
        // Weighted = (14.0 + 0.0 * 2) / 3 = 14.0 / 3 = 4.67 (FAILS STUDENT UNFAIRLY)
        val record = GradeRecord(
            id = "g_pending_exam",
            studentId = "s1",
            continuousAssessment = 15.0,
            testScore = 13.0,
            examScore = null
        )
        val avg = record.calculatedAverage
        assertNotNull("Average must not be null when coursework is entered", avg)
        assertEquals(14.0, avg!!, 0.001)
        assertEquals("جيد جداً", record.mention)

        // Strict verification: must NOT be deflated to 4.67
        assertFalse("Missing exam must never be coerced to 0.0", Math.abs(avg - 4.67) < 0.1)
    }

    @Test
    fun testMissingContinuousAssessmentIsExcludedWithoutZeroDeflation() {
        // Continuous = null (exempt/absent with excuse), Test = 12.0, Exam = 15.0
        // Formula without continuous: (Test + Exam * 2) / 3 = (12.0 + 30.0) / 3 = 14.0
        val record = GradeRecord(
            id = "g_missing_continuous",
            studentId = "s1",
            continuousAssessment = null,
            testScore = 12.0,
            examScore = 15.0
        )
        val avg = record.calculatedAverage
        assertNotNull(avg)
        assertEquals(14.0, avg!!, 0.001)
        assertEquals("جيد جداً", record.mention)
    }

    @Test
    fun testMissingScheduledTestIsExcludedWithoutZeroDeflation() {
        // Continuous = 14.0, Test = null, Exam = 16.0
        // Formula without test: (Continuous + Exam * 2) / 3 = (14.0 + 32.0) / 3 = 46.0 / 3 = 15.33
        val record = GradeRecord(
            id = "g_missing_test",
            studentId = "s1",
            continuousAssessment = 14.0,
            testScore = null,
            examScore = 16.0
        )
        val avg = record.calculatedAverage
        assertNotNull(avg)
        assertEquals(15.33, avg!!, 0.01)
        assertEquals("جيد جداً", record.mention)
    }

    @Test
    fun testOnlyExamScoreEnteredUsesExamDirectly() {
        // Student only has term exam entered: 16.5
        val record = GradeRecord(
            id = "g_exam_only",
            studentId = "s1",
            continuousAssessment = null,
            testScore = null,
            examScore = 16.5
        )
        assertEquals(16.5, record.calculatedAverage!!, 0.001)
        assertEquals("ممتاز", record.mention)
    }

    @Test
    fun testOnlyContinuousAssessmentEnteredUsesContinuousDirectly() {
        val record = GradeRecord(
            id = "g_cont_only",
            studentId = "s1",
            continuousAssessment = 13.5,
            testScore = null,
            examScore = null
        )
        assertEquals(13.5, record.calculatedAverage!!, 0.001)
        assertEquals("جيد", record.mention)
    }

    @Test
    fun testOnlyTestScoreEnteredUsesTestDirectly() {
        val record = GradeRecord(
            id = "g_test_only",
            studentId = "s1",
            continuousAssessment = null,
            testScore = 11.0,
            examScore = null
        )
        assertEquals(11.0, record.calculatedAverage!!, 0.001)
        assertEquals("مقبول", record.mention)
    }

    @Test
    fun testCompletelyUnenteredGradesReturnNullAndShowUnrecordedMention() {
        val unentered = GradeRecord(
            id = "g_unentered",
            studentId = "s1",
            continuousAssessment = null,
            testScore = null,
            examScore = null
        )
        assertNull("All null scores must yield null calculatedAverage", unentered.calculatedAverage)
        assertEquals("غير مرصود", unentered.mention)
    }

    @Test
    fun testExplicitZeroScoreIsPreservedAndDifferentiatedFromNull() {
        // Case 1: Student took all assessments and earned 0.0
        val explicitZeroAll = GradeRecord(
            id = "g_zero_all",
            studentId = "s_zero",
            continuousAssessment = 0.0,
            testScore = 0.0,
            examScore = 0.0
        )
        val zeroAvg = explicitZeroAll.calculatedAverage
        assertNotNull("Explicit 0.0 must NOT be treated as null", zeroAvg)
        assertEquals(0.0, zeroAvg!!, 0.001)
        assertEquals("دون المتوسط", explicitZeroAll.mention)

        // Case 2: Student took test and got 0.0, but Continuous = 10.0 and Exam = 10.0
        // DevoirAvg = (10.0 + 0.0) / 2 = 5.0
        // Weighted = (5.0 + 10.0 * 2) / 3 = 25.0 / 3 = 8.33
        val explicitZeroTest = GradeRecord(
            id = "g_zero_test",
            studentId = "s_zero",
            continuousAssessment = 10.0,
            testScore = 0.0, // Earned zero
            examScore = 10.0
        )
        assertEquals(8.33, explicitZeroTest.calculatedAverage!!, 0.01)
        assertEquals("دون المتوسط", explicitZeroTest.mention)
    }

    // =========================================================================
    // SECTION 2: CLASSROOM AVERAGE CALCULATIONS IN OstadiUiState
    // =========================================================================

    @Test
    fun testClassroomAverageExcludesStudentsWithMissingGrades() {
        val classroomId = "c_4m1"
        val students = listOf(
            Student(id = "s1", classroomId = classroomId, firstName = "أمير", lastName = "حميدي"),
            Student(id = "s2", classroomId = classroomId, firstName = "سارة", lastName = "بوزيد"),
            Student(id = "s3", classroomId = classroomId, firstName = "ياسين", lastName = "قادري"), // Unentered
            Student(id = "s4", classroomId = classroomId, firstName = "مريم", lastName = "علوي")   // Unentered
        )

        val grades = listOf(
            GradeRecord(studentId = "s1", classroomId = classroomId, continuousAssessment = 16.0, testScore = 16.0, examScore = 16.0), // avg = 16.0
            GradeRecord(studentId = "s2", classroomId = classroomId, continuousAssessment = 14.0, testScore = 14.0, examScore = 14.0), // avg = 14.0
            // s3 has empty record
            GradeRecord(studentId = "s3", classroomId = classroomId, continuousAssessment = null, testScore = null, examScore = null)
            // s4 has no record at all in the list
        )

        val state = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "4 متوسط 1")),
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )

        // Graded sum = 16.0 + 14.0 = 30.0
        // Graded students count = 2 (s1 and s2 only; s3 and s4 excluded)
        // Classroom Average = 30.0 / 2 = 15.0
        val classroomAvg = state.getClassroomAverage(classroomId)
        assertEquals(15.0, classroomAvg, 0.001)

        // Proof against zero coercion:
        // If s3 and s4 defaulted to 0.0, average would be (16.0 + 14.0 + 0.0 + 0.0) / 4 = 7.5
        assertFalse(
            "Classroom average must never be deflated by missing grades",
            Math.abs(classroomAvg - 7.5) < 0.1
        )
    }

    @Test
    fun testClassroomAverageIncludesExplicitZeroWhileExcludingMissing() {
        val classroomId = "c_test_zero"
        val students = listOf(
            Student(id = "s_high", classroomId = classroomId, firstName = "خالد", lastName = "منصوري"),
            Student(id = "s_zero", classroomId = classroomId, firstName = "أحمد", lastName = "طاهر"),
            Student(id = "s_missing", classroomId = classroomId, firstName = "سميرة", lastName = "ناصري")
        )

        val grades = listOf(
            // Student 1: 18.0 average
            GradeRecord(studentId = "s_high", classroomId = classroomId, continuousAssessment = 18.0, testScore = 18.0, examScore = 18.0),
            // Student 2: Explicit 0.0 earned
            GradeRecord(studentId = "s_zero", classroomId = classroomId, continuousAssessment = 0.0, testScore = 0.0, examScore = 0.0),
            // Student 3: Pending/Missing grades (null)
            GradeRecord(studentId = "s_missing", classroomId = classroomId, continuousAssessment = null, testScore = null, examScore = null)
        )

        val state = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "قسم التجربة")),
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )

        // Evaluated students: s_high (18.0) and s_zero (0.0)
        // Evaluated count = 2
        // Average = (18.0 + 0.0) / 2 = 9.0
        val avg = state.getClassroomAverage(classroomId)
        assertEquals(9.0, avg, 0.001)

        // Verify that s_missing was not counted as a second 0.0: (18 + 0 + 0) / 3 = 6.0
        assertFalse("Missing student must not be counted as a zero divisor", Math.abs(avg - 6.0) < 0.1)

        // Verify that s_zero was NOT erroneously filtered out as null: (18) / 1 = 18.0
        assertFalse("Explicit zero student must be included in the classroom average", Math.abs(avg - 18.0) < 0.1)
    }

    @Test
    fun testClassroomAverageWithPartialComponentsAcrossStudents() {
        val classroomId = "c_partial"
        val students = listOf(
            Student(id = "s_a", classroomId = classroomId, firstName = "أ", lastName = "1"),
            Student(id = "s_b", classroomId = classroomId, firstName = "ب", lastName = "2"),
            Student(id = "s_c", classroomId = classroomId, firstName = "ج", lastName = "3")
        )

        val grades = listOf(
            // Student A: Continuous = 14.0, Test = 16.0, Exam = null -> avg = (14 + 16)/2 = 15.0
            GradeRecord(studentId = "s_a", classroomId = classroomId, continuousAssessment = 14.0, testScore = 16.0, examScore = null),
            // Student B: Continuous = 18.0, Test = 14.0, Exam = 17.0 -> DevoirAvg = 16.0, Weighted = (16 + 34)/3 = 16.67
            GradeRecord(studentId = "s_b", classroomId = classroomId, continuousAssessment = 18.0, testScore = 14.0, examScore = 17.0),
            // Student C: null scores
            GradeRecord(studentId = "s_c", classroomId = classroomId, continuousAssessment = null, testScore = null, examScore = null)
        )

        val state = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "قسم متعدد")),
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )

        // Evaluated students: s_a (15.0) and s_b (16.67)
        // Classroom Average = (15.0 + 16.67) / 2 = 31.67 / 2 = 15.835 -> 15.84
        val computedClassAvg = state.getClassroomAverage(classroomId)
        assertEquals(15.84, computedClassAvg, 0.01)
    }

    @Test
    fun testMultipleClassroomsIsolationAndAccuracy() {
        val c1Id = "c_4m1"
        val c2Id = "c_3m2"
        val c3Id = "c_2m1"

        val students = listOf(
            Student(id = "s1", classroomId = c1Id, firstName = "طالب 1", lastName = "ع1"),
            Student(id = "s2", classroomId = c1Id, firstName = "طالب 2", lastName = "ع1"),
            Student(id = "s3", classroomId = c2Id, firstName = "طالب 3", lastName = "ع2"),
            Student(id = "s4", classroomId = c2Id, firstName = "طالب 4", lastName = "ع2"),
            Student(id = "s5", classroomId = c3Id, firstName = "طالب 5", lastName = "ع3") // completely unentered
        )

        val grades = listOf(
            GradeRecord(studentId = "s1", classroomId = c1Id, continuousAssessment = 12.0, testScore = 12.0, examScore = 12.0), // avg = 12.0
            GradeRecord(studentId = "s2", classroomId = c1Id, continuousAssessment = 16.0, testScore = 16.0, examScore = 16.0), // avg = 16.0 -> c1 avg = 14.0
            GradeRecord(studentId = "s3", classroomId = c2Id, continuousAssessment = 15.0, testScore = 15.0, examScore = 15.0), // avg = 15.0
            // s4 in c2 has no grades
            // s5 in c3 has no grades
            GradeRecord(studentId = "s5", classroomId = c3Id, continuousAssessment = null, testScore = null, examScore = null)
        )

        val state = OstadiUiState(
            classrooms = listOf(
                Classroom(id = c1Id, name = "4 متوسط 1"),
                Classroom(id = c2Id, name = "3 متوسط 2"),
                Classroom(id = c3Id, name = "2 متوسط 1")
            ),
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )

        // C1: (12.0 + 16.0) / 2 = 14.0
        assertEquals(14.0, state.getClassroomAverage(c1Id), 0.001)

        // C2: s3 has 15.0, s4 has null -> Average must be 15.0 (NOT 7.5!)
        assertEquals(15.0, state.getClassroomAverage(c2Id), 0.001)

        // C3: No graded students -> 0.0
        assertEquals(0.0, state.getClassroomAverage(c3Id), 0.001)
    }

    @Test
    fun testEmptyClassroomAndNonExistentClassroomIdReturnsSafelyWithoutCrash() {
        val state = OstadiUiState(
            classrooms = listOf(Classroom(id = "empty_class", name = "قسم فارغ")),
            students = emptyList(),
            gradeRecords = emptyList()
        )

        // No students in empty_class
        assertEquals(0.0, state.getClassroomAverage("empty_class"), 0.0)

        // Non-existent ID
        assertEquals(0.0, state.getClassroomAverage("does_not_exist"), 0.0)

        // Completely empty state
        val completelyEmpty = OstadiUiState()
        assertEquals(0.0, completelyEmpty.getClassroomAverage("any_id"), 0.0)
    }

    @Test
    fun testFallbackToStudentStaticTermAverageWhenNoGradeRecordExists() {
        val classroomId = "c_fallback"
        val students = listOf(
            // Student has a termAverage set on their profile record directly
            Student(id = "s_static", classroomId = classroomId, firstName = "فاروق", lastName = "زياني", termAverage = 13.5),
            // Student has no termAverage and no GradeRecord
            Student(id = "s_unassigned", classroomId = classroomId, firstName = "نبيل", lastName = "عمار", termAverage = null)
        )

        val state = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "قسم الاحتياط")),
            students = students,
            gradeRecords = emptyList(), // No dynamic GradeRecord
            selectedTerm = AcademicTerm.TERM_1
        )

        // s_static should resolve to 13.5
        assertEquals(13.5, state.getStudentAverage("s_static") ?: 0.0, 0.001)

        // s_unassigned should resolve to null
        assertNull(state.getStudentAverage("s_unassigned"))

        // Classroom average should be 13.5 (only s_static is evaluated; s_unassigned excluded)
        assertEquals(13.5, state.getClassroomAverage(classroomId), 0.001)
    }

    @Test
    fun testDynamicGradeRecordTakesPrecedenceOverStaticTermAverage() {
        val classroomId = "c_precedence"
        val students = listOf(
            Student(id = "s1", classroomId = classroomId, firstName = "حسام", lastName = "مقداد", termAverage = 10.0)
        )

        // Dynamic record has 16.0
        val grades = listOf(
            GradeRecord(studentId = "s1", classroomId = classroomId, continuousAssessment = 16.0, testScore = 16.0, examScore = 16.0)
        )

        val state = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "قسم الأولوية")),
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )

        // Must resolve to dynamic record's 16.0, not static 10.0
        assertEquals(16.0, state.getStudentAverage("s1") ?: 0.0, 0.001)
        assertEquals(16.0, state.getClassroomAverage(classroomId), 0.001)
    }

    // =========================================================================
    // SECTION 3: GENERAL AVERAGE & SUCCESS RATE CALCULATIONS
    // =========================================================================

    @Test
    fun testGeneralAverageScoreAcrossAllStudentsExcludesMissingGrades() {
        val students = listOf(
            Student(id = "s1", classroomId = "c1", firstName = "ط1", lastName = "1"),
            Student(id = "s2", classroomId = "c1", firstName = "ط2", lastName = "2"), // missing
            Student(id = "s3", classroomId = "c2", firstName = "ط3", lastName = "3"),
            Student(id = "s4", classroomId = "c2", firstName = "ط4", lastName = "4"), // missing
            Student(id = "s5", classroomId = "c3", firstName = "ط5", lastName = "5")
        )

        val grades = listOf(
            GradeRecord(studentId = "s1", classroomId = "c1", continuousAssessment = 15.0, testScore = 15.0, examScore = 15.0), // 15.0
            GradeRecord(studentId = "s3", classroomId = "c2", continuousAssessment = 17.0, testScore = 17.0, examScore = 17.0), // 17.0
            GradeRecord(studentId = "s5", classroomId = "c3", continuousAssessment = 10.0, testScore = 10.0, examScore = 10.0)  // 10.0
        )

        val state = OstadiUiState(
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )

        // Graded sum = 15.0 + 17.0 + 10.0 = 42.0
        // Graded count = 3 (s2 and s4 excluded)
        // General Average = 42.0 / 3 = 14.0
        assertEquals(14.0, state.generalAverageScore, 0.001)

        // Anti-Regression: If s2 and s4 were converted to 0.0: 42.0 / 5 = 8.4
        assertFalse(
            "General average must not be deflated to 8.4 by unentered students",
            Math.abs(state.generalAverageScore - 8.4) < 0.1
        )
    }

    @Test
    fun testSuccessRatePercentageUsesGradedStudentsDenominatorOnly() {
        // 5 total students:
        // S1: 15.0 (Passed)
        // S2: 12.0 (Passed)
        // S3: 8.0 (Failed)
        // S4: missing/null
        // S5: missing/null
        val students = listOf(
            Student(id = "s1", classroomId = "c1", firstName = "ن1", lastName = "1"),
            Student(id = "s2", classroomId = "c1", firstName = "ن2", lastName = "2"),
            Student(id = "s3", classroomId = "c1", firstName = "ن3", lastName = "3"),
            Student(id = "s4", classroomId = "c1", firstName = "ن4", lastName = "4"),
            Student(id = "s5", classroomId = "c1", firstName = "ن5", lastName = "5")
        )

        val grades = listOf(
            GradeRecord(studentId = "s1", continuousAssessment = 15.0, testScore = 15.0, examScore = 15.0),
            GradeRecord(studentId = "s2", continuousAssessment = 12.0, testScore = 12.0, examScore = 12.0),
            GradeRecord(studentId = "s3", continuousAssessment = 8.0, testScore = 8.0, examScore = 8.0)
            // s4 and s5 have no records
        )

        val state = OstadiUiState(
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )

        // Graded students = 3 (s1, s2, s3)
        // Passed students (>= 10.0) = 2 (s1, s2)
        // Success Rate = (2 / 3) * 100 = 66%
        assertEquals(66, state.successRatePercentage)

        // Anti-Regression: If s4 and s5 were coerced to 0.0 (and treated as failed):
        // Passed = 2, Total = 5 -> (2 / 5) * 100 = 40%
        assertFalse(
            "Success rate must evaluate against graded students only, not 40%",
            state.successRatePercentage == 40
        )
    }

    @Test
    fun testSuccessRateWhenNoStudentsAreGradedReturnsZeroWithoutDivisionByZero() {
        val state = OstadiUiState(
            students = listOf(
                Student(id = "s1", firstName = "أ", lastName = "1"),
                Student(id = "s2", firstName = "ب", lastName = "2")
            ),
            gradeRecords = emptyList()
        )
        assertEquals(0, state.successRatePercentage)
    }

    // =========================================================================
    // SECTION 4: MULTI-TERM ISOLATION
    // =========================================================================

    @Test
    fun testTermSwitchingRecalculatesAveragesForSpecificTermWithoutBleed() {
        val classroomId = "c_terms"
        val students = listOf(
            Student(id = "s1", classroomId = classroomId, firstName = "طالب 1", lastName = "ل1"),
            Student(id = "s2", classroomId = classroomId, firstName = "طالب 2", lastName = "ل2")
        )

        val grades = listOf(
            // S1 has Term 1 and Term 2
            GradeRecord(studentId = "s1", classroomId = classroomId, term = AcademicTerm.TERM_1, continuousAssessment = 14.0, testScore = 14.0, examScore = 14.0), // 14.0
            GradeRecord(studentId = "s1", classroomId = classroomId, term = AcademicTerm.TERM_2, continuousAssessment = 16.0, testScore = 16.0, examScore = 16.0), // 16.0

            // S2 has Term 1 only (Term 2 is pending/missing)
            GradeRecord(studentId = "s2", classroomId = classroomId, term = AcademicTerm.TERM_1, continuousAssessment = 18.0, testScore = 18.0, examScore = 18.0)  // 18.0
        )

        // In TERM_1: S1 (14.0), S2 (18.0) -> Average = (14.0 + 18.0) / 2 = 16.0
        val stateTerm1 = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "قسم الفصول")),
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )
        assertEquals(16.0, stateTerm1.getClassroomAverage(classroomId), 0.001)

        // In TERM_2: S1 (16.0), S2 is MISSING (null) -> Average = 16.0 (NOT 8.0!)
        val stateTerm2 = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "قسم الفصول")),
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_2
        )
        assertEquals(16.0, stateTerm2.getClassroomAverage(classroomId), 0.001)
        assertFalse(
            "Term 2 average must not be deflated to 8.0 because S2 lacks Term 2 grades",
            Math.abs(stateTerm2.getClassroomAverage(classroomId) - 8.0) < 0.1
        )

        // In TERM_3: Both missing -> Average = 0.0
        val stateTerm3 = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "قسم الفصول")),
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_3
        )
        assertEquals(0.0, stateTerm3.getClassroomAverage(classroomId), 0.0)
    }

    // =========================================================================
    // SECTION 5: ROBOLECTRIC CONTEXT & SAMPLE REPOSITORY INTEGRATION
    // =========================================================================

    @Test
    fun testRobolectricContextAndStringFormatting() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Ostadi – أستاذي", appName)

        val classroomId = "c_format"
        val state = OstadiUiState(
            classrooms = listOf(Classroom(id = classroomId, name = "4M1")),
            students = listOf(
                Student(id = "s1", classroomId = classroomId, firstName = "كريم", lastName = "عمر"),
                Student(id = "s2", classroomId = classroomId, firstName = "فاطمة", lastName = "زيد")
            ),
            gradeRecords = listOf(
                GradeRecord(studentId = "s1", classroomId = classroomId, continuousAssessment = 15.33, testScore = 15.33, examScore = 15.33)
                // s2 is missing
            )
        )

        val avg = state.getClassroomAverage(classroomId)
        assertEquals(15.33, avg, 0.001)

        // Verify Arabic and English formatting
        val formattedUs = String.format(Locale.US, "%.2f", avg)
        assertEquals("15.33", formattedUs)

        val strings = AppStrings.get(AppLanguage.ARABIC)
        assertTrue(strings.calculatedAverage.isNotBlank())
        assertTrue(strings.classroomAverages.isNotBlank())
    }

    @Test
    fun testSampleTeacherRepositoryDataMaintainsStrictAverageValidity() = runBlocking {
        val sampleRepo = SampleTeacherRepository()
        val classrooms = sampleRepo.getClassrooms().first()
        val students = sampleRepo.getStudents().first()
        val grades = sampleRepo.getGradeRecords().first()

        assertTrue("Sample repository must provide default classrooms", classrooms.isNotEmpty())
        assertTrue("Sample repository must provide default students", students.isNotEmpty())

        val state = OstadiUiState(
            classrooms = classrooms,
            students = students,
            gradeRecords = grades,
            selectedTerm = AcademicTerm.TERM_1
        )

        // For every classroom, verify that its computed average is within valid [0.0, 20.0] range
        for (classroom in classrooms) {
            val classAvg = state.getClassroomAverage(classroom.id)
            assertTrue("Classroom average for ${classroom.name} must be >= 0.0", classAvg >= 0.0)
            assertTrue("Classroom average for ${classroom.name} must be <= 20.0", classAvg <= 20.0)
            assertFalse("Classroom average must not be NaN", classAvg.isNaN())
            assertFalse("Classroom average must not be Infinite", classAvg.isInfinite())
        }

        // Verify general average score
        val genAvg = state.generalAverageScore
        assertTrue("General average must be >= 0.0", genAvg >= 0.0)
        assertTrue("General average must be <= 20.0", genAvg <= 20.0)
        assertFalse("General average must not be NaN", genAvg.isNaN())
    }
}
