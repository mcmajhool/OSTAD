package com.example.localization

object AppStrings {
    fun get(lang: AppLanguage): Strings = when (lang) {
        AppLanguage.ARABIC -> ArabicStrings
        AppLanguage.FRENCH -> FrenchStrings
    }

    interface Strings {
        val appName: String
        val appTagline: String
        val welcomeTitle: String
        val welcomeSubtitle: String
        val startButton: String
        val loginButton: String
        val registerButton: String
        val demoModeButton: String
        val loginTitle: String
        val loginSubtitle: String
        val registerTitle: String
        val registerSubtitle: String
        val emailLabel: String
        val passwordLabel: String
        val teacherNameLabel: String
        val schoolNameLabel: String
        val wilayaLabel: String
        val subjectLabel: String
        val cycleLabel: String
        val noAccountPrompt: String
        val alreadyHaveAccountPrompt: String
        
        // Navigation & Titles
        val navDashboard: String
        val navClassrooms: String
        val navStudents: String
        val navAttendance: String
        val navGrades: String
        val navParticipation: String
        val navStatistics: String
        val navProfile: String
        
        // Dashboard cards
        val totalClassrooms: String
        val totalStudents: String
        val maleStudents: String
        val femaleStudents: String
        val todayAbsences: String
        val upcomingExams: String
        val classroomAverages: String
        val quickActions: String
        val takeAttendanceAction: String
        val addGradeAction: String
        val addStudentAction: String
        val addClassroomAction: String
        
        // Preview mode badge & info
        val previewModeBadge: String
        val previewModeBannerTitle: String
        val previewModeBannerDesc: String
        val firebaseReadyNotice: String
        val close: String
        
        // Classrooms
        val classroomsTitle: String
        val addClassroomTitle: String
        val classroomNamePlaceholder: String
        val levelLabel: String
        val roomNumberLabel: String
        val save: String
        val cancel: String
        val studentsCountSuffix: String
        val filterAllLevels: String
        
        // Students
        val studentsTitle: String
        val searchStudentPlaceholder: String
        val addStudentTitle: String
        val firstNameLabel: String
        val lastNameLabel: String
        val genderLabel: String
        val male: String
        val female: String
        val parentPhoneLabel: String
        val regNumberLabel: String
        val filterByClass: String
        val allClassrooms: String
        val callParent: String
        val viewDetails: String
        
        // Attendance
        val attendanceTitle: String
        val sessionLabel: String
        val dateLabel: String
        val present: String
        val absent: String
        val late: String
        val excused: String
        val saveAttendance: String
        val attendanceRecordedSuccess: String
        val todayStats: String
        val presenceRate: String
        
        // Grades & Exams
        val gradesTitle: String
        val term1: String
        val term2: String
        val term3: String
        val continuousAssessment: String
        val testExam: String
        val finalExam: String
        val calculatedAverage: String
        val outOfTwenty: String
        val mentionExcellent: String
        val mentionVeryGood: String
        val mentionGood: String
        val mentionAcceptable: String
        val mentionInsufficient: String
        val saveGrades: String
        
        // Participation
        val participationTitle: String
        val starsCount: String
        val behaviorRemark: String
        val addStar: String
        val removeStar: String
        val excellentParticipation: String
        val goodParticipation: String
        val needsImprovement: String
        
        // Statistics
        val statisticsTitle: String
        val successRate: String
        val attendanceAverage: String
        val genderRatio: String
        val highestAverage: String
        val lowestAverage: String
        val performanceSummary: String
        val topStudents: String
        
        // Profile & Settings
        val profileTitle: String
        val personalInfo: String
        val systemSettings: String
        val appLanguageSetting: String
        val academicYearLabel: String
        val currentAcademicYear: String
        val logout: String
        val futureFirebaseNoticeTitle: String
        val futureFirebaseNoticeBody: String
    }

