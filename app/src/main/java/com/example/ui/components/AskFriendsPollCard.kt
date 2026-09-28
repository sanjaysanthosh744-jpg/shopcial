package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.DecisionEntity
import com.example.data.local.DecisionVoteEntity
import com.example.ui.theme.BrickRed
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.MustardYellow
import kotlinx.coroutines.flow.Flow

@Composable
fun AskFriendsPollCard(
    decision: DecisionEntity,
    votesFlow: Flow<List<DecisionVoteEntity>>,
    onCastVote: (choice: String, comment: String) -> Unit,
    onMessageFriendClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val votes by votesFlow.collectAsState(initial = emptyList())
    val totalVotes = decision.buyVotes + decision.waitVotes + decision.dontBuyVotes

    val buyPercent = if (totalVotes > 0) (decision.buyVotes.toFloat() / totalVotes) else 0f
    val waitPercent = if (totalVotes > 0) (decision.waitVotes.toFloat() / totalVotes) else 0f
    val dontPercent = if (totalVotes > 0) (decision.dontBuyVotes.toFloat() / totalVotes) else 0f

    var showComments by remember { mutableStateOf(false) }
    var userCommentInput by remember { mutableStateOf("") }
    var selectedChoiceForVote by remember { mutableStateOf<String?>(decision.userVotedChoice) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("decision_poll_card_${decision.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Creator + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = decision.creatorAvatar,
                    contentDescription = decision.creatorName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = decision.creatorName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = " is asking friends",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = decision.storeName,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$totalVotes votes",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            .clickable { onMessageFriendClick() }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Message Friend",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Message",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Note / Question
            if (decision.note.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "\"${decision.note}\"",
                        fontSize = 14.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Product snippet preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = decision.productImageUrl,
                    contentDescription = decision.productTitle,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = decision.productTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹${decision.productPrice.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vote breakdown visual bars
            Text(
                text = "FRIEND VERDICT BREAKDOWN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            VoteProgressRow(
                label = "BUY IT",
                votes = decision.buyVotes,
                percentage = (buyPercent * 100).toInt(),
                color = ForestGreen,
                progress = buyPercent
            )

            Spacer(modifier = Modifier.height(6.dp))

            VoteProgressRow(
                label = "WAIT FOR SALE",
                votes = decision.waitVotes,
                percentage = (waitPercent * 100).toInt(),
                color = MustardYellow,
                progress = waitPercent
            )

            Spacer(modifier = Modifier.height(6.dp))

            VoteProgressRow(
                label = "DON'T BUY",
                votes = decision.dontBuyVotes,
                percentage = (dontPercent * 100).toInt(),
                color = BrickRed,
                progress = dontPercent
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Voting Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VoteActionButton(
                    text = "BUY IT",
                    icon = Icons.Default.CheckCircle,
                    color = ForestGreen,
                    isSelected = selectedChoiceForVote == "BUY",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedChoiceForVote = "BUY"
                        onCastVote("BUY", userCommentInput)
                    }
                )

                VoteActionButton(
                    text = "WAIT",
                    icon = Icons.Default.HourglassEmpty,
                    color = MustardYellow,
                    isSelected = selectedChoiceForVote == "WAIT",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedChoiceForVote = "WAIT"
                        onCastVote("WAIT", userCommentInput)
                    }
                )

                VoteActionButton(
                    text = "DON'T",
                    icon = Icons.Default.ThumbDown,
                    color = BrickRed,
                    isSelected = selectedChoiceForVote == "DONT",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedChoiceForVote = "DONT"
                        onCastVote("DONT", userCommentInput)
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Comments Toggle & Feed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { showComments = !showComments }) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comments",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showComments) "Hide Feedback (${votes.size})" else "View Friend Advice (${votes.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (decision.userVotedChoice != null) {
                    Text(
                        text = "Voted: ${decision.userVotedChoice}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (decision.userVotedChoice) {
                            "BUY" -> ForestGreen
                            "WAIT" -> MustardYellow
                            else -> BrickRed
                        }
                    )
                }
            }

            AnimatedVisibility(visible = showComments) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    votes.forEach { vote ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            AsyncImage(
                                model = vote.friendAvatar,
                                contentDescription = vote.friendName,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = vote.friendName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                when (vote.choice) {
                                                    "BUY" -> ForestGreen.copy(alpha = 0.15f)
                                                    "WAIT" -> MustardYellow.copy(alpha = 0.15f)
                                                    else -> BrickRed.copy(alpha = 0.15f)
                                                }
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = vote.choice,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (vote.choice) {
                                                "BUY" -> ForestGreen
                                                "WAIT" -> MustardYellow
                                                else -> BrickRed
                                            }
                                        )
                                    }
                                }
                                if (vote.comment.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = vote.comment,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Input to add comment
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = userCommentInput,
                            onValueChange = { userCommentInput = it },
                            placeholder = { Text("Add advice for friend...", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (userCommentInput.isNotBlank()) {
                                    val choice = selectedChoiceForVote ?: "BUY"
                                    onCastVote(choice, userCommentInput)
                                    userCommentInput = ""
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Comment",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VoteProgressRow(
    label: String,
    votes: Int,
    percentage: Int,
    color: Color,
    progress: Float
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
            Text(
                text = "$percentage% ($votes)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun VoteActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) color else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
