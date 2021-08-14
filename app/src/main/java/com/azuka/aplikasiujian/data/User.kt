package com.azuka.aplikasiujian.data


/**
 * Created by ivanaazuka on 8/14/21.
 * Android Engineer
 */

data class User(
    val name: String,
    val id: String,
    val role: Int = RoleEnum.Teacher.code
)

data class Question(
    val id: String,
    val question: String,
    val imagesUrl: List<String> = emptyList(),
    val answer: List<Answer>,
    val createdBy: User,
    val selectedAnswer: Answer? = null
)

data class Answer(
    val id: String,
    val answer: String = "",
    val imageUrl: String,
    val isRightAnswer: Boolean = false
)

data class StudentAnswer(
    val answeredBy: User,
    val questions: List<Question>
)