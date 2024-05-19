package org.example

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

    companion object {
        // Group that matches the URL in the a-tag
        private const val URL_GROUP_IDX = 2
    }

    /**
     * Gets all hyperlinks for one URL.
     * @param htmlText: HTML from one URL as string
     * @return Sequence of data class hyperlinks
     */
    fun getHyperlinks(htmlText: String) : Sequence<Hyperlink> {
        return getURLs(htmlText = htmlText).map { buildHyperlink(it) }
    }

    /**
     * Gets all URLs for a given html text string.
     * @param htmlText: HTML text as string from a given website
     * @return Sequence of URLs from htmlText of one URL
     */
    private fun getURLs(htmlText: String) : Sequence<String> {
        return aTagRegexPattern.findAll(input = htmlText)
            .mapNotNull { it.groups[URL_GROUP_IDX]?.value }
    }

    /**
     * Constructs the hyperlink as data class from a URL string.
     * @param url: URL string to be inserted into a data class
     * @return Hyperlink dataclass
     */
    fun buildHyperlink(url: String) : Hyperlink {
        val urlWithoutProtocol = url.substringAfter("://")
        return Hyperlink(
            url = url,
            protocol = Protocol.valueOf(value = url.substringBefore(':').uppercase()),
            subdomains = parseSubdomains(urlWithoutProtocol = urlWithoutProtocol),
            domain =  parseDomain(urlWithoutProtocol = urlWithoutProtocol),
            topLevelDomain = parseTopLevelDomain(urlWithoutProtocol = urlWithoutProtocol),
        )
    }

    /**
     * Parses subdomains (http, https) from a URL without the protocol.
     * @param urlWithoutProtocol: URL without protocol
     * @return Set of subdomains or empty set
    */
    private fun parseSubdomains(urlWithoutProtocol: String) : Set<String> {
        return if (hasSubdomain(urlWithoutProtocol = urlWithoutProtocol)) {
            setOf(urlWithoutProtocol.substringBefore('.'))
        } else setOf()
    }

    /**
     * Parses domain from a URL without the protocol.
     * @param urlWithoutProtocol: URL without protocol
     * @return Domain
     */
    private fun parseDomain(urlWithoutProtocol: String) : String {
        return if (hasSubdomain(urlWithoutProtocol = urlWithoutProtocol)) {
            urlWithoutProtocol.substringAfter('.').substringBefore('.')
        } else urlWithoutProtocol.substringBefore('.')
    }

    /**
     * Parses top level domain from a URL without the protocol.
     * @param urlWithoutProtocol: URL without protocol
     * @return Top level domain
     */
    private fun parseTopLevelDomain(urlWithoutProtocol: String) : String {
        return if (hasSubdomain(urlWithoutProtocol = urlWithoutProtocol)) {
            urlWithoutProtocol.substringAfter('.').substringAfter('.').substringBefore('/')
        } else urlWithoutProtocol.substringAfter('.').substringBefore('/')
    }

    /**
     * Checks if subdomain exists in a URL without the protocol.
     * @param urlWithoutProtocol: URL without protocol
     * @return True or false
     */
    private fun hasSubdomain(urlWithoutProtocol: String) : Boolean {
        return urlWithoutProtocol.substringBefore('/').count {it == '.'} > 1
    }
}
