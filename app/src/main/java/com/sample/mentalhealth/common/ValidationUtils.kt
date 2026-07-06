package com.sample.mentalhealth.common

import android.util.Patterns
import java.util.Calendar

object ValidationUtils {

    fun isValidFullName(name: String): Boolean {
        val trimmed = name.trim()
        return trimmed.length >= 3 && trimmed.matches(Regex("^[a-zA-Z]+(?:\\s[a-zA-Z]+)+$"))
    }

    fun isValidUsername(username: String): Boolean {
        val trimmed = username.trim()
        return trimmed.length >= 3 && trimmed.matches(Regex("^[a-zA-Z0-9_.]+$"))
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    fun isValidDob(dob: String): Boolean {
        // Check format YYYY-MM-DD
        if (!dob.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))) return false

        val parts = dob.split("-")
        val year = parts[0].toIntOrNull() ?: return false
        val month = parts[1].toIntOrNull() ?: return false
        val day = parts[2].toIntOrNull() ?: return false

        // Check valid ranges
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return day in 1..31 && month in 1..12 && year in 1900..currentYear
    }

    fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }

    fun passwordsMatch(password: String, confirm: String): Boolean {
        return password == confirm
    }

    fun getPasswordStrength(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.NONE
        if (password.length < 8) return PasswordStrength.WEAK

        var score = 0
        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++

        return when {
            score <= 2 -> PasswordStrength.WEAK
            score == 3 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }
    }
}

enum class PasswordStrength(val label: String) {
    NONE("Password strength"),
    WEAK("Weak password"),
    MEDIUM("Medium strength"),
    STRONG("Strong password")
}