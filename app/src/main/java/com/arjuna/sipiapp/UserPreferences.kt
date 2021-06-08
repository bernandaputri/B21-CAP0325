package com.arjuna.sipiapp

import android.content.Context
import com.arjuna.sipiapp.data.UserModel

class UserPreferences (context: Context) {

    companion object {
        const val PREFS_NAME = "user_pref"
        const val USERNAME = "username"
        const val NAME = "name"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser (value: UserModel) {
        val editor = preferences.edit()
        editor.putString(USERNAME, value.username)
        editor.putString(NAME, value.name)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.username = preferences.getString(USERNAME, "")
        model.name = preferences.getString(NAME, "")

        return model
    }

    fun removeUser() {
        val editor = preferences.edit()
        editor.remove(USERNAME)
        editor.remove(NAME)
        editor.apply()
    }

}