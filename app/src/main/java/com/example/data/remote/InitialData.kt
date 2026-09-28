package com.example.data.remote

import com.example.data.local.CollectionEntity
import com.example.data.local.DecisionEntity
import com.example.data.local.DecisionVoteEntity
import com.example.data.local.FriendEntity
import com.example.data.local.ProductEntity
import com.example.data.local.StoreEntity
import com.example.data.local.StorePostEntity
import com.example.data.local.UserSessionEntity

object InitialData {
    val STORES = listOf(
        StoreEntity(
            id = "store_urban_threads",
            name = "Urban Threads Boutique",
            category = "Streetwear & Designer",
            rating = 4.8,
            reviewCount = 142,
            address = "42 Wallace Garden, Nungambakkam, Chennai",
            neighborhood = "Nungambakkam",
            distanceKm = 0.6,
            phone = "+91 98401 23456",
            whatsappNumber = "+919840123456",
            instagramHandle = "@urbanthreads.chn",
            followersCount = 24800,
            isClaimed = true,
            isFollowed = true,
            latitude = 13.0604,
            longitude = 80.2496,
            bannerUrl = "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=800&q=80",
            openHours = "10:30 AM - 9:30 PM",
            placeId = "ChIJ_urban_threads_nungam"
        ),
        StoreEntity(
            id = "store_street_kicks",
            name = "Street Kicks Hub",
            category = "Sneakers & Kicks",
            rating = 4.9,
            reviewCount = 310,
            address = "2nd Avenue, Anna Nagar, Chennai",
            neighborhood = "Anna Nagar",
            distanceKm = 1.8,
            phone = "+91 98402 34567",
            whatsappNumber = "+919840234567",
            instagramHandle = "@streetkickshub",
            followersCount = 42100,
            isClaimed = true,
            isFollowed = false,
            latitude = 13.0850,
            longitude = 80.2101,
            bannerUrl = "https://images.unsplash.com/photo-1552346154-21d32810aba3?w=800&q=80",
            openHours = "11:00 AM - 10:00 PM",
            placeId = "ChIJ_street_kicks_anna"
        ),
        StoreEntity(
            id = "store_chennai_drip",
            name = "Chennai Drip Co.",
            category = "Oversized & Graphic Drops",
            rating = 4.6,
            reviewCount = 88,
            address = "Khadar Nawaz Khan Rd, Nungambakkam, Chennai",
            neighborhood = "Nungambakkam",
            distanceKm = 0.9,
            phone = "+91 98403 45678",
            whatsappNumber = "+919840345678",
            instagramHandle = "@chennaidrip.in",
            followersCount = 18600,
            isClaimed = false, // Unclaimed store to demonstrate claim flow!
            isFollowed = false,
            latitude = 13.0620,
            longitude = 80.2480,
            bannerUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=800&q=80",
            openHours = "11:00 AM - 9:00 PM",
            placeId = "ChIJ_chennai_drip_knk"
        ),
        StoreEntity(
            id = "store_soul_wear",
            name = "Soul Wear Boutique",
            category = "Casuals & Festive Ethnic",
            rating = 4.7,
            reviewCount = 165,
            address = "G.N. Chetty Road, T. Nagar, Chennai",
            neighborhood = "T. Nagar",
            distanceKm = 2.3,
            phone = "+91 98404 56789",
            whatsappNumber = "+919840456789",
            instagramHandle = "@soulwear.chennai",
            followersCount = 15300,
            isClaimed = true,
            isFollowed = true,
            latitude = 13.0418,
            longitude = 80.2341,
            bannerUrl = "https://images.unsplash.com/photo-1567401893414-76b7b1e5a7a5?w=800&q=80",
            openHours = "10:00 AM - 9:00 PM",
            placeId = "ChIJ_soul_wear_tnagar"
        ),
        StoreEntity(
            id = "store_retro_avenue",
            name = "Retro Avenue Curated",
            category = "Vintage Denim & Jackets",
            rating = 4.5,
            reviewCount = 59,
            address = "Velachery Main Rd, Phoenix Marketcity, Chennai",
            neighborhood = "Phoenix Marketcity",
            distanceKm = 4.5,
            phone = "+91 98405 67890",
            whatsappNumber = "+919840567890",
            instagramHandle = "@retroavenue_vintage",
            followersCount = 9200,
            isClaimed = false,
            isFollowed = false,
            latitude = 12.9915,
            longitude = 80.2170,
            bannerUrl = "https://images.unsplash.com/photo-1525507119028-ed4c629a60a3?w=800&q=80",
            openHours = "10:30 AM - 10:00 PM",
            placeId = "ChIJ_retro_ave_phoenix"
        )
    )

