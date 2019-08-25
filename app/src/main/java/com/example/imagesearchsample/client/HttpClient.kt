package com.example.imagesearchsample.client

import io.reactivex.Single
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HttpClient {
    companion object {
        fun getDocument(url: String): Single<Document> {
            return Single.create<Document> {
                val document = Jsoup.connect(url).get()
                it.onSuccess(document)
            }
        }
    }
}