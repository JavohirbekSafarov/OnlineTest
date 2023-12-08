package com.javokhirbekcoder.onlinetest.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.javokhirbekcoder.onlinetest.databinding.DialogTestsListBinding
import com.javokhirbekcoder.onlinetest.databinding.TestsRvItemBinding
import com.javokhirbekcoder.onlinetest.ui.models.TestModel
import com.javokhirbekcoder.onlinetest.ui.models.TestModelLocal
import okhttp3.internal.cookieToString

/*
Created by Javokhirbek on 04/12/2023 at 12:08
*/

class TestsDialogRvAdapter(private val testList:ArrayList<TestModelLocal>, private var itemClickListener: OnItemClickListener):RecyclerView.Adapter<TestsDialogRvAdapter.MyViewHolder>() {
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    inner class MyViewHolder(private val binding: TestsRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(position: Int) {
            binding.testIdTv.text = testList[position].id.toString()
            if (testList[position].status == 0)
                binding.statusColorView.setBackgroundColor(Color.RED)
            else if (testList[position].status == 1)
                binding.statusColorView.setBackgroundColor(Color.GREEN)

            binding.mlayout.setOnClickListener {
                itemClickListener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = TestsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = testList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }
}


interface OnItemClickListener {
    fun onItemClick(position: Int)
}