    val PRODUCTS = listOf(
        ProductEntity(
            id = "prod_snitch_overshirt",
            title = "Snitch Textured Linen Oxford Overshirt",
            brand = "Snitch",
            category = "Casual",
            price = 1999.0,
            originalPrice = 2999.0,
            description = "Breezy lightweight premium linen-cotton blend overshirt with chest patch pockets and horn buttons. Perfect for tropical evenings.",
            storeId = "store_urban_threads",
            storeName = "Urban Threads Boutique",
            storeNeighborhood = "Nungambakkam",
            storeDistanceKm = 0.6,
            imageUrl = "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=800&q=80",
            sizes = "S, M, L, XL",
            colors = "Sage Olive, Warm Ivory, Sand Beige",
            inStock = true,
            isLocal = true,
            likesCount = 384,
            savesCount = 142,
            isLiked = false,
            isSaved = true,
            onlinePriceEstimate = 2299.0,
            onlineSource = "Myntra / Snitch.co.in"
        ),
        ProductEntity(
            id = "prod_zara_denim_jacket",
            title = "Zara Relaxed Fit Washed Denim Biker",
            brand = "Zara",
            category = "Streetwear",
            price = 3990.0,
            originalPrice = 4990.0,
            description = "Heavyweight 100% rigid cotton denim jacket in vintage washed charcoal with silver hardware and drop-shoulder silhouette.",
            storeId = "store_urban_threads",
            storeName = "Urban Threads Boutique",
            storeNeighborhood = "Nungambakkam",
            storeDistanceKm = 0.6,
            imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&q=80",
            sizes = "S, M, L, XL",
            colors = "Washed Black, Vintage Indigo",
            inStock = true,
            isLocal = true,
            likesCount = 520,
            savesCount = 280,
            isLiked = true,
            isSaved = false,
            onlinePriceEstimate = 4290.0,
            onlineSource = "Zara India & Myntra"
        ),
        ProductEntity(
            id = "prod_nike_jordan_low",
            title = "Nike Air Jordan 1 Low 'Wolf Grey'",
            brand = "Nike",
            category = "Sneakers",
            price = 8995.0,
            originalPrice = 11495.0,
            description = "Iconic low-top silhouette featuring clean wolf grey and white leather panels, encapsulated Air cushioning, and icy translucent outsole.",
            storeId = "store_street_kicks",
            storeName = "Street Kicks Hub",
            storeNeighborhood = "Anna Nagar",
            storeDistanceKm = 1.8,
            imageUrl = "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=800&q=80",
            sizes = "UK 7, UK 8, UK 9, UK 10",
            colors = "Wolf Grey, Pure White",
            inStock = true,
            isLocal = true,
            likesCount = 890,
            savesCount = 612,
            isLiked = true,
            isSaved = true,
            onlinePriceEstimate = 9999.0,
            onlineSource = "Flipkart / Superkicks / Myntra"
        ),
        ProductEntity(
            id = "prod_fabindia_kurta",
            title = "FabIndia Handblock Printed Cotton Short Kurta",
            brand = "FabIndia",
            category = "Ethnic",
            price = 2190.0,
            originalPrice = 2890.0,
            description = "Handcrafted pure breathable cotton with traditional Bagru block print, mandarin collar, and wooden button placket.",
            storeId = "store_soul_wear",
            storeName = "Soul Wear Boutique",
            storeNeighborhood = "T. Nagar",
            storeDistanceKm = 2.3,
            imageUrl = "https://images.unsplash.com/photo-1605518216938-7c31b7b14ad0?w=800&q=80",
            sizes = "38, 40, 42, 44",
            colors = "Indigo Blue, Brick Red, Natural Cream",
            inStock = true,
            isLocal = true,
            likesCount = 315,
            savesCount = 110,
            isLiked = false,
            isSaved = false,
            onlinePriceEstimate = 2490.0,
            onlineSource = "Ajio / Myntra"
        ),
        ProductEntity(
            id = "prod_hm_parachute_cargos",
            title = "H&M Parachute Loose Tactical Cargos",
            brand = "H&M",
            category = "Streetwear",
            price = 1999.0,
            originalPrice = 2699.0,
            description = "Lightweight poplin cotton parachute pants with adjustable toggles at the hem, deep 3D pleated cargo pockets, and relaxed fit.",
            storeId = "store_chennai_drip",
            storeName = "Chennai Drip Co.",
            storeNeighborhood = "Nungambakkam",
            storeDistanceKm = 0.9,
            imageUrl = "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=800&q=80",
            sizes = "30, 32, 34, 36",
            colors = "Deep Olive, Desert Khaki, Matte Black",
            inStock = true,
            isLocal = true,
            likesCount = 445,
            savesCount = 198,
            isLiked = false,
            isSaved = true,
            onlinePriceEstimate = 2299.0,
            onlineSource = "H&M App & Myntra"
        ),
        ProductEntity(
            id = "prod_levis_512_jeans",
            title = "Levi's 512 Slim Taper Flex Denim",
            brand = "Levi's",
            category = "Casual",
            price = 3299.0,
            originalPrice = 4299.0,
            description = "The ultimate modern taper jeans with built-in Levi's Flex stretch for mobility and classic 5-pocket styling in medium stone wash.",
            storeId = "store_retro_avenue",
            storeName = "Retro Avenue Curated",
            storeNeighborhood = "Phoenix Marketcity",
            storeDistanceKm = 4.5,
            imageUrl = "https://images.unsplash.com/photo-1542272604-780c96856592?w=800&q=80",
            sizes = "30, 32, 34, 36",
            colors = "Medium Stonewash, Deep Indigo",
            inStock = true,
            isLocal = true,
            likesCount = 270,
            savesCount = 85,
            isLiked = false,
            isSaved = false,
            onlinePriceEstimate = 3599.0,
            onlineSource = "Amazon.in & Flipkart"
        ),
        ProductEntity(
            id = "prod_souled_store_oversized_tee",
            title = "The Souled Store Heavyweight Graphic Anime Tee",
            brand = "The Souled Store",
            category = "Streetwear",
            price = 999.0,
            originalPrice = 1399.0,
            description = "240 GSM pre-shrunk cotton with durable back screen print and ribbed crewneck. Relaxed street fit.",
            storeId = "store_chennai_drip",
            storeName = "Chennai Drip Co.",
            storeNeighborhood = "Nungambakkam",
            storeDistanceKm = 0.9,
            imageUrl = "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=800&q=80",
            sizes = "M, L, XL, XXL",
            colors = "Washed Charcoal, Pure Black",
            inStock = true,
            isLocal = true,
            likesCount = 680,
            savesCount = 310,
            isLiked = true,
            isSaved = false,
            onlinePriceEstimate = 1099.0,
            onlineSource = "The Souled Store & Myntra"
        ),
        ProductEntity(
            id = "prod_westside_midi_dress",
            title = "Westside Floral Tiered A-Line Midi Dress",
            brand = "Westside",
            category = "Casual",
            price = 1699.0,
            originalPrice = 2299.0,
            description = "Flowy georgette fabric with subtle floral motifs, sweetheart neckline, tiered skirt flare, and smocked back panel.",
            storeId = "store_soul_wear",
            storeName = "Soul Wear Boutique",
            storeNeighborhood = "T. Nagar",
            storeDistanceKm = 2.3,
            imageUrl = "https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=800&q=80",
            sizes = "XS, S, M, L",
            colors = "Terracotta Coral, Sage Blossom",
            inStock = true,
            isLocal = true,
            likesCount = 390,
            savesCount = 205,
            isLiked = true,
            isSaved = true,
            onlinePriceEstimate = 1899.0,
            onlineSource = "Tata CLiQ & Westside"
        )
    )

