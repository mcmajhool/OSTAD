package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.auth.GoogleAuthHelper
import com.example.ui.navigation.OstadiScreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    language: AppLanguage,
    isLoading: Boolean,
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

    var firstName by remember { mutableStateOf("محمد") }
    var lastName by remember { mutableStateOf("بن سالم") }
    var schoolName by remember { mutableStateOf("متوسطة الشهيد العربي بن مهيدي") }
    var wilaya by remember { mutableStateOf("16 - الجزائر العاصمة") }
    var cycle by remember { mutableStateOf(if (isAr) "التعليم المتوسط (CEM)" else "Enseignement Moyen (CEM)") }
    var subject by remember { mutableStateOf(if (isAr) "مادة الرياضيات" else "Mathématiques") }

    val wilayas = listOf(
        "16 - الجزائر العاصمة", "31 - وهران", "25 - قسنطينة", "19 - سطيف",
        "09 - البليدة", "23 - عنابة", "15 - تيزي وزو", "05 - باتنة", "13 - تلمسان"
    )

    val cycles = listOf(
        if (isAr) "التعليم المتوسط (CEM)" else "Enseignement Moyen (CEM)",
        if (isAr) "التعليم الثانوي (Lycée)" else "Enseignement Secondaire (Lycée)",
        if (isAr) "التعليم الابتدائي (Primaire)" else "Enseignement Primaire"
    )

    var wilayaExpanded by remember { mutableStateOf(false) }
    var cycleExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(NavyPrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = s.registerTitle,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = NavyPrimary
        )

        Text(
            text = if (isAr) "إنشاء حساب أستاذ مستقل على سحابة Firebase" else "Inscription Enseignant sur Cloud Firebase",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // First Name
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(if (isAr) "الاسم" else "Prénom") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary) },
            modifier = Modifier.fillMaxWidth().testTag("register_firstname_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Last Name
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(if (isAr) "اللقب" else "Nom") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary) },
            modifier = Modifier.fillMaxWidth().testTag("register_lastname_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // School Name
        OutlinedTextField(
            value = schoolName,
            onValueChange = { schoolName = it },
            label = { Text(s.schoolNameLabel) },
            leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = NavyPrimary) },
            modifier = Modifier.fillMaxWidth().testTag("register_school_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subject
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text(s.subjectLabel) },
            leadingIcon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = NavyPrimary) },
            modifier = Modifier.fillMaxWidth().testTag("register_subject_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Wilaya Dropdown
        ExposedDropdownMenuBox(
            expanded = wilayaExpanded,
            onExpandedChange = { wilayaExpanded = !wilayaExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = wilaya,
                onValueChange = {},
                readOnly = true,
                label = { Text(s.wilayaLabel) },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = NavyPrimary) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = wilayaExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = wilayaExpanded,
                onDismissRequest = { wilayaExpanded = false }
            ) {
                wilayas.forEach { w ->
                    DropdownMenuItem(
                        text = { Text(w) },
                        onClick = {
                            wilaya = w
                            wilayaExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Cycle Dropdown
        ExposedDropdownMenuBox(
            expanded = cycleExpanded,
            onExpandedChange = { cycleExpanded = !cycleExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = cycle,
                onValueChange = {},
                readOnly = true,
                label = { Text(s.cycleLabel) },
                leadingIcon = { Icon(Icons.Default.School, contentDescription = null, tint = NavyPrimary) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cycleExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = cycleExpanded,
                onDismissRequest = { cycleExpanded = false }
            ) {
                cycles.forEach { c ->
                    DropdownMenuItem(
                        text = { Text(c) },
                        onClick = {
                            cycle = c
                            cycleExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Register and authenticate with Google
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
                .testTag("register_google_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
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
                        text = if (isAr) "تسجيل الحساب وتفعيله عبر Google" else "Créer le compte via Google",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        TextButton(
            onClick = { onNavigate(OstadiScreen.LOGIN) },
            modifier = Modifier.testTag("register_to_login_button")
        ) {
            Text(
                text = s.alreadyHaveAccountPrompt,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = NavyPrimary
            )
        }
    }
}
