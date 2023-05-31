package com.example.po_proj03.ui.notifications

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.po_proj03.R
import com.example.po_proj03.databinding.FragmentNotificationsBinding
import okhttp3.Headers
import org.json.JSONArray


class NotificationsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var lessonArr = arrayOf("Trening indywidualny par turniejowych", "Grupa Turniejowa", "Grupa Dorosłych Zaawansowana",
        "Grupa Lady Solo", "Grupa Dorosłych Początkująca","Grupa przedszkolna","Grupa Zaawansowana Dziecięca",
        "Żeliwsławki Grupa Dorosłych","")
    private var locationArr = arrayOf("LOKALIZACJA SP 1", "LOKALIZACJA ZS 2", "LOKALIZACJA SP 3",
        "LOKALIZACJA CKIS", "ŻELISŁAWKI","")
    private val viewModel: NotificationsViewModel by activityViewModels()

    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!

    var data =  JSONArray();
    private val client = AsyncHttpClient()
    private var lesson: String? = null
    var location: String? = null
    private var query: String = ""
    private val queryAll: String =  "day=Poniedziałek&day=Wtorek&day=Środa&day=Czwartek&day=Piątek"
    var connectionResponse:String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val spinnerLesson = root.findViewById<Spinner>(R.id.lesson_spinner)
        spinnerLesson.onItemSelectedListener = this
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.lessons_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerLesson.adapter = adapter
            }
        }
        val spinnerLocation = root.findViewById<Spinner>(R.id.location_spinner)
        spinnerLocation.onItemSelectedListener = this
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.locations_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerLocation.adapter = adapter
            }
        }

        val filterBtn = root.findViewById<Button>(R.id.button2)
        val scrapeBtn = root.findViewById<Button>(R.id.button3)
        val txtFr1 = root.findViewById<TextView>(R.id.txtFr1)
        val params = RequestParams()

        filterBtn.setOnClickListener {
            txtFr1.text = "Filter"

            Log.i("console", lesson!!)
            Log.i("console", location!!)

            query = filter(location!!,lesson!!)

            jsonFromNet(params,query)
            this.findNavController().navigate(R.id.navigation_notifications_to_navigation_plan)
        }

        scrapeBtn.setOnClickListener {
            query = "scrape=1"
            jsonFromNet(params,query)
            txtFr1.text = "Scraping completed"
        }

        return root
    }

private fun jsonFromNet(in_params:RequestParams, query:String) {
    client["http://10.0.2.2:1234/?$query", in_params, object :
        JsonHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
            Log.i("console", "successs")
            Log.i("console",statusCode.toString())
            Log.i("console",headers.toString())
            if(json.jsonArray != null) {
                Log.i("console",json.toString())
                data = JSONArray()
                data = json.jsonArray
                viewModel.selectFilter(data)
            }
            else {
                Log.i("console","json is null")
            }
        }

        override fun onFailure(
            statusCode: Int,
            headers: Headers?,
            response: String,
            throwable: Throwable?
        ) {
            Log.d("console", "failure")
            Log.d("console", response)
        }
    }]
}

private fun filter(loc:String, les:String): String {
    var outQuery = ""
    if(les.equals("") and loc.equals("")) {
        outQuery = queryAll
    }
        else if (loc.equals("") and !les.equals("")) {
            outQuery =  "lesson="+les
    }
    else if (!loc.equals("") and les.equals("")) {
        outQuery = "location=$loc"
    }
    else if (!loc.equals("") and !les.equals("")) {
        outQuery = "location=$loc&lesson=$les"
    }
    return outQuery
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            when(parent.id){
                R.id.lesson_spinner ->{
                    lesson = lessonArr[position]
                    Log.i("console", lesson!!)
                }
                R.id.location_spinner ->{
                    location = locationArr[position]
                    Log.i("console", location!!)
                }
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        if (parent != null) {
            when(parent.id){
                R.id.lesson_spinner ->{
                    lesson = lessonArr[0]
                    Log.i("console", lesson!!)
                }
                R.id.location_spinner ->{
                    location = locationArr[0]
                    Log.i("console", location!!)
                }
            }
        }
    }
}
