// Separate composables for modular SettingsPage sections

package com.ycngmn.nobook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ycngmn.nobook.R
import com.ycngmn.nobook.ui.NobookViewModel
import com.ycngmn.nobook.ui.components.sheet.SheetItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState

@Composable
fun SettingsGroup(
    title: String,
    initiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val arrow = if (expanded) "▼" else "▶"
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
            Text(
                text = arrow,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
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
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        FeedCustomizationSettings(viewModel)
        Divider(modifier = Modifier.padding(vertical = 4.dp))
        AppearanceSettings(viewModel)
        Divider(modifier = Modifier.padding(vertical = 4.dp))
        GeneralSettings(viewModel)
    }
}

@Composable
fun FeedCustomizationSettings(viewModel: NobookViewModel) {
    val hideSuggested = viewModel.hideSuggested.collectAsState()
    val hideReels = viewModel.hideReels.collectAsState()
    val hideStories = viewModel.hideStories.collectAsState()
    val hidePeopleYouMayKnow = viewModel.hidePeopleYouMayKnow.collectAsState()
    val hideGroups = viewModel.hideGroups.collectAsState()
    val muteKeywords = viewModel.muteKeywords.collectAsState()
    var keywordInput by remember { mutableStateOf(muteKeywords.value) }

    SettingsGroup(title = stringResource(R.string.feed_customization_group), initiallyExpanded = true) {
        SheetItem(icon = R.drawable.public_off_24px, title = stringResource(R.string.hide_suggested_title), isActive = hideSuggested.value) {
            viewModel.setHideSuggested(!hideSuggested.value)
        }
        SheetItem(icon = R.drawable.movie_off_24px, title = stringResource(R.string.hide_reels_title), isActive = hideReels.value) {
            viewModel.setHideReels(!hideReels.value)
        }
        SheetItem(icon = R.drawable.landscape_2_off_24px, title = stringResource(R.string.hide_stories_title), isActive = hideStories.value) {
            viewModel.setHideStories(!hideStories.value)
        }
        SheetItem(icon = R.drawable.frame_person_off_24px, title = stringResource(R.string.hide_people_you_may_know_title), isActive = hidePeopleYouMayKnow.value) {
            viewModel.setHidePeopleYouMayKnow(!hidePeopleYouMayKnow.value)
        }
        SheetItem(icon = R.drawable.group_off_24px, title = stringResource(R.string.hide_groups_title), isActive = hideGroups.value) {
            viewModel.setHideGroups(!hideGroups.value)
        }

        Text(text = stringResource(R.string.mute_keywords_title), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, start = 8.dp))
        Text(text = stringResource(R.string.mute_keywords_summary), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, bottom = 4.dp))
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
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
        if (muteKeywords.value.isNotBlank()) {
            Text(
                text = "Muted: ${muteKeywords.value}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun AppearanceSettings(viewModel: NobookViewModel) {
    val removeAds = viewModel.removeAds.collectAsState()
    val enableDownloadContent = viewModel.enableDownloadContent.collectAsState()
    val desktopLayout = viewModel.desktopLayout.collectAsState()
    val immersiveMode = viewModel.immersiveMode.collectAsState()
    val facebookLiteMode = viewModel.facebookLiteMode.collectAsState()
    val stickyNavbar = viewModel.stickyNavbar.collectAsState()
    val pinchToZoom = viewModel.pinchToZoom.collectAsState()
    val amoledBlack = viewModel.amoledBlack.collectAsState()

    SettingsGroup(title = stringResource(R.string.appearance_group)) {
        SheetItem(icon = R.drawable.adblock_24px, title = stringResource(R.string.remove_ads_title), isActive = removeAds.value) {
            viewModel.setRemoveAds(!removeAds.value)
        }
        SheetItem(icon = R.drawable.download_24px, title = stringResource(R.string.download_content_title), isActive = enableDownloadContent.value) {
            viewModel.setEnableDownloadContent(!enableDownloadContent.value)
        }
        SheetItem(icon = R.drawable.computer_24px, title = stringResource(R.string.desktop_layout_title), isActive = desktopLayout.value) {
            val isAutoDesktop = com.ycngmn.nobook.utils.isAutoDesktop()
            if (!isAutoDesktop) viewModel.setDesktopLayout(!desktopLayout.value)
        }
        SheetItem(icon = R.drawable.immersive_mode_24px, title = stringResource(R.string.immersive_mode_title), isActive = immersiveMode.value) {
            viewModel.setImmersiveMode(!immersiveMode.value)
        }
        SheetItem(icon = R.drawable.computer_24px, title = stringResource(R.string.facebook_lite_mode_title), isActive = facebookLiteMode.value) {
            viewModel.setFacebookLiteMode(!facebookLiteMode.value)
        }
        SheetItem(icon = R.drawable.sticky_navbar_24px, title = stringResource(R.string.sticky_navbar_title), isActive = stickyNavbar.value) {
            viewModel.setStickyNavbar(!stickyNavbar.value)
        }
        SheetItem(icon = R.drawable.pinch_zoom_out_24px, title = stringResource(R.string.pinch_to_zoom_title), isActive = pinchToZoom.value) {
            viewModel.setPinchToZoom(!pinchToZoom.value)
        }
        SheetItem(icon = R.drawable.amoled_black_24px, title = stringResource(R.string.amoled_black_title), isActive = amoledBlack.value) {
            viewModel.setAmoledBlack(!amoledBlack.value)
        }
    }
}

@Composable
fun GeneralSettings(viewModel: NobookViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val appName = context.getString(R.string.app_name)
    val versionName = packageInfo.versionName ?: "-"
    val packageName = context.packageName

    SettingsGroup(title = stringResource(R.string.general_group)) {
        Text(text = stringResource(R.string.app_details_title), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, start = 8.dp))
        Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
            Text(text = "${stringResource(R.string.app_name_label)}: $appName", style = MaterialTheme.typography.bodyMedium)
            Text(text = "${stringResource(R.string.app_version_label)}: $versionName", style = MaterialTheme.typography.bodyMedium)
            Text(text = "${stringResource(R.string.app_package_label)}: $packageName", style = MaterialTheme.typography.bodyMedium)
        }
        SheetItem(icon = R.drawable.download_24px, title = stringResource(R.string.check_update_title), isActive = false) {
            scope.launch(Dispatchers.IO) {
                launch(Dispatchers.Main) {
                    android.widget.Toast.makeText(context, context.getString(R.string.update_checking), android.widget.Toast.LENGTH_SHORT).show()
                }
                try {
                    val url = URL("https://api.github.com/repos/mnirayhan/metapipe/releases/latest")
                    val json = url.readText()
                    val latestTag = JSONObject(json).getString("tag_name")
                    if (latestTag.removePrefix("v") > versionName) {
                        val htmlUrl = JSONObject(json).getString("html_url")
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(htmlUrl))
                        context.startActivity(intent)
                    } else {
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
        SheetItem(icon = R.drawable.github_mark_white, title = stringResource(R.string.follow_at_github), isActive = false) {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/mnirayhan/metapipe"))
            context.startActivity(intent)
        }
    }
}
