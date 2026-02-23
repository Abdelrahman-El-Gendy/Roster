package com.gndy.peoplelog.presentation.input.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gndy.peoplelog.ui.theme.*

@Composable
fun ProductionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Black,
            color = Slate500,
            modifier = Modifier.padding(bottom = 10.dp),
            letterSpacing = 0.5.sp
        )
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Slate400,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            leadingIcon = { 
                Icon(
                    icon, 
                    contentDescription = null, 
                    tint = Slate400,
                    modifier = Modifier.size(24.dp)
                ) 
            },
            singleLine = true,
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Slate300,
                focusedBorderColor = Navy800,
                errorBorderColor = Rose600,
                cursorColor = Navy800,
                focusedTextColor = Slate800,
                unfocusedTextColor = Slate800,
                unfocusedContainerColor = Slate50.copy(alpha = 0.5f),
                focusedContainerColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp
            )
        )
        
        AnimatedVisibility(
            visible = error != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, 
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    Icons.Default.Error, 
                    contentDescription = null, 
                    tint = Rose600, 
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = error ?: "",
                    color = Rose600,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
