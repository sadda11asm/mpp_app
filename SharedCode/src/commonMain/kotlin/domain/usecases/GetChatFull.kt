package org.kotlin.mpp.mobile.domain.usecases

import UseCase
import data.repository.ChatFullRepository
import org.kotlin.mpp.mobile.data.ChatFullApi
import org.kotlin.mpp.mobile.data.entity.ChatFull
import org.kotlin.mpp.mobile.data.entity.ChatFullRequest
import org.kotlin.mpp.mobile.data.entity.ChatFullResponse
import org.kotlin.mpp.mobile.domain.model.Either
import org.kotlin.mpp.mobile.domain.model.Failure
import org.kotlin.mpp.mobile.domain.model.Success

class GetChatFull(private val chatFullRepository: ChatFullRepository) : UseCase<ChatFull, ChatFullRequest>() {
    override suspend fun run(params: ChatFullRequest): Either<Exception, ChatFull> {
        return try {
            val response = chatFullRepository.getChatFull(params.token, params.chatId, params.connection, params.cached)
            Success(response)
        } catch (e: Exception) {
            Failure(e)
        }
    }
}