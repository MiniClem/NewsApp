package io.github.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.newsapp.model.News
import io.github.newsapp.utils.DataState


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@AndroidEntryPoint
class ItemListActivity : AppCompatActivity() {

    private lateinit var newsViewModel: NewsViewModel

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var adapter: SimpleItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(findViewById(R.id.item_list))
        setupObservers()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SimpleItemRecyclerViewAdapter(this, twoPane)
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        newsViewModel.getNews().observe(this, { datastate ->
            when (datastate) {
                is DataState.Loading -> println("Loading...")
                is DataState.Success -> adapter.setDataSet(datastate.data)
                is DataState.Error -> datastate.throwable.printStackTrace()
            }
        })
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val twoPane: Boolean
    ) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val values = ArrayList<News>()
        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val item = v.tag as News
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_TITLE, item.title)
                        putString(ItemDetailFragment.ARG_ITEM_DESCRIPTION, item.description)
                        putString(ItemDetailFragment.ARG_ITEM_URL_ARTICLE, item.urlToArticle)
                        putString(ItemDetailFragment.ARG_ITEM_URL_IMAGE, item.urlToImage)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_TITLE, item.title)
                    putExtra(ItemDetailFragment.ARG_ITEM_DESCRIPTION, item.description)
                    putExtra(ItemDetailFragment.ARG_ITEM_URL_ARTICLE, item.urlToArticle)
                    putExtra(ItemDetailFragment.ARG_ITEM_URL_IMAGE, item.urlToImage)
                }
                v.context.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            Glide.with(parentActivity)
                .load(item.urlToImage)
                .centerCrop()
                .into(holder.articleImageView);
            holder.titleView.text = item.title

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        fun setDataSet(data: List<News>) {
            this.values.clear()
            this.values.addAll(data)
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val articleImageView: ImageView = view.findViewById(R.id.article_image)
            val titleView: TextView = view.findViewById(R.id.title)
        }
    }
}