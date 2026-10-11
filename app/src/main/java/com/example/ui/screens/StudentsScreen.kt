package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Gender
import com.example.data.model.Student
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.CrimsonAbsence
import com.example.ui.theme.CrimsonContainer
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.OstadiUiState

@Composable
fun StudentsScreen(
    uiState: OstadiUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectClassroom: (String?) -> Unit,
    onOpenAddStudentDialog: (Boolean) -> Unit,
    onOpenStudentDetails: (Student?) -> Unit,
    onAddStudent: (firstName: String, lastName: String, gender: Gender, classroomId: String, phone: String, regNo: String) -> Unit
) {
    val s = AppStrings.get(uiState.currentLanguage)
    val isAr = uiState.currentLanguage == AppLanguage.ARABIC
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onOpenAddStudentDialog(true) },
                containerColor = NavyPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("fab_add_student")
            ) {
                Icon(Icons.Default.Add, contentDescription = s.addStudentTitle)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // Search Input
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text(s.searchStudentPlaceholder) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = NavyPrimary)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag("search_student_input"),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            // Classroom Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedClassroomId == null,
                        onClick = { onSelectClassroom(null) },
                        label = { Text(s.allClassrooms) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White
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
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Students Count Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${s.studentsTitle} (${uiState.filteredStudents.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Students List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.filteredStudents) { student ->
                    StudentCardItem(
                        student = student,
                        language = uiState.currentLanguage,
                        onClick = { onOpenStudentDetails(student) },
                        onCallParent = {
                            if (student.parentPhone.isNotBlank()) {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${student.parentPhone}"))
                                context.startActivity(intent)
                            }
                        }
                    )
                }
            }
        }
    }

    // Add Student Dialog
    if (uiState.showAddStudentDialog) {
        AddStudentDialog(
            language = uiState.currentLanguage,
            classrooms = uiState.classrooms,
            onDismiss = { onOpenAddStudentDialog(false) },
            onConfirm = onAddStudent
        )
    }

    // Student Details Dialog
    if (uiState.selectedStudentForDetails != null) {
        StudentDetailsDialog(
            student = uiState.selectedStudentForDetails!!,
            language = uiState.currentLanguage,
            onDismiss = { onOpenStudentDetails(null) }
        )
    }
}

@Composable
private fun StudentCardItem(
    student: Student,
    language: AppLanguage,
    onClick: () -> Unit,
    onCallParent: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val isMale = student.gender == Gender.MALE

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("student_card_${student.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Gender Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isMale) Color(0xFFE3F2FD) else Color(0xFFFCE4EC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isMale) Icons.Default.Male else Icons.Default.Female,
                    contentDescription = null,
                    tint = if (isMale) Color(0xFF1976D2) else Color(0xFFC2185B),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.fullName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${student.classroomName} • ${student.registrationNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val avg = student.termAverage
                Surface(
                    color = if (avg != null && avg >= 14.0) EmeraldContainer else if (avg != null && avg >= 10.0) GoldContainer else if (avg != null) CrimsonContainer else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (avg != null) "$avg / 20" else (if (isAr) "غير مرصود" else "Non évalué"),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (avg != null && avg >= 14.0) EmeraldSuccess else if (avg != null && avg >= 10.0) Color(0xFF7A4A00) else if (avg != null) CrimsonAbsence else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (student.totalAbsences > 0) {
                    Text(
                        text = "${student.totalAbsences} ${if (isAr) "غياب" else "abs."}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CrimsonAbsence,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStudentDialog(
    language: AppLanguage,
    classrooms: List<com.example.data.model.Classroom>,
    onDismiss: () -> Unit,
    onConfirm: (firstName: String, lastName: String, gender: Gender, classroomId: String, phone: String, regNo: String) -> Unit
) {
    val s = AppStrings.get(language)
    val isAr = language == AppLanguage.ARABIC

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    var selectedClassId by remember { mutableStateOf(classrooms.firstOrNull()?.id ?: "") }
    var phone by remember { mutableStateOf("") }
    var regNumber by remember { mutableStateOf("") }

    var classExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = s.addStudentTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text(s.lastNameLabel) },
                    modifier = Modifier.fillMaxWidth().testTag("add_student_lastname_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text(s.firstNameLabel) },
                    modifier = Modifier.fillMaxWidth().testTag("add_student_firstname_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                // Gender Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = gender == Gender.MALE,
                        onClick = { gender = Gender.MALE },
                        label = { Text(s.male) },
                        leadingIcon = { Icon(Icons.Default.Male, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF1976D2),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        selected = gender == Gender.FEMALE,
                        onClick = { gender = Gender.FEMALE },
                        label = { Text(s.female) },
                        leadingIcon = { Icon(Icons.Default.Female, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFC2185B),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Classroom Selector
                ExposedDropdownMenuBox(
                    expanded = classExpanded,
                    onExpandedChange = { classExpanded = !classExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val currentClassName = classrooms.firstOrNull { it.id == selectedClassId }?.name ?: s.allClassrooms
                    OutlinedTextField(
                        value = currentClassName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(s.navClassrooms) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = classExpanded,
                        onDismissRequest = { classExpanded = false }
                    ) {
                        classrooms.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(c.name) },
                                onClick = {
                                    selectedClassId = c.id
                                    classExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(s.parentPhoneLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = regNumber,
                    onValueChange = { regNumber = it },
                    label = { Text(s.regNumberLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (firstName.isNotBlank() && lastName.isNotBlank()) {
                        onConfirm(firstName, lastName, gender, selectedClassId, phone, regNumber)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("dialog_confirm_add_student")
            ) {
                Text(s.save)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(s.cancel)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun StudentDetailsDialog(
    student: Student,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val s = AppStrings.get(language)
    val isAr = language == AppLanguage.ARABIC
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (student.gender == Gender.MALE) Icons.Default.Male else Icons.Default.Female,
                    contentDescription = null,
                    tint = if (student.gender == Gender.MALE) Color(0xFF1976D2) else Color(0xFFC2185B)
                )
                Text(
                    text = student.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DetailRow(label = if (isAr) "القسم:" else "Classe:", value = student.classroomName)
                DetailRow(label = if (isAr) "رقم التسجيل:" else "N° d'immatriculation:", value = student.registrationNumber)
                DetailRow(label = if (isAr) "معدل المادة الحالي:" else "Moyenne actuelle:", value = if (student.termAverage != null) "${student.termAverage} / 20" else (if (isAr) "غير مرصود" else "Non évalué"))
                DetailRow(label = if (isAr) "مجموع الغيابات:" else "Total absences:", value = "${student.totalAbsences}")
                DetailRow(label = if (isAr) "نجوم المشاركة:" else "Étoiles de mérite:", value = "⭐ ${student.participationStars} / 5")
                DetailRow(label = if (isAr) "هاتف الولي:" else "Téléphone parent:", value = student.parentPhone)

                if (student.notes.isNotBlank()) {
                    DetailRow(label = if (isAr) "ملاحظات تربوية:" else "Observations:", value = student.notes)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${student.parentPhone}"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyContainer, contentColor = NavyPrimary)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(s.callParent, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(s.close)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}
