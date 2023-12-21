package com.example.university.usefullStuff

import android.content.Context
import android.widget.Toast


fun showToast(text: String, context: Context) {
    //if(text.isEmpty())
    //    return

    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}