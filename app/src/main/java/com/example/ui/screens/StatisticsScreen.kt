package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun StatisticsScreen(
    uiState: OstadiUiState
) {
    val s = AppStrings.get(uiState.currentLanguage)
    val isAr = uiState.currentLanguage == AppLanguage.ARABIC

    val students = uiState.students
    val gradedStudents = students.mapNotNull { st ->
        uiState.getStudentAverage(st.id)?.let { avg -> st to avg }
    }
    val highestScore = gradedStudents.maxOfOrNull { it.second } ?: 0.0
    val lowestScore = gradedStudents.minOfOrNull { it.second } ?: 0.0
    val topStudents = gradedStudents.sortedByDescending { it.second }.take(5)

    // Distribution brackets out of graded students
    val excellentCount = gradedStudents.count { it.second >= 16.0 }
    val veryGoodCount = gradedStudents.count { it.second in 14.0..<16.0 }
    val goodCount = gradedStudents.count { it.second in 12.0..<14.0 }
    val acceptableCount = gradedStudents.count { it.second in 10.0..<12.0 }
    val belowAvgCount = gradedStudents.count { it.second < 10.0 }
    val totalGraded = gradedStudents.size.coerceAtLeast(1)
    val totalCount = students.size.coerceAtLeast(1)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // High-level statistics summary
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stats_summary_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = s.statisticsTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${uiState.teacherProfile?.academicYear ?: "2024 / 2025"} • ${uiState.teacherProfile?.schoolName ?: ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(GoldAccent.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = GoldAccent)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatMiniItem(label = s.successRate, value = "${uiState.successRatePercentage}%")
                        StatMiniItem(label = if (isAr) "المعدل العام" else "Moyenne", value = "${uiState.generalAverageScore}")
                        StatMiniItem(label = s.highestAverage, value = "$highestScore")
                        StatMiniItem(label = s.lowestAverage, value = "$lowestScore")
                    }
                }
            }
        }

        // Gender Distribution Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = s.genderRatio,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Male, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(18.dp))
                            Text(
                                text = "${s.maleStudents}: ${uiState.maleStudentsCount} (${((uiState.maleStudentsCount.toDouble() / totalCount) * 100).toInt()}%)",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Female, contentDescription = null, tint = Color(0xFFC2185B), modifier = Modifier.size(18.dp))
                            Text(
                                text = "${s.femaleStudents}: ${uiState.femaleStudentsCount} (${((uiState.femaleStudentsCount.toDouble() / totalCount) * 100).toInt()}%)",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { (uiState.maleStudentsCount.toFloat() / totalCount.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF1976D2),
                        trackColor = Color(0xFFC2185B)
                    )
                }
            }
        }

        // Grade Distribution Brackets
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isAr) "توزيع التلاميذ حسب سلم النقاط" else "Répartition des Notes",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    DistributionBracketRow(label = if (isAr) "ممتاز (≥ 16/20)" else "Excellent (≥16)", count = excellentCount, total = totalGraded, color = EmeraldSuccess)
                    DistributionBracketRow(label = if (isAr) "جيد جداً (14 - 16)" else "Très bien (14-16)", count = veryGoodCount, total = totalGraded, color = Color(0xFF0D9488))
                    DistributionBracketRow(label = if (isAr) "جيد (12 - 14)" else "Bien (12-14)", count = goodCount, total = totalGraded, color = GoldAccent)
                    DistributionBracketRow(label = if (isAr) "مقبول (10 - 12)" else "Passable (10-12)", count = acceptableCount, total = totalGraded, color = Color(0xFFD97706))
                    DistributionBracketRow(label = if (isAr) "دون المتوسط (< 10)" else "Insuffisant (<10)", count = belowAvgCount, total = totalGraded, color = CrimsonAbsence)
                }
            }
        }

        // Honor Roll (لوحة الشرف)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldAccent)
                    Text(
                        text = s.topStudents,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        items(topStudents) { (student, avg) ->
            HonorRollStudentItem(student = student, average = avg, language = uiState.currentLanguage)
        }
    }
}

@Composable
private fun StatMiniItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun DistributionBracketRow(
    label: String,
    count: Int,
    total: Int,
    color: Color
) {
    val pct = (count.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "$count (${(pct * 100).toInt()}%)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = color)
        }
        LinearProgressIndicator(
            progress = { pct },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun HonorRollStudentItem(student: Student, average: Double, language: AppLanguage) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GoldContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.fullName,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${student.classroomName} • ${student.registrationNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            Surface(
                color = EmeraldContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "$average / 20",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldSuccess
                )
            }
        }
    }
}