    val FRIENDS = listOf(
        FriendEntity(
            id = "friend_priya",
            name = "Priya Sharma",
            handle = "@priya_fits",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&q=80",
            mutualFriends = 8,
            isOnline = true,
            phone = "+91 98401 11111"
        ),
        FriendEntity(
            id = "friend_rahul",
            name = "Rahul Mehta",
            handle = "@rahul_kicks",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&q=80",
            mutualFriends = 12,
            isOnline = true,
            phone = "+91 98402 22222"
        ),
        FriendEntity(
            id = "friend_ananya",
            name = "Ananya Krishnan",
            handle = "@ananya_chn",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&q=80",
            mutualFriends = 5,
            isOnline = false,
            phone = "+91 98403 33333"
        ),
        FriendEntity(
            id = "friend_vikram",
            name = "Vikram Sundaram",
            handle = "@vikram_drip",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&q=80",
            mutualFriends = 14,
            isOnline = true,
            phone = "+91 98404 44444"
        )
    )

    val DECISIONS = listOf(
        DecisionEntity(
            id = "decision_jacket_1",
            productId = "prod_zara_denim_jacket",
            productTitle = "Zara Relaxed Fit Washed Denim Biker",
            productPrice = 3990.0,
            storeName = "Urban Threads Boutique (Nungambakkam)",
            productImageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&q=80",
            creatorName = "You",
            creatorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
            note = "Tried this in Nungambakkam! Should I cop it or wait for Diwali sale online?",
            createdAt = System.currentTimeMillis() - 1000 * 60 * 45,
            buyVotes = 3,
            waitVotes = 1,
            dontBuyVotes = 0,
            userVotedChoice = "BUY"
        ),
        DecisionEntity(
            id = "decision_kicks_2",
            productId = "prod_nike_jordan_low",
            productTitle = "Nike Air Jordan 1 Low 'Wolf Grey'",
            productPrice = 8995.0,
            storeName = "Street Kicks Hub (Anna Nagar)",
            productImageUrl = "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=800&q=80",
            creatorName = "Rahul Mehta",
            creatorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&q=80",
            note = "They have my exact UK 9 size in Anna Nagar right now. Legit check and cop?",
            createdAt = System.currentTimeMillis() - 1000 * 60 * 120,
            buyVotes = 5,
            waitVotes = 0,
            dontBuyVotes = 1,
            userVotedChoice = null
        )
    )

