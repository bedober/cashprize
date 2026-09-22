package com.bedober.cashprize.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

enum class Platform { TIKTOK, YOUTUBE }
enum class PostStatus { DRAFT, PUBLISHING, PUBLISHED, FAILED, DELETED }

data class SocialPost(
    val id: String = UUID.randomUUID().toString(),
    val platform: Platform,
    val title: String = "",
    val caption: String = "",
    val mediaUri: String? = null,
    val tags: String = "",
    val status: PostStatus = PostStatus.DRAFT,
    val error: String? = null
)

interface PostRepository {
    fun posts(): Flow<List<SocialPost>>
    suspend fun save(post: SocialPost)
    suspend fun delete(id: String)
}

class InMemoryPostRepository : PostRepository {
    private val state = MutableStateFlow<List<SocialPost>>(emptyList())
    override fun posts(): Flow<List<SocialPost>> = state
    override suspend fun save(post: SocialPost) { state.value = listOf(post) + state.value.filterNot { it.id == post.id } }
    override suspend fun delete(id: String) { state.value = state.value.map { if (it.id == id) it.copy(status = PostStatus.DELETED) else it } }
}
