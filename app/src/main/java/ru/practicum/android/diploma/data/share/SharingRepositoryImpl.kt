package ru.practicum.android.diploma.data.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.share.SharingRepository
import ru.practicum.android.diploma.domain.share.model.EmailData
import ru.practicum.android.diploma.domain.share.model.PhoneData
import ru.practicum.android.diploma.domain.share.model.SharingData

class SharingRepositoryImpl(
    private val context: Context
): SharingRepository{
    override fun sendEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun callPhone(phoneData: PhoneData) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:" + phoneData.phone)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun sharingVacancy(sharingData: SharingData) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getSharingText(sharingData.data))
            type = "text/plain"
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun getSharingText(message: String): String {
        return context.getString(R.string.share_message) + "\n" + message
    }
}