package com.bedober.cashprize.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedober.cashprize.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CashPrizeViewModel : ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()
    private val publishers = mapOf(Platform.TIKTOK to TikTokPublisher(), Platform.YOUTUBE to YouTubePublisher())
    val posts: StateFlow<List<SocialPost>> = repository.posts().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(post: SocialPost) = viewModelScope.launch { repository.save(post) }
    fun delete(id: String) = viewModelScope.launch { repository.delete(id) }
    fun publish(post: SocialPost) = viewModelScope.launch {
        repository.save(post.copy(status = PostStatus.PUBLISHING, error = null))
        publishers.getValue(post.platform).publish(post).fold(
            onSuccess = { repository.save(post.copy(status = PostStatus.PUBLISHED)) },
            onFailure = { repository.save(post.copy(status = PostStatus.FAILED, error = it.message)) }
        )
    }
}
