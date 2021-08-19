package com.azuka.aplikasiujian.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azuka.aplikasiujian.R
import com.azuka.aplikasiujian.data.Answer
import com.azuka.aplikasiujian.data.Question
import com.azuka.aplikasiujian.databinding.ItemQuizGroupBinding
import com.squareup.picasso.Picasso


/**
 * Created by ivanaazuka on 8/18/21.
 * Android Engineer
 */

class QuestionAdapter(
    private val onAnswerClick: (Question, Answer) -> Unit
) : ListAdapter<Question, QuestionAdapter.ViewHolder>(QuestionItemDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemQuizGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onAnswerClick)
    }

    class ViewHolder(private val binding: ItemQuizGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question, onAnswerClick: (Question, Answer) -> Unit) {
            binding.tvQuestion.text = question.question
            val answers = question.answer.shuffled()
            binding.rbAnswer1.text = answers[0].answer
            binding.rbAnswer2.text = answers[1].answer
            binding.rbAnswer3.text = answers[2].answer

            if (question.imageUrl.isNotEmpty()) {
                binding.ivQuestion.visibility = View.VISIBLE
                Picasso.get().load(question.imageUrl)
                    .into(binding.ivQuestion)
            }
            binding.rgQuestion.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rbAnswer1 -> onAnswerClick(question, answers[0])
                    R.id.rbAnswer2 -> onAnswerClick(question, answers[1])
                    R.id.rbAnswer3 -> onAnswerClick(question, answers[2])
                }
            }
        }
    }

}


object QuestionItemDiff : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem == newItem

}