package com.azuka.aplikasiujian.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azuka.aplikasiujian.data.Quiz
import com.azuka.aplikasiujian.databinding.ItemQuizGroupBinding


/**
 * Created by ivanaazuka on 8/19/21.
 * Android Engineer
 */

class QuizStudentAdapter(
    private val onClickListener: (Quiz) -> Unit
) : ListAdapter<Quiz, QuizStudentAdapter.ViewHolder>(QuizItemDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemQuizGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }

    class ViewHolder(private val binding: ItemQuizGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz, onClickListener: (Quiz) -> Unit) {
            with(binding) {
                tvName.text = quiz.name
            }
        }
    }
}

object QuizItemDiff : DiffUtil.ItemCallback<Quiz>() {
    override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean =
        oldItem == newItem

}