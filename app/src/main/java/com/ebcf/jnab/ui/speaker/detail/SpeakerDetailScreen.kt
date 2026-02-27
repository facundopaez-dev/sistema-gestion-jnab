package com.ebcf.jnab.ui.speaker.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ebcf.jnab.domain.model.SpeakerModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerDetailScreen(
    viewModel: SpeakerDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTalkHistory: (Int) -> Unit
) {
    val speaker by viewModel.speaker.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Expositor") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            speaker?.let {
                SpeakerInfoCard(it)
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { onNavigateToTalkHistory(it.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Historial de Charlas")
                }
            } ?: run {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SpeakerInfoCard(speaker: SpeakerModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${speaker.firstName} ${speaker.lastName}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(
                icon = Icons.Default.Business,
                text = speaker.institution
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(
                icon = Icons.Default.Email,
                text = speaker.email
            )
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}