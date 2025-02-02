package org.kotlin.mpp.mobile.presentation.doctorlist

import org.kotlin.mpp.mobile.data.entity.Doctor
import org.kotlin.mpp.mobile.data.entity.DoctorResponse

interface DoctorListView {

    fun token(): String

    fun isConnectedToNetwork(): Boolean

    fun showDoctors(doctors: MutableList<Doctor>)

    fun showRefreshedDoctors(doctors: MutableList<Doctor>)

    fun showNoConnection()

    fun hideLoading()

    fun hidePaging()

    fun showPagingFailed()

    fun showLoadingFailed()

    fun showRefreshingFailed()

    fun showChatCreateLoader(position: Int)

    fun hideChatCreateLoader(position: Int)

    fun showChatCreationError(e: Exception)

    fun openChat(chatId: Int, avatar: String, title: String?)
}