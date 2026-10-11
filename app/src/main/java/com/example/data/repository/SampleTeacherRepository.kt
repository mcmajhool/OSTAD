package com.example.data.repository

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * SampleTeacherRepository provides realistic Algerian school demo data for previewing the UI.
 * This class is decoupled from the UI via the TeacherRepository interface, making it
 * straightforward to swap with Firebase Firestore (FirestoreTeacherRepository) when
 * Firebase is configured.
 */
class SampleTeacherRepository : TeacherRepository {

    private val _profile = MutableStateFlow(
        TeacherProfile(
            id = "teacher_demo_01",
            firstName = "محمد أمين",
            lastName = "بن سالم",
            email = "m.bensalem@education.gov.dz",
            schoolName = "متوسطة الشهيد العربي بن مهيدي",
            wilaya = "16 - الجزائر العاصمة",
            educationalLevel = "التعليم المتوسط (CEM)",
            subject = "مادة الرياضيات",
            academicYear = "2024 / 2025"
        )
    )

    private val _classrooms = MutableStateFlow(
        listOf(
            Classroom(id = "c1", name = "4 متوسط 1 (BEM)", level = "السنة الرابعة متوسط", roomNumber = "القاعة 04", studentCount = 32, averageScore = 14.85),
            Classroom(id = "c2", name = "4 متوسط 2", level = "السنة الرابعة متوسط", roomNumber = "القاعة 06", studentCount = 30, averageScore = 13.20),
            Classroom(id = "c3", name = "3 متوسط 1", level = "السنة الثالثة متوسط", roomNumber = "القاعة 02", studentCount = 28, averageScore = 15.10),
            Classroom(id = "c4", name = "2 متوسط 3", level = "السنة الثانية متوسط", roomNumber = "القاعة 09", studentCount = 34, averageScore = 12.75)
        )
    )

    private val _students = MutableStateFlow(
        listOf(
            Student("s1", "c1", "4 متوسط 1 (BEM)", "ياسين", "بوزيد", Gender.MALE, "2024/401", "0661234567", 17.5, 0, 5, "تلميذ متميز ومنضبط جداً"),
            Student("s2", "c1", "4 متوسط 1 (BEM)", "مريم", "زروقي", Gender.FEMALE, "2024/402", "0550987654", 18.25, 0, 5, "الأولى على الدفعة، استيعاب سريع"),
            Student("s3", "c1", "4 متوسط 1 (BEM)", "أمين", "بلحاج", Gender.MALE, "2024/403", "0771122334", 14.0, 1, 4, "مشاركة جيدة ويحتاج تعزيز الهندسة"),
            Student("s4", "c1", "4 متوسط 1 (BEM)", "إيمان", "مزيان", Gender.FEMALE, "2024/404", "0662334455", 15.5, 0, 4, "حضور دائم واهتمام بالواجبات"),
            Student("s5", "c1", "4 متوسط 1 (BEM)", "وليد", "دحماني", Gender.MALE, "2024/405", "0553445566", 9.75, 3, 2, "يحتاج إلى دعم وتكثيف التمارين"),
            Student("s6", "c1", "4 متوسط 1 (BEM)", "نور الهدى", "رحماني", Gender.FEMALE, "2024/406", "0774556677", 16.0, 0, 4, "مستوى متقدم وخط منظم"),
            Student("s7", "c2", "4 متوسط 2", "عبد الرؤوف", "قادري", Gender.MALE, "2024/421", "0665667788", 12.5, 1, 3, "مستوى متوسط"),
            Student("s8", "c2", "4 متوسط 2", "سارة", "شريفي", Gender.FEMALE, "2024/422", "0556778899", 16.5, 0, 4, "ممتازة في الحساب الحرفي"),
            Student("s9", "c2", "4 متوسط 2", "حمزة", "عماري", Gender.MALE, "2024/423", "0777889900", 11.0, 2, 3, "كثير الحركة، لكنه ذكي"),
            Student("s10", "c3", "3 متوسط 1", "ريان", "بوجمعة", Gender.MALE, "2024/301", "0668990011", 15.75, 0, 4, "تفاعل ممتاز في القسم"),
            Student("s11", "c3", "3 متوسط 1", "خديجة", "بن عيسى", Gender.FEMALE, "2024/302", "0559001122", 17.0, 0, 5, "تلميذة نجيبة ومتفوقة"),
            Student("s12", "c4", "2 متوسط 3", "إسحاق", "سعدي", Gender.MALE, "2024/201", "0770112233", 13.0, 1, 3, "تحسن ملحوظ في الفصل الحالي")
        )
    )

