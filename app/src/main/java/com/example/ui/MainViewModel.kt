package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CollectionEntity
import com.example.data.local.DecisionEntity
import com.example.data.local.DecisionVoteEntity
import com.example.data.local.FriendEntity
import com.example.data.local.ProductEntity
import com.example.data.local.ShopcialDatabase
import com.example.data.local.StoreEntity
import com.example.data.local.StorePostEntity
import com.example.data.local.UserSessionEntity
import com.example.data.local.ChatConversationEntity
import com.example.data.local.ChatMessageEntity
import com.example.data.remote.GeminiSearchService
import com.example.data.remote.OnlinePriceComparisonResult
import com.example.data.repository.ShopcialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ShopcialRepository
    private val geminiService = GeminiSearchService()

    init {
        val db = ShopcialDatabase.getDatabase(application)
        repository = ShopcialRepository(
            productDao = db.productDao(),
            storeDao = db.storeDao(),
            decisionDao = db.decisionDao(),
            friendDao = db.friendDao(),
            collectionDao = db.collectionDao(),
            storePostDao = db.storePostDao(),
            userSessionDao = db.userSessionDao(),
            chatDao = db.chatDao(),
            geminiSearchService = geminiService
        )
    }

    val currentUser: StateFlow<UserSessionEntity?> = repository.getCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Location / City state
    private val _selectedCity = MutableStateFlow("Chennai")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    private val _selectedNeighborhood = MutableStateFlow("All Chennai")
    val selectedNeighborhood: StateFlow<String> = _selectedNeighborhood.asStateFlow()

    private val _showLocationDialog = MutableStateFlow(false)
    val showLocationDialog: StateFlow<Boolean> = _showLocationDialog.asStateFlow()

    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    // Column Layout Count (1, 2, or 3)
    private val _columnCount = MutableStateFlow(2)
    val columnCount: StateFlow<Int> = _columnCount.asStateFlow()

    // Search Query & Mode ("All", "Products", "Friends")
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchMode = MutableStateFlow("All")
    val searchMode: StateFlow<String> = _searchMode.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isMerchantMode = MutableStateFlow(false)
    val isMerchantMode: StateFlow<Boolean> = _isMerchantMode.asStateFlow()

    // Dialog & Detail states
    private val _askFriendsProduct = MutableStateFlow<ProductEntity?>(null)
    val askFriendsProduct: StateFlow<ProductEntity?> = _askFriendsProduct.asStateFlow()

    private val _selectedStoreForDetail = MutableStateFlow<StoreEntity?>(null)
    val selectedStoreForDetail: StateFlow<StoreEntity?> = _selectedStoreForDetail.asStateFlow()

    private val _storeToClaim = MutableStateFlow<StoreEntity?>(null)
    val storeToClaim: StateFlow<StoreEntity?> = _storeToClaim.asStateFlow()

    private val _showAddProductDialog = MutableStateFlow(false)
    val showAddProductDialog: StateFlow<Boolean> = _showAddProductDialog.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Online Search Grounding Comparison State
    private val _comparingProduct = MutableStateFlow<ProductEntity?>(null)
    val comparingProduct: StateFlow<ProductEntity?> = _comparingProduct.asStateFlow()

    private val _isComparingLoading = MutableStateFlow(false)
    val isComparingLoading: StateFlow<Boolean> = _isComparingLoading.asStateFlow()

    private val _comparisonResult = MutableStateFlow<OnlinePriceComparisonResult?>(null)
    val comparisonResult: StateFlow<OnlinePriceComparisonResult?> = _comparisonResult.asStateFlow()

    // Chat / Messages Area State
    val conversations: StateFlow<List<ChatConversationEntity>> = repository.getAllConversations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeConversation = MutableStateFlow<ChatConversationEntity?>(null)
    val activeConversation: StateFlow<ChatConversationEntity?> = _activeConversation.asStateFlow()

    val activeMessages: StateFlow<List<ChatMessageEntity>> = _activeConversation.flatMapLatest { conv ->
        if (conv != null) repository.getMessagesForConversation(conv.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Mock Social Sharing State
    private val _sharingProduct = MutableStateFlow<ProductEntity?>(null)
    val sharingProduct: StateFlow<ProductEntity?> = _sharingProduct.asStateFlow()

    private val _lastSharedPlatform = MutableStateFlow<String?>(null)
    val lastSharedPlatform: StateFlow<String?> = _lastSharedPlatform.asStateFlow()

    // Login modal / sheet visibility
    private val _showLoginSheet = MutableStateFlow(false)
    val showLoginSheet: StateFlow<Boolean> = _showLoginSheet.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()
        }
    }

    val stores: StateFlow<List<StoreEntity>> = _selectedNeighborhood.flatMapLatest { neighborhood ->
        repository.getStoresByNeighborhood(neighborhood)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered products
    val products: StateFlow<List<ProductEntity>> = combine(
        _selectedNeighborhood,
        _selectedCategory,
        _searchQuery,
        stores
    ) { neighborhood, category, query, allStores ->
        Quad(neighborhood, category, query, allStores)
    }.flatMapLatest { (neighborhood, category, query, storeList) ->
        combine(
            repository.getAllProducts()
        ) { productListArray ->
            var list = productListArray[0]
            if (neighborhood != "All Chennai" && !neighborhood.startsWith("All")) {
                list = list.filter { it.storeNeighborhood.equals(neighborhood, ignoreCase = true) }
            }
            if (category == "Following") {
                val followedStoreIds = storeList.filter { it.isFollowed }.map { it.id }.toSet()
                list = list.filter { followedStoreIds.contains(it.storeId) }
            } else if (category != "All") {
                list = list.filter { it.category.equals(category, ignoreCase = true) }
            }
            if (query.isNotBlank()) {
                list = list.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.brand.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
            }
            list
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val decisions: StateFlow<List<DecisionEntity>> = repository.getAllDecisions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val friends: StateFlow<List<FriendEntity>> = repository.getAllFriends()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered friends matching search query
    val searchFriends: StateFlow<List<FriendEntity>> = combine(
        friends,
        _searchQuery
    ) { allFriends, query ->
        if (query.isBlank()) allFriends
        else allFriends.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.handle.contains(query, ignoreCase = true) ||
                    it.phone.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val collections: StateFlow<List<CollectionEntity>> = repository.getAllCollections()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val storePosts: StateFlow<List<StorePostEntity>> = repository.getAllStorePosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedProducts: StateFlow<List<ProductEntity>> = repository.getSavedProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Layout & Search Mode Actions
    fun setColumnCount(count: Int) {
        _columnCount.value = count.coerceIn(1, 3)
    }

    fun setSearchMode(mode: String) {
        _searchMode.value = mode
    }

    fun openLocationDialog() {
        _showLocationDialog.value = true
    }

    fun closeLocationDialog() {
        _showLocationDialog.value = false
    }

    fun selectCity(city: String, neighborhood: String = "All $city") {
        _selectedCity.value = city
        _selectedNeighborhood.value = neighborhood
        _showLocationDialog.value = false
        viewModelScope.launch {
            repository.updateUserCity(city)
        }
        _userMessage.value = "Location set to $neighborhood"
    }

    fun openSettingsDialog() {
        _showSettingsDialog.value = true
    }

    fun closeSettingsDialog() {
        _showSettingsDialog.value = false
    }

    // Authentication Actions
    fun openLoginSheet() {
        _showLoginSheet.value = true
    }

    fun closeLoginSheet() {
        _showLoginSheet.value = false
    }

    fun loginWithPhoneOtp(phoneNumber: String, name: String, handle: String) {
        viewModelScope.launch {
            repository.loginWithPhone(phoneNumber, name, handle)
            _showLoginSheet.value = false
            _userMessage.value = "Welcome, $name! Logged in with OTP."
        }
    }

    fun loginWithGoogle(name: String, email: String) {
        viewModelScope.launch {
            repository.loginWithGoogle(name, email)
            _showLoginSheet.value = false
            _userMessage.value = "Welcome, $name! Signed in with Google."
        }
    }

    fun switchUserAccount(account: UserSessionEntity) {
        viewModelScope.launch {
            repository.switchUserSession(account)
            _isMerchantMode.value = account.isMerchant
            _showLoginSheet.value = false
            _userMessage.value = "Switched to ${account.name} (${if (account.isMerchant) "Merchant" else "Shopper"})"
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _userMessage.value = "You have logged out."
        }
    }

    // Store Follow Actions
    fun toggleFollowStore(store: StoreEntity) {
        viewModelScope.launch {
            val newFollow = !store.isFollowed
            repository.toggleFollowStore(store.id, newFollow)
            _userMessage.value = if (newFollow) "Following ${store.name} for exclusive drops!" else "Unfollowed ${store.name}"
        }
    }

    // Friend Actions
    fun addFriend(name: String, handle: String, phone: String = "") {
        viewModelScope.launch {
            repository.addFriend(name, handle, phone)
            _userMessage.value = "Added $name to your friends!"
        }
    }

    fun deleteFriend(friend: FriendEntity) {
        viewModelScope.launch {
            repository.deleteFriend(friend.id)
            _userMessage.value = "Removed ${friend.name}"
        }
    }

    fun clearDemoFriendsAndPolls() {
        viewModelScope.launch {
            repository.clearDemoFriendsAndPolls()
            _showSettingsDialog.value = false
            _userMessage.value = "Fresh App Mode enabled: demo friends & polls cleared."
        }
    }

    // Chat Actions
    fun selectConversation(conv: ChatConversationEntity) {
        _activeConversation.value = conv
    }

    fun closeActiveConversation() {
        _activeConversation.value = null
    }

    fun openChatWithFriend(friend: FriendEntity, attachedProduct: ProductEntity? = null) {
        viewModelScope.launch {
            val convId = "chat_" + friend.id
            val existing = repository.getConversationById(convId)
            val conv = existing ?: ChatConversationEntity(
                id = convId,
                title = friend.name,
                avatarUrl = friend.avatarUrl,
                isGroup = false,
                participantNames = "${friend.name}, You",
                lastMessage = attachedProduct?.let { "Shared: ${it.title}" } ?: "Started chat",
                lastMessageTimestamp = System.currentTimeMillis(),
                unreadCount = 0,
                pinnedProductId = attachedProduct?.id
            )
            if (existing == null) {
                repository.insertConversation(conv)
            }
            if (attachedProduct != null) {
                repository.sendChatMessage(
                    conversationId = convId,
                    text = "Hey ${friend.name}, what do you think of this ${attachedProduct.title} (₹${attachedProduct.price.toInt()})?",
                    attachedProduct = attachedProduct
                )
            }
            _activeConversation.value = conv
        }
    }

    fun sendChatMessage(
        text: String,
        attachedProduct: ProductEntity? = null,
        attachedDecisionId: String? = null
    ) {
        val conv = _activeConversation.value ?: return
        viewModelScope.launch {
            repository.sendChatMessage(conv.id, text, attachedProduct, attachedDecisionId)
        }
    }

    fun shareProductToConversation(
        conversationId: String,
        product: ProductEntity,
        comment: String
    ) {
        viewModelScope.launch {
            repository.sendChatMessage(
                conversationId = conversationId,
                text = comment.ifBlank { "Hey, check out this ${product.title} from ${product.brand}!" },
                attachedProduct = product
            )
            _sharingProduct.value = null
            _userMessage.value = "Product shared to chat!"
        }
    }

    // Mock Social Sharing State
    fun openAskFriendsDialog(product: ProductEntity) {
        _askFriendsProduct.value = product
    }

    fun closeAskFriendsDialog() {
        _askFriendsProduct.value = null
    }

    fun openSocialShareSheet(product: ProductEntity) {
        _sharingProduct.value = product
    }

    fun closeSocialShareSheet() {
        _sharingProduct.value = null
    }

    fun shareProductToPlatform(product: ProductEntity, platform: String) {
        _lastSharedPlatform.value = platform
        _sharingProduct.value = null
        _userMessage.value = "Shared '${product.title}' to $platform with link & preview!"
    }

    fun shareProductToChat(product: ProductEntity) {
        viewModelScope.launch {
            _sharingProduct.value = null
            repository.sendChatMessage(
                conversationId = "chat_festive_gang",
                text = "Check out this find: ${product.title} (₹${product.price.toInt()}) at ${product.brand}!",
                attachedProduct = product
            )
            _userMessage.value = "Shared to 'Festive Shopping Gang' group chat!"
        }
    }

    fun openStoreDetail(store: StoreEntity) {
        _selectedStoreForDetail.value = store
    }

    fun closeStoreDetail() {
        _selectedStoreForDetail.value = null
    }

    fun openClaimStoreDialog(store: StoreEntity) {
        _storeToClaim.value = store
    }

    fun closeClaimStoreDialog() {
        _storeToClaim.value = null
    }

    fun claimStore(storeId: String) {
        viewModelScope.launch {
            repository.claimStore(storeId)
            _storeToClaim.value = null
            _userMessage.value = "Store claimed successfully! Merchant Dashboard activated."
        }
    }

    fun openAddProductDialog() {
        _showAddProductDialog.value = true
    }

    fun closeAddProductDialog() {
        _showAddProductDialog.value = false
    }

    fun createDecisionPoll(product: ProductEntity, note: String, selectedFriendNames: List<String>) {
        viewModelScope.launch {
            val combinedNote = if (selectedFriendNames.isNotEmpty()) {
                val friendsStr = selectedFriendNames.joinToString(", ")
                if (note.isNotBlank()) "$note (Asked $friendsStr)" else "Asked $friendsStr for their purchase verdict!"
            } else {
                note.ifBlank { "Thinking of buying this nearby. What do you think?" }
            }

            val decisionId = repository.createDecision(
                productId = product.id,
                productTitle = product.title,
                productPrice = product.price,
                storeName = "${product.brand} (${product.storeNeighborhood})",
                productImageUrl = product.imageUrl,
                note = combinedNote
            )

            // Automatically share into group chat as well!
            repository.sendChatMessage(
                conversationId = "chat_festive_gang",
                text = "Launched new Ask Friends poll: $combinedNote",
                attachedProduct = product,
                attachedDecisionId = decisionId
            )

            _askFriendsProduct.value = null
            _userMessage.value = "Poll created! Sent 'Ask Friends' alert."
        }
    }

    fun castVote(decisionId: String, choice: String, comment: String) {
        viewModelScope.launch {
            repository.castVote(decisionId, choice, comment)
            _userMessage.value = "Your vote '$choice' was recorded!"
        }
    }

    fun getVotesForDecision(decisionId: String): Flow<List<DecisionVoteEntity>> {
        return repository.getVotesForDecision(decisionId)
    }

    fun addMerchantProduct(
        title: String,
        category: String,
        price: Double,
        originalPrice: Double,
        description: String,
        sizes: String,
        colors: String,
        storeName: String,
        neighborhood: String
    ) {
        viewModelScope.launch {
            repository.addMerchantProduct(
                title = title,
                category = category,
                price = price,
                originalPrice = originalPrice,
                description = description,
                sizes = sizes,
                colors = colors,
                storeId = "store_urban_threads",
                storeName = storeName.ifBlank { "Urban Threads Boutique" },
                storeNeighborhood = neighborhood.ifBlank { "Nungambakkam" },
                imageUrl = "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=800&q=80"
            )
            _showAddProductDialog.value = false
            _userMessage.value = "Product '$title' published to your local store feed!"
        }
    }

    fun createCollection(name: String, description: String) {
        viewModelScope.launch {
            repository.createCollection(name, description)
            _userMessage.value = "Collection '$name' created!"
        }
    }

    // Google Search Grounding with Gemini 3.5 Flash
    fun startOnlineComparison(product: ProductEntity) {
        _comparingProduct.value = product
        _isComparingLoading.value = true
        _comparisonResult.value = null

        viewModelScope.launch {
            try {
                val result = repository.compareProductWithOnlineSearch(
                    productTitle = product.title,
                    brand = product.brand,
                    localPrice = product.price,
                    category = product.category
                )
                _comparisonResult.value = result
            } catch (e: Exception) {
                _userMessage.value = "Comparison failed: ${e.message}"
            } finally {
                _isComparingLoading.value = false
            }
        }
    }

    fun closeComparison() {
        _comparingProduct.value = null
        _comparisonResult.value = null
        _isComparingLoading.value = false
    }

    fun setNeighborhood(neighborhood: String) {
        _selectedNeighborhood.value = neighborhood
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleMerchantMode() {
        _isMerchantMode.value = !_isMerchantMode.value
    }

    fun toggleLike(product: ProductEntity) {
        viewModelScope.launch {
            repository.toggleLike(product.id, product.isLiked)
        }
    }

    fun toggleSave(product: ProductEntity) {
        viewModelScope.launch {
            repository.toggleSave(product.id, product.isSaved)
            val msg = if (!product.isSaved) "Saved to your Shopcial Wishlist!" else "Removed from Wishlist"
            _userMessage.value = msg
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
