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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Classroom
import com.example.data.model.ExamEvent
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.components.StatCard
import com.example.ui.navigation.OstadiScreen
import com.example.ui.theme.CrimsonAbsence
import com.example.ui.theme.CrimsonContainer
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.OstadiUiState

@Composable
fun DashboardScreen(
    uiState: OstadiUiState,
    onNavigate: (OstadiScreen) -> Unit,
    onOpenAddClassroom: () -> Unit,
    onOpenAddStudent: () -> Unit
) {
    val s = AppStrings.get(uiState.currentLanguage)
    val isAr = uiState.currentLanguage == AppLanguage.ARABIC
    val profile = uiState.teacherProfile

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (uiState.isDemoMode) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(OstadiScreen.LOGIN) }
                        .testTag("demo_mode_banner"),
                    color = GoldContainer,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) "⚠️ وضع المعاينة التجريبية (بيانات توضيحية)" else "⚠️ Mode Démo (Données d'aperçu)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B4300)
                            )
                            Text(
                                text = if (isAr) "انقر هنا لتسجيل الدخول أو فتح حساب حقيقي لحفظ بياناتك في السحابة" else "Cliquez pour vous connecter et enregistrer vos données en ligne",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B4300).copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Teacher Welcome Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_teacher_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) "مرحباً بك،" else "Bienvenue,",
                                style = MaterialTheme.typography.bodySmall,
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = profile?.fullName ?: "الأستاذ",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${profile?.subject ?: "مادة التدريس"} • ${profile?.schoolName ?: "المؤسسة التعليمية"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isAr) "24/25" else "24/25",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${profile?.wilaya ?: "الجزائر"} • ${profile?.cycle ?: "التعليم المتوسط"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (isAr) "المعدل العام: ${uiState.generalAverageScore}/20" else "Moyenne: ${uiState.generalAverageScore}/20",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Quick Actions Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = s.quickActions,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        title = s.takeAttendanceAction,
                        icon = Icons.Default.AssignmentTurnedIn,
                        containerColor = NavyContainer,
                        contentColor = NavyPrimary,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_attendance",
                        onClick = { onNavigate(OstadiScreen.ATTENDANCE) }
                    )
                    QuickActionButton(
                        title = s.addGradeAction,
                        icon = Icons.Default.Grade,
                        containerColor = GoldContainer,
                        contentColor = Color(0xFF6B4300),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_grade",
                        onClick = { onNavigate(OstadiScreen.GRADES) }
                    )
                    QuickActionButton(
                        title = s.addStudentAction,
                        icon = Icons.Default.PersonAdd,
                        containerColor = EmeraldContainer,
                        contentColor = EmeraldSuccess,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_student",
                        onClick = onOpenAddStudent
                    )
                    QuickActionButton(
                        title = s.addClassroomAction,
                        icon = Icons.Default.Add,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_classroom",
                        onClick = onOpenAddClassroom
                    )
                }
            }
        }

        // Core Statistics Grid Cards
        item {
            Text(
                text = if (isAr) "المؤشرات التربوية والإحصائية" else "Indicateurs Pédagogiques",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Row 1: Total Classrooms & Total Students
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = s.totalClassrooms,
                    value = uiState.totalClassroomsCount.toString(),
                    subtitle = if (isAr) "أفواج تربوية" else "groupes",
                    icon = Icons.Default.Class,
                    iconTint = NavyPrimary,
                    iconBackgroundColor = NavyContainer,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stat_card_classrooms"),
                    onClick = { onNavigate(OstadiScreen.CLASSROOMS) }
                )

                StatCard(
                    title = s.totalStudents,
                    value = uiState.totalStudentsCount.toString(),
                    subtitle = if (isAr) "تلميذ مسجل" else "élèves inscrits",
                    icon = Icons.Default.People,
                    iconTint = EmeraldSuccess,
                    iconBackgroundColor = EmeraldContainer,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stat_card_students"),
                    onClick = { onNavigate(OstadiScreen.STUDENTS) }
                )
            }
        }

        // Row 2: Male Students & Female Students
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = s.maleStudents,
                    value = uiState.maleStudentsCount.toString(),
                    subtitle = "${((uiState.maleStudentsCount.toDouble() / (uiState.totalStudentsCount.coerceAtLeast(1))) * 100).toInt()}%",
                    icon = Icons.Default.Male,
                    iconTint = Color(0xFF1976D2),
                    iconBackgroundColor = Color(0xFFE3F2FD),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stat_card_male_students")
                )

                StatCard(
                    title = s.femaleStudents,
                    value = uiState.femaleStudentsCount.toString(),
                    subtitle = "${((uiState.femaleStudentsCount.toDouble() / (uiState.totalStudentsCount.coerceAtLeast(1))) * 100).toInt()}%",
                    icon = Icons.Default.Female,
                    iconTint = Color(0xFFC2185B),
                    iconBackgroundColor = Color(0xFFFCE4EC),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stat_card_female_students")
                )
            }
        }

        // Row 3: Today's Absences & Success Rate
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = s.todayAbsences,
                    value = uiState.todayAbsencesCount.toString(),
                    subtitle = if (uiState.todayAbsencesCount == 0) (if (isAr) "حضور تام" else "Zéro absence") else (if (isAr) "تلاميذ غائبين" else "absents"),
                    icon = Icons.Default.AssignmentLate,
                    iconTint = if (uiState.todayAbsencesCount > 0) CrimsonAbsence else EmeraldSuccess,
                    iconBackgroundColor = if (uiState.todayAbsencesCount > 0) CrimsonContainer else EmeraldContainer,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stat_card_today_absences"),
                    onClick = { onNavigate(OstadiScreen.ATTENDANCE) }
                )

                StatCard(
                    title = if (isAr) "نسبة النجاح" else "Taux de réussite",
                    value = "${uiState.successRatePercentage}%",
                    subtitle = if (isAr) "معدل ≥ 10/20" else "Moyenne ≥ 10",
                    icon = Icons.Default.TrendingUp,
                    iconTint = GoldAccent,
                    iconBackgroundColor = GoldContainer,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stat_card_success_rate"),
                    onClick = { onNavigate(OstadiScreen.STATISTICS) }
                )
            }
        }

        // Classroom Averages Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = s.classroomAverages,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isAr) "عرض الكل" else "Voir tout",
                    style = MaterialTheme.typography.bodySmall,
                    color = NavyPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigate(OstadiScreen.CLASSROOMS) }
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.classrooms.forEach { classroom ->
                    val dynamicStudentCount = uiState.getClassroomStudentCount(classroom.id)
                    val dynamicAverage = uiState.getClassroomAverage(classroom.id)
                    ClassroomAverageRow(
                        classroom = classroom,
                        studentCount = dynamicStudentCount,
                        averageScore = dynamicAverage,
                        onClick = { onNavigate(OstadiScreen.GRADES) }
                    )
                }
            }
        }

        // Upcoming Exams Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = s.upcomingExams,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isAr) "دفتر الامتحانات" else "Examens",
                    style = MaterialTheme.typography.bodySmall,
                    color = NavyPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigate(OstadiScreen.GRADES) }
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                uiState.upcomingExams.forEach { exam ->
                    ExamEventCard(exam = exam)
                }
            }
        }

        // Additional Modules Direct Links (Participation, Statistics, Settings)
        item {
            Text(
                text = if (isAr) "المصالح التربوية الإضافية" else "Autres Outils",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ModuleShortcutCard(
                    title = s.navParticipation,
                    icon = Icons.Default.Star,
                    color = GoldAccent,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(OstadiScreen.PARTICIPATION) }
                )
                ModuleShortcutCard(
                    title = s.navStatistics,
                    icon = Icons.Default.Leaderboard,
                    color = EmeraldSuccess,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(OstadiScreen.STATISTICS) }
                )
                ModuleShortcutCard(
                    title = s.navProfile,
                    icon = Icons.Default.EventNote,
                    color = NavyLight,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(OstadiScreen.PROFILE_SETTINGS) }
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ClassroomAverageRow(
    classroom: Classroom,
    studentCount: Int,
    averageScore: Double,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = classroom.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${classroom.level} • $studentCount تلميذ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { (averageScore / 20.0).toFloat().coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (averageScore >= 14.0) EmeraldSuccess else if (averageScore >= 10.0) GoldAccent else CrimsonAbsence,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Surface(
                color = if (averageScore >= 14.0) EmeraldContainer else if (averageScore >= 10.0) GoldContainer else CrimsonContainer,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "$averageScore / 20",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (averageScore >= 14.0) EmeraldSuccess else if (averageScore >= 10.0) Color(0xFF7A4A00) else CrimsonAbsence
                )
            }
        }
    }
}

@Composable
private fun ExamEventCard(exam: ExamEvent) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavyContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EventNote,
                    contentDescription = null,
                    tint = NavyPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exam.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${exam.classroomName} • ${exam.room}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = exam.date,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Text(
                    text = exam.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun ModuleShortcutCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
