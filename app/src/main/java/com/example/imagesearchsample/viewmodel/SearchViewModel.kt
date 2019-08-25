package com.example.imagesearchsample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imagesearchsample.extensions.into
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import com.example.imagesearchsample.client.HttpClient

class SearchViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    data class ImagesResult(val imagesData: List<String>)

    private val _imagesResult = MutableLiveData<ImagesResult>()
    val imagesResult: LiveData<ImagesResult> = _imagesResult

    companion object {
        val TAG: String = SearchViewModel::class.java.simpleName
    }

    init { }

    fun getImages(keyword: String)
    {
        // Yahoo画像検索
        val url = "https://search.yahoo.co.jp/image/search?n=60&p=${keyword}&search.x=1"
        Log.d(TAG, url)

        // 検索結果のHTMLをパースしてimgタグのsrcを配列で返す
        HttpClient.getDocument(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    var tableHTML = result.select("#gridlist").first()
                    val divs = tableHTML.getElementsByTag("div")

                    _imagesResult.value = ImagesResult(
                        divs.map { it.getElementsByTag("img").attr("src") }
                            .distinct()
                            .filterNotNull()
                            .filter { it.startsWith("https://msp.c.yimg.jp") }
                    )
                },
                {
                    // TODO: エラー処理
                    it.printStackTrace()
                }
            ).into(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    class Factory(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel() as T
        }
    }
}