package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import java.time.LocalDate
import javax.inject.Inject

class NewDateUseCase @Inject constructor(private val repository: Repository){
    suspend operator fun invoke(user: UserModel, date: Pair<LocalDate, String>) {

        val updatedWotDatesList = mutableListOf<Pair<LocalDate, String>>()
        updatedWotDatesList.addAll(user.wotDates)
        updatedWotDatesList.add(date)
        repository.updateDate(user.uid, updatedWotDatesList)
        repository.insertUserInRoom(user.copy(wotDates = updatedWotDatesList.toList()))
    }
}