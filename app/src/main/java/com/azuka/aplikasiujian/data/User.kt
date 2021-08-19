package com.azuka.aplikasiujian.data


/**
 * Created by ivanaazuka on 8/14/21.
 * Android Engineer
 */

data class User(
    val name: String = "",
    val id: String = "",
    val role: Int = RoleEnum.Teacher.code
)

data class Question(
    val id: String = (System.currentTimeMillis() / 1000).toString(),
    val question: String = "",
    val imageUrl: String = "",
    val answer: List<Answer> = emptyList(),
    val createdBy: User = User(),
    val selectedAnswer: Answer? = null
)

data class Answer(
    val id: String = (System.currentTimeMillis() / 1000).toString(),
    val answer: String = "",
    val imageUrl: String = "",
    @field:JvmField val isRightAnswer: Boolean = false
)

data class StudentAnswer(
    val answeredBy: User,
    val questions: List<Question>
)

val questions = listOf(
    Question(
        question = "Mana kah angka satu..?",
        answer = listOf(
            Answer(answer = "1", isRightAnswer = true),
            Answer(answer = "2", isRightAnswer = false),
            Answer(answer = "3", isRightAnswer = false)
        ),
        createdBy = User()
    ),
    Question(
        question = "Mana kah angka empat..?",
        answer = listOf(
            Answer(answer = "4", isRightAnswer = true),
            Answer(answer = "7", isRightAnswer = false),
            Answer(answer = "3", isRightAnswer = false)
        ),
        createdBy = User()
    ),
    Question(
        question = "Mana kah angka lima..?",
        answer = listOf(
            Answer(answer = "5", isRightAnswer = true),
            Answer(answer = "3", isRightAnswer = false),
            Answer(answer = "1", isRightAnswer = false)
        ),
        createdBy = User()
    )
)