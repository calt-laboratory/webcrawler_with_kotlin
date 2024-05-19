package org.example

import java.net.URI


class WebCrawler(private val seedURL: String) {

    private val htmlParser = HTMLParser()

    fun run() {
        val seedHyperlink = htmlParser.buildHyperlink(url = seedURL)
        val linkQueue = ArrayDeque(elements = setOf(seedHyperlink))
        val collectedRootDomains: MutableSet<String> = mutableSetOf()

        while (linkQueue.isNotEmpty()) {
            if (collectedRootDomains.count() > 10) break

            val link = linkQueue.removeFirst()
            val htmlText = readTextFromURL(url = link.url) ?: continue

            val urls = htmlParser.getHyperlinks(htmlText = htmlText).toSet()
            urls.forEach { linkQueue.add(it) }
            collectedRootDomains.add(element = link.rootDomain)

            println("Current Link: ${link.url}")
        }
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
}
