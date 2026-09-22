package com.bedober.cashprize.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

enum class Platform { INSTAGRAM, TIKTOK, YOUTUBE, FACEBOOK, X, LINKEDIN }
enum class PostStatus { DRAFT, PUBLISHING, PUBLISHED, FAILED, DELETED }

data class SocialPost(
    val id: String = UUID.randomUUID().toString(), val platform: Platform,
    val title: String = "", val caption: String = "", val mediaUri: String? = null,
    val tags: String = "", val status: PostStatus = PostStatus.DRAFT,
    val error: String? = null, val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String, val platform: String, val title: String,
    val caption: String, val mediaUri: String?, val tags: String,
    val status: String, val error: String?, val createdAt: Long
)

private fun SocialPost.toEntity() = PostEntity(id, platform.name, title, caption, mediaUri, tags, status.name, error, createdAt)
private fun PostEntity.toModel() = SocialPost(id, Platform.valueOf(platform), title, caption, mediaUri, tags, PostStatus.valueOf(status), error, createdAt)

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<PostEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(post: PostEntity)
    @Query("UPDATE posts SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)
}

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    companion object {
        @Volatile private var instance: AppDatabase? = null
        fun get(context: android.content.Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: androidx.room.Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "cashprize.db").build().also { instance = it }
        }
    }
}

class PostRepository(private val dao: PostDao) {
    fun posts(): Flow<List<SocialPost>> = dao.observeAll().map { rows -> rows.map { it.toModel() } }
    suspend fun save(post: SocialPost) = dao.upsert(post.toEntity())
    suspend fun delete(id: String) = dao.updateStatus(id, PostStatus.DELETED.name)
}
