package com.ycngmn.nobook.ui.components.sheet

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import com.ycngmn.nobook.R
import com.ycngmn.nobook.ui.NobookViewModel
import com.ycngmn.nobook.utils.isAutoDesktop

@Composable
fun SheetContent(
    viewModel: NobookViewModel,
    onRestart: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val removeAds = viewModel.removeAds.collectAsState()
    val enableDownloadContent = viewModel.enableDownloadContent.collectAsState()
    val desktopLayout = viewModel.desktopLayout.collectAsState()
    val immersiveMode = viewModel.immersiveMode.collectAsState()
    val stickyNavbar = viewModel.stickyNavbar.collectAsState()
    val pinchToZoom = viewModel.pinchToZoom.collectAsState()
    val amoledBlack = viewModel.amoledBlack.collectAsState()
    val facebookLiteMode = viewModel.facebookLiteMode.collectAsState()
    val muteKeywords = viewModel.muteKeywords.collectAsState()
    var keywordInput by remember { mutableStateOf(muteKeywords.value) }
    val isAutoDesktop = com.ycngmn.nobook.utils.isAutoDesktop()
    val scope = rememberCoroutineScope()
    var updateResult by remember { mutableStateOf<String?>(null) }
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val appName = context.getString(R.string.app_name)
    val versionName = packageInfo.versionName ?: "-"
    val packageName = context.packageName

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        // Feed Customization
        SettingsGroup(title = stringResource(R.string.feed_customization_group), initiallyExpanded = true) {
            SheetItem(icon = R.drawable.customize_feed_24px, title = stringResource(R.string.customize_feed_title), tailIcon = R.drawable.chevron_forward_24px) {
                // Show dialog or action for customize feed
            }
            Text(text = stringResource(R.string.mute_keywords_title), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, start = 8.dp))
            Text(text = stringResource(R.string.mute_keywords_summary), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, bottom = 4.dp))
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.OutlinedTextField(
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
            if (muteKeywords.value.isNotBlank()) {
                Text(
                    text = "Muted: ${muteKeywords.value}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
        }
        Divider(modifier = Modifier.padding(vertical = 4.dp))
        // Appearance
        SettingsGroup(title = stringResource(R.string.appearance_group)) {
            SheetItem(icon = R.drawable.amoled_black_24px, title = stringResource(R.string.amoled_black_title), isActive = amoledBlack.value) { viewModel.setAmoledBlack(!amoledBlack.value) }
            SheetItem(icon = R.drawable.sticky_navbar_24px, title = stringResource(R.string.sticky_navbar_title), isActive = stickyNavbar.value) { viewModel.setStickyNavbar(!stickyNavbar.value) }
            SheetItem(icon = R.drawable.immersive_mode_24px, title = stringResource(R.string.immersive_mode_title), isActive = immersiveMode.value) { viewModel.setImmersiveMode(!immersiveMode.value) }
            SheetItem(icon = R.drawable.pinch_zoom_out_24px, title = stringResource(R.string.pinch_to_zoom_title), isActive = pinchToZoom.value) { viewModel.setPinchToZoom(!pinchToZoom.value) }
        }
        Divider(modifier = Modifier.padding(vertical = 4.dp))
        // General
        SettingsGroup(title = stringResource(R.string.general_group)) {
            SheetItem(icon = R.drawable.adblock_24px, title = stringResource(R.string.remove_ads_title), isActive = removeAds.value) { viewModel.setRemoveAds(!removeAds.value) }
            SheetItem(icon = R.drawable.download_24px, title = stringResource(R.string.download_content_title), isActive = enableDownloadContent.value) { viewModel.setEnableDownloadContent(!enableDownloadContent.value) }
            SheetItem(icon = R.drawable.computer_24px, title = stringResource(R.string.desktop_layout_title), isActive = desktopLayout.value) { if (!isAutoDesktop) viewModel.setDesktopLayout(!desktopLayout.value) }
            SheetItem(icon = R.drawable.computer_24px, title = stringResource(R.string.facebook_lite_mode_title), isActive = facebookLiteMode.value) { viewModel.setFacebookLiteMode(!facebookLiteMode.value) }
            // App Details
            Text(text = stringResource(R.string.app_details_title), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, start = 8.dp))
            Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                Text(text = "${stringResource(R.string.app_name_label)}: $appName", style = MaterialTheme.typography.bodyMedium)
                Text(text = "${stringResource(R.string.app_version_label)}: $versionName", style = MaterialTheme.typography.bodyMedium)
                Text(text = "${stringResource(R.string.app_package_label)}: $packageName", style = MaterialTheme.typography.bodyMedium)
            }
            // Check for Updates
            SheetItem(icon = R.drawable.update_24px, title = stringResource(R.string.check_update_title), isActive = false) {
                scope.launch(Dispatchers.IO) {
                    updateResult = context.getString(R.string.update_checking)
                    try {
                        val url = java.net.URL("https://api.github.com/repos/mnirayhan/metapipe/releases/latest")
                        val json = org.json.JSONObject(url.readText())
                        val latestTag = json.getString("tag_name")
                        if (latestTag.removePrefix("v") > versionName) {
                            val htmlUrl = json.getString("html_url")
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(htmlUrl))
                            context.startActivity(intent)
                        } else {
                            updateResult = context.getString(R.string.update_up_to_date)
                        }
                    } catch (e: Exception) {
                        updateResult = "Update check failed"
                    }
                }
            }
            if (updateResult != null) {
                Text(text = updateResult!!, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
            }
            SheetItem(icon = R.drawable.github_mark_white, title = stringResource(R.string.follow_at_github), isActive = false) {
                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/mnirayhan/metapipe"))
                context.startActivity(intent)
            }
        }
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card  (
                modifier = Modifier.clickable { onRestart() },
                shape = RoundedCornerShape(6.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(
                    text = stringResource(R.string.apply_immediately),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }

            VerticalDivider(Modifier.height(30.dp), color = Color.Gray, thickness = 2.dp)

            Card  (
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {

                Text(
                    text = stringResource(R.string.close_menu),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable { onClose() }
                )
            }
        }
    }
}

            // Appearance Group
            GroupDropdown(
                title = stringResource(R.string.appearance_group),
                expanded = appearanceExpanded.value,
                onClick = { appearanceExpanded.value = !appearanceExpanded.value }
            ) {
                SheetItem(
                    icon = R.drawable.amoled_black_24px,
                    title = stringResource(R.string.amoled_black_title),
                    isActive = amoledBlack.value,
                ) {
                    viewModel.setAmoledBlack(!amoledBlack.value)
                }
                SheetItem(
                    icon = R.drawable.sticky_navbar_24px,
                    title = stringResource(R.string.sticky_navbar_title),
                    isActive = stickyNavbar.value
                ) {
                    viewModel.setStickyNavbar(!stickyNavbar.value)
                }
                SheetItem(
                    icon = R.drawable.immersive_mode_24px,
                    title = stringResource(R.string.immersive_mode_title),
                    isActive = immersiveMode.value
                ) {
                    viewModel.setImmersiveMode(!immersiveMode.value)
                }
                SheetItem(
                    icon = R.drawable.pinch_zoom_out_24px,
                    title = stringResource(R.string.pinch_to_zoom_title),
                    isActive = pinchToZoom.value
                ) {
                    viewModel.setPinchToZoom(!pinchToZoom.value)
                }
            }

            // General Group
            GroupDropdown(
                title = stringResource(R.string.general_group),
                expanded = generalExpanded.value,
                onClick = { generalExpanded.value = !generalExpanded.value }
            ) {
                SheetItem(
                    icon = R.drawable.adblock_24px,
                    title = stringResource(R.string.remove_ads_title),
                    isActive = removeAds.value
                ) {
                    viewModel.setRemoveAds(!removeAds.value)
                }
                SheetItem(
                    icon = R.drawable.download_24px,
                    title = stringResource(R.string.download_content_title),
                    isActive = enableDownloadContent.value
                ) {
                    viewModel.setEnableDownloadContent(!enableDownloadContent.value)
                }
                val isAutoDesktop = isAutoDesktop()
                SheetItem(
                    icon = R.drawable.computer_24px,
                    title = stringResource(R.string.desktop_layout_title),
                    isActive = desktopLayout.value
                ) {
                    if (!isAutoDesktop) viewModel.setDesktopLayout(!desktopLayout.value)
                }
                SheetItem(
                    icon = R.drawable.computer_24px, // You may want a new icon for Lite Mode
                    title = stringResource(R.string.facebook_lite_mode_title),
                    isActive = facebookLiteMode.value
                ) {
                    viewModel.setFacebookLiteMode(!facebookLiteMode.value)
                }
                SheetItem(
                    icon = R.drawable.info_24px,
                    title = stringResource(R.string.app_details_title),
                ) {
                    isAppDetailsDialogOpen.value = true
                }
                SheetItem(
                    icon = R.drawable.update_24px,
                    title = stringResource(R.string.check_update_title),
                ) {
                    isUpdateDialogOpen.value = true
                    updateStatus.value = context.getString(R.string.update_checking)
                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(1500)
                        updateStatus.value = context.getString(R.string.update_up_to_date)
                    }
                }
                SheetItem(
                    icon = R.drawable.github_mark_white,
                    title = stringResource(R.string.follow_at_github),
                    tailIcon = R.drawable.arrow_outward_24px
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, "https://github.com/mnirayhan/metapipe".toUri())
                    context.startActivity(intent)
                }
            }
            if (isAppDetailsDialogOpen.value) {
                AppDetailsDialog(onClose = { isAppDetailsDialogOpen.value = false }, context = context)
            }
            if (isUpdateDialogOpen.value) {
                UpdateDialog(
                    status = updateStatus.value,
                    onClose = { isUpdateDialogOpen.value = false }
                )
            }

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card  (
                    modifier = Modifier.clickable { onRestart() },
                    shape = RoundedCornerShape(6.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = stringResource(R.string.apply_immediately),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }

                VerticalDivider(Modifier.height(30.dp), color = Color.Gray, thickness = 2.dp)

                Card  (
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {

                    Text(
                        text = stringResource(R.string.close_menu),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable { onClose() }
                    )
                }
            }
        }
    }
}

