package com.morishjs.englishbuddy.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActionRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        ActionButton(text = "Write")
        ActionButton(text = "🎤")
        ActionButton(text = "Scan")
    }
}

@Composable
fun ActionButton(text: String) {
    Button(onClick = { /* Handle button click here */ }) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionRow() {
    ActionRow()
}