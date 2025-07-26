package com.ycngmn.nobook.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ycngmn.nobook.R
import com.ycngmn.nobook.ui.NobookViewModel
import com.ycngmn.nobook.ui.components.sheet.SheetItem
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@Composable
fun SettingsGroup(
    title: String,
    initiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 14.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsPage(
    viewModel: NobookViewModel,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Close icon at the top right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 2.dp,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onClose() }
                            .padding(6.dp)
                    )
                }
            }

            // Feed Customization Group
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                SettingsGroup(title = stringResource(R.string.feed_customization_group), initiallyExpanded = true) {
                    SheetItem(
                        icon = R.drawable.public_off_24px,
                        title = stringResource(R.string.hide_suggested_title),
                        isActive = viewModel.hideSuggested.collectAsState().value
                    ) { viewModel.setHideSuggested(!viewModel.hideSuggested.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.movie_off_24px,
                        title = stringResource(R.string.hide_reels_title),
                        isActive = viewModel.hideReels.collectAsState().value
                    ) { viewModel.setHideReels(!viewModel.hideReels.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.landscape_2_off_24px,
                        title = stringResource(R.string.hide_stories_title),
                        isActive = viewModel.hideStories.collectAsState().value
                    ) { viewModel.setHideStories(!viewModel.hideStories.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.frame_person_off_24px,
                        title = stringResource(R.string.hide_people_you_may_know_title),
                        isActive = viewModel.hidePeopleYouMayKnow.collectAsState().value
                    ) { viewModel.setHidePeopleYouMayKnow(!viewModel.hidePeopleYouMayKnow.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.group_off_24px,
                        title = stringResource(R.string.hide_groups_title),
                        isActive = viewModel.hideGroups.collectAsState().value
                    ) { viewModel.setHideGroups(!viewModel.hideGroups.collectAsState().value) }

                    // Keyword/Hashtag Mute Section
                    Text(
                        text = stringResource(R.string.mute_keywords_title),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp, bottom = 2.dp, start = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.mute_keywords_summary),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, bottom = 2.dp)
                    )
                    var keywordInput by remember { mutableStateOf(viewModel.muteKeywords.collectAsState().value) }
                    Row(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = keywordInput,
                            onValueChange = { keywordInput = it },
                            label = { Text(stringResource(R.string.mute_keywords_hint)) },
                            placeholder = { Text(stringResource(R.string.mute_keywords_value_example)) },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { viewModel.setMuteKeywords(keywordInput) }) {
                            Text(stringResource(R.string.mute_keywords_save))
                        }
                    }
                    if (viewModel.muteKeywords.collectAsState().value.isNotBlank()) {
                        Text(
                            text = "Muted: ${viewModel.muteKeywords.collectAsState().value}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                        )
                    }
                }
            }
            Divider(modifier = Modifier.padding(vertical = 4.dp))

            // Appearance Group
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                SettingsGroup(title = stringResource(R.string.appearance_group)) {
                    SheetItem(
                        icon = R.drawable.adblock_24px,
                        title = stringResource(R.string.remove_ads_title),
                        isActive = viewModel.removeAds.collectAsState().value
                    ) { viewModel.setRemoveAds(!viewModel.removeAds.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.download_24px,
                        title = stringResource(R.string.download_content_title),
                        isActive = viewModel.enableDownloadContent.collectAsState().value
                    ) { viewModel.setEnableDownloadContent(!viewModel.enableDownloadContent.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.computer_24px,
                        title = stringResource(R.string.desktop_layout_title),
                        isActive = viewModel.desktopLayout.collectAsState().value
                    ) {
                        val isAutoDesktop = com.ycngmn.nobook.utils.isAutoDesktop()
                        if (!isAutoDesktop) viewModel.setDesktopLayout(!viewModel.desktopLayout.collectAsState().value)
                    }
                    SheetItem(
                        icon = R.drawable.immersive_mode_24px,
                        title = stringResource(R.string.immersive_mode_title),
                        isActive = viewModel.immersiveMode.collectAsState().value
                    ) { viewModel.setImmersiveMode(!viewModel.immersiveMode.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.computer_24px,
                        title = stringResource(R.string.facebook_lite_mode_title),
                        isActive = viewModel.facebookLiteMode.collectAsState().value
                    ) { viewModel.setFacebookLiteMode(!viewModel.facebookLiteMode.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.sticky_navbar_24px,
                        title = stringResource(R.string.sticky_navbar_title),
                        isActive = viewModel.stickyNavbar.collectAsState().value
                    ) { viewModel.setStickyNavbar(!viewModel.stickyNavbar.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.pinch_zoom_out_24px,
                        title = stringResource(R.string.pinch_to_zoom_title),
                        isActive = viewModel.pinchToZoom.collectAsState().value
                    ) { viewModel.setPinchToZoom(!viewModel.pinchToZoom.collectAsState().value) }
                    SheetItem(
                        icon = R.drawable.amoled_black_24px,
                        title = stringResource(R.string.amoled_black_title),
                        isActive = viewModel.amoledBlack.collectAsState().value
                    ) { viewModel.setAmoledBlack(!viewModel.amoledBlack.collectAsState().value) }
                }
            }
            Divider(modifier = Modifier.padding(vertical = 4.dp))

            // General Group
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                SettingsGroup(title = stringResource(R.string.general_group)) {
                    // App Details Section
                    Text(
                        text = stringResource(R.string.app_details_title),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 8.dp)
                    )
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                    val appName = context.getString(R.string.app_name)
                    val versionName = packageInfo.versionName ?: "-"
                    val packageName = context.packageName
                    Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                        Text(text = "${stringResource(R.string.app_name_label)}: $appName", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "${stringResource(R.string.app_version_label)}: $versionName", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "${stringResource(R.string.app_package_label)}: $packageName", style = MaterialTheme.typography.bodyMedium)
                    }
                    // Update Check
                    SheetItem(
                        icon = R.drawable.download_24px,
                        title = stringResource(R.string.check_update_title),
                        isActive = false
                    ) {
                        scope.launch(Dispatchers.IO) {
                            // Show checking toast
                            launch(Dispatchers.Main) {
                                android.widget.Toast.makeText(context, context.getString(R.string.update_checking), android.widget.Toast.LENGTH_SHORT).show()
                            }
                            try {
                                val url = URL("https://api.github.com/repos/mnirayhan/metapipe/releases/latest")
                                val json = url.readText()
                                val latestTag = JSONObject(json).getString("tag_name")
                                val currentVersion = versionName
                                if (latestTag.removePrefix("v") > currentVersion) {
                                    // Newer version available
                                    val htmlUrl = JSONObject(json).getString("html_url")
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(htmlUrl))
                                    context.startActivity(intent)
                                } else {
                                    // Up to date
                                    launch(Dispatchers.Main) {
                                        android.widget.Toast.makeText(context, context.getString(R.string.update_up_to_date), android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } catch (e: Exception) {
                                launch(Dispatchers.Main) {
                                    android.widget.Toast.makeText(context, "Update check failed", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    // Follow us on Github
                    SheetItem(
                        icon = R.drawable.github_mark_white,
                        title = stringResource(R.string.follow_at_github),
                        isActive = false
                    ) {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/mnirayhan/metapipe"))
                        context.startActivity(intent)
                    }
                }
            }
            // Other settings (customize feed, github, etc.) can be added as needed
        }
    }
}