package com.example.ui.navigation

enum class OstadiScreen(val route: String) {
    WELCOME("welcome"),
    LOGIN("login"),
    REGISTER("register"),
    DASHBOARD("dashboard"),
    CLASSROOMS("classrooms"),
    STUDENTS("students"),
    ATTENDANCE("attendance"),
    GRADES("grades"),
    PARTICIPATION("participation"),
    STATISTICS("statistics"),
    PROFILE_SETTINGS("profile_settings");

    val isTopLevel: Boolean
        get() = this in listOf(
            DASHBOARD,
            CLASSROOMS,
            STUDENTS,
            ATTENDANCE,
            GRADES,
            PARTICIPATION,
            STATISTICS,
            PROFILE_SETTINGS
        )
}
