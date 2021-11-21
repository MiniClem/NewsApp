package io.github.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.github.newsapp.model.News

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private lateinit var item: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            item = News(
                title = if (it.containsKey(ARG_ITEM_TITLE))
                    it.getString(ARG_ITEM_TITLE)
                else "",
                urlToImage = if (it.containsKey(ARG_ITEM_URL_IMAGE))
                    it.getString(ARG_ITEM_URL_IMAGE)
                else "",
                description = if (it.containsKey(ARG_ITEM_DESCRIPTION))
                    it.getString(ARG_ITEM_DESCRIPTION)
                else "",
                urlToArticle = if (it.containsKey(ARG_ITEM_URL_ARTICLE))
                    it.getString(ARG_ITEM_URL_ARTICLE)
                else "",
            )

            activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
                ?.title = item.title
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // Show the dummy content as text in a TextView.
        item.let {
            rootView.findViewById<TextView>(R.id.item_detail).text = it.description
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_TITLE = "arg_item_title"
        const val ARG_ITEM_DESCRIPTION = "arg_item_description"
        const val ARG_ITEM_URL_IMAGE = "arg_item_url_image"
        const val ARG_ITEM_URL_ARTICLE = "arg_item_url_article"
    }
}