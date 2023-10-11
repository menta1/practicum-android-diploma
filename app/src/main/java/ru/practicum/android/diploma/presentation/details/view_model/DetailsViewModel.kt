package ru.practicum.android.diploma.presentation.details.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.details.DetailsInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.domain.share.model.EmailData
import ru.practicum.android.diploma.domain.share.model.PhoneData
import ru.practicum.android.diploma.domain.share.model.SharingData
import ru.practicum.android.diploma.presentation.details.models.DetailsState
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val interactor: DetailsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {
    private val detailsStateLiveData = MutableLiveData<DetailsState>()
    fun getDetailsStateLiveData(): LiveData<DetailsState> = detailsStateLiveData

    private val favouriteStateLiveData = MutableLiveData<Boolean>()
    fun getFavouriteStateLiveData(): LiveData<Boolean> = favouriteStateLiveData

    private var currentVacancy: VacancyDetail? = null
    fun initData() {
        detailsStateLiveData.postValue(DetailsState.Loading)
        viewModelScope.launch {
            val vacancy = testValue()
            detailsStateLiveData.postValue(DetailsState.Content(vacancy))
            currentVacancy = vacancy
        }
    }

    fun existInFavourite() {
        viewModelScope.launch {
            //request in db with interactor
            val isExist = false // interactor.isExistFavourite
            favouriteStateLiveData.postValue(isExist)
        }
    }

    fun setFavourite() {
        viewModelScope.launch {
            //request in db
        }
    }

    fun sharingVacancy() {
        sharingInteractor.sharingVacancy(
            SharingData("")
        )
    }

    fun employerPhone() {
        currentVacancy?.phone?.let { phone ->
            sharingInteractor.callPhone(
                PhoneData(phone = phone)
            )
        }
    }

    fun employerEmail() {
        currentVacancy?.email?.let { email ->
            sharingInteractor.sendEmail(
                EmailData(email = email)
            )
        }
    }


    private fun testValue() =
        VacancyDetail(
            id = "123123",
            name = "Android-разработчик",
            city = "Москва",
            employer = "Еда",
            employerLogoUrls = "",
            currency = "₽",
            salaryFrom = 100000,
            salaryTo = null,
            experience = "От 1 года до 3 лет",
            employmentType = "Полная занятость",
            schedule = "Удаленная работа",
            description = "Обязанности\n" +
                    "Разрабатывать новую функциональность приложения\n" +
                    "Помогать с интеграцией нашего SDK в другие приложения\n" +
                    "Проектировать большие новые модули\n" +
                    "Писать UI- и unit-тесты\n" +
                    "Следить за работоспособностью сервиса и устранять технический долг\n" +
                    "Требования\n" +
                    "100% Kotlin\n" +
                    "WebRTC\n" +
                    "CI по модели Trunk Based Development\n" +
                    "Условия\n" +
                    "сильная команда, с которой можно расти\n" +
                    "возможность влиять на процесс и результат\n" +
                    "расширенная программа ДМС: стоматология, обследования, вызов врача на дом и многое другое",
            keySkills = listOf(
                "Знание классических алгоритмов и структуры данных",
                "Программирование для Android больше одного года",
                "Знание WebRTC"
            ),
            phone = "111111111",
            email = "tmpl@yandex.ru",
            contactPerson = "Ирина"
        )
}