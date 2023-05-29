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
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.po_proj03.R
import com.example.po_proj03.databinding.FragmentNotificationsBinding
import com.example.po_proj03.models.DanceClasses
import com.example.po_proj03.ui.calendar.PlanFragment
import okhttp3.Headers
import org.json.JSONArray


class NotificationsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    var lessonArr = arrayOf("Trening indywidualny par turniejowych", "Grupa Turniejowa", "Grupa Dorosłych Zaawansowana",
        "Grupa Lady Solo", "Grupa Dorosłych Początkująca","Grupa przedszkolna","Grupa Zaawansowana Dziecięca",
        "Żeliwsławki Grupa Dorosłych","")
    var locationArr = arrayOf("LOKALIZACJA SP 1", "LOKALIZACJA ZS 2", "LOKALIZACJA SP 3",
        "LOKALIZACJA CKIS", "ŻELISŁAWKI","")
    private val viewModel: NotificationsViewModel by activityViewModels()

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var data =  JSONArray();
    val client = AsyncHttpClient()
    //var lessonEditText: EditText? = null
    var lesson:String? = null
    //var locationEditText: EditText? = null
    var location:String? = null
    var query:String = ""
    val query_all:String =  "day=Poniedziałek&day=Wtorek&day=Środa&day=Czwartek&day=Piątek";

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
/*
val spin = root.findViewById<Spinner>(R.id.lesson_spinner)
        spin.setOnItemClickListener(this)

 */
        val spinnerLesson = root.findViewById<Spinner>(R.id.lesson_spinner)
        spinnerLesson.onItemSelectedListener = this
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.lessons_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
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
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerLocation.adapter = adapter
            }
        }

        val filterBtn = root.findViewById<Button>(R.id.button2)
        val scrapeBtn = root.findViewById<Button>(R.id.button3)
        val txtFr1 = root.findViewById<TextView>(R.id.txtFr1)
        val params = RequestParams()
        //params["location"] = "LOKALIZACJA SP 2"
        //params["location"] = ""
        //params["lesson"] = ""

        val fragment: Fragment = PlanFragment()

        val fm: FragmentManager = parentFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction()

        filterBtn.setOnClickListener {
            filterBtn.setBackgroundColor(Color.RED)
            Log.i("console", "click")
            txtFr1.text = "Filter"


            Log.i("console", lesson!!)
            Log.i("console", location!!)

            query = filter(location!!,lesson!!)

        jsonFromNet(params,query)
            Log.i("console", dataToSet().toString())




            transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
            transaction.commit()

        }
        scrapeBtn.setOnClickListener {
            scrapeBtn.setBackgroundColor(Color.YELLOW)

            query="scrape=1"
            jsonFromNet(params,query)
            txtFr1.text = "Scraping completed"
        }

        return root
    }

fun jsonFromNet(in_params:RequestParams,query:String) {
    client["http://10.0.2.2:1234/?"+query, in_params, object :
        JsonHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
            // Access a JSON array response with `json.jsonArray`
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

fun filter(loc:String,les:String): String {
    var out_query:String = ""
    //val out_params = RequestParams()
    if(les.equals("") and loc.equals("")) {
        //out_params["location"] = ""
        //out_params["lesson"] = ""
        out_query = query_all
    }
        else if (loc.equals("") and !les.equals("")) {
            out_query =  "lesson="+les
    }
    else if (!loc.equals("") and les.equals("")) {
        out_query =  "location="+loc
    }
    else if (!loc.equals("") and !les.equals("")) {
        out_query =  "location="+loc+"&lesson="+les
    }
    return out_query
}

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataToSet(): MutableSet<DanceClasses>? {
        var sdc: MutableSet<DanceClasses>? = null
        for (i in 0 until data.length()) {
            val dc = data.getJSONObject(i)
            val danceClasses:DanceClasses = DanceClasses(
                dc.get("timeslot-start") as String,
                dc.get("timeslot-end") as String,
                dc.get("day") as String, dc.get("lesson") as String,dc.get("location") as String)
            if (sdc != null) {
                sdc.add(danceClasses)
            }
            else
            {
                sdc = mutableSetOf(danceClasses)
            }
        }
        return sdc
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
