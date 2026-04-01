package com.example.quicknotes.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.example.quicknotes.domain.model.ImageAttachment
import com.example.quicknotes.domain.model.VoiceRecording
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quicknotes.domain.model.ChecklistItem
import com.example.quicknotes.domain.model.Note
import com.example.quicknotes.ui.theme.DarkColors
import com.example.quicknotes.ui.theme.LightColors
import com.example.quicknotes.viewmodel.NoteViewModel
import java.util.*

@Composable
fun NoteEditorScreen(
    note: Note?,
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var isChecklist by remember { mutableStateOf(note?.isChecklist ?: false) }
    var checkItems by remember { mutableStateOf(note?.checkItems ?: emptyList<ChecklistItem>()) }
    var colorIdx by remember { mutableIntStateOf(note?.colorIdx ?: 0) }
    var pinned by remember { mutableStateOf(note?.pinned ?: false) }
    var recordings by remember { mutableStateOf(note?.recordings ?: emptyList()) }
    var images by remember { mutableStateOf(note?.images ?: emptyList()) }
    var showColorPicker by remember { mutableStateOf(false) }

    val dark = isSystemInDarkTheme()
    val colors = if (dark) DarkColors else LightColors
    val bgColor = colors.getOrElse(colorIdx) { colors[0] }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri ->
            images = images + ImageAttachment(
                id = UUID.randomUUID().toString(),
                data = uri.toString(),
                name = "Image"
            )
        }
    }

    val onSave = {
        if (title.isNotEmpty() || content.isNotEmpty() || checkItems.isNotEmpty() || recordings.isNotEmpty() || images.isNotEmpty()) {
            val newNote = Note(
                id = note?.id ?: UUID.randomUUID().toString(),
                title = title,
                content = content,
                isChecklist = isChecklist,
                checkItems = checkItems,
                colorIdx = colorIdx,
                pinned = pinned,
                recordings = recordings,
                images = images,
                createdAt = note?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            viewModel.insertNote(newNote)
        } else if (note != null) {
            viewModel.deleteNote(note)
        }
        onBack()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSave) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { pinned = !pinned }) {
                    Icon(
                        imageVector = if (pinned) Icons.Default.PushPin else Icons.Default.PushPin,
                        contentDescription = "Pin",
                        tint = if (pinned) Color(0xFFFBBC04) else LocalContentColor.current
                    )
                }
                IconButton(onClick = { isChecklist = !isChecklist }) {
                    Icon(
                        imageVector = if (isChecklist) Icons.Default.FormatListBulleted else Icons.Default.CheckBox,
                        contentDescription = "Toggle Checklist"
                    )
                }
                IconButton(onClick = { imageLauncher.launch("image/*") }) {
                    Icon(Icons.Default.Image, contentDescription = "Add Image")
                }
                IconButton(onClick = {
                    // Simulating a voice recording for now
                    recordings = recordings + VoiceRecording(
                        id = UUID.randomUUID().toString(),
                        url = "voice_recording_${System.currentTimeMillis()}",
                        duration = 0,
                        createdAt = System.currentTimeMillis()
                    )
                }) {
                    Icon(Icons.Default.Mic, contentDescription = "Record Voice")
                }
                IconButton(onClick = { showColorPicker = !showColorPicker }) {
                    Icon(Icons.Default.Palette, contentDescription = "Color")
                }
                if (note != null) {
                    IconButton(onClick = {
                        viewModel.deleteNote(note)
                        onBack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        },
        containerColor = bgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (images.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(images, key = { it.id }) { image ->
                        Box(modifier = Modifier.size(100.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(image.data),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            IconButton(
                                onClick = { images = images.filter { it.id != image.id } },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }

            if (recordings.isNotEmpty()) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    recordings.forEach { recording ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color.Black.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Voice memo", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { recordings = recordings.filter { it.id != recording.id } }) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (dark) Color.White else Color.Black
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 22.sp,
                            color = if (dark) Color.Gray else Color.LightGray
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isChecklist) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(checkItems) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = item.checked,
                                onCheckedChange = { checked ->
                                    checkItems = checkItems.map {
                                        if (it.id == item.id) it.copy(checked = checked) else it
                                    }
                                }
                            )
                            BasicTextField(
                                value = item.text,
                                onValueChange = { text ->
                                    checkItems = checkItems.map {
                                        if (it.id == item.id) it.copy(text = text) else it
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = if (dark) Color.White else Color.Black,
                                    textDecoration = if (item.checked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                )
                            )
                            IconButton(onClick = {
                                checkItems = checkItems.filter { it.id != item.id }
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Remove")
                            }
                        }
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    checkItems = checkItems + ChecklistItem(
                                        id = UUID.randomUUID().toString(),
                                        text = "",
                                        checked = false
                                    )
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "List item", color = if (dark) Color.Gray else Color.Gray)
                        }
                    }
                }
            } else {
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = if (dark) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    decorationBox = { innerTextField ->
                        if (content.isEmpty()) {
                            Text(
                                text = "Note",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (dark) Color.Gray else Color.LightGray
                            )
                        }
                        innerTextField()
                    }
                )
            }

            if (showColorPicker) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.forEachIndexed { index, color ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color, shape = CircleShape)
                                .clickable { colorIdx = index }
                                .let {
                                    if (colorIdx == index) {
                                        it.background(Color.Black.copy(alpha = 0.2f), shape = CircleShape)
                                    } else it
                                }
                        )
                    }
                }
            }
        }
    }
}
