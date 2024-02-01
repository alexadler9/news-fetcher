package ru.alexadler9.newsfetcher.base.ext

import android.text.Editable

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)