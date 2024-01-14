package ru.alexadler9.newsfetcher.utility.ext

import org.mockito.Mockito

/**
 * Implementation of Mockito.any with non-nullable parameter.
 */
fun <T> anyExt(type: Class<T>): T = Mockito.any<T>(type)