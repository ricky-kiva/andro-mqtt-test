package com.rickyslash.mqtttestapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.rickyslash.mqtttestapp.database.Process

class ProcessDiffCallback(private val mOldProcessList: List<Process>, private val mNewProcessList: List<Process>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldProcessList.size
    }

    override fun getNewListSize(): Int {
        return mNewProcessList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldProcessList[oldItemPosition].id == mNewProcessList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldProcessList[oldItemPosition]
        val newEmployee = mNewProcessList[newItemPosition]
        return oldEmployee.title == newEmployee.title
                && oldEmployee.topic == newEmployee.topic
                && oldEmployee.input == newEmployee.input
                && oldEmployee.output == newEmployee.output
                && oldEmployee.reject == newEmployee.reject
    }

}