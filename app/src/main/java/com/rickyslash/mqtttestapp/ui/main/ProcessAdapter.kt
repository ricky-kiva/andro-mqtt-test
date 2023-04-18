package com.rickyslash.mqtttestapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rickyslash.mqtttestapp.databinding.ItemProcessBinding
import com.rickyslash.mqtttestapp.database.Process
import com.rickyslash.mqtttestapp.helper.ProcessDiffCallback

class ProcessAdapter: RecyclerView.Adapter<ProcessAdapter.ProcessViewHolder>() {

    private val listProcess = ArrayList<Process>()

    fun setListProcess(listProcess: List<Process>) {
        val diffCallback = ProcessDiffCallback(this.listProcess, listProcess)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listProcess.clear()
        this.listProcess.addAll(listProcess)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ProcessViewHolder(private val binding: ItemProcessBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(process: Process) {
            with(binding) {
                tvItemTitle.text = process.title
                tvItemTopic.text = process.topic
                tvItemInputValue.text = process.input.toString()
                tvItemOutputValue.text = process.output.toString()
                tvItemRejectValue.text = process.reject.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessViewHolder {
        val binding = ItemProcessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProcessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProcessViewHolder, position: Int) {
        holder.bind(listProcess[position])
    }

    override fun getItemCount(): Int = listProcess.size

}