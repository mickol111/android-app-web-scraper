package com.example.po_proj03.ui.notifications

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray

class NotificationsViewModel : ViewModel() {
    private val mutableFilters = MutableLiveData<JSONArray?>()
    val filters: MutableLiveData<JSONArray?> get() = mutableFilters
    fun selectFilter(data:JSONArray){
        mutableFilters.value = data
        Log.d("console", "selectFilter $data")
    }
}