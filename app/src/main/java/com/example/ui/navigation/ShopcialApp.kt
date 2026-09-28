package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.chat.ChatScreen
import com.example.ui.compare.CompareScreen
import com.example.ui.components.AddProductDialog
import com.example.ui.components.AskFriendsDialog
import com.example.ui.components.ClaimStoreDialog
import com.example.ui.components.SocialShareSheet
import com.example.ui.components.StoreDetailSheet
import com.example.ui.decisions.AskFriendsScreen
import com.example.ui.feed.FeedScreen
import com.example.ui.nearby.NearbyScreen
import com.example.ui.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopcialApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedScreenIndex by remember { mutableIntStateOf(0) }
    var previousScreenIndex by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    val userMessage by viewModel.userMessage.collectAsState()
    val askFriendsProduct by viewModel.askFriendsProduct.collectAsState()
    val sharingProduct by viewModel.sharingProduct.collectAsState()
    val storeToClaim by viewModel.storeToClaim.collectAsState()
    val showAddProductDialog by viewModel.showAddProductDialog.collectAsState()
    val selectedStoreForDetail by viewModel.selectedStoreForDetail.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val storePosts by viewModel.storePosts.collectAsState()
    val allProducts by viewModel.products.collectAsState()
    val decisions by viewModel.decisions.collectAsState()
    val conversations by viewModel.conversations.collectAsState()

    val totalUnreadMessages = conversations.sumOf { it.unreadCount }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    BackHandler(enabled = selectedScreenIndex != 0) {
        selectedScreenIndex = if (selectedScreenIndex == 5) previousScreenIndex else 0
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedScreenIndex == 5) "Compare Online" else "Shopcial",
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    if (selectedScreenIndex == 5) {
                        IconButton(onClick = { selectedScreenIndex = previousScreenIndex }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    if (selectedScreenIndex != 5) {
                        IconButton(
                            onClick = {
                                previousScreenIndex = selectedScreenIndex
                                selectedScreenIndex = 5
                            },
                            modifier = Modifier.testTag("top_bar_compare_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CompareArrows,
                                contentDescription = "Compare Online",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Text(
                        text = "CHENNAI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("shopcial_bottom_navigation")
            ) {
                // Discover Tab
                NavigationBarItem(
                    selected = selectedScreenIndex == 0,
                    onClick = { selectedScreenIndex = 0 },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Discover") },
                    label = { Text("Discover", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_discover")
                )

                // Nearby Stores Tab
                NavigationBarItem(
                    selected = selectedScreenIndex == 1,
                    onClick = { selectedScreenIndex = 1 },
                    icon = { Icon(Icons.Default.NearMe, contentDescription = "Nearby") },
                    label = { Text("Nearby", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_nearby")
                )

                // Ask Friends Tab (With Active Polls Badge)
                NavigationBarItem(
                    selected = selectedScreenIndex == 2,
                    onClick = { selectedScreenIndex = 2 },
                    icon = {
                        BadgedBox(badge = {
                            if (decisions.isNotEmpty()) {
                                Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                                    Text("${decisions.size}")
                                }
                            }
                        }) {
                            Icon(Icons.Default.HowToVote, contentDescription = "Ask Friends")
                        }
                    },
                    label = { Text("Ask Friends", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        indicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_ask_friends")
                )

                // Messages / Chat Tab (Normal chat + Ask Friends product discussions)
                NavigationBarItem(
                    selected = selectedScreenIndex == 3,
                    onClick = { selectedScreenIndex = 3 },
                    icon = {
                        BadgedBox(badge = {
                            if (totalUnreadMessages > 0) {
                                Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                                    Text("$totalUnreadMessages")
                                }
                            }
                        }) {
                            Icon(Icons.Default.Forum, contentDescription = "Messages")
                        }
                    },
                    label = { Text("Messages", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_messages")
                )

                // Profile / Merchant Tab
                NavigationBarItem(
                    selected = selectedScreenIndex == 4,
                    onClick = { selectedScreenIndex = 4 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_profile")
                )
            }
        }
    ) { innerPadding ->
        when (selectedScreenIndex) {
            0 -> FeedScreen(
                viewModel = viewModel,
                onNavigateToCompare = {
                    previousScreenIndex = 0
                    selectedScreenIndex = 5
                },
                onNavigateToAskFriends = { selectedScreenIndex = 2 },
                modifier = Modifier.padding(innerPadding)
            )
            1 -> NearbyScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            2 -> AskFriendsScreen(
                viewModel = viewModel,
                onNavigateToFeed = { selectedScreenIndex = 0 },
                modifier = Modifier.padding(innerPadding)
            )
            3 -> ChatScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            4 -> ProfileScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            5 -> CompareScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    // Interactive Dialogs
    askFriendsProduct?.let { prod ->
        AskFriendsDialog(
            product = prod,
            friends = friends,
            onDismiss = { viewModel.closeAskFriendsDialog() },
            onSubmitPoll = { note, selectedFriends ->
                viewModel.createDecisionPoll(prod, note, selectedFriends)
            }
        )
    }

    // Mock Social Sharing Sheet
    sharingProduct?.let { prod ->
        SocialShareSheet(
            product = prod,
            onDismiss = { viewModel.closeSocialShareSheet() },
            onShareToPlatform = { platform ->
                viewModel.shareProductToPlatform(prod, platform)
            },
            onShareToChat = {
                viewModel.shareProductToChat(prod)
            }
        )
    }

    storeToClaim?.let { store ->
        ClaimStoreDialog(
            store = store,
            onDismiss = { viewModel.closeClaimStoreDialog() },
            onConfirmClaim = { viewModel.claimStore(store.id) }
        )
    }

    if (showAddProductDialog) {
        AddProductDialog(
            onDismiss = { viewModel.closeAddProductDialog() },
            onSubmit = { title, category, price, origPrice, desc, sizes, colors, storeName, neighborhood ->
                viewModel.addMerchantProduct(title, category, price, origPrice, desc, sizes, colors, storeName, neighborhood)
            }
        )
    }

    selectedStoreForDetail?.let { store ->
        StoreDetailSheet(
            store = store,
            storePosts = storePosts,
            storeProducts = allProducts,
            onDismiss = { viewModel.closeStoreDetail() },
            onProductClick = { prod ->
                viewModel.closeStoreDetail()
                viewModel.openAskFriendsDialog(prod)
            }
        )
    }
}
