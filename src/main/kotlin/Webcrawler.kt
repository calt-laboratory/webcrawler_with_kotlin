package org.example

import java.net.URI


class WebCrawler(private val seedURL: String) {

    private val htmlParser = HTMLParser()

    fun run(maxURLsCollected: Int) {
        val seedHyperlink = htmlParser.buildHyperlink(url = seedURL)
        val linkQueue = ArrayDeque(elements = setOf(seedHyperlink))

        val collectedHyperlinks: MutableSet<Hyperlink> = mutableSetOf()
        val collectedDomains: MutableSet<String> = mutableSetOf()


        while (linkQueue.isNotEmpty()) {
            if (collectedHyperlinks.count() > maxURLsCollected) break
            val link = linkQueue.removeFirst()

            // Skip already collected hyperlinks and full domains
            if (link in collectedHyperlinks) continue

            val htmlText = readTextFromURL(url = link.url) ?: continue

            val urls = htmlParser.getHyperlinks(htmlText = htmlText).toSet()
            urls.forEach { linkQueue.add(it) }

            collectedHyperlinks.add(element = link)
            collectedDomains.add(element = link.fullDomain)

            println("Current Link: ${link.url}")
            println("${linkQueue.count()} hyperlinks are Currently queued ...")
        }

        val protocolStats = getHyperlinkQuantityStats(hyperlinks = collectedHyperlinks) { it.protocol }
        val topLevelDomainStats = getHyperlinkQuantityStats(hyperlinks = collectedHyperlinks) { it.topLevelDomain }
        val rootDomainStats = getHyperlinkQuantityStats(hyperlinks = collectedHyperlinks) { it.rootDomain}

        println("\n")
        println("+++++++++++++++++ Statistics +++++++++++++++++")
        println("Protocol statistics: $protocolStats")
        println("Top-level-domain statistics: $topLevelDomainStats")
        println("Root domain statistics: $rootDomainStats")

    }

    private fun readTextFromURL(url: String) : String? {
        return try {
            URI.create(url).toURL().readText()
        } catch(exception: Exception) {
            println("Reading html text from URL $url was not possible ... ")
            println("Error message: ${exception.message}")
            null
        }
    }

    private fun getHyperlinkQuantityStats(hyperlinks: Set<Hyperlink>, key: (Hyperlink) -> Any) : Map<String, Int> {
        return hyperlinks
            .groupBy { key(it).toString() }
            .mapValues { it.value.count() }
            .toList()
            .sortedByDescending { (_ , value) -> value }
            .toMap()
    }
}
