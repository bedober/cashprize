package com.bedober.cashprize.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bedober.cashprize.data.*

@Composable
fun CashPrizeApp(vm: CashPrizeViewModel) {
    var showComposer by remember { mutableStateOf(false) }
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(topBar = { TopAppBar(title = { Text("CashPrize Manager") }) }, floatingActionButton = {
        FloatingActionButton(onClick = { showComposer = true }) { Icon(Icons.Default.Add, "Create post") }
    }) { padding ->
        Column(Modifier.padding(padding)) {
            TabRow(selectedTabIndex = tab) {
                listOf("Posts", "Accounts").forEachIndexed { index, label -> Tab(selected = tab == index, onClick = { tab = index }, text = { Text(label) }) }
            }
            if (tab == 0) PostList(vm) else AccountsScreen()
        }
    }
    if (showComposer) ComposerDialog(vm, onDismiss = { showComposer = false })
}

@Composable
private fun PostList(vm: CashPrizeViewModel) {
    val posts by vm.posts.collectAsState()
    if (posts.isEmpty()) Box(Modifier.fillMaxSize().padding(32.dp)) { Text("No posts yet. Tap + to create one.") }
    else LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(posts, key = { it.id }) { post ->
            Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(post.platform.name, style = MaterialTheme.typography.titleMedium); AssistChip(onClick = {}, label = { Text(post.status.name) }) }
                if (post.title.isNotBlank()) Text(post.title, style = MaterialTheme.typography.bodyLarge)
                Text(post.caption.ifBlank { "No caption" }, maxLines = 2)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (post.status == PostStatus.DRAFT || post.status == PostStatus.FAILED) Button(onClick = { vm.publish(post) }) { Icon(Icons.Default.PlayArrow, null); Text(" Publish") }
                    IconButton(onClick = { vm.delete(post.id) }) { Icon(Icons.Default.Delete, "Delete") }
                }
            } }
        }
    }
}

@Composable
private fun ComposerDialog(vm: CashPrizeViewModel, onDismiss: () -> Unit) {
    var platform by remember { mutableStateOf(Platform.TIKTOK) }
    var title by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var media by remember { mutableStateOf<Uri?>(null) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { media = it }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Create post") }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Platform.values().forEach { p -> FilterChip(selected = platform == p, onClick = { platform = p }, label = { Text(p.name) }) } }
            if (platform == Platform.YOUTUBE) OutlinedTextField(title, { title = it }, label = { Text("Video title") }, singleLine = true)
            OutlinedTextField(caption, { caption = it }, label = { Text("Caption / description") }, minLines = 3)
            OutlinedTextField(tags, { tags = it }, label = { Text("Tags (optional)") }, singleLine = true)
            OutlinedButton(onClick = { picker.launch("video/*") }) { Text(media?.lastPathSegment ?: "Choose video") }
        }
    }, confirmButton = { Button(enabled = caption.isNotBlank() && media != null, onClick = { vm.save(SocialPost(platform = platform, title = title, caption = caption, tags = tags, mediaUri = media.toString())); onDismiss() }) { Text("Save draft") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}

@Composable
private fun AccountsScreen() { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { Text("Connected accounts", style = MaterialTheme.typography.headlineSmall); Text("Connect accounts before publishing."); OutlinedButton(onClick = {}) { Text("Connect TikTok") }; OutlinedButton(onClick = {}) { Text("Connect YouTube") } } }
