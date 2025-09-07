package com.ycngmn.nobook.utils

import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder

fun fbRedirectSanitizer(link: String): String {
    try {
        var url = URL(link)

        // Handle Facebook redirect URLs
        if (url.host == "l.facebook.com" && url.path == "/l.php") {
            val params = url.query?.split("&")?.associate {
                val (key, value) = it.split("=", limit = 2)
                key to URLDecoder.decode(value, "UTF-8")
            } ?: emptyMap()

            val targetUrl = params["u"] ?: return link
            url = URL(targetUrl)
        }

        // Process query parameters while preserving existing encoding
        val processedQuery = url.query?.let { queryString ->
            queryString.split("&")
                .filter { param ->
                    // Remove fbclid parameters while preserving all others
                    !param.startsWith("fbclid=")
                }
                .takeIf { it.isNotEmpty() }
                ?.joinToString("&")
        }

        // Reconstruct URL without unnecessary re-encoding
        return buildString {
            append("${url.protocol}://${url.host}")
            if (url.port != -1 && url.port != url.defaultPort) {
                append(":${url.port}")
            }
            append(url.path)
            processedQuery?.let { query ->
                append("?").append(query)
            }
        }
    } catch (exception: Exception) {
        // Log exception for debugging purposes in development environment
        return link
    }
}

// Optional: Enhanced version with additional validation
fun fbRedirectSanitizerEnhanced(link: String): String {
    return try {
        val originalUrl = URL(link)
        var processedUrl = originalUrl

        // Handle Facebook redirect mechanism
        if (originalUrl.host == "l.facebook.com" && originalUrl.path == "/l.php") {
            val queryParams = parseQueryParameters(originalUrl.query)
            val targetUrlString = queryParams["u"]

            if (!targetUrlString.isNullOrBlank()) {
                processedUrl = URL(URLDecoder.decode(targetUrlString, "UTF-8"))
            } else {
                return link // Return original if no target URL found
            }
        }

        // Filter out Facebook tracking parameters while preserving URL integrity
        val sanitizedQuery = processedUrl.query?.let { queryString ->
            queryString.split("&")
                .filterNot { param ->
                    param.startsWith("fbclid=") ||
                    param.startsWith("fb_action_ids=") ||
                    param.startsWith("fb_action_types=")
                }
                .takeIf { it.isNotEmpty() }
                ?.joinToString("&")
        }

        constructUrl(processedUrl, sanitizedQuery)
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

private fun constructUrl(url: URL, query: String?): String {
    return buildString {
        append("${url.protocol}://${url.host}")
        if (url.port != -1 && url.port != url.defaultPort) {
            append(":${url.port}")
        }
        append(url.path)
        query?.takeIf { it.isNotBlank() }?.let { validQuery ->
            append("?").append(validQuery)
        }
    }
}