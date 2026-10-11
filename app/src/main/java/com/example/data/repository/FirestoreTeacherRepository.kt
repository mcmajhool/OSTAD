package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceStatus
import com.example.data.model.Classroom
import com.example.data.model.ExamEvent
import com.example.data.model.GradeRecord
import com.example.data.model.ParticipationRecord
import com.example.data.model.Student
import com.example.data.model.TeacherProfile
import com.example.data.util.OperationType
import com.example.data.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreTeacherRepository(
    private val db: FirebaseFirestore
) : TeacherRepository {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireTeacherUid(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("Teacher must be authenticated with Firebase.")
    }

    override fun getTeacherProfile(): Flow<TeacherProfile> = callbackFlow {
        val uid = requireTeacherUid()
        val docRef = db.collection("teachers").document(uid)

        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.GET, docRef.path)
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val profile = snapshot.toObject(TeacherProfile::class.java)
                if (profile != null) {
                    trySend(profile)
                }
            } else {
                val currentUser = auth.currentUser
                val names = (currentUser?.displayName ?: "أستاذ جزائري").split(" ")
                val fName = if (names.size > 1) names.drop(1).joinToString(" ") else names.firstOrNull() ?: "أستاذ"
                val lName = names.firstOrNull() ?: "الجزائري"

                val defaultProfile = TeacherProfile(
                    id = uid,
                    firstName = fName,
                    lastName = lName,
                    email = currentUser?.email ?: "",
                    schoolName = "المؤسسة التعليمية",
                    educationalLevel = "التعليم المتوسط (CEM)",
                    accountCreationDate = Timestamp.now(),
                    wilaya = "16 - الجزائر العاصمة",
                    subject = "مادة الرياضيات"
                )
                docRef.set(defaultProfile)
                trySend(defaultProfile)
            }
        }

        awaitClose { registration.remove() }
    }

    override suspend fun updateTeacherProfile(profile: TeacherProfile) {
        val uid = requireTeacherUid()
        val docRef = db.collection("teachers").document(uid)
        try {
            docRef.set(profile.copy(id = uid)).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    override fun getClassrooms(): Flow<List<Classroom>> = callbackFlow {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("classrooms")

        val registration = colRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, colRef.path)
                close(error)
                return@addSnapshotListener
            }

            val list = snapshot?.documents?.mapNotNull { it.toObject(Classroom::class.java) } ?: emptyList()
            trySend(list)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun addClassroom(classroom: Classroom) {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("classrooms")
        try {
            val docId = classroom.id.ifBlank { "c_${System.currentTimeMillis()}" }
            colRef.document(docId).set(classroom.copy(id = docId)).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, colRef.path)
            throw e
        }
    }

    override fun getStudents(): Flow<List<Student>> = callbackFlow {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("students")

        val registration = colRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, colRef.path)
                close(error)
                return@addSnapshotListener
            }

            val list = snapshot?.documents?.mapNotNull { it.toObject(Student::class.java) } ?: emptyList()
            trySend(list)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun addStudent(student: Student) {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("students")
        try {
            val docId = student.id.ifBlank { "s_${System.currentTimeMillis()}" }
            colRef.document(docId).set(student.copy(id = docId)).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, colRef.path)
            throw e
        }
    }

    override fun getAttendanceRecords(): Flow<List<AttendanceRecord>> = callbackFlow {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("attendance")

        val registration = colRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, colRef.path)
                close(error)
                return@addSnapshotListener
            }
            val list = snapshot?.documents?.mapNotNull { it.toObject(AttendanceRecord::class.java) } ?: emptyList()
            trySend(list)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun saveAttendance(records: List<AttendanceRecord>) {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("attendance")
        try {
            val batch = db.batch()
            for (rec in records) {
                val docId = rec.id.ifBlank { "att_${System.currentTimeMillis()}_${rec.studentId}" }
                batch.set(colRef.document(docId), rec.copy(id = docId))
            }
            batch.commit().await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, colRef.path)
            throw e
        }
    }

    override fun getGradeRecords(): Flow<List<GradeRecord>> = callbackFlow {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("grades")

        val registration = colRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, colRef.path)
                close(error)
                return@addSnapshotListener
            }
            val list = snapshot?.documents?.mapNotNull { it.toObject(GradeRecord::class.java) } ?: emptyList()
            trySend(list)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun saveGrade(record: GradeRecord) {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("grades")
        try {
            val docId = record.id.ifBlank { "grade_${record.studentId}_${record.term.name}" }
            colRef.document(docId).set(record.copy(id = docId)).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, colRef.path)
            throw e
        }
    }

    override fun getParticipationRecords(): Flow<List<ParticipationRecord>> = callbackFlow {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("participation")

        val registration = colRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, colRef.path)
                close(error)
                return@addSnapshotListener
            }
            val list = snapshot?.documents?.mapNotNull { it.toObject(ParticipationRecord::class.java) } ?: emptyList()
            trySend(list)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun updateParticipation(studentId: String, starsDelta: Int, note: String) {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("participation")
        try {
            val recId = "p_${System.currentTimeMillis()}"
            val record = ParticipationRecord(
                id = recId,
                studentId = studentId,
                studentName = "تلميذ",
                classroomId = "",
                stars = starsDelta.coerceIn(0, 5),
                date = "اليوم",
                note = note,
                positiveRemark = starsDelta >= 0
            )
            colRef.document(recId).set(record).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, colRef.path)
            throw e
        }
    }

    override fun getUpcomingExams(): Flow<List<ExamEvent>> = callbackFlow {
        val uid = requireTeacherUid()
        val colRef = db.collection("teachers").document(uid).collection("exams")

        val registration = colRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, colRef.path)
                close(error)
                return@addSnapshotListener
            }
            val list = snapshot?.documents?.mapNotNull { it.toObject(ExamEvent::class.java) } ?: emptyList()
            trySend(list)
        }

        awaitClose { registration.remove() }
    }
}
