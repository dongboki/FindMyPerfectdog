package com.sdc.findmyperfectdog.dictionaryUi


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sdc.findmyperfectdog.dictionaryData.DictionaryDogBreed
import com.sdc.findmyperfectdog.dictionaryData.DogDatabase
import kotlinx.coroutines.launch

@Composable
fun BreedListScreen(
    onItemClick: (DictionaryDogBreed) -> Unit = {}
) {
    val context = LocalContext.current
    val dao = remember { DogDatabase.getDatabase(context).dogBreedDao() }
    var dogList by remember { mutableStateOf(emptyList<DictionaryDogBreed>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            dogList = dao.getAll()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(dogList.size) { index ->
            val dog = dogList[index]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(dog) }
                    .padding(16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(dog.imageUrl),
                    contentDescription = dog.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop

                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = dog.name ?: "이름 없음",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = dog.origin ?: "출신지 없음",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

}