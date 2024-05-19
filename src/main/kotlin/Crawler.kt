package org.example

import java.net.URI


class WebCrawler(private val seedURL: String) {

    private val htmlParser = HTMLParser()

    fun start() {
        val seedHTMLText = readTextFromURL(url = seedURL)
        val urls = htmlParser.getURLs(htmlText = seedHTMLText)
        println(urls)
    }

    private fun readTextFromURL(url: String) : String {
        return URI.create(url).toURL().readText()
    }
}
