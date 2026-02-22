package com.gndy.peoplelog.presentation.display.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.gndy.peoplelog.domain.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserListView(
    users: List<User>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(top = 8.dp, bottom = 80.dp, start = 20.dp, end = 20.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(users, key = { _, user -> user.id }) { index, user ->
            val scale = remember { Animatable(0.8f) }
            val alpha = remember { Animatable(0f) }

            LaunchedEffect(index) {
                launch {
                    delay(minOf(index * 60L, 300L))
                    scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
                }
                launch {
                    delay(minOf(index * 60L, 300L))
                    alpha.animateTo(1f, tween(400))
                }
            }

            UserListItem(
                user = user,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        this.alpha = alpha.value
                    }
            )
        }
    }
}
