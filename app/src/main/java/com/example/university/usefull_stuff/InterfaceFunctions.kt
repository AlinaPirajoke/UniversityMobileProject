package com.example.university.usefull_stuff

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.university.R


fun showToast(text: String, context: Context) {
    //if(text.isEmpty())
    //    return

    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

// Этого парня можно вот так, вне класса оставлять?
fun getColorScheme(){

}