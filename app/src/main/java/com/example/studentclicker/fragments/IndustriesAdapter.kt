package com.example.studentclicker.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.navigation.findNavController
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.studentclicker.R
import com.example.studentclicker.model.Industries
import com.example.studentclicker.model.Points
import com.example.studentclicker.viewmodel.IndustriesViewModel
import com.example.studentclicker.viewmodel.PointsViewModel
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.custom_row.view.Pub_Profit
import kotlinx.android.synthetic.main.overview.view.*
import kotlinx.android.synthetic.main.overview_rv.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class ListAdapter(val vm:IndustriesViewModel,val view:View,val vmp:PointsViewModel): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    val handler = Handler(Looper.getMainLooper())
    var run = false
    private var industriesList = emptyList<Industries>()
    private var pointsList = emptyList<Points>()
    private lateinit var progressbaranimation: ProgressBar
    private lateinit var progressanimation: ObjectAnimator
    private lateinit var startPub: Button
    private lateinit var pubcounter: TextView
    private lateinit var imagetap: ImageView
    private lateinit var cost: TextView
    private lateinit var PurchaseNumber:TextView
    private lateinit var licznik:TextView

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
        return industriesList.size
    }

    /*
    fun calculatecost(coef:Double,power:Int,profit:Double,multiplier:Int): Double {
        return (Math.pow(coef,power.toDouble())*profit).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }*/
    fun calculatecost(coef:Double,power:Int,profit:Int,multiplier:String): Long {
        var cost:Double
            val real_multiplier=multiplier.replace("x","").toInt()
            cost= 0.0
            for(power in power..power+real_multiplier){
                cost+=Math.pow(coef,power.toDouble())*profit
            }
            return cost.toLong()
    }
    fun purchase_number(coef:Double,power:Int,profit:Int,multiplier:String): String {
        var cost:Double
        if(multiplier=="max"){
            var cost=Math.pow(coef,power.toDouble())*profit
            var real_power=power
            while(cost+Math.pow(coef,real_power.toDouble())*profit<pointsList[0].points)
            {
                real_power+=1
                cost+=Math.pow(coef,real_power.toDouble())*profit
            }
            if(real_power-power==0){
                return "x1"
            }
            else{
            return "x"+(real_power-power).toString()
        }}
        else{
            return multiplier
        }
    }
    fun calculatenumber(act:Int,th:Int):String{
        return act.toString()+"/"+(th*25).toString()
    }

    fun calculateproduction(number:Int,profit:Int):Int{
        return number*profit
    }
    fun calculatetime(time:Double,threshold:Int):String{
        val t=(time/threshold)
        val m:Long=60
        val h:Long=3600
        if(time<60){
            return t.toBigDecimal().setScale(2,RoundingMode.HALF_EVEN).toString()+"s"
        }
        else if(time>60&&t<3600){
            return (t/m).toBigDecimal().setScale(2,RoundingMode.HALF_EVEN).toString()+"m"
        }
        else{
            return (t/h).toBigDecimal().setScale(2,RoundingMode.HALF_EVEN).toString()+"h"
        }
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = industriesList[position]
        val point=pointsList[0]

        if(currentItem.id==1){
            holder.itemView.Pub_image.setImageResource(R.drawable.icon1)
        }
        if(currentItem.id==2){
            holder.itemView.Pub_image.setImageResource(R.drawable.icon2)
        }
        if(currentItem.id==3){
            holder.itemView.Pub_image.setImageResource(R.drawable.icon3)
        }
        if(currentItem.id==4){
            holder.itemView.Pub_image.setImageResource(R.drawable.icon4)
        }
        if(currentItem.id==5){
            holder.itemView.Pub_image.setImageResource(R.drawable.icon5)
        }
        if(currentItem.unlock<pointsList[0].points&&currentItem.unlocked!=1)
        {
            holder.itemView.rowLayout.setBackgroundResource(R.color.orange)
        }
        else{
            holder.itemView.rowLayout.setBackgroundResource(R.color.darkgray)
        }
        //holder.itemView.Pub_Profit.text=calculatecost(currentItem.coefficient,currentItem.actnumber,currentItem.profit).toString()
        holder.itemView.Pub_Profit.text=calculateproduction(currentItem.actnumber,currentItem.profit).toString()
        holder.itemView.Add_Pub.Purchase_Number.text=purchase_number(currentItem.coefficient,currentItem.actnumber,currentItem.cost,view.multiplier.text.toString())
        holder.itemView.Purchase_Price.text=calculatecost(currentItem.coefficient,currentItem.actnumber,currentItem.cost,holder.itemView.Purchase_Number.text.toString()).toString()
        holder.itemView.Pub_Number.text=calculatenumber(currentItem.actnumber,currentItem.threshold)
        if(point.points<calculatecost(currentItem.coefficient,currentItem.actnumber,currentItem.cost,holder.itemView.Purchase_Number.text.toString())){
            holder.itemView.Add_Pub.setBackgroundResource(R.drawable.custom_button_false)
        }
        else{
            holder.itemView.Add_Pub.setBackgroundResource(R.drawable.custom_button)
        }
        if(currentItem.unlocked==1){
            holder.itemView.Unlock_Text.visibility=View.INVISIBLE
            holder.itemView.Unlock_Cost.visibility=View.INVISIBLE
            holder.itemView.Pub_layout.visibility=View.VISIBLE
        }
        else{
        holder.itemView.Unlock_Cost.text=currentItem.unlock.toString()
            holder.itemView.Unlock_layout.setOnClickListener{
                if(point.points>holder.itemView.Unlock_Cost.text.toString().toLong()){
                    vm.updateIndustry(
                        Industries(
                            currentItem.id,
                            currentItem.profit,
                            currentItem.coefficient,
                            currentItem.cost,
                            currentItem.actnumber,
                            currentItem.threshold,
                            currentItem.time,
                            currentItem.unlock,
                            1
                        )
                    )
                    vmp.updateIndustry(Points(point.id,(point.points-holder.itemView.Unlock_Cost.text.toString().toLong())))
                }
            }
        }
        view.Points_Counter.text=point.points.toString()
        holder.itemView.Pub_Counter.text=calculatetime(currentItem.time,currentItem.threshold)
        pubcounter = holder.itemView.findViewById(R.id.Pub_Counter)
        imagetap=holder.itemView.findViewById(R.id.Pub_image)

        holder.itemView.Add_Pub.setOnClickListener{
            if(point.points>=holder.itemView.Add_Pub.Purchase_Price.text.toString().replace("x","").toLong()) {
                val add =
                    currentItem.actnumber + holder.itemView.Add_Pub.Purchase_Number.text.toString()
                        .replace("x", "").toInt()
                val th = add / 25 + 1
                vm.updateIndustry(
                    Industries(
                        currentItem.id,
                        currentItem.profit,
                        currentItem.coefficient,
                        currentItem.cost,
                        add,
                        th,
                        currentItem.time,
                        currentItem.unlock,
                        currentItem.unlocked
                    )
                )
            //val minus:Long=
                vmp.updateIndustry(Points(point.id,(point.points-holder.itemView.Add_Pub.Purchase_Price.text.toString().replace("x","").toLong())))
                        notifyDataSetChanged()
        }
        }
        /*holder.itemView.rowLayout.idEditIcon.setOnClickListener{

            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }*/
        holder.itemView.Pub_image.setOnClickListener{
            holder.itemView.Pub_image.isEnabled = false
            if(calculatetime(currentItem.time,currentItem.threshold).contains("s")){
                var time=((calculatetime(currentItem.time,currentItem.threshold).replace("s","").toDouble()*1000).toLong())
                progressanimation=ObjectAnimator.ofInt(holder.itemView.findViewById(R.id.progressBar),"progress",100,0)
                progressanimation.setDuration(time)
                progressanimation.start()
                Handler().postDelayed({ holder.itemView.Pub_image.isEnabled = true
                    val point=pointsList[0].points
                    vmp.updateIndustry(Points(1,point+calculateproduction(currentItem.actnumber,currentItem.profit).toString().toLong()))

                }, time)
            }
            else if(calculatetime(currentItem.time,currentItem.threshold).contains("m")){
                var time=((calculatetime(currentItem.time,currentItem.threshold).replace("m","").toDouble()*1000*60).toLong())
                progressanimation=ObjectAnimator.ofInt(holder.itemView.findViewById(R.id.progressBar),"progress",100,0)
                progressanimation.setDuration(time)
                progressanimation.start()
                Handler().postDelayed({ holder.itemView.Pub_image.isEnabled = true
                    val point=pointsList[0].points
                    vmp.updateIndustry(Points(1,point+calculateproduction(currentItem.actnumber,currentItem.profit).toString().toLong()))

                }, time)}


            notifyDataSetChanged()
            //TEST
            //holder.itemView.Purchase_Number.text="10x"
            //holder.itemView.Purchase_Price.text=calculatecost(currentItem.coefficient,currentItem.actnumber,currentItem.profit,holder.itemView.Purchase_Number.text.toString().replace("x","").toInt()).toString()
        }
        view.multiplier.setOnClickListener{
            if(view.multiplier.text=="x1"){
                view.multiplier.text="x10"
                notifyDataSetChanged()
            }
            else if(view.multiplier.text=="x10"){
                view.multiplier.text="x100"
                notifyDataSetChanged()
            }
            else if(view.multiplier.text=="x100"){
                view.multiplier.text="max"
                notifyDataSetChanged()
            }
            else{
                view.multiplier.text="x1"
                notifyDataSetChanged()
            }
        }
    }
    fun Minus1(first:Long,second:Long):Long{
        return first-second
    }
    fun setData(industries: List<Industries>){
        this.industriesList = industries
        notifyDataSetChanged()
    }
    fun setData2(points: List<Points>){
        this.pointsList = points
        notifyDataSetChanged()
    }
}