    private val _attendanceRecords = MutableStateFlow(
        listOf(
            AttendanceRecord("att1", "s5", "دحماني وليد", "c1", "2024-10-11", "الحصة 1 (08:00 - 09:00)", AttendanceStatus.ABSENT, "غياب غير مبرر"),
            AttendanceRecord("att2", "s9", "عماري حمزة", "c2", "2024-10-11", "الحصة 2 (09:00 - 10:00)", AttendanceStatus.LATE, "تأخر 15 دقيقة"),
            AttendanceRecord("att3", "s7", "قادري عبد الرؤوف", "c2", "2024-10-10", "الحصة 3 (10:00 - 11:00)", AttendanceStatus.EXCUSED, "شهادة طبية معتمدة")
        )
    )

    private val _gradeRecords = MutableStateFlow(
        listOf(
            GradeRecord("g1", "s1", "بوزيد ياسين", "c1", AcademicTerm.TERM_1, continuousAssessment = 18.0, testScore = 17.0, examScore = 17.5, remarks = "ممتاز واصل العمل"),
            GradeRecord("g2", "s2", "زروقي مريم", "c1", AcademicTerm.TERM_1, continuousAssessment = 19.0, testScore = 18.5, examScore = 18.0, remarks = "أداء استثنائي وتفوق دائم"),
            GradeRecord("g3", "s3", "بلحاج أمين", "c1", AcademicTerm.TERM_1, continuousAssessment = 15.0, testScore = 13.5, examScore = 14.0, remarks = "جيد، راجع حل المسائل"),
            GradeRecord("g4", "s4", "مزيان إيمان", "c1", AcademicTerm.TERM_1, continuousAssessment = 16.5, testScore = 15.0, examScore = 15.5, remarks = "تلميذة مجتهدة ومواظبة"),
            GradeRecord("g5", "s5", "دحماني وليد", "c1", AcademicTerm.TERM_1, continuousAssessment = 11.0, testScore = 09.0, examScore = 09.5, remarks = "عليك التركيز ومراجعة الدروس"),
            GradeRecord("g6", "s6", "رحماني نور الهدى", "c1", AcademicTerm.TERM_1, continuousAssessment = 17.0, testScore = 16.0, examScore = 15.5, remarks = "عمل منظم جداً"),
            GradeRecord("g7", "s7", "قادري عبد الرؤوف", "c2", AcademicTerm.TERM_1, continuousAssessment = 13.0, testScore = 12.0, examScore = 12.5, remarks = "مقبول ولديك إمكانيات أفضل"),
            GradeRecord("g8", "s8", "شريفي سارة", "c2", AcademicTerm.TERM_1, continuousAssessment = 17.5, testScore = 16.0, examScore = 16.5, remarks = "ممتازة وإجابات دقيقة")
        )
    )

    private val _participationRecords = MutableStateFlow(
        listOf(
            ParticipationRecord("p1", "s2", "زروقي مريم", "c1", 5, "2024-10-10", "إجابة نموذجية على مسألة طاليس", positiveRemark = true),
            ParticipationRecord("p2", "s1", "بوزيد ياسين", "c1", 5, "2024-10-09", "حل تمرين الحساب الحرفي على السبورة", positiveRemark = true),
            ParticipationRecord("p3", "s3", "بلحاج أمين", "c1", 4, "2024-10-08", "مبادرة جيدة في العمل الفوجي", positiveRemark = true),
            ParticipationRecord("p4", "s5", "دحماني وليد", "c1", 2, "2024-10-07", "تشتت الانتباه أثناء الشرح", positiveRemark = false)
        )
    )

