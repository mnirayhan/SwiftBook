package com.ycngmn.nobook.utils

import android.content.Context
import android.content.Intent
import com.multiplatform.webview.request.RequestInterceptor
import com.multiplatform.webview.request.WebRequest
import com.multiplatform.webview.request.WebRequestInterceptResult
import com.multiplatform.webview.web.WebViewNavigator
import java.net.URL
import java.net.URLDecoder

class ExternalRequestInterceptor(
    private val context: Context,
    private val toggleMessenger: () -> Unit
) : RequestInterceptor {

    private val internalLinkRegex = Regex(
        "https?://(?!(l|lm)\\.facebook\\.com)([^/]+\\.)?(facebook\\.com|messenger\\.com)/.*"
    )

    override fun onInterceptUrlRequest(
        request: WebRequest,
        navigator: WebViewNavigator
    ): WebRequestInterceptResult {
        return if (internalLinkRegex.containsMatchIn(request.url) && request.isForMainFrame) {
            WebRequestInterceptResult.Allow
        } else {
            openInBrowser(request.url)
            WebRequestInterceptResult.Reject
        }
    }

    private fun openInBrowser(url: String) {
        val cleanUrl = fbRedirectSanitizer(url)

        try {
            val intent = createExternalIntent(cleanUrl)
            context.startActivity(intent)
        } catch (exception: Exception) {
            handleIntentFailure(url, exception)
        }
    }

    private fun createExternalIntent(url: String): Intent {
        return try {
            Intent.parseUri(url, Intent.URI_INTENT_SCHEME).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addCategory(Intent.CATEGORY_BROWSABLE)
            }
        } catch (exception: Exception) {
            // Fallback to standard ACTION_VIEW intent
            Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

    private fun handleIntentFailure(originalUrl: String, exception: Exception) {
        when {
            originalUrl.contains("fb-messenger://threads") -> {
                toggleMessenger()
            }
            originalUrl.startsWith("whatsapp://") -> {
                // Attempt to open WhatsApp web as fallback
                openWebFallback("https://web.whatsapp.com/")
            }
            else -> {
                // Log the exception for debugging purposes in development
                // Consider implementing proper logging mechanism here
            }
        }
    }

    private fun openWebFallback(fallbackUrl: String) {
        try {
            val fallbackIntent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(fallbackUrl))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(fallbackIntent)
        } catch (exception: Exception) {
            // Final fallback - silently handle if all attempts fail
        }
    }
}

// Enhanced Facebook URL sanitizer with improved encoding handling
fun fbRedirectSanitizer(link: String): String {
    return try {
        var url = URL(link)

        // Handle Facebook redirect URLs
        if (url.host == "l.facebook.com" && url.path == "/l.php") {
            val params = parseQueryParameters(url.query)
            val targetUrl = params["u"]

            if (!targetUrl.isNullOrBlank()) {
                url = URL(URLDecoder.decode(targetUrl, "UTF-8"))
            } else {
                return link
            }
        }

        // Process query parameters while preserving existing encoding
        val processedQuery = url.query?.let { queryString ->
            queryString.split("&")
                .filterNot { param ->
                    param.startsWith("fbclid=") ||
                    param.startsWith("fb_action_ids=") ||
                    param.startsWith("fb_action_types=") ||
                    param.startsWith("fb_source=")
                }
                .takeIf { it.isNotEmpty() }
                ?.joinToString("&")
        }

        // Reconstruct URL without unnecessary re-encoding
        buildString {
            append("${url.protocol}://${url.host}")
            if (url.port != -1 && url.port != url.defaultPort) {
                append(":${url.port}")
            }
            append(url.path ?: "")
            processedQuery?.let { query ->
                append("?").append(query)
            }
        }
    } catch (exception: Exception) {
        link // Graceful fallback to original link
    }
}

private fun parseQueryParameters(query: String?): Map<String, String> {
    return query?.split("&")?.associate { param ->
        val parts = param.split("=", limit = 2)
        parts[0] to (parts.getOrNull(1) ?: "")
    } ?: emptyMap()
}