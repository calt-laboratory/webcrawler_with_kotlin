package org.example

class HTMLParser {

    fun findURLs (htmlText: String) : Set<String> {
        println(aTagRegexPattern.findAll(input = htmlText).first().groups[2]?.value)
        return setOf()
    }

    companion object {
        val aTagRegexPattern = Regex(pattern = "<a[^>]*href=(['\"])(http.+?)\\1")
    }
}