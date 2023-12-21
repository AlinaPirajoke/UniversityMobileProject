package com.example.university.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.university.R

val cocon_regular = Font(R.font.cocon_regular, FontWeight.W600)
val Cocon = FontFamily(cocon_regular)

val KotobaTypography = Typography(
    h4 = TextStyle(
        fontFamily = Cocon,
        fontWeight = FontWeight.W600,
        fontSize = 30.sp
    ),

    h5 = TextStyle(
        fontFamily = Cocon,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),

    h6 = TextStyle(
        fontFamily = Cocon,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp
    ),

    subtitle1 = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
        textAlign = TextAlign.Center
    ),

    subtitle2 = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),

    body1 = TextStyle(
        //fontFamily = Domine,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 17.sp
    ),
    button = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.W500,
        fontSize = 15.sp
    ),
    caption = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    )
)