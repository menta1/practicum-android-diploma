package ru.practicum.android.diploma.data.details

import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.data.Constants.SERVER_ERROR
import ru.practicum.android.diploma.data.db.VacancyDao
import ru.practicum.android.diploma.data.db.converters.VacancyDetailDbConverters
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.dto.VacancyDetailResponse
import ru.practicum.android.diploma.domain.details.DetailsRepository
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val vacancyDao: VacancyDao,
    private val converter: VacancyDetailDbConverters,
    private val networkClient: NetworkClient,
) : DetailsRepository {

    override suspend fun saveVacancy(vacancy: VacancyDetail) {
        vacancyDao.insertVacancy(converter.map(vacancy))
    }

    override suspend fun deleteVacancy(vacancy: VacancyDetail) {
        vacancyDao.deleteVacancy(converter.map(vacancy))
    }

    override suspend fun getFavouriteVacancy(vacancyId: String): VacancyDetail {
        return converter.map(vacancyDao.getVacancyById(vacancyId.toInt()))
    }

    override suspend fun isVacancyInFavourites(vacancyId: String): Boolean {
        return vacancyDao.isVacancyInFavourites(vacancyId.toInt())
    }

    override suspend fun getVacancyDetails(vacancyId: String): NetworkResource<VacancyDetail> {
        val response = networkClient.getVacancyDetail(vacancyId)
        return when (response.resultCode) {
            NO_INTERNET -> {
                NetworkResource(code = NO_INTERNET)
            }

            OK_RESPONSE -> {
                NetworkResource(
                    (response as VacancyDetailResponse).result.toVacancyDetail(), OK_RESPONSE
                )
            }

            else -> {
                NetworkResource(code = SERVER_ERROR)
            }
        }
    }
}