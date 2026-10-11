package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.auth.GoogleAuthHelper
import com.example.ui.navigation.OstadiScreen
import com.example.ui.theme.CrimsonAbsence
import com.example.ui.theme.CrimsonContainer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary

@Composable
fun LoginScreen(
    language: AppLanguage,
    isLoading: Boolean,
    errorMessage: String?,
    onLoadingChange: (Boolean) -> Unit,
    onErrorChange: (String?) -> Unit,
    onAuthSuccess: () -> Unit,
    onNavigate: (OstadiScreen) -> Unit
) {
    val s = AppStrings.get(language)
    val isAr = language == AppLanguage.ARABIC
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    var showRecoveryDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(NavyPrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = s.loginTitle,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = NavyPrimary
        )

        Text(
            text = if (isAr) "تسجيل الدخول الآمن بحساب الأستاذ المستقل" else "Connexion sécurisée Enseignant",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Error Banner if auth failed
        if (errorMessage != null) {
            Surface(
                color = CrimsonContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CrimsonAbsence,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = CrimsonAbsence,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Primary Official Sign-In Button (Google Identity via Jetpack Credential Manager)
        Button(
            onClick = {
                onLoadingChange(true)
                onErrorChange(null)
                GoogleAuthHelper.onGoogleSignInClicked(
                    context = context,
                    credentialManager = credentialManager,
                    onAuthSuccess = {
                        onLoadingChange(false)
                        onAuthSuccess()
                    },
                    onAuthError = { errorMsg ->
                        onLoadingChange(false)
                        onErrorChange(errorMsg)
                    },
                    scope = coroutineScope,
                    onAuthCancelled = {
                        onLoadingChange(false)
                    }
                )
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("google_sign_in_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NavyPrimary,
                contentColor = Color.White
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = if (isAr) "تسجيل الدخول بحساب Google" else "Continuer avec Google",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Account Recovery Button
        TextButton(
            onClick = { showRecoveryDialog = true },
            modifier = Modifier.testTag("account_recovery_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LockReset,
                    contentDescription = null,
                    tint = NavyLight,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isAr) "استرجاع الحساب / مساعدة الدخول" else "Récupération de compte",
                    style = MaterialTheme.typography.bodySmall,
                    color = NavyLight,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Multi-Tenant Isolation Security Note
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (isAr) "أمان وخصوصية حساب كل أستاذ" else "Sécurité & Isolation des données",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }
                Text(
                    text = if (isAr)
                        "يتم إنشاء معرف مشفر فريد (UID) خاص بك عبر Firebase Authentication. تُحفظ بياناتك وأقسامك حصرياً في المسار السحابي /teachers/{UID} ولا يمكن لأي أستاذ آخر الوصول إليها."
                    else
                        "Chaque enseignant dispose d'un identifiant unique (UID) Firebase. Vos classes et notes sont isolées sous /teachers/{UID}.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register link
        TextButton(
            onClick = { onNavigate(OstadiScreen.REGISTER) },
            modifier = Modifier.testTag("login_to_register_link")
        ) {
            Text(
                text = s.noAccountPrompt,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = NavyPrimary
            )
        }
    }

    // Account Recovery Dialog
    if (showRecoveryDialog) {
        AlertDialog(
            onDismissRequest = { showRecoveryDialog = false },
            title = {
                Text(
                    text = if (isAr) "استرجاع الحساب والأمان" else "Récupération de compte",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isAr)
                            "يعتمد تطبيق أستاذي على الحساب المعتمد للأستاذ عبر Firebase و Google Authentication. يمكنك إدارة وتأمين كلمة المرور أو استرجاع حسابك مباشرة من إعدادات حساب Google."
                        else
                            "L'authentification Ostadi est synchronisée avec Firebase et Google. Vous pouvez réinitialiser vos identifiants via le portail de sécurité Google.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://myaccount.google.com/signinoptions/password")
                            )
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isAr) "فتح بوابة أمان حساب Google" else "Portail de sécurité Google")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showRecoveryDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(s.close)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}
