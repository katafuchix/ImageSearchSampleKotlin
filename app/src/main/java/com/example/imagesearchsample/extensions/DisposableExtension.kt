package com.example.imagesearchsample.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.into(disposable: CompositeDisposable?) = disposable?.add(this)