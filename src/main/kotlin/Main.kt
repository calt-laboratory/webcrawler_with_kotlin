package org.example


fun main() {
    val crawler = WebCrawler(seedURL = "https://kotlinlang.org/")
    crawler.start()

}