package com.azuka.aplikasiujian.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azuka.aplikasiujian.R
import com.azuka.aplikasiujian.data.QuizStudent
import com.azuka.aplikasiujian.databinding.ItemQuizGroupBinding
import com.azuka.aplikasiujian.external.formatToDay
import com.azuka.aplikasiujian.external.formatToTime
import com.azuka.aplikasiujian.external.toDateTime
import org.joda.time.Minutes


/**
 * Created by ivanaazuka on 8/19/21.
 * Android Engineer
 */

class QuizStudentAdapter(
    private val onClickListener: (QuizStudent) -> Unit
) : ListAdapter<QuizStudent, QuizStudentAdapter.ViewHolder>(QuizStudentItemDiff) {

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
        fun bind(quiz: QuizStudent, onClickListener: (QuizStudent) -> Unit) {
            with(binding) {
                val context = root.context
                val quizTime = try {
                    Minutes.minutesBetween(
                        quiz.startTime.toDateTime(), quiz.endTime.toDateTime()
                    ).minutes.toString()
                } catch (e: Exception) {
                    "-"
                }
                tvName.text = quiz.name
                tvScore.text = context.getString(R.string.quiz_student_score, quiz.score)
                val endTime = if (quiz.endTime.isEmpty()) "-"
                else quiz.endTime.toDateTime().formatToTime()
                tvDay.text = context.getString(
                    R.string.quiz_student_day_taken,
                    quiz.startTime.toDateTime().formatToDay(),
                    quiz.startTime.toDateTime().formatToTime(),
                    endTime
                )
                tvTime.text = context.getString(R.string.quiz_student_time_taken, quizTime)
                root.setOnClickListener { onClickListener(quiz) }
            }
        }
    }
}

object QuizStudentItemDiff : DiffUtil.ItemCallback<QuizStudent>() {
    override fun areItemsTheSame(oldItem: QuizStudent, newItem: QuizStudent): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: QuizStudent, newItem: QuizStudent): Boolean =
        oldItem == newItem

}