    private object ArabicStrings : Strings {
        override val appName = "أستاذي"
        override val appTagline = "المساعد الرقمي المتكامل للأساتذة في الجزائر"
        override val welcomeTitle = "مرحباً بك في منصة أستاذي"
        override val welcomeSubtitle = "نظام حديث واحترافي لمتابعة الأقسام، التلاميذ، الغيابات، ونقاط الفصول بدقة متناهية"
        override val startButton = "ابدأ الاستخدام"
        override val loginButton = "تسجيل الدخول"
        override val registerButton = "إنشاء حساب أستاذ جديد"
        override val demoModeButton = "الدخول المباشر (نسخة المعاينة التجريبية)"
        override val loginTitle = "تسجيل الدخول"
        override val loginSubtitle = "أدخل بيانات حسابك كأستاذ للمتابعة"
        override val registerTitle = "تسجيل أستاذ جديد"
        override val registerSubtitle = "انضم إلى أساتذة الجزائر ونظم دفاتر أقسامك بسهولة"
        override val emailLabel = "البريد الإلكتروني"
        override val passwordLabel = "كلمة المرور"
        override val teacherNameLabel = "اسم الأستاذ الكامل"
        override val schoolNameLabel = "اسم المؤسسة التعليمية (متوسطة / ثانوية / ابتدائية)"
        override val wilayaLabel = "الولاية"
        override val subjectLabel = "المادة المدرّسة"
        override val cycleLabel = "الطور التعليمي"
        override val noAccountPrompt = "ليس لديك حساب؟ سجّل الآن"
        override val alreadyHaveAccountPrompt = "لديك حساب بالفعل؟ سجّل دخولك"

        override val navDashboard = "الرئيسية"
        override val navClassrooms = "الأقسام"
        override val navStudents = "التلاميذ"
        override val navAttendance = "الغيابات"
        override val navGrades = "النقاط"
        override val navParticipation = "المشاركة"
        override val navStatistics = "الإحصائيات"
        override val navProfile = "الإعدادات"

        override val totalClassrooms = "مجموع الأقسام"
        override val totalStudents = "مجموع التلاميذ"
        override val maleStudents = "الذكور"
        override val femaleStudents = "الإناث"
        override val todayAbsences = "غيابات اليوم"
        override val upcomingExams = "الامتحانات القادمة"
        override val classroomAverages = "معدلات الأقسام"
        override val quickActions = "الوصول السريع"
        override val takeAttendanceAction = "تسجيل الغياب"
        override val addGradeAction = "إدخال نقطة"
        override val addStudentAction = "تلميذ جديد"
        override val addClassroomAction = "إضافة قسم"

        override val previewModeBadge = "نسخة معاينة تجريبية"
        override val previewModeBannerTitle = "جاهزية بنية Firebase السحابية"
        override val previewModeBannerDesc = "بيانات تجريبية محلية تمهيداً لربط Firebase Authentication و Firestore لتأمين وحفظ حساب كل أستاذ بشكل مستقل."
        override val firebaseReadyNotice = "هذه النسخة مبنية بهندسة برمجية نظيفة (MVVM) مع عزل كامل لحساب الأستاذ، وستتصل مباشرة بقاعدة بيانات Firebase في المرحلة التالية دون تغيير واجهة المستخدم."
        override val close = "إغلاق"

        override val classroomsTitle = "إدارة الأقسام والأفواج"
        override val addClassroomTitle = "إضافة قسم جديد"
        override val classroomNamePlaceholder = "اسم القسم (مثال: 4 متوسط 1)"
        override val levelLabel = "المستوى التعليمي"
        override val roomNumberLabel = "رقم القاعة"
        override val save = "حفظ"
        override val cancel = "إلغاء"
        override val studentsCountSuffix = "تلميذ"
        override val filterAllLevels = "جميع المستويات"

        override val studentsTitle = "قائمة التلاميذ"
        override val searchStudentPlaceholder = "البحث بالاسم أو اللقب أو الرقم..."
        override val addStudentTitle = "إضافة تلميذ جديد"
        override val firstNameLabel = "الاسم"
        override val lastNameLabel = "اللقب"
        override val genderLabel = "الجنس"
        override val male = "ذكر"
        override val female = "أنثى"
        override val parentPhoneLabel = "رقم هاتف الولي"
        override val regNumberLabel = "رقم التسجيل المدرسي"
        override val filterByClass = "تصفية حسب القسم"
        override val allClassrooms = "جميع الأقسام"
        override val callParent = "اتصال بالولي"
        override val viewDetails = "عرض التفاصيل"

        override val attendanceTitle = "دفتر الحضور والغياب"
        override val sessionLabel = "الحصة التعليمية"
        override val dateLabel = "التاريخ"
        override val present = "حاضر"
        override val absent = "غائب"
        override val late = "متأخر"
        override val excused = "مبرر"
        override val saveAttendance = "تثبيت حضور الحصة"
        override val attendanceRecordedSuccess = "تم تسجيل قائمة الحضور بنجاح"
        override val todayStats = "إحصائية غيابات اليوم"
        override val presenceRate = "نسبة الحضور الإجمالية"

