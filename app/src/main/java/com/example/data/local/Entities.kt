package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val brand: String,
    val category: String, // "Streetwear", "Sneakers", "Casual", "Ethnic", "Accessories"
    val price: Double,
    val originalPrice: Double,
    val description: String,
    val storeId: String,
    val storeName: String,
    val storeNeighborhood: String,
    val storeDistanceKm: Double,
    val imageUrl: String,
    val sizes: String, // comma separated e.g. "S,M,L,XL"
    val colors: String, // comma separated e.g. "Onyx Black,Vintage Grey"
    val inStock: Boolean = true,
    val isLocal: Boolean = true,
    val likesCount: Int = 0,
    val savesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val onlinePriceEstimate: Double = 0.0,
    val onlineSource: String = "Amazon & Myntra"
)

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val address: String,
    val neighborhood: String, // "Nungambakkam", "T. Nagar", "Anna Nagar", "Phoenix Marketcity"
    val distanceKm: Double,
    val phone: String,
    val whatsappNumber: String,
    val instagramHandle: String,
    val followersCount: Int,
    val isClaimed: Boolean,
    val isFollowed: Boolean = false,
    val latitude: Double = 13.0604,
    val longitude: Double = 80.2496,
    val bannerUrl: String,
    val openHours: String,
    val placeId: String = ""
)

@Entity(tableName = "decisions")
data class DecisionEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val productTitle: String,
    val productPrice: Double,
    val storeName: String,
    val productImageUrl: String,
    val creatorName: String,
    val creatorAvatar: String,
    val note: String,
    val createdAt: Long,
    val buyVotes: Int = 0,
    val waitVotes: Int = 0,
    val dontBuyVotes: Int = 0,
    val userVotedChoice: String? = null, // "BUY", "WAIT", "DONT", or null
    val isResolved: Boolean = false,
    val finalDecision: String? = null // "BOUGHT", "PASSED"
)

@Entity(tableName = "decision_votes")
data class DecisionVoteEntity(
    @PrimaryKey val id: String,
    val decisionId: String,
    val friendName: String,
    val friendAvatar: String,
    val choice: String, // "BUY", "WAIT", "DONT"
    val comment: String,
    val timestamp: Long
)

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey val id: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val mutualFriends: Int,
    val isOnline: Boolean = false,
    val phone: String = ""
)

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val itemCount: Int = 0,
    val coverImageUrl: String = "",
    val isPrivate: Boolean = false
)

@Entity(tableName = "store_posts")
data class StorePostEntity(
    @PrimaryKey val id: String,
    val storeId: String,
    val storeName: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val likesCount: Int,
    val timeAgo: String
)

@Entity(tableName = "user_sessions")
data class UserSessionEntity(
    @PrimaryKey val id: String = "current_user",
    val phoneNumber: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val city: String = "Chennai",
    val isLoggedIn: Boolean = true,
    val authProvider: String = "PHONE_OTP", // "PHONE_OTP", "GOOGLE", "GUEST", "MERCHANT_TEST"
    val isMerchant: Boolean = false,
    val managedStoreId: String? = null
)

@Entity(tableName = "chat_conversations")
data class ChatConversationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val avatarUrl: String,
    val isGroup: Boolean = false,
    val participantNames: String,
    val lastMessage: String,
    val lastMessageTimestamp: Long,
    val unreadCount: Int = 0,
    val pinnedProductId: String? = null
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderName: String,
    val senderAvatar: String,
    val isFromMe: Boolean,
    val text: String,
    val timestamp: Long,
    val attachedProductId: String? = null,
    val attachedProductTitle: String? = null,
    val attachedProductPrice: Double? = null,
    val attachedProductImage: String? = null,
    val attachedStoreName: String? = null,
    val attachedDecisionId: String? = null
)
