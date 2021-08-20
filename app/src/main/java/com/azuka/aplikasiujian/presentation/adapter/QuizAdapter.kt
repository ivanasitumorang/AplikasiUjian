package com.azuka.aplikasiujian.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azuka.aplikasiujian.R
import com.azuka.aplikasiujian.data.Quiz
import com.azuka.aplikasiujian.databinding.ItemQuizBinding
import com.azuka.aplikasiujian.external.formatToDay
import com.azuka.aplikasiujian.external.formatToTime
import com.azuka.aplikasiujian.external.toDateTime


/**
 * Created by ivanaazuka on 8/20/21.
 * Android Engineer
 */

class QuizAdapter(
    private val onClickListener: (Quiz) -> Unit
): ListAdapter<Quiz, QuizAdapter.ViewHolder>(QuizItemDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }

    class ViewHolder(private val binding: ItemQuizBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz, onClickListener: (Quiz) -> Unit) {
            with(binding) {
                tvName.text = quiz.name
                tvDay.text = binding.root.context.getString(
                    R.string.quiz_student_day_taken,
                    quiz.startTime.toDateTime().formatToDay(),
                    quiz.startTime.toDateTime().formatToTime(),
                    quiz.endTime.toDateTime().formatToTime()
                )
                root.setOnClickListener { onClickListener(quiz) }
            }
        }
    }
}

object QuizItemDiff: DiffUtil.ItemCallback<Quiz>() {
    override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean =
        oldItem == newItem

}