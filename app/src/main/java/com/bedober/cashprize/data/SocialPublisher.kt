package com.bedober.cashprize.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/** Production API contracts. Tokens must be issued by OAuth and stored server-side or encrypted. */
interface YouTubeApi {
    @POST("upload/youtube/v3/videos") suspend fun uploadVideo(@Query("part") part: String = "snippet,status", @Body request: UploadRequest): UploadResponse
    @DELETE("youtube/v3/videos") suspend fun deleteVideo(@Query("id") id: String)
}

interface TikTokApi {
    @POST("v2/post/publish/video/init/") suspend fun initializeVideo(@Body request: UploadRequest): UploadResponse
}

data class UploadRequest(val title: String, val description: String, val mediaUri: String?, val privacy: String = "PUBLIC")
data class UploadResponse(val id: String? = null, val status: String? = null)

interface SocialPublisher {
    val platform: Platform
    suspend fun publish(post: SocialPost): Result<String>
    suspend fun delete(remoteId: String): Result<Unit>
}

class ApiPublisher(override val platform: Platform) : SocialPublisher {
    override suspend fun publish(post: SocialPost): Result<String> = Result.failure(IllegalStateException("Configure ${platform.name} OAuth and API credentials"))
    override suspend fun delete(remoteId: String): Result<Unit> = Result.failure(IllegalStateException("Configure ${platform.name} OAuth and API credentials"))
}