    val DECISION_VOTES = listOf(
        DecisionVoteEntity(
            id = "vote_1",
            decisionId = "decision_jacket_1",
            friendName = "Priya Sharma",
            friendAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&q=80",
            choice = "BUY",
            comment = "100% BUY! The washed charcoal color looks way better in person than photos.",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 30
        ),
        DecisionVoteEntity(
            id = "vote_2",
            decisionId = "decision_jacket_1",
            friendName = "Rahul Mehta",
            friendAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&q=80",
            choice = "BUY",
            comment = "Fits crazy good with black cargos. Cop it before it runs out.",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 20
        ),
        DecisionVoteEntity(
            id = "vote_3",
            decisionId = "decision_jacket_1",
            friendName = "Ananya Krishnan",
            friendAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&q=80",
            choice = "WAIT",
            comment = "Looks good, but check if Myntra has an extra ₹300 coupon code first.",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 10
        )
    )

    val COLLECTIONS = listOf(
        CollectionEntity(
            id = "col_college_fits",
            name = "Weekend Outing Fits",
            description = "Curated linen overshirts and statement jackets from Nungambakkam boutiques.",
            itemCount = 3,
            coverImageUrl = "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=800&q=80",
            isPrivate = false
        ),
        CollectionEntity(
            id = "col_sneaker_wishlist",
            name = "Sneaker Wishlist 2026",
            description = "Local store drops vs online hype sneakers.",
            itemCount = 2,
            coverImageUrl = "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=800&q=80",
            isPrivate = false
        )
    )

