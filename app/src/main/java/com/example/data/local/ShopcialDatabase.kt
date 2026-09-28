package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProductEntity::class,
        StoreEntity::class,
        DecisionEntity::class,
        DecisionVoteEntity::class,
        FriendEntity::class,
        CollectionEntity::class,
        StorePostEntity::class,
        UserSessionEntity::class,
        ChatConversationEntity::class,
        ChatMessageEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ShopcialDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun storeDao(): StoreDao
    abstract fun decisionDao(): DecisionDao
    abstract fun friendDao(): FriendDao
    abstract fun collectionDao(): CollectionDao
    abstract fun storePostDao(): StorePostDao
    abstract fun userSessionDao(): UserSessionDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: ShopcialDatabase? = null

        fun getDatabase(context: Context): ShopcialDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopcialDatabase::class.java,
                    "shopcial_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
