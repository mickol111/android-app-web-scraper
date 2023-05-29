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


class PlanFragment : Fragment() {

    private var _binding: FragmentPlanBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by activityViewModels()

    var data = JSONArray();
    var dCList: MutableList<DanceClasses>? = null
    val colorsMap: Map<String, Int?> = mapOf(
        "Trening indywidualny par turniejowych" to Color.parseColor("#949263"),
        "Grupa Turniejowa" to Color.parseColor("#FFFEEB"),
        "Grupa Dorosłych Zaawansowana" to Color.parseColor("#E0DFB8"),
        "Grupa Lady Solo" to Color.parseColor("#CAB8E0"),
        "Grupa Dorosłych Początkująca" to Color.parseColor("#F4EBFF"),
        "Grupa przedszkolna" to Color.parseColor("#796394"),
        "Grupa Zaawansowana Dziecięca" to Color.parseColor("#DFD8A0"),
        "Żeliwsławki Grupa Dorosłych" to Color.parseColor("#DFDAD6")
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        })
        Log.d("console", "PLAN " + data.toString())
        btn.setOnClickListener {
            this.data = JSONArray()
            btn.setBackgroundColor(Color.YELLOW)
            viewModel.filters.observe(viewLifecycleOwner, Observer {
                this.data = viewModel.filters.value!!
            })
            Log.d("console", "btnPLAN " + data.toString())
            dCList = null
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

            } else {
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

    fun dCToString(dC: DanceClasses): String {
        var str = ""
        str += "zajęcia: " + dC.summary + "\n"
        str += "lokalizacja: " + dC.location + "\n"
        str += "start: " + dC.start + "\n"
        str += "koniec: " + dC.end + "\n"
        return str
    }

    fun addText(str: String, linearLayout: LinearLayout, color: Int) {
        val lparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val tv = TextView(context)
        tv.layoutParams = lparams
        tv.text = str
        linearLayout.addView(tv)
        tv.setBackgroundColor(color)
    }

    fun clearTable(tableRow: TableRow) {

        val childCount: Int = tableRow.childCount
        for (i in 0 until childCount) {
            val ll: LinearLayout = tableRow.getChildAt(i) as LinearLayout
            ll.removeAllViews()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

