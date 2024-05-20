package org.example

import java.net.URI


class WebCrawler(private val seedURL: String) {

    private val htmlParser = HTMLParser()

    fun run() {
        val seedHyperlink = htmlParser.buildHyperlink(url = seedURL)
        val linkQueue = ArrayDeque(elements = setOf(seedHyperlink))

        val collectedRootHyperlinks: MutableSet<Hyperlink> = mutableSetOf()
        val collectedRootDomains: MutableSet<String> = mutableSetOf()


        while (linkQueue.isNotEmpty()) {
            if (collectedRootDomains.count() > 5) break

            val link = linkQueue.removeFirst()
            val htmlText = readTextFromURL(url = link.url) ?: continue

            val urls = htmlParser.getHyperlinks(htmlText = htmlText).toSet()
            urls.forEach { linkQueue.add(it) }

            collectedRootHyperlinks.add(element = link)
            collectedRootDomains.add(element = link.rootDomain)

            println("Current Link: ${link.url}")
            println("${linkQueue.count()} hyperlinks are Currently queued ...")
        }

        val protocolStats = getHyperlinkQuantityStats(hyperlinks = collectedRootHyperlinks) { it.protocol }
        val topLevelDomainStats = getHyperlinkQuantityStats(hyperlinks = collectedRootHyperlinks) { it.topLevelDomain }
        val rootDomainStats = getHyperlinkQuantityStats(hyperlinks = collectedRootHyperlinks) { it.rootDomain}

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
        return hyperlinks.groupBy { key(it).toString() }.mapValues { it.value.count() }
    }
}
