@file:OptIn(ExperimentalMaterial3Api::class)
package com.ycngmn.nobook.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

@Composable
fun FacebookLiteScreen(onBackToNormal: () -> Unit) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val showScrollToTop = remember { derivedStateOf { listState.firstVisibleItemIndex > 2 } }
    var showSettings by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(false) }
    var hideStories by remember { mutableStateOf(false) }
    var hideReels by remember { mutableStateOf(false) }
    var adblock by remember { mutableStateOf(false) }
    var feedFilter by remember { mutableStateOf("All") }
    val feedFilters = listOf("All", "Friends", "Groups", "Pages")
    var showProfileDrawer by remember { mutableStateOf(false) }
    var showCommentsFor by remember { mutableStateOf<Int?>(null) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Liquid glass background colors
    val glassBg = if (darkMode) Color(0xCC23272F) else Color(0xCCF0F2F5)
    val glassGradient = if (darkMode) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF23272F).copy(alpha = 0.85f), Color(0xFF101820).copy(alpha = 0.85f))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFFF0F2F5).copy(alpha = 0.85f), Color(0xFFB3C6E0).copy(alpha = 0.85f))
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (showProfileDrawer) {
                Column(Modifier.padding(24.dp)) {
                    Text("User Name", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("user@email.com", color = Color.Gray)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { /* TODO: Open profile */ }, modifier = Modifier.fillMaxWidth()) {
                        Text("View Profile")
                    }
                    Button(onClick = { /* TODO: Logout */ }, modifier = Modifier.fillMaxWidth()) {
                        Text("Logout")
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(glassGradient)
        ) {
            // Loading bar
            AnimatedVisibility(visible = isLoading) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color(0xFF1877F2))
                        .zIndex(2f)
                )
            }
            // Main content
            Column(modifier = Modifier.fillMaxSize()) {
                // Sticky TopAppBar with Back to Normal button and menu
                TopAppBar(
                    title = {
                        Text(
                            text = "Facebook Lite",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { showProfileDrawer = true; coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSettings = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                        }
                        TextButton(onClick = onBackToNormal) {
                            Text("Back to Normal", color = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (darkMode) Color(0xFF101820) else Color(0xFF1877F2)
                    ),
                    modifier = Modifier.zIndex(1f)
                )
                // Quick Actions Row
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(if (darkMode) Color(0x8823272F) else Color(0x88FFFFFF))
                        .blur(16.dp)
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilterDropdown(feedFilter, feedFilters) { feedFilter = it }
                    QuickToggle("Dark Mode", darkMode) { darkMode = !darkMode }
                    QuickToggle("Hide Stories", hideStories) { hideStories = !hideStories }
                    QuickToggle("Hide Reels", hideReels) { hideReels = !hideReels }
                    QuickToggle("Adblock", adblock) { adblock = !adblock }
                }
                // Stories Bar
                if (!hideStories) {
                    StoriesBar(darkMode)
                }
                // Newsfeed with pull-to-refresh and liquid glass effect
                Box(
                    Modifier
                        .weight(1f)
                        .graphicsLayer { alpha = 0.98f }
                        .background(glassBg)
                        .blur(24.dp)
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { _, _ -> },
                                onDragEnd = {
                                    coroutineScope.launch {
                                        isLoading = true
                                        kotlinx.coroutines.delay(1200)
                                        isLoading = false
                                    }
                                }
                            )
                        }
                ) {
                    LazyColumn(state = listState) {
                        items(20) { idx ->
                            FeedCardLite(idx, darkMode, hideReels, onShowComments = { showCommentsFor = idx })
                        }
                    }
                }
                // Bottom navigation
                BottomNavigationBarLite(
                    onMessenger = {},
                    onNotifications = {},
                    notificationCount = 3,
                    darkMode = darkMode
                )
            }
            // Scroll to top button
            AnimatedVisibility(
                visible = showScrollToTop.value,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 80.dp, end = 20.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch { listState.animateScrollToItem(0) }
                    },
                    containerColor = Color(0xFF1877F2)
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to top", tint = Color.White)
                }
            }
            // Post Composer FAB
            FloatingActionButton(
                onClick = { /* TODO: Open post composer */ },
                containerColor = Color(0xFF1877F2),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 150.dp, end = 20.dp)
            ) {
                Icon(Icons.Default.Create, contentDescription = "Compose Post", tint = Color.White)
            }
            // Comments Popup
            if (showCommentsFor != null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                        .blur(24.dp)
                        .zIndex(100f)
                ) {}
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .clip(MaterialTheme.shapes.large)
                        .background(
                            Brush.verticalGradient(
                                colors = if (darkMode) listOf(Color(0xFF23272F).copy(alpha = 0.92f), Color(0xFF101820).copy(alpha = 0.92f))
                                else listOf(Color(0xFFF0F2F5).copy(alpha = 0.92f), Color(0xFFB3C6E0).copy(alpha = 0.92f))
                            )
                        )
                        .padding(0.dp)
                        .widthIn(max = 420.dp)
                        .heightIn(max = 480.dp)
                        .zIndex(101f)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Comments for Post #${showCommentsFor}", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                            IconButton(onClick = { showCommentsFor = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        LazyColumn(Modifier.weight(1f)) {
                            items(8) { cidx ->
                                Column(Modifier.padding(vertical = 6.dp)) {
                                    Text("User $cidx", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("This is a comment for post #${showCommentsFor}.", fontSize = 14.sp)
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            var commentText by remember { mutableStateOf("") }
                            OutlinedTextField(value = commentText, onValueChange = { commentText = it }, modifier = Modifier.weight(1f), placeholder = { Text("Add a comment...") })
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = { /* TODO: Add comment */ }) { Text("Send") }
                        }
                    }
                }
            }
            // Settings Sheet
            if (showSettings) {
                ModalBottomSheet(onDismissRequest = { showSettings = false }) {
                    Column(Modifier.padding(24.dp)) {
                        Text("Lite Settings", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Font Size", Modifier.weight(1f))
                            // Placeholder for font size slider
                            Slider(value = 1f, onValueChange = {}, valueRange = 0.8f..1.2f, steps = 2, modifier = Modifier.width(120.dp))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Compact Mode", Modifier.weight(1f))
                            Switch(checked = false, onCheckedChange = {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDropdown(selected: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(text = { Text(it) }, onClick = {
                    onSelect(it)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun QuickToggle(label: String, checked: Boolean, onCheckedChange: () -> Unit) {
    Row(
        Modifier
            .clip(MaterialTheme.shapes.small)
            .background(if (checked) Color(0xFF1877F2) else Color(0xFFE4E6EB))
            .clickable { onCheckedChange() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(checked = checked, onCheckedChange = { onCheckedChange() }, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF1877F2)))
        Spacer(Modifier.width(4.dp))
        Text(label, color = if (checked) Color.White else Color.Black, fontSize = 13.sp)
    }
}

@Composable
fun StoriesBar(darkMode: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .background(if (darkMode) Color(0xFF23272F) else Color.White)
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(10) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(60.dp)
                        .background(Color(0xFF1877F2), shape = MaterialTheme.shapes.large)
                )
                Spacer(Modifier.height(4.dp))
                Text("Story", fontSize = 12.sp, color = if (darkMode) Color.White else Color.Black)
            }
        }
    }
}

@Composable
fun FeedCardLite(idx: Int, darkMode: Boolean, hideReels: Boolean, onShowComments: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(52.dp)
                        .background(Color(0xFFE4E6EB), shape = MaterialTheme.shapes.large)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("User Name", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (darkMode) Color.White else Color.Black)
                    Text("2h", color = Color(0xFF65676B), fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            if (!hideReels && idx % 5 == 0) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFFB0C4DE), shape = MaterialTheme.shapes.medium)
                ) {
                    Text("Reel", Modifier.align(Alignment.Center), color = Color.White)
                }
                Spacer(Modifier.height(12.dp))
            }
            Text("This is a placeholder for post #$idx.", fontSize = 16.sp, color = if (darkMode) Color.White else Color(0xFF050505))
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionButtonPlaceholder("Like")
                ActionButtonPlaceholder("Comment", onClick = onShowComments)
                ActionButtonPlaceholder("Share")
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("123 Likes", fontSize = 12.sp, color = Color.Gray)
                Text("45 Comments", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ActionButtonPlaceholder(label: String, onClick: (() -> Unit)? = null) {
    Box(
        Modifier
            .background(Color(0xFFF0F2F5), shape = MaterialTheme.shapes.small)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(label, color = Color(0xFF65676B), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

@Composable
fun BottomNavigationBarLite(
    onMessenger: () -> Unit,
    onNotifications: () -> Unit,
    notificationCount: Int,
    darkMode: Boolean
) {
    NavigationBar(
        containerColor = if (darkMode) Color(0xFF23272F) else Color.White,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Feed") },
            label = { Text("Feed") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onMessenger,
            icon = { Icon(Icons.Default.Menu, contentDescription = "Messenger") },
            label = { Text("Messenger") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onNotifications,
            icon = {
                Box {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    if (notificationCount > 0) {
                        Box(
                            Modifier
                                .size(16.dp)
                                .background(Color.Red, shape = MaterialTheme.shapes.small)
                                .align(Alignment.TopEnd)
                        ) {
                            Text("$notificationCount", color = Color.White, fontSize = 10.sp, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            },
            label = { Text("Notifications") }
        )
    }
}
