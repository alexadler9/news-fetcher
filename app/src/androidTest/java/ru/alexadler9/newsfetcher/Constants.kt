package ru.alexadler9.newsfetcher

import ru.alexadler9.newsfetcher.data.news.remote.model.ArticleRemoteModel

val ARTICLE_FIRST = ArticleRemoteModel(
    url = "https://www.tampabay.com/life-culture/2024/01/23/watch-fallon-colbert-kimmel-opening-desantis-drop-out/",
    title = "Ron DeSantis jokes fill Fallon, Colbert and Kimmel opening monologues - Tampa Bay Times",
    author = "Christopher Spata",
    description = "Fallon and Colbert ran with the news, but we never got a regular DeSantis character on SNL.",
    urlToImage = "https://www.tampabay.com/resizer/LJ_F1nb6XodgoXYY_np7LuOP0dw=/1200x675/smart/cloudfront-us-east-1.images.arcpublishing.com/tbt/7XW5VC47CRABBGTXJZDGYGVQBY.jpg",
    publishedAt = "2024-01-23T15:53:29Z"
)

val ARTICLE_QUERY = ArticleRemoteModel(
    url = "https://9to5mac.com/2024/01/23/apple-vision-pro-ebay-scalper-prices/",
    title = "Apple Vision Pro resellers already price gouging on eBay, here's how much they're asking - 9to5Mac",
    author = "Michael Potuck",
    description = "Vision Pro is landing at the end of next week and with pre-orders locked in for the first customers, eBay...",
    urlToImage = "https://i0.wp.com/9to5mac.com/wp-content/uploads/sites/6/2024/01/apple-vision-pro.jpg?resize=1200%2C628&quality=82&strip=all&ssl=1",
    publishedAt = "2024-01-23T14:45:00Z"
)