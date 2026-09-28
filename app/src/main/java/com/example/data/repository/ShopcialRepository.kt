package com.example.data.repository

import com.example.data.local.ChatConversationEntity
import com.example.data.local.ChatMessageEntity
import com.example.data.local.CollectionEntity
import com.example.data.local.DecisionEntity
import com.example.data.local.DecisionVoteEntity
import com.example.data.local.FriendEntity
import com.example.data.local.ProductEntity
import com.example.data.local.ShopcialDatabase
import com.example.data.local.StoreEntity
import com.example.data.local.StorePostEntity
import com.example.data.local.UserSessionEntity
import com.example.data.remote.GeminiSearchService
import com.example.data.remote.InitialData
import com.example.data.remote.OnlinePriceComparisonResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ShopcialRepository(
    private val database: ShopcialDatabase,
    private val geminiService: GeminiSearchService = GeminiSearchService()
) {
    private val productDao = database.productDao()
    private val storeDao = database.storeDao()
    private val decisionDao = database.decisionDao()
    private val friendDao = database.friendDao()
    private val collectionDao = database.collectionDao()
    private val storePostDao = database.storePostDao()
    private val userSessionDao = database.userSessionDao()
    private val chatDao = database.chatDao()

    suspend fun initializeIfNeeded() {
        if (productDao.countProducts() == 0) {
            productDao.insertProducts(InitialData.PRODUCTS)
        }
        if (storeDao.countStores() == 0) {
            storeDao.insertStores(InitialData.STORES)
        }
        if (friendDao.countFriends() == 0) {
            friendDao.insertFriends(InitialData.FRIENDS)
        }
        if (decisionDao.countDecisions() == 0) {
            InitialData.INITIAL_DECISIONS.forEach { decision ->
                decisionDao.insertDecision(decision)
            }
            decisionDao.insertVotes(InitialData.INITIAL_VOTES)
        }
        if (collectionDao.countCollections() == 0) {
            collectionDao.insertCollections(InitialData.COLLECTIONS)
        }
        if (storePostDao.countPosts() == 0) {
            storePostDao.insertPosts(InitialData.STORE_POSTS)
        }
        if (userSessionDao.getCurrentUserSync() == null) {
            userSessionDao.saveUserSession(InitialData.DEFAULT_USER_SESSION)
        }
        if (chatDao.countConversations() == 0) {
            chatDao.insertConversations(InitialData.INITIAL_CONVERSATIONS)
            chatDao.insertMessages(InitialData.INITIAL_MESSAGES)
        }
    }

    // User Sessions & Authentication
    fun getCurrentUser(): Flow<UserSessionEntity?> = userSessionDao.getCurrentUser()

    suspend fun loginWithPhone(phoneNumber: String, name: String, handle: String): UserSessionEntity {
        val session = UserSessionEntity(
            id = "current_user",
            phoneNumber = phoneNumber,
            name = name.ifBlank { "Sanjay Santhosh" },
            handle = handle.ifBlank { "@sanjay_chn" },
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
            city = "Chennai",
            isLoggedIn = true,
            authProvider = "PHONE_OTP"
        )
        userSessionDao.saveUserSession(session)
        return session
    }

    suspend fun loginWithGoogle(name: String, email: String): UserSessionEntity {
        val handle = "@" + email.substringBefore("@").replace(".", "_")
        val session = UserSessionEntity(
            id = "current_user",
            phoneNumber = "+91 98401 99999",
            name = name,
            handle = handle,
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
            city = "Chennai",
            isLoggedIn = true,
            authProvider = "GOOGLE"
        )
        userSessionDao.saveUserSession(session)
        return session
    }

    suspend fun logout() {
        userSessionDao.logout()
    }

    suspend fun switchUserSession(session: UserSessionEntity) {
        userSessionDao.saveUserSession(session.copy(id = "current_user", isLoggedIn = true))
    }

    suspend fun updateUserCity(city: String) {
        val current = userSessionDao.getCurrentUserSync()
        if (current != null) {
            userSessionDao.saveUserSession(current.copy(city = city))
        }
    }

    suspend fun toggleFollowStore(storeId: String, isFollowed: Boolean) {
        storeDao.toggleFollowStore(storeId, isFollowed)
    }

    suspend fun addFriend(name: String, handle: String, phone: String = "") {
        val cleanHandle = if (handle.startsWith("@")) handle else "@$handle"
        val newFriend = FriendEntity(
            id = "friend_" + UUID.randomUUID().toString().take(6),
            name = name.ifBlank { cleanHandle.removePrefix("@") },
            handle = cleanHandle,
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&q=80",
            mutualFriends = 0,
            isOnline = true,
            phone = phone
        )
        friendDao.insertFriend(newFriend)
    }

    suspend fun deleteFriend(friendId: String) {
        friendDao.deleteFriend(friendId)
    }

    suspend fun clearDemoFriendsAndPolls() {
        friendDao.deleteAllFriends()
        decisionDao.deleteAllDecisions()
        decisionDao.deleteAllVotes()
    }

    // Chat Area & In-Chat Ask Friends
    fun getAllConversations(): Flow<List<ChatConversationEntity>> = chatDao.getAllConversations()

    suspend fun getConversationById(id: String): ChatConversationEntity? = chatDao.getConversationById(id)

    suspend fun insertConversation(conv: ChatConversationEntity) = chatDao.insertConversation(conv)

    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessageEntity>> =
        chatDao.getMessagesForConversation(conversationId)

    suspend fun sendChatMessage(
        conversationId: String,
        text: String,
        attachedProduct: ProductEntity? = null,
        attachedDecisionId: String? = null
    ) {
        val msgId = "msg_" + UUID.randomUUID().toString().take(8)
        val now = System.currentTimeMillis()
        val message = ChatMessageEntity(
            id = msgId,
            conversationId = conversationId,
            senderName = "You",
            senderAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
            isFromMe = true,
            text = text,
            timestamp = now,
            attachedProductId = attachedProduct?.id,
            attachedProductTitle = attachedProduct?.title,
            attachedProductPrice = attachedProduct?.price,
            attachedProductImage = attachedProduct?.imageUrl,
            attachedStoreName = attachedProduct?.let { "${it.brand} (${it.storeNeighborhood})" },
            attachedDecisionId = attachedDecisionId
        )
        chatDao.insertMessage(message)
        val preview = if (attachedProduct != null) "Shared product: ${attachedProduct.title}" else text
        chatDao.updateLastMessage(conversationId, preview, now)
    }

    // Products
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> =
        if (category == "All") productDao.getAllProducts()
        else productDao.getProductsByCategory(category)

    fun getProductsByNeighborhood(neighborhood: String): Flow<List<ProductEntity>> =
        if (neighborhood == "All Chennai") productDao.getAllProducts()
        else productDao.getProductsByNeighborhood(neighborhood)

    suspend fun getProductById(id: String): ProductEntity? = productDao.getProductById(id)

    fun getSavedProducts(): Flow<List<ProductEntity>> = productDao.getSavedProducts()

    suspend fun toggleLike(productId: String, currentLiked: Boolean) {
        val delta = if (currentLiked) -1 else 1
        productDao.updateLike(productId, !currentLiked, delta)
    }

    suspend fun toggleSave(productId: String, currentSaved: Boolean) {
        val delta = if (currentSaved) -1 else 1
        productDao.updateSave(productId, !currentSaved, delta)
    }

    // Stores
    fun getAllStores(): Flow<List<StoreEntity>> = storeDao.getAllStores()

    fun getStoresByNeighborhood(neighborhood: String): Flow<List<StoreEntity>> =
        if (neighborhood == "All Chennai") storeDao.getAllStores()
        else storeDao.getStoresByNeighborhood(neighborhood)

    suspend fun getStoreById(id: String): StoreEntity? = storeDao.getStoreById(id)

    suspend fun claimStore(storeId: String) {
        storeDao.claimStore(storeId)
    }

    // Decisions & Ask Friends Loop
    fun getAllDecisions(): Flow<List<DecisionEntity>> = decisionDao.getAllDecisions()

    fun getVotesForDecision(decisionId: String): Flow<List<DecisionVoteEntity>> =
        decisionDao.getVotesForDecision(decisionId)

    suspend fun createDecision(
        productId: String,
        productTitle: String,
        productPrice: Double,
        storeName: String,
        productImageUrl: String,
        note: String
    ): String {
        val id = "decision_" + UUID.randomUUID().toString().take(8)
        val decision = DecisionEntity(
            id = id,
            productId = productId,
            productTitle = productTitle,
            productPrice = productPrice,
            storeName = storeName,
            productImageUrl = productImageUrl,
            creatorName = "You",
            creatorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
            note = note,
            createdAt = System.currentTimeMillis(),
            buyVotes = 0,
            waitVotes = 0,
            dontBuyVotes = 0,
            userVotedChoice = null,
            isResolved = false
        )
        decisionDao.insertDecision(decision)
        return id
    }

    suspend fun castVote(
        decisionId: String,
        choice: String, // "BUY", "WAIT", "DONT"
        comment: String,
        userName: String = "You"
    ) {
        val decision = decisionDao.getDecisionById(decisionId) ?: return

        var buy = decision.buyVotes
        var wait = decision.waitVotes
        var dont = decision.dontBuyVotes

        when (decision.userVotedChoice) {
            "BUY" -> buy = (buy - 1).coerceAtLeast(0)
            "WAIT" -> wait = (wait - 1).coerceAtLeast(0)
            "DONT" -> dont = (dont - 1).coerceAtLeast(0)
        }

        when (choice) {
            "BUY" -> buy += 1
            "WAIT" -> wait += 1
            "DONT" -> dont += 1
        }

        val updatedDecision = decision.copy(
            buyVotes = buy,
            waitVotes = wait,
            dontBuyVotes = dont,
            userVotedChoice = choice
        )
        decisionDao.updateDecision(updatedDecision)

        if (comment.isNotBlank()) {
            val voteEntity = DecisionVoteEntity(
                id = "vote_" + UUID.randomUUID().toString().take(8),
                decisionId = decisionId,
                friendName = userName,
                friendAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
                choice = choice,
                comment = comment,
                timestamp = System.currentTimeMillis()
            )
            decisionDao.insertVote(voteEntity)
        }
    }

    // Friends & Collections
    fun getAllFriends(): Flow<List<FriendEntity>> = friendDao.getAllFriends()

    fun getAllCollections(): Flow<List<CollectionEntity>> = collectionDao.getAllCollections()

    suspend fun createCollection(name: String, description: String, coverImageUrl: String = "") {
        val col = CollectionEntity(
            id = "col_" + UUID.randomUUID().toString().take(8),
            name = name,
            description = description,
            itemCount = 0,
            coverImageUrl = coverImageUrl.ifBlank { "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=800&q=80" },
            isPrivate = false
        )
        collectionDao.insertCollection(col)
    }

    // Store Posts & Drops
    fun getAllStorePosts(): Flow<List<StorePostEntity>> = storePostDao.getAllPosts()

    suspend fun addMerchantProduct(
        title: String,
        category: String,
        price: Double,
        originalPrice: Double,
        description: String,
        sizes: String,
        colors: String,
        storeId: String,
        storeName: String,
        storeNeighborhood: String,
        imageUrl: String
    ) {
        val newProduct = ProductEntity(
            id = "prod_" + UUID.randomUUID().toString().take(8),
            title = title,
            brand = storeName,
            category = category,
            price = price,
            originalPrice = if (originalPrice > 0) originalPrice else price,
            description = description,
            storeId = storeId,
            storeName = storeName,
            storeNeighborhood = storeNeighborhood,
            storeDistanceKm = 0.5,
            imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&q=80" },
            sizes = sizes.ifBlank { "S, M, L, XL" },
            colors = colors.ifBlank { "Standard" },
            inStock = true,
            isLocal = true,
            likesCount = 1,
            savesCount = 0,
            isLiked = false,
            isSaved = false,
            onlinePriceEstimate = price * 1.1,
            onlineSource = "Amazon & Myntra"
        )
        productDao.insertProduct(newProduct)
    }

    // Live Google Search Grounding with Gemini 3.5 Flash
    suspend fun compareProductWithOnlineSearch(
        productTitle: String,
        brand: String,
        localPrice: Double,
        category: String
    ): OnlinePriceComparisonResult {
        return geminiService.compareProductOnline(productTitle, brand, localPrice, category)
    }
}