        override val gradesTitle = "دفتر التنقيط والامتحانات"
        override val term1 = "الفصل الأول"
        override val term2 = "الفصل الثاني"
        override val term3 = "الفصل الثالث"
        override val continuousAssessment = "التقويم المستمر"
        override val testExam = "الفرض المحروس"
        override val finalExam = "الاختبار الفصلي"
        override val calculatedAverage = "المعدل الفصلي"
        override val outOfTwenty = "/ 20"
        override val mentionExcellent = "ممتاز"
        override val mentionVeryGood = "جيد جداً"
        override val mentionGood = "جيد"
        override val mentionAcceptable = "مقبول"
        override val mentionInsufficient = "دون المتوسط"
        override val saveGrades = "حفظ النقاط"

        override val participationTitle = "المشاركة والسلوك داخل القسم"
        override val starsCount = "نجوم التفاعل"
        override val behaviorRemark = "ملاحظة السلوك"
        override val addStar = "إضافة نقطة تميز"
        override val removeStar = "خصم نقطة"
        override val excellentParticipation = "مشاركة ممتازة ونشيطة"
        override val goodParticipation = "مشاركة جيدة ومنتظمة"
        override val needsImprovement = "يحتاج إلى تركيز أكثر"

        override val statisticsTitle = "الإحصائيات والتقارير التربوية"
        override val successRate = "نسبة المعدلات الإيجابية (≥ 10/20)"
        override val attendanceAverage = "معدل الحضور والانضباط"
        override val genderRatio = "توزيع التلاميذ حسب الجنس"
        override val highestAverage = "أعلى معدل فصلي"
        override val lowestAverage = "أدنى معدل مسجل"
        override val performanceSummary = "ملخص الأداء العام للمستويات"
        override val topStudents = "التلاميذ المتفوقون (لوحة الشرف)"

        override val profileTitle = "الملف الشخصي والإعدادات"
        override val personalInfo = "المعلومات التربوية للأستاذ"
        override val systemSettings = "إعدادات التطبيق"
        override val appLanguageSetting = "لغة التطبيق (اللغة)"
        override val academicYearLabel = "السنة الدراسية"
        override val currentAcademicYear = "2024 / 2025"
        override val logout = "تسجيل الخروج"
        override val futureFirebaseNoticeTitle = "جاهز للربط السحابي مع Firebase"
        override val futureFirebaseNoticeBody = "تطبيق أستاذي مصمم ليعمل مع Firebase Authentication و Cloud Firestore ليتمكن كل أستاذ في الجزائر من إنشاء حسابه الخاص ومزامنة أقسامه وتلاميذه بين هاتفه وجهازه اللوحي بكل أمان."
    }

    private object FrenchStrings : Strings {
        override val appName = "Ostadi"
        override val appTagline = "L'assistant numérique des enseignants en Algérie"
        override val welcomeTitle = "Bienvenue sur Ostadi"
        override val welcomeSubtitle = "Solution moderne et professionnelle pour gérer vos classes, élèves, absences et notes scolaires."
        override val startButton = "Commencer"
        override val loginButton = "Se connecter"
        override val registerButton = "Créer un compte Enseignant"
        override val demoModeButton = "Mode Aperçu Démo"
        override val loginTitle = "Connexion Enseignant"
        override val loginSubtitle = "Accédez à vos classes et registres"
        override val registerTitle = "Inscription Enseignant"
        override val registerSubtitle = "Rejoignez la communauté éducative nationale"
        override val emailLabel = "Adresse e-mail"
        override val passwordLabel = "Mot de passe"
        override val teacherNameLabel = "Nom complet de l'enseignant"
        override val schoolNameLabel = "Établissement (CEM / Lycée / Primaire)"
        override val wilayaLabel = "Wilaya"
        override val subjectLabel = "Matière enseignée"
        override val cycleLabel = "Cycle d'enseignement"
        override val noAccountPrompt = "Pas encore de compte ? Inscrivez-vous"
        override val alreadyHaveAccountPrompt = "Déjà un compte ? Connectez-vous"

        override val navDashboard = "Tableau de bord"
        override val navClassrooms = "Classes"
        override val navStudents = "Élèves"
        override val navAttendance = "Absences"
        override val navGrades = "Notes"
        override val navParticipation = "Participation"
        override val navStatistics = "Statistiques"
        override val navProfile = "Profil"

        override val totalClassrooms = "Total Classes"
        override val totalStudents = "Total Élèves"
        override val maleStudents = "Garçons"
        override val femaleStudents = "Filles"
        override val todayAbsences = "Absences du jour"
        override val upcomingExams = "Examens à venir"
        override val classroomAverages = "Moyennes de classe"
        override val quickActions = "Actions rapides"
        override val takeAttendanceAction = "Faire l'appel"
        override val addGradeAction = "Ajouter note"
        override val addStudentAction = "Nouvel élève"
        override val addClassroomAction = "Nouvelle classe"

