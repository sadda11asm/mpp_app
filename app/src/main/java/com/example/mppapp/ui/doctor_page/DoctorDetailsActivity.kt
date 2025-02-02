package com.example.mppapp.ui.doctor_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.mppapp.R
import com.example.mppapp.model.DoctorO
import com.example.mppapp.model.ServiceO
import com.example.mppapp.ui.chat.ChatActivity
import com.example.mppapp.util.CloseableActivity
import com.example.mppapp.util.getAccessToken
import com.example.mppapp.util.getName
import kotlinx.android.synthetic.main.activity_doctor_details.*
import org.kotlin.mpp.mobile.ServiceLocator
import presentation.doctorpage.DoctorPageView
import java.util.*
import kotlin.collections.ArrayList

class DoctorDetailsActivity : CloseableActivity(R.layout.activity_doctor_details), DoctorPageView {

    private val presenter = ServiceLocator.doctorPagePresenter

    private lateinit var doctor: DoctorO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        doctor = intent.getSerializableExtra(EXTRA_DOCTOR) as DoctorO

        showDoctorInfo()

        val adapter =
            DoctorPagerAdapter(doctor.services as ArrayList<ServiceO>, doctor.id.toInt(), this, supportFragmentManager)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        buttonChat.setOnClickListener { showDialog() }
        buttonCall.setOnClickListener { makePhoneCall() }
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun getFullName() = getName()

    override fun token() = getAccessToken()

    override fun showDoctorInfo() {
        textName.text = doctor.name
        textSpecializations.text = doctor.specializations
        textExperience.text = resources.getString(R.string.doctor_details_experience, doctor.experience)
        textReviews.text = resources.getString(R.string.doctor_details_reviews, doctor.commentsCount)
        textQualification.text =
            if (doctor.qualifications.isNotEmpty()) doctor.qualifications[0].name else resources.getString(R.string.doctor_card_qualification)
        rating.rating = doctor.avgRate.toFloat()
        textRating.text = String.format(Locale.US, "%.1f", doctor.avgRate)

        Glide
            .with(this)
            .load(doctor.imageLink)
            .error(R.drawable.default_avatar)
            .into(imageAvatar)
    }

    override fun openChat(chatId: Int, avatar: String, title: String?) {
        ChatActivity.open(this, chatId, avatar, title?.split(',')?.get(1))
    }

    override fun createChat() {
        val title = getFullName() + "," + doctor.name
        presenter.createChat(token(), title, doctor.userId?.toInt()!!, false, doctor.id.toInt(), doctor.imageLink)
    }

    override fun showCreationError(e: Exception) {
        Log.v("Details", e.toString())
        Toast.makeText(this, R.string.doctor_details_creation_error, Toast.LENGTH_SHORT).show()
    }

    override fun showLoader() {
        progressChat.visibility = View.VISIBLE
        buttonChat.text = null
        buttonChat.alpha = 0.7F
        buttonChat.isClickable = false
        buttonChat.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

    override fun hideLoader() {
        progressChat.visibility = View.GONE
        buttonChat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chats_white, 0, 0, 0)
        buttonChat.text = resources.getString(R.string.doctor_details_chat)
        buttonChat.alpha = 1.0F
        buttonChat.isClickable = true
    }

    override fun makePhoneCall() {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", doctor.showingPhone, null))
        startActivity(intent)
    }

    private fun showDialog() {
        AlertDialog.Builder(this, R.style.ChatCreateAlertDialog)
            .setView(R.layout.dialog_chat_create)
            .setPositiveButton(R.string.doctor_list_chat_dialog_positive) { _, _ -> createChat() }
            .setNegativeButton(R.string.doctor_list_chat_dialog_negative) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    companion object {
        const val EXTRA_DOCTOR = "extra_doctor"

        fun open(context: Context, doctor: DoctorO) {
            val intent = Intent(context, DoctorDetailsActivity::class.java)
            intent.putExtra(EXTRA_DOCTOR, doctor)
            context.startActivity(intent)
        }
    }
}
