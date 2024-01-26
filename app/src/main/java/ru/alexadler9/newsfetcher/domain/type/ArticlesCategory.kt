package ru.alexadler9.newsfetcher.domain.type

enum class ArticlesCategory(val title: String) {
    GENERAL("Все новости"),
    BUSINESS("Бизнес"),
    ENTERTAINMENT("Развлечения"),
    HEALTH("Здоровье"),
    SCIENCE("Наука"),
    SPORTS("Спорт"),
    TECHNOLOGY("Технологии");

    override fun toString(): String {
        return title
    }

    companion object {

        fun forTitle(value: String): ArticlesCategory {
            return values().first { it.title == value }
        }
    }
}