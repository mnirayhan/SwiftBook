package com.ycngmn.nobook.ui.screens

import androidx.compose.runtime.Composable
import com.ycngmn.nobook.ui.NobookViewModel
import com.ycngmn.nobook.utils.DESKTOP_USER_AGENT

@Composable
fun MessengerWebView(onNavigateFB: () -> Unit, viewModel: NobookViewModel) {
    BaseWebView(
        url = "https://www.facebook.com/messages",
        userAgent = DESKTOP_USER_AGENT,
        onInterceptAction = onNavigateFB,
        viewModel = viewModel
    )
}