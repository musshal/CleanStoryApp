package com.dicoding.storyapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.core.ui.StoriesBookmarkAdapter
import com.dicoding.storyapp.databinding.FragmentBookmarkBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarkFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var storiesBookmarkAdapter: StoriesBookmarkAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupData()
        setData()
    }

    private fun setupAdapter() {
        storiesBookmarkAdapter = StoriesBookmarkAdapter { story ->
            if (story.isBookmarked) {
                viewModel.deleteStory(story)
            } else {
                viewModel.saveStory(story)
            }
        }
    }

    private fun setupData() {
        viewModel.getBookmarkedStories().observe(viewLifecycleOwner) { bookmarkedStory ->
            if (bookmarkedStory.isEmpty()) {
                binding.tvMessage.visibility = View.VISIBLE
                storiesBookmarkAdapter.submitList(bookmarkedStory)
            } else {
                binding.tvMessage.visibility = View.GONE
                storiesBookmarkAdapter.submitList(bookmarkedStory)
            }
        }
    }

    private fun setData() {
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storiesBookmarkAdapter
        }
    }

    fun scrollToTop() {
        val recyclerView = binding.rvStories
        recyclerView.smoothScrollToPosition(0)
    }
}