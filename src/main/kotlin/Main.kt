package org.example


fun main() {
    println("Webcrawler is running ... \n")
    val crawler = WebCrawler(seedURL = "https://kotlinlang.org/")
    crawler.start()

}