package com.example.imagesearchsample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageSearchAdapterViewModel : ViewModel() {
    val url = MutableLiveData<String>()
}
