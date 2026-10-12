package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.navigation.OstadiScreen
import com.example.ui.theme.CrimsonAbsence
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.OstadiCardBorder
import com.example.ui.theme.OstadiDarkBg

@Composable
fun OstadiTopAppBar(
    currentScreen: OstadiScreen,
    language: AppLanguage,
    onNavigateBack: () -> Unit,
    onToggleLanguage: () -> Unit,
    onOpenFirebaseInfo: () -> Unit,
    hasNotifications: Boolean = true
) {
    val s = AppStrings.get(language)
    val isAr = language == AppLanguage.ARABIC

    val title = when (currentScreen) {
        OstadiScreen.DASHBOARD -> s.appName
        OstadiScreen.CLASSROOMS -> if (isAr) "إدارة الأقسام الدراسية" else "Gestion des Classes"
        OstadiScreen.STUDENTS -> if (isAr) "إدارة التلاميذ" else "Gestion des Élèves"
        OstadiScreen.ATTENDANCE -> if (isAr) "الحضور والغياب" else "Présences & Absences"
        OstadiScreen.GRADES -> if (isAr) "النقاط والاختبارات" else "Notes & Évaluations"
        OstadiScreen.PARTICIPATION -> if (isAr) "مركز التواصل" else "Centre de Communication"
        OstadiScreen.STATISTICS -> if (isAr) "الإحصائيات والتحليلات" else "Statistiques & Analyses"
        OstadiScreen.PROFILE_SETTINGS -> if (isAr) "الملف الشخصي والإعدادات" else "Profil & Paramètres"
        OstadiScreen.LOGIN -> s.loginTitle
        OstadiScreen.REGISTER -> s.registerTitle
        OstadiScreen.WELCOME -> s.appName
    }

    val subtitle = when (currentScreen) {
        OstadiScreen.DASHBOARD -> if (isAr) "منصة المعلم في الجزائر" else "Plateforme de l'enseignant"
        OstadiScreen.WELCOME -> if (isAr) "معاً نصنع جيلاً أفضل" else "Ensemble pour une éducation d'excellence"
        else -> if (isAr) "معاً نصنع جيلاً أفضل" else "Plateforme officielle"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = OstadiDarkBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Controls: Back button or Language pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!currentScreen.isTopLevel && currentScreen != OstadiScreen.WELCOME) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF13223C))
                            .border(1.dp, OstadiCardBorder, CircleShape)
                            .clickable { onNavigateBack() }
                            .testTag("top_bar_back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Language Switcher Pill
                Surface(
                    onClick = onToggleLanguage,
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Transparent,
                    modifier = Modifier
                        .border(1.dp, Color(0xFF263A5E), RoundedCornerShape(20.dp))
                        .testTag("top_bar_language_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (isAr) "🌐 Français" else "🌐 العربية",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Right: Branding & Notifications
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Notifications bell (on main screens)
                if (currentScreen.isTopLevel) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF13223C))
                            .border(1.dp, OstadiCardBorder, CircleShape)
                            .clickable { onOpenFirebaseInfo() }
                            .testTag("top_bar_notifications_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = GoldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        if (hasNotifications) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(top = 4.dp, end = 4.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonAbsence)
                            )
                        }
                    }
                }

                // Title and graduation cap logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 17.sp
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
