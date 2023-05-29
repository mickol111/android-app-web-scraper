package com.example.po_proj03.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.po_proj03.models.DanceClasses
import org.json.JSONArray

class NotificationsViewModel : ViewModel() {
    val mutableFilters = MutableLiveData<JSONArray>()
    val filters: LiveData<JSONArray> get() = mutableFilters

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
    fun selectFilter(data:JSONArray){
        mutableFilters.value = data
        Log.d("console","selectFilter "+data.toString())
    }
}