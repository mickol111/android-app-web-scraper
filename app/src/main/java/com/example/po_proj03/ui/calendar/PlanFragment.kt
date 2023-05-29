package com.example.po_proj03.ui.calendar

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.po_proj03.databinding.FragmentPlanBinding
import com.example.po_proj03.models.DanceClasses
import com.example.po_proj03.ui.notifications.NotificationsViewModel
import org.json.JSONArray


public class PlanFragment : Fragment() {

    private var _binding: FragmentPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by activityViewModels()

    var data =  JSONArray();
     var dCList : MutableList<DanceClasses>? = null
    final val colorsMap:Map<String,Int?> = mapOf("Trening indywidualny par turniejowych" to Color.parseColor("blue"),
        "Grupa Turniejowa" to Color.parseColor("#636100"), "Grupa Dorosłych Zaawansowana" to Color.parseColor("#634061"),
        "Grupa Lady Solo" to Color.parseColor("#636120"), "Grupa Dorosłych Początkująca" to Color.parseColor("#635061"),
        "Grupa przedszkolna" to Color.parseColor("#636140"), "Grupa Zaawansowana Dziecięca" to Color.parseColor("#637061"),
        "Żeliwsławki Grupa Dorosłych" to Color.parseColor("#636611"))

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val planViewModel =
                ViewModelProvider(this).get(PlanViewModel::class.java)

        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val btn = root.findViewById<Button>(com.example.po_proj03.R.id.button4)
        val textView = root.findViewById<TextView>(com.example.po_proj03.R.id.textView)
        val contentRow = root.findViewById<TableRow>(com.example.po_proj03.R.id.content_row)
        val llP = root.findViewById<LinearLayout>(com.example.po_proj03.R.id.llP)
        val llW = root.findViewById<LinearLayout>(com.example.po_proj03.R.id.llW)
        val llS = root.findViewById<LinearLayout>(com.example.po_proj03.R.id.llS)
        val llC = root.findViewById<LinearLayout>(com.example.po_proj03.R.id.llC)
        val llPt = root.findViewById<LinearLayout>(com.example.po_proj03.R.id.llPt)
        val llSb = root.findViewById<LinearLayout>(com.example.po_proj03.R.id.llSb)
        val llN = root.findViewById<LinearLayout>(com.example.po_proj03.R.id.llN)

        viewModel.filters.observe(viewLifecycleOwner, Observer {
            this.data = viewModel.filters.value!!
            // Perform an action with the latest item data.
        })
        Log.d("console","PLAN "+data.toString())
        btn.setOnClickListener {
            this.data = JSONArray()
            btn.setBackgroundColor(Color.YELLOW)
            viewModel.filters.observe(viewLifecycleOwner, Observer {
                this.data = viewModel.filters.value!!
                // Perform an action with the latest item data.
            })
            Log.d("console","btnPLAN "+data.toString())
            dCList=null
            dataToDC()
            clearTable(contentRow)
            if (dCList?.isNotEmpty() == true) {
                for (dC: DanceClasses in dCList!!) {
                    Log.i("console", dC.day)
                    val colorInt = colorsMap[dC.summary]
                    when (dC.day) {
                        "Poniedziałek" -> {
                            if (colorInt != null) {
                                addText(dCToString(dC), llP, colorInt)
                            }
                        }

                        "Wtorek" -> {
                            if (colorInt != null) {
                                addText(dCToString(dC), llW, colorInt)
                            }
                        }

                        "Środa" -> {
                            if (colorInt != null) {
                                addText(dCToString(dC), llS, colorInt)
                            }
                        }

                        "Czwartek" -> {
                            if (colorInt != null) {
                                addText(dCToString(dC), llC, colorInt)
                            }
                        }

                        "Piątek" -> {
                            if (colorInt != null) {
                                addText(dCToString(dC), llPt, colorInt)
                            }
                        }

                        "Sobota" -> {
                            if (colorInt != null) {
                                addText(dCToString(dC), llSb, colorInt)
                            }
                        }

                        "Niedziela" -> {
                            if (colorInt != null) {
                                addText(dCToString(dC), llN, colorInt)
                            }
                        }

                        else -> {
                            addText("default", llW, Color.YELLOW)
                        }
                    }
                }

            }
            else{
                textView.text = "no lessons matching filter"
            }

        }
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataToDC() {
        for (i in 0 until data.length()) {
            val dc = data.getJSONObject(i)
            val danceClasses: DanceClasses = DanceClasses(
                dc.get("timeslot-start") as String,
                dc.get("timeslot-end") as String,
                dc.get("day") as String, dc.get("lesson") as String, dc.get("location") as String
            )
            if (dCList != null) {
                dCList!!.add(danceClasses)
            } else {
                dCList = mutableListOf(danceClasses)
            }
        }
    }
    fun dCToString(dC:DanceClasses):String{
var str = ""
str+="zajęcia: "+dC.summary+"\n"
        str+="lokalizacja: "+dC.location+"\n"
        str+="start: "+dC.start+"\n"
        str+="koniec: "+dC.end+"\n"
        return str
    }

fun addText(str:String,linearLayout: LinearLayout,color:Int){
        val lparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val tv = TextView(context)
        tv.layoutParams = lparams
        tv.text = str
        linearLayout.addView(tv)
        tv.setBackgroundColor(color)
}
  fun clearTable(tableRow: TableRow){

      val childCount: Int = tableRow.childCount
      for (i in 0 until childCount) {
          val ll: LinearLayout = tableRow.getChildAt(i) as LinearLayout
          ll.removeAllViews()
          /*
          val childCountLl: Int = ll.childCount
          for (j in 0 until childCountLl) {
              val v:TextView = ll.getChildAt(j) as TextView
              //Log.i("console", v.text as String)

          }

           */
      }
  }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


/*
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val RESULT = "result"
    final val EVENT = "event"
    private val ADD_NOTE = 44

    var mCalendarView: CalendarView? = null
    private val mEventDays: List<EventDay> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mCalendarView = root.findViewById(com.example.po_proj03.R.id.calendarView)

        mCalendarView!!.setOnDayClickListener(OnDayClickListener(){
            fun onDayClick(eventDay:EventDay) {
                previewNote(eventDay);
            }
        })

        /*
        val events: MutableList<EventDay> = ArrayList()

        val calendar: Calendar = Calendar.getInstance()
        events.add(EventDay(calendar, com.example.po_proj03.R.drawable.sample_icon)!!)

        val calendarView: CalendarView = root.findViewById(com.example.po_proj03.R.id.calendarView)
        calendarView.setEvents(events)

        calendarView.setOnDayClickListener { eventDay ->
            val clickedDayCalendar = eventDay.calendar
        }
         */

        return
    }
    /*
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            val myEventDay: MyEventDay = data.getParcelableExtra(RESULT)
            mCalendarView.setDate(myEventDay.getCalendar())
            mEventDays.add(myEventDay)
            mCalendarView!!.setEvents(mEventDays)
        }

    }
    */

    fun previewNote(eventDay: EventDay) {
        val intent = Intent(this, NotePreviewActivity::class.java)
        if (eventDay is MyEventDay) {
            intent.putExtra(EVENT, eventDay as MyEventDay)
        }
        startActivity(intent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
*/