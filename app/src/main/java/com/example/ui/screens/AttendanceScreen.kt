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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceStatus
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
fun AttendanceScreen(
    uiState: OstadiUiState,
    onSelectClassroom: (String?) -> Unit,
    onRecordStatus: (studentId: String, status: AttendanceStatus, note: String) -> Unit,
    onSaveFullRollCall: () -> Unit
) {
    val s = AppStrings.get(uiState.currentLanguage)
    val isAr = uiState.currentLanguage == AppLanguage.ARABIC

    // In-memory status map for immediate roll-call interaction
    val studentStatusMap = remember {
        mutableStateMapOf<String, AttendanceStatus>().apply {
            uiState.students.forEach { st ->
                // Check if recorded as absent in demo
                val existing = uiState.attendanceRecords.firstOrNull { it.studentId == st.id && it.date == uiState.selectedAttendanceDate }
                put(st.id, existing?.status ?: AttendanceStatus.PRESENT)
            }
        }
    }

    val sessions = listOf(
        "الحصة 1 (08:00 - 09:00)",
        "الحصة 2 (09:00 - 10:00)",
        "الحصة 3 (10:00 - 11:00)",
        "الحصة 4 (11:00 - 12:00)",
        "الحصة 5 (13:30 - 14:30)",
        "الحصة 6 (14:30 - 15:30)"
    )

    var selectedSession by remember { mutableStateOf(sessions.first()) }

    // Counts from active state map for current filtered students
    val currentStudents = uiState.filteredStudents
    val presentCount = currentStudents.count { (studentStatusMap[it.id] ?: AttendanceStatus.PRESENT) == AttendanceStatus.PRESENT }
    val absentCount = currentStudents.count { studentStatusMap[it.id] == AttendanceStatus.ABSENT }
    val lateCount = currentStudents.count { studentStatusMap[it.id] == AttendanceStatus.LATE }
    val excusedCount = currentStudents.count { studentStatusMap[it.id] == AttendanceStatus.EXCUSED }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Date & Session Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = NavyPrimary)
                            Text(
                                text = "${s.dateLabel}: ${uiState.selectedAttendanceDate}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = NavyContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (isAr) "الفترة الصباحية" else "Matin",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = NavyPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Session Selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(18.dp))
                        Text(
                            text = selectedSession,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Classroom Filter Chips
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
        }

        // Attendance Quick Summary Bar
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AttendancePill(label = s.present, count = presentCount, color = EmeraldSuccess, bg = EmeraldContainer)
                    AttendancePill(label = s.absent, count = absentCount, color = CrimsonAbsence, bg = CrimsonContainer)
                    AttendancePill(label = s.late, count = lateCount, color = Color(0xFFD97706), bg = GoldContainer)
                    AttendancePill(label = s.excused, count = excusedCount, color = Color(0xFF2563EB), bg = Color(0xFFDBEAFE))
                }
            }
        }

        // Students Roll Call Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAr) "قائمة المناداة للتلاميذ" else "Feuille d'Appel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${currentStudents.size} ${s.studentsCountSuffix}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Roll Call Student Items
        items(currentStudents) { student ->
            val currentStatus = studentStatusMap[student.id] ?: AttendanceStatus.PRESENT
            RollCallItem(
                student = student,
                status = currentStatus,
                language = uiState.currentLanguage,
                onStatusChange = { newStatus ->
                    studentStatusMap[student.id] = newStatus
                    onRecordStatus(student.id, newStatus, "")
                }
            )
        }

        // Save Roll Call Button
        item {
            Button(
                onClick = onSaveFullRollCall,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_attendance_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GoldAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = s.saveAttendance,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AttendancePill(
    label: String,
    count: Int,
    color: Color,
    bg: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun RollCallItem(
    student: Student,
    status: AttendanceStatus,
    language: AppLanguage,
    onStatusChange: (AttendanceStatus) -> Unit
) {
    val s = AppStrings.get(language)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("roll_call_item_${student.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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

                // Current Status Badge
                Surface(
                    color = when (status) {
                        AttendanceStatus.PRESENT -> EmeraldContainer
                        AttendanceStatus.ABSENT -> CrimsonContainer
                        AttendanceStatus.LATE -> GoldContainer
                        AttendanceStatus.EXCUSED -> Color(0xFFDBEAFE)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = when (status) {
                            AttendanceStatus.PRESENT -> s.present
                            AttendanceStatus.ABSENT -> s.absent
                            AttendanceStatus.LATE -> s.late
                            AttendanceStatus.EXCUSED -> s.excused
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (status) {
                            AttendanceStatus.PRESENT -> EmeraldSuccess
                            AttendanceStatus.ABSENT -> CrimsonAbsence
                            AttendanceStatus.LATE -> Color(0xFF7A4A00)
                            AttendanceStatus.EXCUSED -> Color(0xFF1D4ED8)
                        }
                    )
                }
            }

            // Quick Status Selector Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusToggleButton(
                    label = s.present,
                    isSelected = status == AttendanceStatus.PRESENT,
                    activeColor = EmeraldSuccess,
                    activeBg = EmeraldContainer,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusChange(AttendanceStatus.PRESENT) }
                )
                StatusToggleButton(
                    label = s.absent,
                    isSelected = status == AttendanceStatus.ABSENT,
                    activeColor = CrimsonAbsence,
                    activeBg = CrimsonContainer,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusChange(AttendanceStatus.ABSENT) }
                )
                StatusToggleButton(
                    label = s.late,
                    isSelected = status == AttendanceStatus.LATE,
                    activeColor = Color(0xFFD97706),
                    activeBg = GoldContainer,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusChange(AttendanceStatus.LATE) }
                )
                StatusToggleButton(
                    label = s.excused,
                    isSelected = status == AttendanceStatus.EXCUSED,
                    activeColor = Color(0xFF2563EB),
                    activeBg = Color(0xFFDBEAFE),
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusChange(AttendanceStatus.EXCUSED) }
                )
            }
        }
    }
}

@Composable
private fun StatusToggleButton(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    activeBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(8.dp)),
        color = if (isSelected) activeBg else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
            )
        }
    }
}
