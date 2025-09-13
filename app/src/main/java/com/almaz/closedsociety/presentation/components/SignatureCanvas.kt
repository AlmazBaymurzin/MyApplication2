package com.almaz.closedsociety.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SignatureCanvas(
    onSignatureDrawn: (ByteArray) -> Unit,
    modifier: Modifier = Modifier
) {
    var path by remember { mutableStateOf(Path()) }
    var lastOffset by remember { mutableStateOf(Offset.Zero) }
    var isEmpty by remember { mutableStateOf(true) }

    Column(modifier = modifier) {
        // Инструкция для пользователя
        Text(
            text = if (isEmpty) "Нарисуйте вашу подпись" else "✓ Подпись нарисована",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isEmpty) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Холст для рисования
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                path.moveTo(offset.x, offset.y)
                                lastOffset = offset
                                isEmpty = false
                            },
                            onDrag = { change, dragAmount ->
                                val newOffset = lastOffset + dragAmount
                                path.lineTo(newOffset.x, newOffset.y)
                                lastOffset = newOffset
                            },
                            onDragEnd = {
                                // Конвертируем путь в байтовый массив
                                val signatureData = path.toString().toByteArray()
                                onSignatureDrawn(signatureData)
                            }
                        )
                    }
            ) {
                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            // Водяной знак (только если подпись не нарисована)
            if (isEmpty) {
                Text(
                    text = "Перетащите палец для создания подписи",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }

        // Кнопка очистки
        if (!isEmpty) {
            Button(
                onClick = {
                    path = Path()
                    isEmpty = true
                    onSignatureDrawn(ByteArray(0)) // Пустые данные при очистке
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            ) {
                Text("Очистить подпись")
            }
        }
    }
}

// Вспомогательная функция для сложения Offset
operator fun Offset.plus(dragAmount: Offset): Offset {
    return Offset(this.x + dragAmount.x, this.y + dragAmount.y)
}