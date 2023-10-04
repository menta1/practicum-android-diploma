package ru.practicum.android.diploma.domain.share

import ru.practicum.android.diploma.domain.share.model.EmailData
import ru.practicum.android.diploma.domain.share.model.PhoneData
import ru.practicum.android.diploma.domain.share.model.SharingData

interface SharingRepository {
    fun sendEmail(emailData: EmailData)
    fun callPhone(phoneData: PhoneData)
    fun sharingVacancy(sharingData: SharingData)
}