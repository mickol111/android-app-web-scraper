package com.example.po_proj03.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    private val _textInfo = MutableLiveData<String>().apply {
        value = "Programowanie Obiektowe\nProjekt 03\n" +
                "MK"
    }
    val text: LiveData<String> = _text
    val textInfo: LiveData<String> = _textInfo
}