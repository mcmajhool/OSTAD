package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.components.AlgiersMonumentSkyline
import com.example.ui.navigation.OstadiScreen
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.GoldLight
import com.example.ui.theme.OstadiCardBg
import com.example.ui.theme.OstadiCardBorder
import com.example.ui.theme.OstadiDarkBg

@Composable
fun WelcomeScreen(
    language: AppLanguage,
    onNavigate: (OstadiScreen) -> Unit,
    onDemoLogin: () -> Unit,
    onToggleLanguage: () -> Unit
) {
    val s = AppStrings.get(language)
    val isAr = language == AppLanguage.ARABIC
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OstadiDarkBg)
            .statusBarsPadding()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header Bar with Language Switcher and Logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language Pill Button
            Surface(
                onClick = onToggleLanguage,
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
                modifier = Modifier
                    .border(1.dp, Color(0xFF263A5E), RoundedCornerShape(20.dp))
                    .testTag("welcome_language_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (isAr) "🌐 Français" else "🌐 العربية",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // App Name & Graduation Cap Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "أستاذي",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Text(
                        text = if (isAr) "معاً نصنع جيلاً أفضل" else "Ensemble pour l'excellence",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldAccent,
                        fontSize = 10.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF13223C))
                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Hero Art Card with Algerian classroom illustration & golden border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(22.dp))
                .border(1.dp, Color(0xFF334A6E), RoundedCornerShape(22.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ostadi_hero_banner),
                contentDescription = "Ostadi Classroom Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient vignette overlay for depth
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0x9908101E), OstadiDarkBg)
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big Main Branding Title with Graduation Cap
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "أستاذي",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 38.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = GoldLight,
                modifier = Modifier.size(34.dp)
            )
        }

        Text(
            text = if (isAr) "منصة المعلم الشاملة في الجزائر" else "Plateforme Numérique de l'Enseignant Algérien",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = GoldAccent,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (isAr) "إدارة سهلة .. متابعة دقيقة .. تعليم أكثر تأثيراً" else "Gestion fluide • Suivi rigoureux • Pédagogie d'impact",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 3 Feature Cards (Horizontal Row as seen in Image 1)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WelcomeFeaturePill(
                title = if (isAr) "إدارة الأقسام" else "Gestion Classes",
                desc = if (isAr) "تنظيم التلاميذ والمواد بكل سهولة" else "Organisation fluide",
                icon = Icons.Default.People,
                iconColor = GoldAccent,
                modifier = Modifier.weight(1f)
            )
            WelcomeFeaturePill(
                title = if (isAr) "الغيابات والحضور" else "Présences",
                desc = if (isAr) "متابعة دقيقة وسجلات منظمة" else "Suivi automatisé",
                icon = Icons.Default.AssignmentTurnedIn,
                iconColor = EmeraldSuccess,
                modifier = Modifier.weight(1f)
            )
            WelcomeFeaturePill(
                title = if (isAr) "النقاط والتقارير" else "Notes & Bilans",
                desc = if (isAr) "حساب المعدلات وإحصائيات مفصلة" else "Moyennes officielles",
                icon = Icons.Default.Assessment,
                iconColor = Color(0xFF38BDF8),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Primary Action Button 1: Glowing Golden Gradient "الدخول المباشر (نسخة المعاينة)"
        Surface(
            onClick = onDemoLogin,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .testTag("welcome_demo_login_button"),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GoldGradient)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAr) "الدخول المباشر ( نسخة المعاينة )" else "Accès Direct ( Mode Démo )",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1A1305),
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (isAr) "اكتشف المنصة وتجريب جميع المزايا" else "Découvrez toutes les fonctionnalités sans inscription",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4A3409),
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1A1305)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Button 2: Midnight Navy Surface with Gold Border "إنشاء حساب أستاذ أو تسجيل الدخول"
        Surface(
            onClick = { onNavigate(OstadiScreen.LOGIN) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF1E3A66), RoundedCornerShape(16.dp))
                .testTag("welcome_login_button"),
            color = Color(0xFF0F1E38)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAr) "إنشاء حساب أستاذ أو تسجيل الدخول" else "Créer un compte ou Se connecter",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    Text(
                        text = if (isAr) "انضم إلى آلاف الأساتذة في الجزائر لحفظ ومزامنة بياناتك" else "Rejoignez les enseignants algériens connectés",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E3A66)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3 Trust Badges (Bottom Row matching Image 1)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TrustBadgeItem(
                icon = Icons.Default.VerifiedUser,
                title = if (isAr) "آمن وموثوق" else "Sécurisé",
                subtitle = if (isAr) "حماية بياناتك" else "Données protégées"
            )
            TrustBadgeItem(
                icon = Icons.Default.School,
                title = if (isAr) "مصمم للجزائر" else "Conforme",
                subtitle = if (isAr) "وفق البرامج الرسمية" else "Programmes officiels"
            )
            TrustBadgeItem(
                icon = Icons.Default.People,
                title = if (isAr) "مع آلاف الأساتذة" else "Communauté",
                subtitle = if (isAr) "مجتمع دائم النمو" else "Enseignants actifs"
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Algerian Monument of Martyrs (Maqam Echahid) skyline golden line art
        AlgiersMonumentSkyline(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun WelcomeFeaturePill(
    title: String,
    desc: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFF1E314D), RoundedCornerShape(14.dp)),
        color = Color(0xFF0E1A2E)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )

            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
private fun TrustBadgeItem(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color(0xFF142442))
                .border(1.dp, GoldAccent.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 11.sp
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8),
            fontSize = 9.sp
        )
    }
}
