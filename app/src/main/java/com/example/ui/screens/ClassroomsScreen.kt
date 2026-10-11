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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Classroom
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.navigation.OstadiScreen
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.OstadiUiState

@Composable
fun ClassroomsScreen(
    uiState: OstadiUiState,
    onNavigate: (OstadiScreen) -> Unit,
    onSelectClassroom: (String?) -> Unit,
    onOpenAddDialog: (Boolean) -> Unit,
    onAddClassroom: (name: String, level: String, room: String) -> Unit
) {
    val s = AppStrings.get(uiState.currentLanguage)
    val isAr = uiState.currentLanguage == AppLanguage.ARABIC

    var selectedLevelFilter by remember { mutableStateOf<String?>(null) }

    val levels = listOf(
        null to s.filterAllLevels,
        "السنة الرابعة متوسط" to if (isAr) "الرابعة متوسط" else "4ème AM",
        "السنة الثالثة متوسط" to if (isAr) "الثالثة متوسط" else "3ème AM",
        "السنة الثانية متوسط" to if (isAr) "الثانية متوسط" else "2ème AM"
    )

    val filteredList = uiState.classrooms.filter {
        selectedLevelFilter == null || it.level == selectedLevelFilter
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onOpenAddDialog(true) },
                containerColor = NavyPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("fab_add_classroom")
            ) {
                Icon(Icons.Default.Add, contentDescription = s.addClassroomTitle)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // Level Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(levels) { (lvlKey, lvlLabel) ->
                    val isSelected = selectedLevelFilter == lvlKey
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedLevelFilter = lvlKey },
                        label = { Text(lvlLabel) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Classrooms List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { classroom ->
                    ClassroomCardItem(
                        classroom = classroom,
                        language = uiState.currentLanguage,
                        onViewStudents = {
                            onSelectClassroom(classroom.id)
                            onNavigate(OstadiScreen.STUDENTS)
                        },
                        onViewGrades = {
                            onSelectClassroom(classroom.id)
                            onNavigate(OstadiScreen.GRADES)
                        }
                    )
                }
            }
        }
    }

    // Add Classroom Dialog
    if (uiState.showAddClassroomDialog) {
        AddClassroomDialog(
            language = uiState.currentLanguage,
            onDismiss = { onOpenAddDialog(false) },
            onConfirm = onAddClassroom
        )
    }
}

@Composable
private fun ClassroomCardItem(
    classroom: Classroom,
    language: AppLanguage,
    onViewStudents: () -> Unit,
    onViewGrades: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("classroom_item_${classroom.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(NavyContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Class,
                            contentDescription = null,
                            tint = NavyPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = classroom.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = classroom.level,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = if (classroom.averageScore >= 14.0) EmeraldContainer else GoldContainer,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "${classroom.averageScore} / 20",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (classroom.averageScore >= 14.0) EmeraldSuccess else Color(0xFF7A4A00)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.People, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                        Text(
                            text = "${classroom.studentCount} ${if (isAr) "تلميذ" else "élèves"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                        Text(
                            text = classroom.roomNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        onClick = onViewStudents,
                        color = NavyContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isAr) "التلاميذ" else "Élèves",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = NavyPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        onClick = onViewGrades,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isAr) "النقاط" else "Notes",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddClassroomDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onConfirm: (name: String, level: String, room: String) -> Unit
) {
    val s = AppStrings.get(language)
    val isAr = language == AppLanguage.ARABIC

    var name by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("السنة الرابعة متوسط") }
    var room by remember { mutableStateOf("القاعة 08") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = s.addClassroomTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(s.classroomNamePlaceholder) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_classroom_name_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = level,
                    onValueChange = { level = it },
                    label = { Text(s.levelLabel) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_classroom_level_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = { Text(s.roomNumberLabel) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_classroom_room_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name, level, room)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("dialog_confirm_add_classroom")
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
