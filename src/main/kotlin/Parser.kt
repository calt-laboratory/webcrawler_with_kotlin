package org.example

// Group that matches the URL in the a-tag
private const val URL_GROUP_IDX = 2

enum class Protocol {
    HTTP,
    HTTPS,
}

data class Hyperlink(
    val url: String,
    val protocol: Protocol,
    val subdomains: Set<String>,
    val domain: String,
    val topLevelDomain: String,
    val rootDomain: String = "${domain}.${topLevelDomain}",
)


class HTMLParser {

    private val aTagRegexPattern = Regex(pattern = "<a[^>]*href=(['\"])(http.+?)\\1")

    fun getHyperlinks(htmlText: String) : Sequence<Hyperlink> {
        return getURLs(htmlText = htmlText).map { buildHyperlink(it) }
    }

    /**
     * Gets all URLs for a given html text string.
     * @param htmlText: HTML text from a given website
     */
    private fun getURLs(htmlText: String) : Sequence<String> {
        return aTagRegexPattern.findAll(input = htmlText)
            .mapNotNull { it.groups[URL_GROUP_IDX]?.value }
    }

    private fun buildHyperlink(url: String) : Hyperlink {
        val urlWithoutProtocol = url.substringAfter("://")

        return Hyperlink(
            url = url,
            protocol = Protocol.valueOf(value = url.substringBefore(':').uppercase()),
            subdomains = parseSubdomains(urlWithoutProtocol = urlWithoutProtocol),
            domain =  parseDomain(urlWithoutProtocol = urlWithoutProtocol),
            topLevelDomain = parseTopLevelDomain(urlWithoutProtocol = urlWithoutProtocol),
        )
    }

    private fun parseSubdomains(urlWithoutProtocol: String) : Set<String> {
        return if (hasSubdomain(urlWithoutProtocol = urlWithoutProtocol)) {
            setOf(urlWithoutProtocol.substringBefore('.'))
        } else setOf()
    }

    private fun parseDomain(urlWithoutProtocol: String) : String {
        return if (hasSubdomain(urlWithoutProtocol = urlWithoutProtocol)) {
            urlWithoutProtocol.substringAfter('.').substringBefore('.')
        } else urlWithoutProtocol.substringBefore('.')
    }
    private fun parseTopLevelDomain(urlWithoutProtocol: String) : String {
        return if (hasSubdomain(urlWithoutProtocol = urlWithoutProtocol)) {
            urlWithoutProtocol.substringAfter('.').substringAfter('.').substringBefore('/')
        } else urlWithoutProtocol.substringAfter('.').substringBefore('/')
    }

    private fun hasSubdomain(urlWithoutProtocol: String) : Boolean {
        return urlWithoutProtocol.substringBefore('/').count {it == '.'} > 1
    }

}
