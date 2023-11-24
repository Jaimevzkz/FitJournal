package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.ExerciseModel
import javax.inject.Inject

class GetExerciseByNameUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(name: String): List<ExerciseModel>? {
        return repository.getExercisesByName(name)
    }
}