package com.bedober.cashprize.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bedober.cashprize.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CashPrizeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PostRepository(AppDatabase.get(application).postDao())
    private val publishers = Platform.values().associateWith { ApiPublisher(it) }
    val posts: StateFlow<List<SocialPost>> = repository.posts().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun save(post: SocialPost) = viewModelScope.launch { repository.save(post) }
    fun delete(id: String) = viewModelScope.launch { repository.delete(id) }
    fun publish(post: SocialPost) = viewModelScope.launch {
        repository.save(post.copy(status = PostStatus.PUBLISHING, error = null))
        publishers.getValue(post.platform).publish(post).fold(
            { repository.save(post.copy(status = PostStatus.PUBLISHED)) },
            { repository.save(post.copy(status = PostStatus.FAILED, error = it.message)) }
        )
    }
}
