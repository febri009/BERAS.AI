package com.example.berasai.ui.deskripsi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DeskripsiViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is deskripsi Fragment"
    }
    val text: LiveData<String> = _text
}