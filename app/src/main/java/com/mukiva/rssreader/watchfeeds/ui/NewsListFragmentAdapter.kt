//TODO(Migrate to compose)
//package com.mukiva.rssreader.watchfeeds.ui
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.lifecycle.Lifecycle
//import androidx.recyclerview.widget.DiffUtil
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.mukiva.rssreader.watchfeeds.domain.FeedSummary
//import kotlinx.coroutines.FlowPreview
//
//@FlowPreview
//class NewsListFragmentAdapter(
//    fm: FragmentManager,
//    lifecycle: Lifecycle
//) : FragmentStateAdapter(fm, lifecycle) {
//
//    var feedSummaries: List<FeedSummary> = listOf()
//        set (value) {
//            val diffCallback = FeedFragmentDiffUtilCallback(field, value)
//            val diffResult = DiffUtil.calculateDiff(diffCallback)
//            field = value
//            diffResult.dispatchUpdatesTo(this)
//        }
//
//    private var _scrollMap: MutableMap<Long, Int> = mutableMapOf()
//    override fun getItemCount(): Int = feedSummaries.size
//
//    override fun getItemId(position: Int): Long {
//        return feedSummaries[position].id
//    }
//
//    override fun containsItem(itemId: Long): Boolean {
//        return feedSummaries.firstOrNull { it.id == itemId } != null
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        val feed = feedSummaries[position]
//        val scrollPos = _scrollMap[feed.id]
//        val view = NewsListFragment.newInstance(feed.id, scrollPos ?: 0)
//        view.setPositionChangeListener { pos ->
//            _scrollMap[feed.id] = pos
//        }
//        return view
//    }
//
//    fun getScrollMap(): Map<Long, Int> {
//        return _scrollMap.toMap()
//    }
//
//    fun setScrollMap(map: Map<Long, Int>) {
//        _scrollMap = map.toMutableMap()
//    }
//}