package com.example.rentit.domain.chat.usecase

import com.example.rentit.core.exceptions.ForbiddenException
import com.example.rentit.data.chat.mapper.toModel
import com.example.rentit.data.product.mapper.toChatRoomSummaryModel
import com.example.rentit.data.rental.mapper.toChatRoomSummaryModel
import com.example.rentit.domain.chat.model.ChatRoomDetailModel
import com.example.rentit.domain.chat.repository.ChatRepository
import com.example.rentit.domain.product.repository.ProductRepository
import com.example.rentit.domain.rental.repository.RentalRepository
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 채팅방 상세 정보를 가져오는 UseCase
 *
 * - 현재 로그인한 사용자가 채팅방 참가자인지 확인
 * - 채팅 참가자가 아닌 경우 ForbiddenException 발생
 * - 채팅방의 상대방 정보와 메시지 리스트를 도메인 모델로 변환
 */

class GetChatRoomDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val rentalRepository: RentalRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) {
    companion object {
        private const val DEFAULT_PAGE = 0
        private const val DEFAULT_PAGE_SIZE = 10
    }

    suspend operator fun invoke(chatRoomId: String): Result<ChatRoomDetailModel> {
        return runCatching {
            val authUserId = userRepository.getAuthUserIdFromPrefs()
            val chatRoomDetail = chatRepository.getChatDetail(chatRoomId, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).getOrThrow()
            val productId = chatRoomDetail.chatRoom.productId
            val reservationId = chatRoomDetail.chatRoom.reservationId
            val participants = chatRoomDetail.chatRoom.participants

            if(participants.find { it.userId == authUserId } == null) throw ForbiddenException()
            val partnerNickname = participants.find { it.userId != authUserId }?.nickname ?: ""

            val rentalSummary = rentalRepository.getRentalDetail(productId, reservationId).map { it.rental.toChatRoomSummaryModel() }.getOrThrow()
            val productSummary = productRepository.getProductDetail(productId).map { it.product.toChatRoomSummaryModel() }.getOrThrow()

            ChatRoomDetailModel(
                rentalSummary = rentalSummary,
                productSummary = productSummary,
                reservationId = reservationId,
                productId = productId,
                partnerNickname = partnerNickname,
                messages = chatRoomDetail.messages.map { it.toModel(it.sender.userId == authUserId) }
            )
        }
    }
}