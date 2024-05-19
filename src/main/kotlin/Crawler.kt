package org.example

import java.net.URI


class WebCrawler(private val seedURL: String) {

    fun start() {
        println(readTextFromURL(url = seedURL))
    }

    private fun readTextFromURL(url: String) : String {
        return URI.create(url).toURL().readText()
    }
}