    private val _upcomingExams = MutableStateFlow(
        listOf(
            ExamEvent("e1", "الفرض الأول للفصل الأول", "4 متوسط 1 (BEM)", "2024-10-22", "09:00 - 10:00", AcademicTerm.TERM_1, "القاعة 04"),
            ExamEvent("e2", "الفرض الأول للفصل الأول", "4 متوسط 2", "2024-10-23", "10:00 - 11:00", AcademicTerm.TERM_1, "القاعة 06"),
            ExamEvent("e3", "الفرض المحروس لمادة الرياضيات", "3 متوسط 1", "2024-10-24", "08:00 - 09:00", AcademicTerm.TERM_1, "القاعة 02"),
            ExamEvent("e4", "اختبار الفصل الأول الرسمي", "4 متوسط 1 (BEM)", "2024-12-01", "08:30 - 10:30", AcademicTerm.TERM_1, "مدرج الامتحانات")
        )
    )

    override fun getTeacherProfile(): Flow<TeacherProfile> = _profile.asStateFlow()

    override suspend fun updateTeacherProfile(profile: TeacherProfile) {
        _profile.value = profile
    }

    override fun getClassrooms(): Flow<List<Classroom>> = _classrooms.asStateFlow()

    override suspend fun addClassroom(classroom: Classroom) {
        _classrooms.update { current -> listOf(classroom) + current }
    }

    override fun getStudents(): Flow<List<Student>> = _students.asStateFlow()

    override suspend fun addStudent(student: Student) {
        _students.update { current -> listOf(student) + current }
        // Update student count in classroom
        _classrooms.update { classes ->
            classes.map { c ->
                if (c.id == student.classroomId) c.copy(studentCount = c.studentCount + 1) else c
            }
        }
    }

    override fun getAttendanceRecords(): Flow<List<AttendanceRecord>> = _attendanceRecords.asStateFlow()

    override suspend fun saveAttendance(records: List<AttendanceRecord>) {
        _attendanceRecords.update { current -> records + current }
        // Update students' total absences if absent
        val absentStudentIds = records.filter { it.status == AttendanceStatus.ABSENT }.map { it.studentId }.toSet()
        if (absentStudentIds.isNotEmpty()) {
            _students.update { list ->
                list.map { st ->
                    if (st.id in absentStudentIds) st.copy(totalAbsences = st.totalAbsences + 1) else st
                }
            }
        }
    }

    override fun getGradeRecords(): Flow<List<GradeRecord>> = _gradeRecords.asStateFlow()

    override suspend fun saveGrade(record: GradeRecord) {
        _gradeRecords.update { current ->
            val exists = current.any { it.id == record.id || (it.studentId == record.studentId && it.term == record.term) }
            if (exists) {
                current.map { if (it.id == record.id || (it.studentId == record.studentId && it.term == record.term)) record else it }
            } else {
                listOf(record) + current
            }
        }
    }

    override fun getParticipationRecords(): Flow<List<ParticipationRecord>> = _participationRecords.asStateFlow()

    override suspend fun updateParticipation(studentId: String, starsDelta: Int, note: String) {
        _students.update { list ->
            list.map { st ->
                if (st.id == studentId) {
                    val newStars = (st.participationStars + starsDelta).coerceIn(0, 5)
                    st.copy(participationStars = newStars)
                } else st
            }
        }
        val targetStudent = _students.value.firstOrNull { it.id == studentId }
        if (targetStudent != null) {
            val record = ParticipationRecord(
                id = "p_${System.currentTimeMillis()}",
                studentId = studentId,
                studentName = targetStudent.fullName,
                classroomId = targetStudent.classroomId,
                stars = targetStudent.participationStars,
                date = "اليوم",
                note = note.ifBlank { if (starsDelta > 0) "إضافة نقطة تميز ومشاركة" else "تنبيه حول الانضباط" },
                positiveRemark = starsDelta >= 0
            )
            _participationRecords.update { listOf(record) + it }
        }
    }

    override fun getUpcomingExams(): Flow<List<ExamEvent>> = _upcomingExams.asStateFlow()
}
