package ru.alexadler9.newsfetcher.feature

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.detailsscreen.ui.DetailsFragment

fun articleDetailsShow(fragmentManager: FragmentManager, article: ArticleModel) {
    fragmentManager.commit {
        // TODO: делегировать MainActivity?
        add(R.id.fragmentContainerView, DetailsFragment.newInstance(article))
        addToBackStack(null)
    }
}

fun articleLinkSendViaMessenger(context: Context, article: ArticleModel) {
    Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.article_link_message, article.title, article.url)
        )
        putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(R.string.description_label)
        )
    }.also { intent ->
        val chooserIntent = Intent.createChooser(
            intent,
            context.getString(R.string.article_link_messenger_title)
        )
        context.startActivity(chooserIntent)
    }
}