@Composable
private fun HideOptionsDialog(viewModel: NobookViewModel, onClose: () -> Unit) {

    val hideSuggested = viewModel.hideSuggested.collectAsState()
    val hideReels = viewModel.hideReels.collectAsState()
    val hideStories = viewModel.hideStories.collectAsState()
    val hidePeopleYouMayKnow = viewModel.hidePeopleYouMayKnow.collectAsState()
    val hideGroups = viewModel.hideGroups.collectAsState()

    Dialog(
        onDismissRequest = { onClose() }
    ) {
        Card(
            shape = RoundedCornerShape(10.dp)
        ) {
            SheetItem(
                icon = R.drawable.public_off_24px,
                title = stringResource(R.string.hide_suggested_title),
                isActive = hideSuggested.value

            ) {
                viewModel.setHideSuggested(!hideSuggested.value)
            }

            SheetItem(
                icon = R.drawable.movie_off_24px,
                title = stringResource(R.string.hide_reels_title),
                isActive = hideReels.value
            ) {
                viewModel.setHideReels(!hideReels.value)
            }

            SheetItem(
                icon = R.drawable.landscape_2_off_24px,
                title = stringResource(R.string.hide_stories_title),
                isActive = hideStories.value
            ) {
                viewModel.setHideStories(!hideStories.value)
            }

            if (viewModel.desktopLayout.collectAsState().value) return@Card

            SheetItem(
                icon = R.drawable.frame_person_off_24px,
                title = stringResource(R.string.hide_people_you_may_know_title),
                isActive = hidePeopleYouMayKnow.value
            ) {
                viewModel.setHidePeopleYouMayKnow(!hidePeopleYouMayKnow.value)
            }

            SheetItem(
                icon = R.drawable.group_off_24px,
                title = stringResource(R.string.hide_groups_title),
                isActive = hideGroups.value
            ) {
                viewModel.setHideGroups(!hideGroups.value)
            }
        }
    }
}

@Composable
fun GroupDropdown(
    title: String,
    expanded: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = if (expanded) "▲" else "▼",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (expanded) {
            content()
        }
    }
}
