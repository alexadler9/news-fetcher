package ru.alexadler9.newsfetcher.domain.type

enum class ArticlesCountry(val title: String) {
    RUSSIA("Россия"),
    USA("США");

    override fun toString(): String {
        return title
    }

    companion object {

        fun forTitle(value: String): ArticlesCountry {
            return values().first { it.title == value }
        }
    }
}