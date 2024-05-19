package org.example

// Group that matches the URL in the a-tag
private const val URL_GROUP_IDX = 2

class HTMLParser {

    private val aTagRegexPattern = Regex(pattern = "<a[^>]*href=(['\"])(http.+?)\\1")

    /**
     * Gets all URLs for a given html text string.
     * @param htmlText: HTML text from a given website
     */
    fun getURLs(htmlText: String) : Set<String> {
        return aTagRegexPattern.findAll(input = htmlText)
            .mapNotNull { it.groups[URL_GROUP_IDX]?.value }
            .toSet()
    }
}