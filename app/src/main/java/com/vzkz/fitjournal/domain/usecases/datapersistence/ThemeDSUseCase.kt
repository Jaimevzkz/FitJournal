package com.vzkz.fitjournal.domain.usecases.datapersistence

import com.vzkz.fitjournal.domain.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeDSUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend fun switchTheme() {
        dataStoreRepository.switchAppTheme()
    }

    suspend operator fun invoke(): Flow<Boolean> {
        return dataStoreRepository.getAppTheme()
    }
}