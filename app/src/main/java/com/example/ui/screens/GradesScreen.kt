package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicTerm
import com.example.data.model.GradeRecord
import com.example.data.model.Student
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.CrimsonAbsence
import com.example.ui.theme.CrimsonContainer
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldLight
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.OstadiUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(
    uiState: OstadiUiState,
    onSelectTerm: (AcademicTerm) -> Unit,
    onSelectClassroom: (String?) -> Unit,
    onOpenEditGrade: (Student) -> Unit,
    onCloseEditGrade: () -> Unit,
    onSaveGrade: (studentId: String, continuous: Double?, test: Double?, exam: Double?, remarks: String) -> Unit
) {
    val s = AppStrings.get(uiState.currentLanguage)
    val isAr = uiState.currentLanguage == AppLanguage.ARABIC

    val terms = listOf(
        AcademicTerm.TERM_1 to s.term1,
        AcademicTerm.TERM_2 to s.term2,
        AcademicTerm.TERM_3 to s.term3
    )

    val currentTermIndex = terms.indexOfFirst { it.first == uiState.selectedTerm }.coerceAtLeast(0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Trimester Selector Tabs
        item {
            PrimaryTabRow(
                selectedTabIndex = currentTermIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = GoldAccent,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .testTag("grades_term_tabs")
            ) {
                terms.forEachIndexed { index, (term, label) ->
                    Tab(
                        selected = currentTermIndex == index,
                        onClick = { onSelectTerm(term) },
                        text = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (currentTermIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Classroom Filters
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedClassroomId == null,
                        onClick = { onSelectClassroom(null) },
                        label = { Text(s.allClassrooms) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = Color(0xFF1A1305)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                items(uiState.classrooms) { classroom ->
                    val isSelected = uiState.selectedClassroomId == classroom.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectClassroom(classroom.id) },
                        label = { Text(classroom.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = Color(0xFF1A1305)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Algerian Official Calculation Formula Note
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = NavyContainer.copy(alpha = 0.7f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            text = if (isAr) "سلم التنقيط وحساب المعدل الوزاري الجزائري:" else "Barème et formule de moyenne officielle :",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                        Text(
                            text = if (isAr)
                                "المعدل = [ (التقويم المستمر + الفرض) ÷ 2 + (الاختبار × 2) ] ÷ 3 (دون تحويل النقاط غير المرصودة إلى صفر)"
                            else
                                "Moyenne = [ (Évaluation + Devoir) / 2 + (Composition x 2) ] / 3 (notes manquantes non comptées à zéro)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = s.gradesTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${uiState.filteredStudents.size} ${s.studentsCountSuffix}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Grades Student Rows
        if (uiState.filteredStudents.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isAr) "لا يوجد تلاميذ لعرض نقاطهم" else "Aucun élève disponible",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (isAr) "قم بإضافة فوج أو تلاميذ لبدء رصد النقاط" else "Ajoutez une classe ou des élèves pour commencer",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        } else {
            items(uiState.filteredStudents) { student ->
                val gradeRecord = uiState.getStudentGradeRecord(student.id)

                GradeStudentCard(
                    student = student,
                    gradeRecord = gradeRecord,
                    language = uiState.currentLanguage,
                    onEdit = { onOpenEditGrade(student) }
                )
            }
        }
    }

    // Edit Grade Dialog
    if (uiState.showAddGradeDialog && uiState.selectedStudentForGrade != null) {
        val student = uiState.selectedStudentForGrade!!
        val existingRecord = uiState.getStudentGradeRecord(student.id)

        EditGradeDialog(
            student = student,
            existingRecord = existingRecord,
            term = uiState.selectedTerm,
            language = uiState.currentLanguage,
            onDismiss = onCloseEditGrade,
            onConfirm = { cont, test, exam, remarks ->
                onSaveGrade(student.id, cont, test, exam, remarks)
            }
        )
    }
}

@Composable
private fun GradeStudentCard(
    student: Student,
    gradeRecord: GradeRecord?,
    language: AppLanguage,
    onEdit: () -> Unit
) {
    val s = AppStrings.get(language)
    val isAr = language == AppLanguage.ARABIC
    val avg = gradeRecord?.calculatedAverage

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .testTag("grade_card_${student.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = student.fullName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = student.classroomName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (avg != null) {
                        Surface(
                            color = if (avg >= 14.0) EmeraldContainer else if (avg >= 10.0) GoldContainer else CrimsonContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "$avg / 20",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (avg >= 14.0) EmeraldSuccess else if (avg >= 10.0) Color(0xFF7A4A00) else CrimsonAbsence
                            )
                        }
                    } else {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (isAr) "غير مرصود" else "Non évalué",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = GoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Score components breakdown
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ScoreBadge(title = if (isAr) "التقويم" else "Éval.", score = gradeRecord?.continuousAssessment)
                    ScoreBadge(title = if (isAr) "الفرض" else "Devoir", score = gradeRecord?.testScore)
                    ScoreBadge(title = if (isAr) "الاختبار (×2)" else "Compo (×2)", score = gradeRecord?.examScore)
                    ScoreBadge(title = if (isAr) "التقدير" else "Mention", text = gradeRecord?.mention ?: (if (isAr) "قيد الرصد" else "En cours"))
                }
            }

            if (!gradeRecord?.remarks.isNullOrBlank()) {
                Text(
                    text = "📝 ${gradeRecord!!.remarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun ScoreBadge(title: String, score: Double? = null, text: String? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
        Text(
            text = text ?: (if (score != null) "$score" else "--"),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = if (score != null || text != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun EditGradeDialog(
    student: Student,
    existingRecord: GradeRecord?,
    term: AcademicTerm,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onConfirm: (continuous: Double?, test: Double?, exam: Double?, remarks: String) -> Unit
) {
    val s = AppStrings.get(language)
    val isAr = language == AppLanguage.ARABIC

    var continuousStr by remember { mutableStateOf(existingRecord?.continuousAssessment?.toString() ?: "") }
    var testStr by remember { mutableStateOf(existingRecord?.testScore?.toString() ?: "") }
    var examStr by remember { mutableStateOf(existingRecord?.examScore?.toString() ?: "") }
    var remarks by remember { mutableStateOf(existingRecord?.remarks ?: "") }

    val cVal = continuousStr.toDoubleOrNull()
    val tVal = testStr.toDoubleOrNull()
    val eVal = examStr.toDoubleOrNull()

    // Live preview average with dynamic adaptive Algerian formula
    val calculatedPreview = remember(cVal, tVal, eVal) {
        GradeRecord(
            continuousAssessment = cVal,
            testScore = tVal,
            examScore = eVal
        ).calculatedAverage
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "${s.gradesTitle} - ${student.fullName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Live preview badge
                Surface(
                    color = if (calculatedPreview != null) {
                        if (calculatedPreview >= 14.0) EmeraldContainer else if (calculatedPreview >= 10.0) GoldContainer else CrimsonContainer
                    } else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = s.calculatedAverage,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (calculatedPreview != null) "$calculatedPreview / 20" else (if (isAr) "في انتظار النقاط" else "En attente"),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (calculatedPreview != null) {
                                if (calculatedPreview >= 14.0) EmeraldSuccess else if (calculatedPreview >= 10.0) Color(0xFF7A4A00) else CrimsonAbsence
                            } else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                OutlinedTextField(
                    value = continuousStr,
                    onValueChange = { continuousStr = it },
                    label = { Text("${s.continuousAssessment} (/20)") },
                    placeholder = { Text("مثال: 15.5") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("dialog_grade_continuous_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = testStr,
                    onValueChange = { testStr = it },
                    label = { Text("${s.testExam} (/20)") },
                    placeholder = { Text("مثال: 14.0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("dialog_grade_test_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = examStr,
                    onValueChange = { examStr = it },
                    label = { Text("${s.finalExam} (/20 - المعامل 2)") },
                    placeholder = { Text("مثال: 16.0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("dialog_grade_exam_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text(if (isAr) "ملاحظات الأستاذ على التلميذ" else "Observations") },
                    modifier = Modifier.fillMaxWidth().testTag("dialog_grade_remarks_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(cVal, tVal, eVal, remarks)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldAccent,
                    contentColor = Color(0xFF1A1305)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("dialog_confirm_save_grade")
            ) {
                Text(s.save)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dialog_cancel_save_grade")
            ) {
                Text(s.cancel)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
