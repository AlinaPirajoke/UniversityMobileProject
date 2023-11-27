package com.example.university.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

val KotobaTypography = Typography(
    h4 = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.W600,
        fontSize = 30.sp
    ),

    h5 = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.W400,
        fontSize = 25.sp
    ),

    h6 = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),

    subtitle1 = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        textAlign = TextAlign.Center
    ),

    subtitle2 = TextStyle(
        //fontFamily = Montserrat,
        fontWeight = FontWeight.W300,
        fontSize = 14.sp
    ),

    body1 = TextStyle(
        //fontFamily = Domine,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp
    ),
    body2 = TextStyle(
        //fontFamily = Montserrat,
        fontSize = 14.sp
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