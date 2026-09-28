package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY likesCount DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category = :category ORDER BY likesCount DESC")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE storeNeighborhood = :neighborhood ORDER BY storeDistanceKm ASC")
    fun getProductsByNeighborhood(neighborhood: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: String): ProductEntity?

    @Query("SELECT * FROM products WHERE isSaved = 1")
    fun getSavedProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET isLiked = :isLiked, likesCount = likesCount + :delta WHERE id = :productId")
    suspend fun updateLike(productId: String, isLiked: Boolean, delta: Int)

    @Query("UPDATE products SET isSaved = :isSaved, savesCount = savesCount + :delta WHERE id = :productId")
    suspend fun updateSave(productId: String, isSaved: Boolean, delta: Int)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun countProducts(): Int
}

@Dao
interface StoreDao {
    @Query("SELECT * FROM stores ORDER BY distanceKm ASC")
    fun getAllStores(): Flow<List<StoreEntity>>

    @Query("SELECT * FROM stores WHERE neighborhood = :neighborhood ORDER BY distanceKm ASC")
    fun getStoresByNeighborhood(neighborhood: String): Flow<List<StoreEntity>>

    @Query("SELECT * FROM stores WHERE id = :id LIMIT 1")
    suspend fun getStoreById(id: String): StoreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(stores: List<StoreEntity>)

    @Query("UPDATE stores SET isClaimed = 1 WHERE id = :storeId")
    suspend fun claimStore(storeId: String)

    @Query("UPDATE stores SET isFollowed = :isFollowed, followersCount = followersCount + (CASE WHEN :isFollowed = 1 THEN 1 ELSE -1 END) WHERE id = :storeId")
    suspend fun toggleFollowStore(storeId: String, isFollowed: Boolean)

    @Query("SELECT COUNT(*) FROM stores")
    suspend fun countStores(): Int
}

@Dao
interface DecisionDao {
    @Query("SELECT * FROM decisions ORDER BY createdAt DESC")
    fun getAllDecisions(): Flow<List<DecisionEntity>>

    @Query("SELECT * FROM decisions WHERE id = :id LIMIT 1")
    suspend fun getDecisionById(id: String): DecisionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecision(decision: DecisionEntity)

    @Update
    suspend fun updateDecision(decision: DecisionEntity)

    @Query("SELECT * FROM decision_votes WHERE decisionId = :decisionId ORDER BY timestamp ASC")
    fun getVotesForDecision(decisionId: String): Flow<List<DecisionVoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVote(vote: DecisionVoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVotes(votes: List<DecisionVoteEntity>)

    @Query("SELECT COUNT(*) FROM decisions")
    suspend fun countDecisions(): Int

    @Query("DELETE FROM decisions")
    suspend fun deleteAllDecisions()

    @Query("DELETE FROM decision_votes")
    suspend fun deleteAllVotes()
}

@Dao
interface FriendDao {
    @Query("SELECT * FROM friends ORDER BY name ASC")
    fun getAllFriends(): Flow<List<FriendEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends: List<FriendEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend: FriendEntity)

    @Query("DELETE FROM friends WHERE id = :friendId")
    suspend fun deleteFriend(friendId: String)

    @Query("DELETE FROM friends")
    suspend fun deleteAllFriends()

    @Query("SELECT COUNT(*) FROM friends")
    suspend fun countFriends(): Int
}

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections")
    fun getAllCollections(): Flow<List<CollectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollections(collections: List<CollectionEntity>)

    @Query("UPDATE collections SET itemCount = itemCount + 1 WHERE id = :collectionId")
    suspend fun incrementItemCount(collectionId: String)

    @Query("SELECT COUNT(*) FROM collections")
    suspend fun countCollections(): Int
}

@Dao
interface StorePostDao {
    @Query("SELECT * FROM store_posts ORDER BY timeAgo ASC")
    fun getAllPosts(): Flow<List<StorePostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<StorePostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: StorePostEntity)

    @Query("SELECT COUNT(*) FROM store_posts")
    suspend fun countPosts(): Int
}

@Dao
interface UserSessionDao {
    @Query("SELECT * FROM user_sessions WHERE id = 'current_user' LIMIT 1")
    fun getCurrentUser(): Flow<UserSessionEntity?>

    @Query("SELECT * FROM user_sessions WHERE id = 'current_user' LIMIT 1")
    suspend fun getCurrentUserSync(): UserSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserSession(session: UserSessionEntity)

    @Query("UPDATE user_sessions SET isLoggedIn = 0 WHERE id = 'current_user'")
    suspend fun logout()

    @Query("DELETE FROM user_sessions")
    suspend fun clearSession()
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_conversations ORDER BY lastMessageTimestamp DESC")
    fun getAllConversations(): Flow<List<ChatConversationEntity>>

    @Query("SELECT * FROM chat_conversations WHERE id = :id LIMIT 1")
    suspend fun getConversationById(id: String): ChatConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ChatConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ChatConversationEntity>)

    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Query("UPDATE chat_conversations SET lastMessage = :lastMessage, lastMessageTimestamp = :timestamp WHERE id = :conversationId")
    suspend fun updateLastMessage(conversationId: String, lastMessage: String, timestamp: Long)

    @Query("SELECT COUNT(*) FROM chat_conversations")
    suspend fun countConversations(): Int
}