    val STORE_POSTS = listOf(
        StorePostEntity(
            id = "post_1",
            storeId = "store_urban_threads",
            storeName = "Urban Threads Boutique",
            title = "Weekend Drop: Premium Linen Overshirts Landed",
            content = "Only 25 pieces in Nungambakkam! Sizes S to XL. Walk in to try sizing or message us directly.",
            imageUrl = "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=800&q=80",
            likesCount = 89,
            timeAgo = "2h ago"
        ),
        StorePostEntity(
            id = "post_2",
            storeId = "store_street_kicks",
            storeName = "Street Kicks Hub",
            title = "Restock Alert: Air Jordan 1 Low Wolf Grey",
            content = "Back in stock in Anna Nagar across UK 7 to 10. Instant trial available.",
            imageUrl = "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=800&q=80",
            likesCount = 145,
            timeAgo = "5h ago"
        )
    )

    val DEFAULT_USER_SESSION = UserSessionEntity(
        id = "current_user",
        phoneNumber = "+91 98401 23456",
        name = "Sanjay Santhosh",
        handle = "@sanjay_chn",
        avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
        city = "Chennai",
        isLoggedIn = true,
        authProvider = "PHONE_OTP",
        isMerchant = false
    )

    val INITIAL_CONVERSATIONS = listOf(
        com.example.data.local.ChatConversationEntity(
            id = "chat_priya",
            title = "Priya Sharma",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&q=80",
            isGroup = false,
            participantNames = "Priya Sharma, You",
            lastMessage = "Bro 100% BUY! The fit looks unreal at Urban Threads.",
            lastMessageTimestamp = System.currentTimeMillis() - 1000 * 60 * 15,
            unreadCount = 1,
            pinnedProductId = "prod_zara_denim_jacket"
        ),
        com.example.data.local.ChatConversationEntity(
            id = "chat_festive_gang",
            title = "Festive Shopping Gang 🛍️",
            avatarUrl = "https://images.unsplash.com/photo-1511632765486-a01980e01a18?w=200&q=80",
            isGroup = true,
            participantNames = "Priya, Rahul, Vikram, Ananya, You",
            lastMessage = "Rahul: Street Kicks Hub just restocked the Wolf Grey Jordans!",
            lastMessageTimestamp = System.currentTimeMillis() - 1000 * 60 * 45,
            unreadCount = 2,
            pinnedProductId = "prod_nike_jordan_low"
        )
    )

    val MERCHANT_TEST_ACCOUNTS = listOf(
        UserSessionEntity(
            id = "current_user",
            phoneNumber = "+91 98401 23456",
            name = "Sanjay Santhosh",
            handle = "@sanjay_chn",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
            city = "Chennai",
            isLoggedIn = true,
            authProvider = "PHONE_OTP",
            isMerchant = false
        ),
        UserSessionEntity(
            id = "current_user",
            phoneNumber = "+91 98401 88888",
            name = "Karthik (Urban Threads Owner)",
            handle = "@karthik_urbanthreads",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&q=80",
            city = "Chennai",
            isLoggedIn = true,
            authProvider = "MERCHANT_TEST",
            isMerchant = true,
            managedStoreId = "store_urban_threads"
        ),
        UserSessionEntity(
            id = "current_user",
            phoneNumber = "+91 98402 77777",
            name = "Pooja (Soul Wear T. Nagar)",
            handle = "@pooja_soulwear",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&q=80",
            city = "Chennai",
            isLoggedIn = true,
            authProvider = "MERCHANT_TEST",
            isMerchant = true,
            managedStoreId = "store_soul_wear"
        )
    )
}
