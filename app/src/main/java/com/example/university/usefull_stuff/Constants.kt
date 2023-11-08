package com.example.university.usefull_stuff

import androidx.datastore.preferences.core.stringPreferencesKey

object UserScheme {
    val FIELD_NAME = stringPreferencesKey("name")
    val FIELD_LAST_NAME = stringPreferencesKey("last_name")
    val FIELD_AGE = stringPreferencesKey("age")
    val FIELD_ACTIVE = stringPreferencesKey("active")
}
