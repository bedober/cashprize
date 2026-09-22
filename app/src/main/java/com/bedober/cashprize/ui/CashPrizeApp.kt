package com.bedober.cashprize.ui

import android.content.Context
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
import com.bedober.cashprize.auth.OAuthManager
import com.bedober.cashprize.data.*

private fun label(p: Platform) = p.name.lowercase().replaceFirstChar { it.uppercase() }.replace("X", "X")

@Composable
fun CashPrizeApp(vm: CashPrizeViewModel) {
    var composer by remember { mutableStateOf(false) }
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(topBar = { TopAppBar(title = { Text("CashPrize", style = MaterialTheme.typography.headlineSmall) }) }, floatingActionButton = { FloatingActionButton({ composer = true }) { Icon(Icons.Default.Add, "Create post") } }) { pad ->
        Column(Modifier.padding(pad)) {
            TabRow(tab) { listOf("Content", "Accounts").forEachIndexed { i, t -> Tab(tab == i, { tab = i }, text = { Text(t) }) } }
            if (tab == 0) ContentList(vm) else Accounts()
        }
    }
    if (composer) Composer(vm) { composer = false }
}

@Composable private fun ContentList(vm: CashPrizeViewModel) {
    val posts by vm.posts.collectAsState()
    if (posts.isEmpty()) Box(Modifier.fillMaxSize().padding(32.dp)) { Text("Your content calendar is empty. Create your first post.") }
    else LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { items(posts, key = { it.id }) { post ->
        ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(label(post.platform), style = MaterialTheme.typography.titleMedium); AssistChip({}, label = { Text(post.status.name) }) }
            if (post.title.isNotBlank()) Text(post.title, style = MaterialTheme.typography.titleSmall)
            Text(post.caption.ifBlank { "No caption" }, maxLines = 3)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { if (post.status == PostStatus.DRAFT || post.status == PostStatus.FAILED) Button({ vm.publish(post) }) { Icon(Icons.Default.PlayArrow, null); Text(" Publish") }; IconButton({ vm.delete(post.id) }) { Icon(Icons.Default.Delete, "Delete") } }
        } }
    } }
}

@Composable private fun Composer(vm: CashPrizeViewModel, close: () -> Unit) {
    var platform by remember { mutableStateOf(Platform.INSTAGRAM) }; var caption by remember { mutableStateOf("") }; var title by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = close, title = { Text("New content") }, text = { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Select channels")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { Platform.values().forEach { p -> FilterChip(platform == p, { platform = p }, label = { Text(label(p)) }) } }
        OutlinedTextField(title, { title = it }, label = { Text("Title") }, singleLine = true)
        OutlinedTextField(caption, { caption = it }, label = { Text("Caption") }, minLines = 4)
    } }, confirmButton = { Button(enabled = caption.isNotBlank(), onClick = { vm.save(SocialPost(platform = platform, title = title, caption = caption)); close() }) { Text("Save draft") } }, dismissButton = { TextButton(close) { Text("Cancel") } })
}

@Composable private fun Accounts() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val oauth = remember { OAuthManager(context) }
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("Connected accounts", style = MaterialTheme.typography.headlineSmall); Text("Authorize each channel to publish and manage content.")
        Platform.values().forEach { p -> OutlinedButton(onClick = { oauth.connect(p) }, modifier = Modifier.fillMaxWidth()) { Text("Connect ${label(p)}") } }
    }
}
