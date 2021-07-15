package com.uni.information_security.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uni.information_security.ui.chat.ChatViewModel
import com.uni.information_security.ui.chat_info.ChatInfoViewModel
import com.uni.information_security.ui.create_group.CreateGroupViewModel
import com.uni.information_security.ui.login.LoginViewModel
import com.uni.information_security.ui.main.MainViewModel
import com.uni.information_security.ui.personal.PersonalViewModel

class ViewModelFactory(private val context: Context): ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }
        if (modelClass.isAssignableFrom(PersonalViewModel::class.java)) {
            return PersonalViewModel() as T
        }
        if (modelClass.isAssignableFrom(CreateGroupViewModel::class.java)) {
            return CreateGroupViewModel() as T
        }
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel() as T
        }
        if (modelClass.isAssignableFrom(ChatInfoViewModel::class.java)) {
            return ChatInfoViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}