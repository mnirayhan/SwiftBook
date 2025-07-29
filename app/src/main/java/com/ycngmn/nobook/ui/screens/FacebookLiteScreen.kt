@file:OptIn(ExperimentalMaterial3Api::class)
package com.ycngmn.nobook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ycngmn.nobook.R
import com.ycngmn.nobook.ui.NobookViewModel
import com.ycngmn.nobook.ui.components.sheet.SheetContent
import com.ycngmn.nobook.utils.DESKTOP_USER_AGENT
import com.ycngmn.nobook.utils.Script
import com.ycngmn.nobook.utils.fetchScripts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FacebookLiteScreen(onBackToNormal: () -> Unit) {
    val viewModel: NobookViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // States for UI components
    var showSettings by remember { mutableStateOf(false) }
    var showPostComposer by remember { mutableStateOf(false) }
    val shouldRestart = remember { mutableStateOf(false) }

    // ViewModel states
    val darkMode = viewModel.amoledBlack.collectAsState()
    val isDesktop = viewModel.desktopLayout.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Facebook",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { showSettings = true }
                            )
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Implement profile drawer or remove */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(onClick = onBackToNormal) {
                        Text("Back to Normal", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (darkMode.value) Color(0xFF101820) else Color(0xFF1877F2)
                )
            )
        },
        bottomBar = {
            BottomNavigationBarLite(
                onMessenger = { /* Cannot open messenger from here */ },
                onNotifications = {},
                notificationCount = 0, // Dynamic count not available here
                darkMode = darkMode.value
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPostComposer = true },
                containerColor = Color(0xFF1877F2)
            ) {
                Icon(Icons.Default.Create, contentDescription = "Compose Post", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            key(shouldRestart.value) {
                BaseWebView(
                    url = "https://m.facebook.com/", // Use mobile site for Lite experience
                    userAgent = if (isDesktop.value) DESKTOP_USER_AGENT else null,
                    onInterceptAction = { /* Cannot open messenger from here */ },
                    onRestart = { shouldRestart.value = !shouldRestart.value },
                    viewModel = viewModel,
                    onPostLoad = {
                        val cdnBase = "https://raw.githubusercontent.com/ycngmn/Nobook/refs/heads/main/app/src/main/res/raw"
                        val scripts = listOf(
                            Script(true, R.raw.scripts, "$cdnBase/scripts.js"),
                            Script(viewModel.removeAds.value, R.raw.adblock, "$cdnBase/adblock.js"),
                            Script(viewModel.enableDownloadContent.value, R.raw.download_content, "$cdnBase/download_content.js"),
                            Script(viewModel.stickyNavbar.value, R.raw.sticky_navbar, "$cdnBase/sticky_navbar.js"),
                            Script(!viewModel.pinchToZoom.value, R.raw.pinch_to_zoom, "$cdnBase/pinch_to_zoom.js"),
                            Script(viewModel.amoledBlack.value, R.raw.amoled_black, "$cdnBase/amoled_black.js"),
                            Script(viewModel.hideStories.value, R.raw.hide_stories, "$cdnBase/hide_stories.js"),
                            Script(viewModel.hideReels.value, R.raw.hide_reels, "$cdnBase/hide_reels.js"),
                            Script(viewModel.hideSuggested.value, R.raw.hide_suggested, "$cdnBase/hide_suggested.js"),
                            Script(viewModel.hidePeopleYouMayKnow.value, R.raw.hide_pymk, "$cdnBase/hide_pymk.js"),
                            Script(viewModel.hideGroups.value, R.raw.hide_groups, "$cdnBase/hide_groups.js")
                        )
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                viewModel.scripts.value = fetchScripts(scripts, context)
                            }
                        }
                    }
                )
            }

            if (showPostComposer) {
                Dialog(onDismissRequest = { showPostComposer = false }) {
                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("Create Post", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(Modifier.height(16.dp))
                            var postText by remember { mutableStateOf("") }
                            OutlinedTextField(
                                value = postText,
                                onValueChange = { postText = it },
                                placeholder = { Text("What's on your mind?") },
                                modifier = Modifier.fillMaxWidth().height(120.dp)
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { showPostComposer = false }, modifier = Modifier.align(Alignment.End)) {
                                Text("Post")
                            }
                        }
                    }
                }
            }

            if (showSettings) {
                ModalBottomSheet(onDismissRequest = { showSettings = false }) {
                    SheetContent(
                        viewModel = viewModel,
                        onRestart = {
                            shouldRestart.value = !shouldRestart.value
                            showSettings = false
                        },
                        onClose = { showSettings = false },
                        onOpenFacebookLite = { /* Already in lite mode */ }
                    )
                }
            }
        }
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
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "Feed") },
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
                BadgedBox(badge = { if (notificationCount > 0) Badge { Text("$notificationCount") } }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
            },
            label = { Text("Notifications") }
        )
    }
}
