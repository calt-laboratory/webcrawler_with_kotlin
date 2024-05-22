package org.example


fun main() {
    println("Webcrawler is running ... \n")
    val webcrawler = WebCrawler(seedURL = "https://www.kotlinlang.org/")
    //val webcrawler = WebCrawler(seedURL = "https://www.rottentomatoes.com/")
    webcrawler.run()
}