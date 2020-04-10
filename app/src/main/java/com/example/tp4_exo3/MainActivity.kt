package com.example.tp4_exo3


import android.app.Activity
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    var tachsList : MutableList<Tache> = ArrayList()
    lateinit var adapter: TachAdapter
    lateinit var layoutManager : LinearLayoutManager
    lateinit var usedList : MutableList<Int>
    var positionIndex : Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager






        usedList = getTodaysTasks(tachsList)
        Log.i("test", "${usedList.size}")
        adapter = TachAdapter(this)
        recyclerView.adapter = adapter


        val isTablet = resources.getBoolean(R.bool.isTablet)
        val isLandscape = this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE
        Toast.makeText(this, "$isTablet", Toast.LENGTH_LONG).show()
        if (!isTablet){


        }else if (!isLandscape){
            val spinner: Spinner = findViewById(R.id.combobox)
            ArrayAdapter.createFromResource(
                this,
                R.array.options,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            combobox.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    positionIndex = position
                    changeTasksViewing(position)

                }

            }

        } else {

            todayTaskBtnView.setOnClickListener {
                positionIndex = 0
                changeTasksViewing(positionIndex)
            }

            weekTaskBtnView.setOnClickListener{
                positionIndex = 1
                changeTasksViewing(positionIndex)
            }

            allTasksBtnView.setOnClickListener{
                positionIndex = 2
                changeTasksViewing(positionIndex)
            }


        }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        addTacheBtnView.setOnClickListener {
            val tacheName = taskInputView.text.toString()
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, dayOfMonth ->
                val tache = Tache(tacheName, mYear, mMonth, dayOfMonth)
                tachsList.add(tache)
                Log.i("Length is ", "${tachsList.size}")
                val list = tachsList.distinct()
                Log.i("Length is ", "${list.size}")
                changeTasksViewing(positionIndex)
            }, year, month, day)

            datePickerDialog.show()


        }


    }


    fun changeTasksViewing(position : Int){
        if (position == 0){
            usedList.clear()
            usedList = getTodaysTasks(tachsList)
            adapter.notifyDataSetChanged()
        }

        if (position == 1){
            usedList.clear()
            usedList = getWeekTasks(tachsList)
            adapter.notifyDataSetChanged()
        }

        if (position == 2){
            usedList.clear()
            usedList = getAllTasks(tachsList)
            adapter.notifyDataSetChanged()
        }
    }



    fun getTodaysTasks(mainList : MutableList<Tache>):MutableList<Int>{
        var list : MutableList<Int> = ArrayList()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        for (i in 0 until mainList.size){
            if (year == mainList.get(i).year && month == mainList.get(i).month && day == mainList.get(i).day){
                list.add(i)
            }
        }
        return list
    }

    fun getWeekTasks(mainList : MutableList<Tache>):MutableList<Int>{
        var list : MutableList<Int> = ArrayList()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val week = c.get(Calendar.WEEK_OF_YEAR)

        for (i in 0 until mainList.size){
            val cal = Calendar.getInstance()
            cal.set(mainList.get(i).year, mainList.get(i).month, mainList.get(i).day)

            if (year == mainList.get(i).year && week == cal.get(Calendar.WEEK_OF_YEAR)){
                list.add(i)
            }
        }
        return list
    }

    fun getAllTasks(mainList : MutableList<Tache>):MutableList<Int>{
        var list : MutableList<Int> = ArrayList()
        for (i in 0 until mainList.size){
            list.add(i)
        }
        return list
    }


    class TachAdapter(val activity : MainActivity) : RecyclerView.Adapter<TachAdapter.TachViewHolder>(){
        class TachViewHolder(v : View) : RecyclerView.ViewHolder(v){
            val textViewTacheName = v.findViewById<TextView>(R.id.tacheView)
            val textViewTacheDate = v.findViewById<TextView>(R.id.tacheDateView)
            val tacheLayout = v.findViewById<RelativeLayout>(R.id.tacheLayoutView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachViewHolder {
            return TachViewHolder(LayoutInflater.from(activity).inflate(R.layout.tache_view, parent, false))
        }

        override fun getItemCount(): Int {
            return activity.usedList.size
        }

        override fun onBindViewHolder(holder: TachViewHolder, position: Int) {
            holder.textViewTacheName.text = activity.tachsList[activity.usedList[position]].name
            holder.textViewTacheDate.text = activity.tachsList[activity.usedList[position]].dateToString()
            holder.tacheLayout.setOnClickListener {
                activity.tachsList.removeAt(activity.usedList[position])
                activity.changeTasksViewing(activity.positionIndex)
            }
        }
    }
}
