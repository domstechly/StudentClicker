package com.example.studentclicker.fragments

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import java.util.Collections.max
import android.os.Handler
import android.os.Looper
import android.view.*
import java.math.RoundingMode
import java.lang.NumberFormatException
import android.widget.*
import java.math.BigDecimal
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentclicker.R
import com.example.studentclicker.model.Industries
import com.example.studentclicker.model.Points
import com.example.studentclicker.viewmodel.IndustriesViewModel
import com.example.studentclicker.viewmodel.PointsViewModel
import kotlinx.android.synthetic.main.custom_row.*
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.overview.*
import kotlinx.android.synthetic.main.overview.view.*
import kotlinx.android.synthetic.main.overview_rv.view.*
import kotlin.math.round
import kotlin.math.roundToLong
import android.content.Context.MODE_PRIVATE






class overviewFragment :Fragment(){
    val ic=Industries(
        1,
        profit = 1,
        coefficient = 1.05,
        cost = 4,
        actnumber = 1,
        threshold = 1,
        time=1.0,
        unlock = 0,
        unlocked = 1
    )
    val ic2=Industries(
        2,
        profit = 5,
        coefficient = 1.12,
        cost = 10,
        actnumber = 1,
        threshold = 1,
        time=3.0,
        unlock = 100,
        unlocked = 0
    )
    val ic3=Industries(
        3,
        profit = 15,
        coefficient = 1.11,
        cost = 50,
        actnumber = 1,
        threshold = 1,
        time=10.0,
        unlock = 5000,
        unlocked = 0
    )
    val ic4=Industries(
        4,
        profit = 80,
        coefficient = 1.10,
        cost = 400,
        actnumber = 1,
        threshold = 1,
        time=40.0,
        unlock = 20000,
        unlocked = 0
    )
    val ic5=Industries(
        5,
        profit = 300,
        coefficient = 1.09,
        cost = 1500,
        actnumber = 1,
        threshold = 1,
        time=120.0,
        unlock = 50000,
        unlocked = 0
    )

    val handler = Handler(Looper.getMainLooper())
    var run = false
    private lateinit var pubvisible:Button
    private lateinit var multiplier:Button
    private lateinit var startPub:RelativeLayout
    private lateinit var pubcounter:TextView
    private lateinit var imagetap:ImageView
    private lateinit var progressbaranimation:ProgressBar
    private lateinit var progressanimation:ObjectAnimator
    private lateinit var profit:TextView
    private lateinit var mIndustriesViewModel: IndustriesViewModel
    private lateinit var mPointsViewModel: PointsViewModel
    private lateinit var licznik:TextView
    private val args by navArgs<overviewFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View?{
        val view = inflater.inflate(R.layout.overview_rv, container,false)
        val viewc=inflater.inflate(R.layout.custom_row,container,false)
        pubvisible = view.findViewById(R.id.Pub)
        multiplier = view.findViewById(R.id.multiplier)
        startPub = viewc.findViewById(R.id.Add_Pub)
        pubcounter =viewc.findViewById(R.id.Pub_Counter)
        imagetap = viewc.findViewById(R.id.Pub_image)
        profit=viewc.findViewById(R.id.Pub_Profit)
        licznik=view.findViewById(R.id.Points_Counter)
        mIndustriesViewModel=ViewModelProvider(this).get(IndustriesViewModel::class.java)
        mPointsViewModel=ViewModelProvider(this).get(PointsViewModel::class.java)

        val adapter=ListAdapter(mIndustriesViewModel,view,mPointsViewModel)
        val recyclerView = view.recyclerview
        recyclerView.adapter=adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mIndustriesViewModel.readAllData.observe(viewLifecycleOwner, Observer { industries->adapter.setData(industries) })
        mPointsViewModel.readAllData.observe(viewLifecycleOwner, Observer { points->adapter.setData2(points) })

        val ic6=Industries(
            0,
            profit = 1300,
            coefficient = 1.08,
            cost = 5000,
            actnumber = 1,
            threshold = 1,
            time=400.0,
            unlock = 160000,
            unlocked = 0
        )
        val ic7=Industries(
            0,
            profit = 4000,
            coefficient = 1.09,
            cost = 8000,
            actnumber = 1,
            threshold = 1,
            time=1000.0,
            unlock = 5000000,
            unlocked = 0
        )
        val ic8=Industries(
            8,
            profit = 8000,
            coefficient = 1.07,
            cost = 15000,
            actnumber = 1,
            threshold = 1,
            time=2000.0,
            unlock = 1000000,
            unlocked = 0
        )
        val points=Points(
            1,
            points = 0
        )
       //profit.setText(R.dimen.coefficient.toDouble().toString())
        view.Pub.setOnClickListener{
            if(pubvisible.isVisible)
            {
                view.Pub.visibility=View.INVISIBLE
                adapter.notifyDataSetChanged()
            }
        }/*
        view.multiplier.setOnClickListener{
            if(multiplier.text=="x1"){
                multiplier.text="x10"
                adapter.notifyDataSetChanged()

            }
            else if(multiplier.text=="x10"){
                multiplier.text="x100"
                view.Purchase_Number.text="x100"
                adapter.notifyDataSetChanged()
            }
            else if(multiplier.text=="x100"){
                multiplier.text="max"
                view.Purchase_Number.text="max"
                adapter.notifyDataSetChanged()
            }
            else{
                multiplier.text="x1"
                view.Purchase_Number.text="x1"
                adapter.notifyDataSetChanged()
            }
        }
        viewc.Pub_image.setOnClickListener{
            run = true
            runnableCode.run()

        }*/

        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reset, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_reset){
            restartAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun restartAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Tak") { _, _ ->
            mPointsViewModel.updateIndustry(Points(1,0))
            val number=1
            mIndustriesViewModel.updateIndustry(ic)
            mIndustriesViewModel.updateIndustry(ic2)
            mIndustriesViewModel.updateIndustry(ic3)
            mIndustriesViewModel.updateIndustry(ic4)
            mIndustriesViewModel.updateIndustry(ic5)
            findNavController().navigate(R.id.action_overview)
            Toast.makeText(
                requireContext(),
                "Pomyślnie zresetowano progress",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Nie") { _, _ -> }
        builder.setTitle("Zresetować grę?")
        builder.setMessage("Jesteś pewny?")
        builder.create().show()
    }
}