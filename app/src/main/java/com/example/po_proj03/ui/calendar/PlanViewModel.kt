package com.example.po_proj03.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is calendar Fragment"
    }
    val text: LiveData<String> = _text
}