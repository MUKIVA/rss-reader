package com.mukiva.navigation.ui.theme

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object RssTypography : ReadOnlyProperty<Any, Typography> {

    private val headline: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        lineHeight = 23.sp
    )
    private val title: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        lineHeight = 16.sp
    )
    private val body: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal
    )
    private val label: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal
    )

    fun createTypography(): Typography = Typography(
        headlineLarge = headline,
        headlineMedium = headline,
        headlineSmall = headline,
        bodyLarge = body,
        bodyMedium = body,
        bodySmall = body,
        titleLarge = title,
        titleMedium = title,
        titleSmall = title,
        labelLarge = label,
        labelMedium = label,
        labelSmall = label
    )

    override fun getValue(thisRef: Any, property: KProperty<*>): Typography {
        return createTypography()
    }
}
