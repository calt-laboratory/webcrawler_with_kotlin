package org.example


fun main() {
    println("Webcrawler is running ... \n")
    val crawler = WebCrawler(seedURL = "https://www.kotlinlang.org/")
    crawler.start()
}