package ru.practicum.android.diploma.domain.models

//https://api.hh.ru/vacancies/86586739 запрос выглядит вот так
data class VacancyDetail(
    val id: String,
    val name: String,
    val city: String,
    //Работодатель
    val employer: String,
    val logoUrls: String,
    //Тип валюты
    val currency: String,
    //в ответе от сервера в графе salary: from:от to:до
    val salaryFrom: Int?,
    val salaryTo: Int?,
    //Опыт работы
    val experience: String,
    //Занятость //полная занятость
    val employment: String,
    //Полный день Сменный график
    val schedule: String,
    //Описание работы со всеми атрибутами HTML
    val description: String,
    //Передаются как массив
    val keySkills: List<String>
    //Осталась информация по Контакт работодателя
)