        override val previewModeBadge = "Mode Aperçu Démo"
        override val previewModeBannerTitle = "Architecture prête pour Firebase"
        override val previewModeBannerDesc = "Données d'aperçu locales réalistes prêtes à être connectées à Firebase Auth et Cloud Firestore pour sécuriser les données de chaque enseignant."
        override val firebaseReadyNotice = "Cette version utilise une architecture MVVM propre, prête pour l'intégration de Firebase Cloud Firestore sans modification de l'interface."
        override val close = "Fermer"

        override val classroomsTitle = "Gestion des Classes et Groupes"
        override val addClassroomTitle = "Ajouter une classe"
        override val classroomNamePlaceholder = "Nom de la classe (ex: 4AM1)"
        override val levelLabel = "Niveau scolaire"
        override val roomNumberLabel = "Numéro de salle"
        override val save = "Enregistrer"
        override val cancel = "Annuler"
        override val studentsCountSuffix = "élèves"
        override val filterAllLevels = "Tous les niveaux"

        override val studentsTitle = "Liste des Élèves"
        override val searchStudentPlaceholder = "Rechercher par nom, prénom ou n°..."
        override val addStudentTitle = "Ajouter un élève"
        override val firstNameLabel = "Prénom"
        override val lastNameLabel = "Nom"
        override val genderLabel = "Genre"
        override val male = "Garçon"
        override val female = "Fille"
        override val parentPhoneLabel = "Téléphone du parent"
        override val regNumberLabel = "N° d'immatriculation"
        override val filterByClass = "Filtrer par classe"
        override val allClassrooms = "Toutes les classes"
        override val callParent = "Appeler parent"
        override val viewDetails = "Détails"

        override val attendanceTitle = "Registre de Présence et d'Absences"
        override val sessionLabel = "Séance de cours"
        override val dateLabel = "Date"
        override val present = "Présent"
        override val absent = "Absent"
        override val late = "En retard"
        override val excused = "Justifié"
        override val saveAttendance = "Valider l'appel"
        override val attendanceRecordedSuccess = "Appel enregistré avec succès"
        override val todayStats = "Bilan des absences aujourd'hui"
        override val presenceRate = "Taux de présence globale"

        override val gradesTitle = "Registre des Notes et Évaluations"
        override val term1 = "1er Trimestre"
        override val term2 = "2ème Trimestre"
        override val term3 = "3ème Trimestre"
        override val continuousAssessment = "Évaluation continue"
        override val testExam = "Devoir surveillé"
        override val finalExam = "Composition"
        override val calculatedAverage = "Moyenne trimestrielle"
        override val outOfTwenty = "/ 20"
        override val mentionExcellent = "Excellent"
        override val mentionVeryGood = "Très bien"
        override val mentionGood = "Bien"
        override val mentionAcceptable = "Passable"
        override val mentionInsufficient = "Insuffisant"
        override val saveGrades = "Sauvegarder les notes"

        override val participationTitle = "Discipline et Participation en classe"
        override val starsCount = "Étoiles de mérite"
        override val behaviorRemark = "Observation de conduite"
        override val addStar = "Ajouter un point"
        override val removeStar = "Retirer un point"
        override val excellentParticipation = "Participation remarquable et active"
        override val goodParticipation = "Bonne participation régulière"
        override val needsImprovement = "Doit redoubler d'efforts"

        override val statisticsTitle = "Statistiques et Rapports Pédagogiques"
        override val successRate = "Taux de réussite (≥ 10/20)"
        override val attendanceAverage = "Assiduité moyenne"
        override val genderRatio = "Répartition garçons / filles"
        override val highestAverage = "Meilleure moyenne"
        override val lowestAverage = "Plus basse moyenne"
        override val performanceSummary = "Aperçu de la performance scolaire"
        override val topStudents = "Tableau d'Honneur (Meilleurs élèves)"

        override val profileTitle = "Profil Enseignant et Paramètres"
        override val personalInfo = "Informations Pédagogiques"
        override val systemSettings = "Paramètres de l'application"
        override val appLanguageSetting = "Langue de l'application"
        override val academicYearLabel = "Année scolaire"
        override val currentAcademicYear = "2024 / 2025"
        override val logout = "Se déconnecter"
        override val futureFirebaseNoticeTitle = "Prêt pour la synchronisation Cloud Firebase"
        override val futureFirebaseNoticeBody = "L'application Ostadi est configurée pour fonctionner avec Firebase Auth et Firestore afin de synchroniser les classes et données privées de chaque enseignant en toute sécurité."
    }
}
