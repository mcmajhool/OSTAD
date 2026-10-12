package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.navigation.OstadiScreen
import com.example.ui.theme.AmberLate
import com.example.ui.theme.BlueMale
import com.example.ui.theme.CrimsonAbsence
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.GoldLight
import com.example.ui.theme.OstadiCardBg
import com.example.ui.theme.OstadiCardBorder
import com.example.ui.theme.OstadiDarkBg
import com.example.ui.theme.PinkFemale
import com.example.ui.theme.PurpleExam
import com.example.ui.theme.TealClass
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

    val teacherName = profile?.fullName?.ifBlank { "أحمد بن يوسف" } ?: "أحمد بن يوسف"
    val subject = profile?.subject ?: "مادة الرياضيات"
    val school = profile?.schoolName ?: "متوسطة الشهيد العربي بن مهيدي - الجزائر العاصمة"

    val totalStudents = if (uiState.isDemoMode && uiState.totalStudentsCount == 0) 148 else uiState.totalStudentsCount
    val maleCount = if (uiState.isDemoMode && uiState.maleStudentsCount == 0) 82 else uiState.maleStudentsCount
    val femaleCount = if (uiState.isDemoMode && uiState.femaleStudentsCount == 0) 66 else uiState.femaleStudentsCount
    val classCount = if (uiState.isDemoMode && uiState.totalClassroomsCount == 0) 6 else uiState.totalClassroomsCount
    val avgScore = if (uiState.generalAverageScore > 0.0) {
        String.format(java.util.Locale.US, "%.1f", uiState.generalAverageScore)
    } else if (uiState.isDemoMode) {
        "15.8"
    } else {
        "--"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OstadiDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Demo Mode Notice Banner (if active)
        if (uiState.isDemoMode) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .clickable { onNavigate(OstadiScreen.LOGIN) }
                        .testTag("demo_mode_banner"),
                    color = Color(0xFF1E170A)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAr) "⚠️ وضع المعاينة التجريبية (بيانات توضيحية كاملة)" else "⚠️ Mode Démo (Données d'aperçu complètes)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                            Text(
                                text = if (isAr) "اضغط هنا للانتقال لربط حساب سحابي رسمي وحفظ بياناتك" else "Cliquez pour créer un compte officiel",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE2C485),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Top Teacher Hero Header Card (Matching Image 2)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_teacher_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C172B)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1F355A))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Subtle background illustration banner
                    Image(
                        painter = painterResource(id = R.drawable.ostadi_hero_banner),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
                        contentScale = ContentScale.Crop,
                        alpha = 0.25f
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isAr) "صباح الخير أستاذ" else "Bonjour Professeur,",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = teacherName,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = subject,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFCBD5E1),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Teacher Avatar with Golden Border and Edit Icon
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, GoldAccent, CircleShape)
                                    .background(Color(0xFF1B2A4A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Avatar",
                                    tint = GoldLight,
                                    modifier = Modifier.size(36.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.BottomEnd)
                                        .clip(CircleShape)
                                        .background(GoldAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color(0xFF1A1305),
                                        modifier = Modifier.size(11.dp)
                                    )
                                }
                            }
                        }

                        // School and Academic Year Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFF22385E), RoundedCornerShape(12.dp)),
                                color = Color(0xFF13223C)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "السنة 2024 - 2025",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .weight(1.5f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFF22385E), RoundedCornerShape(12.dp)),
                                color = Color(0xFF13223C)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Class,
                                        contentDescription = null,
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = school,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFE2E8F0),
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Stats Grid: 2 rows of 4 Metric Cards (Matching Image 2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricMiniCard(
                        value = "$maleCount",
                        label = if (isAr) "الذكور" else "Garçons",
                        sub = if (isAr) "من التلاميذ" else "élèves",
                        icon = Icons.Default.Male,
                        iconColor = BlueMale,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        value = "$totalStudents",
                        label = if (isAr) "إجمالي التلاميذ" else "Total Élèves",
                        sub = if (isAr) "في أقسامي" else "inscrits",
                        icon = Icons.Default.People,
                        iconColor = GoldAccent,
                        isHighlighted = true,
                        modifier = Modifier.weight(1.2f)
                    )
                    MetricMiniCard(
                        value = "$classCount",
                        label = if (isAr) "الأقسام" else "Classes",
                        sub = if (isAr) "أقسامي" else "groupes",
                        icon = Icons.Default.Class,
                        iconColor = TealClass,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricMiniCard(
                        value = "$avgScore",
                        label = if (isAr) "المعدل العام" else "Moyenne",
                        sub = "من 20",
                        icon = Icons.Default.TrendingUp,
                        iconColor = PurpleExam,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        value = if (uiState.isDemoMode && uiState.upcomingExams.isEmpty()) "3" else "${uiState.upcomingExams.size}",
                        label = if (isAr) "اختبارات قادمة" else "Examens",
                        sub = if (isAr) "خلال 7 أيام" else "7 jours",
                        icon = Icons.Default.EventNote,
                        iconColor = EmeraldSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        value = if (uiState.isDemoMode && uiState.todayAbsencesCount == 0) "5" else "${uiState.todayAbsencesCount}",
                        label = if (isAr) "غيابات اليوم" else "Absences",
                        sub = if (isAr) "اليوم" else "aujourd'hui",
                        icon = Icons.Default.AssignmentLate,
                        iconColor = CrimsonAbsence,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        value = "$femaleCount",
                        label = if (isAr) "الإناث" else "Filles",
                        sub = if (isAr) "من التلاميذ" else "élèves",
                        icon = Icons.Default.Female,
                        iconColor = PinkFemale,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Actions Section (Matching Image 2 horizontal colorful tiles)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "إجراءات سريعة ⚡" else "Actions Rapides ⚡",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = if (isAr) "عرض الكل <" else "Voir tout >",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigate(OstadiScreen.CLASSROOMS) }
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        ActionSquareTile(
                            title = if (isAr) "تسجيل الحضور" else "Présences",
                            desc = if (isAr) "اليومي للتلاميذ" else "Appel du jour",
                            icon = Icons.Default.AssignmentTurnedIn,
                            containerColor = Color(0xFF132742),
                            accentColor = Color(0xFF38BDF8),
                            onClick = { onNavigate(OstadiScreen.ATTENDANCE) }
                        )
                    }
                    item {
                        ActionSquareTile(
                            title = if (isAr) "إضافة نقطة" else "Noter",
                            desc = if (isAr) "الفروض والتقويم" else "Évaluations",
                            icon = Icons.Default.Grade,
                            containerColor = Color(0xFF10362B),
                            accentColor = EmeraldSuccess,
                            onClick = { onNavigate(OstadiScreen.GRADES) }
                        )
                    }
                    item {
                        ActionSquareTile(
                            title = if (isAr) "إنشاء اختبار" else "Examen",
                            desc = if (isAr) "واجب أو فرض" else "Créer épreuve",
                            icon = Icons.Default.Assessment,
                            containerColor = Color(0xFF3B1A24),
                            accentColor = Color(0xFFF43F5E),
                            onClick = { onNavigate(OstadiScreen.GRADES) }
                        )
                    }
                    item {
                        ActionSquareTile(
                            title = if (isAr) "إرسال رسالة" else "Message",
                            desc = if (isAr) "للأولياء والزملاء" else "Aux parents",
                            icon = Icons.AutoMirrored.Filled.Chat,
                            containerColor = Color(0xFF271B42),
                            accentColor = PurpleExam,
                            onClick = { onNavigate(OstadiScreen.PARTICIPATION) }
                        )
                    }
                    item {
                        ActionSquareTile(
                            title = if (isAr) "إدارة الأقسام" else "Classes",
                            desc = if (isAr) "الأفواج والقوائم" else "Groupes",
                            icon = Icons.Default.Class,
                            containerColor = Color(0xFF2D2411),
                            accentColor = GoldAccent,
                            onClick = { onNavigate(OstadiScreen.CLASSROOMS) }
                        )
                    }
                }
            }
        }

        // Upcoming Exams Card & Weekly Attendance Chart (Matching Image 2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Upcoming Exams Section Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = OstadiCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OstadiCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.EventNote, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                                Text(
                                    text = if (isAr) "الاختبارات القادمة" else "Examens à venir",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = if (isAr) "عرض الكل <" else "Voir tout >",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldAccent,
                                modifier = Modifier.clickable { onNavigate(OstadiScreen.GRADES) }
                            )
                        }

                        UpcomingExamRowItem(date = "15 أفريل", title = "فرض في الجبر", sub = "السنة الثالثة متوسط • بعد 3 أيام", badge = "المعامل 2")
                        UpcomingExamRowItem(date = "18 أفريل", title = "اختبار الفصل الثاني", sub = "السنة الرابعة متوسط • بعد 6 أيام", badge = "المعامل 2")
                        UpcomingExamRowItem(date = "20 أفريل", title = "فرض في الهندسة", sub = "السنة الأولى متوسط • بعد 8 أيام", badge = "المعامل 1")
                    }
                }

                // Weekly Attendance Progress Bar Chart Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = OstadiCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OstadiCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text(
                                    text = if (isAr) "معدل الحضور الأسبوعي 📊" else "Présence Hebdomadaire 📊",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "91% معدل عام",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            WeeklyDayBar(day = "السبت", pct = 93, color = EmeraldSuccess)
                            WeeklyDayBar(day = "الأحد", pct = 88, color = GoldAccent)
                            WeeklyDayBar(day = "الإثنين", pct = 91, color = EmeraldSuccess)
                            WeeklyDayBar(day = "الثلاثاء", pct = 87, color = GoldAccent)
                            WeeklyDayBar(day = "الأربعاء", pct = 95, color = EmeraldSuccess)
                        }
                    }
                }
            }
        }

        // Recent Activities Section (Matching Image 2)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = OstadiCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, OstadiCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                            Text(
                                text = if (isAr) "آخر النشاطات 🕒" else "Activités Récentes 🕒",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    ActivityRowItem(
                        icon = Icons.Default.Check,
                        iconColor = EmeraldSuccess,
                        containerColor = Color(0xFF0C3827),
                        title = "تم تسجيل حضور قسم 3 متوسط أ",
                        time = "10:24 ض"
                    )
                    ActivityRowItem(
                        icon = Icons.Default.Grade,
                        iconColor = PurpleExam,
                        containerColor = Color(0xFF271542),
                        title = "تم إضافة نقاط اختبار في الجبر - 4 متوسط ب",
                        time = "أمس 4:30 م"
                    )
                    ActivityRowItem(
                        icon = Icons.AutoMirrored.Filled.Chat,
                        iconColor = BlueMale,
                        containerColor = Color(0xFF11264A),
                        title = "تم إرسال رسالة إلى أولياء 2 متوسط أ",
                        time = "أمس 2:15 م"
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricMiniCard(
    value: String,
    label: String,
    sub: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = if (isHighlighted) GoldAccent else OstadiCardBorder,
                shape = RoundedCornerShape(14.dp)
            ),
        color = if (isHighlighted) Color(0xFF1E170A) else OstadiCardBg
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (isHighlighted) GoldLight else Color.White,
                fontSize = 17.sp
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE2E8F0),
                fontSize = 11.sp
            )
            Text(
                text = sub,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8),
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun ActionSquareTile(
    title: String,
    desc: String,
    icon: ImageVector,
    containerColor: Color,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .size(width = 115.dp, height = 115.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
        color = containerColor
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 12.sp
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFCBD5E1),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun UpcomingExamRowItem(
    date: String,
    title: String,
    sub: String,
    badge: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = Color(0xFF13223C)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E3354)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 10.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                    Text(
                        text = sub,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            }

            Surface(
                color = Color(0xFF2C2211),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = badge,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun WeeklyDayBar(
    day: String,
    pct: Int,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$pct%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = 10.sp
        )
        Box(
            modifier = Modifier
                .width(28.dp)
                .height((pct * 0.7).dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color)
        )
        Text(
            text = day,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun ActivityRowItem(
    icon: ImageVector,
    iconColor: Color,
    containerColor: Color,
    title: String,
    time: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE2E8F0),
                fontSize = 12.sp
            )
        }
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8),
            fontSize = 11.sp
        )
    }
}
