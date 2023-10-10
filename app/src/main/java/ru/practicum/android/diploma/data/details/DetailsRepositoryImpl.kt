package ru.practicum.android.diploma.data.details

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
    private val networkClient: NetworkClient
): DetailsRepository {

    override suspend fun saveVacancy(vacancy: VacancyDetail) {
        vacancyDao.insertVacancy(converter.map(vacancy))
    }

    override suspend fun deleteVacancy(vacancy: VacancyDetail) {
        vacancyDao.deleteVacancy(converter.map(vacancy))
    }

    override suspend fun getFavouriteVacancy(vacancyId: String): VacancyDetail {
        return converter.map(vacancyDao.getVacancyById(vacancyId))
    }

    override suspend fun isVacancyInFavourites(vacancyId: String): Boolean {
        return vacancyDao.isVacancyInFavourites(vacancyId)
    }

    override suspend fun getVacancyDetails(vacancyId: String): NetworkResource<VacancyDetail> {
        val response = networkClient.getVacancyDetail(vacancyId)
        when (response.resultCode) {
            -1 -> {
                return NetworkResource<VacancyDetail>(code = -1)
            }
            200 -> {
                return NetworkResource(
                    (response as VacancyDetailResponse).result.toVacancyDetail(), 200
                )
            }
            else -> {
                return NetworkResource<VacancyDetail>(code = 400)
            }
        }
    }


}