package ru.practicum.android.diploma.data.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.share.SharingRepository
import ru.practicum.android.diploma.domain.share.model.EmailData
import ru.practicum.android.diploma.domain.share.model.PhoneData
import ru.practicum.android.diploma.domain.share.model.SharingData
import javax.inject.Inject

class SharingRepositoryImpl @Inject constructor(
    private val context: Context
) : SharingRepository {
    override fun sendEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun callPhone(phoneData: PhoneData) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("tel:" + phoneData.phone)
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun sharingVacancy(sharingData: SharingData) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getSharingText(sharingData.link))
            type = "text/plain"
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSharingText(linkVacancy: String): String {
        return context.getString(R.string.share_message) + "\n" + linkVacancy
    }

    override fun openLink(sharingData: SharingData) {
        val address = Uri.parse(sharingData.link)
        val intent = Intent(Intent.ACTION_VIEW, address)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}