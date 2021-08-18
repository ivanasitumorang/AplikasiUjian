package com.azuka.aplikasiujian.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azuka.aplikasiujian.data.Question
import com.azuka.aplikasiujian.databinding.ItemQuizGroupBinding


/**
 * Created by ivanaazuka on 8/18/21.
 * Android Engineer
 */

class QuestionAdapter() : ListAdapter<Question, QuestionAdapter.ViewHolder>(QuestionItemDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemQuizGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemQuizGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.tvQuestion.text = question.question
            binding.rbAnswer1.text = question.answer[0].answer
            binding.rbAnswer2.text = question.answer[1].answer
            binding.rbAnswer3.text = question.answer[2].answer
        }
    }

}


object QuestionItemDiff : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem == newItem

}