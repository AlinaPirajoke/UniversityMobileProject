package com.example.university.UsefullStuff

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

// Этого парня можно вот так, вне класса оставлять?
fun getColorScheme(){

}