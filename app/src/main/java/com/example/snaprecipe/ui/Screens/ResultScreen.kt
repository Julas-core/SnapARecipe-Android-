package com.example.snaprecipe.ui.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snaprecipe.R

@Composable
fun ResultScreen(
	resultText: String,
	modifier: Modifier = Modifier,
	onTakePhoto: () -> Unit = {},
	onUploadImage: () -> Unit = {}
) {
	val background = Brush.verticalGradient(
		colors = listOf(
			Color(0xFF081939),
			Color(0xFF0A1731),
			Color(0xFF060E21)
		)
	)
	val brandColor = Color(0xFFF7A61A)
	val cardColor = Color(0xFF162643)
	val mutedText = Color(0xFFF1D99F)
	val languages = listOf("English", "Swahili", "French", "Spanish")

	var selectedLanguage by remember { mutableStateOf(languages.first()) }
	var expanded by remember { mutableStateOf(false) }

	Box(
		modifier = modifier
			.fillMaxSize()
			.background(background)
			.statusBarsPadding()
			.navigationBarsPadding()
			.padding(horizontal = 20.dp, vertical = 12.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Surface(
						shape = RoundedCornerShape(10.dp),
						color = Color(0xFF2B1D0A)
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_launcher_foreground),
							contentDescription = "Snap a Recipe logo",
							tint = brandColor,
							modifier = Modifier
								.size(40.dp)
								.padding(5.dp)
						)
					}
					Spacer(modifier = Modifier.size(10.dp))
					Text(
						text = "SNAP A RECIPE",
						color = brandColor,
						fontWeight = FontWeight.Bold,
						fontSize = 14.sp
					)
				}

				Text(
					text = "Sign Out",
					color = mutedText,
					fontSize = 14.sp,
					fontWeight = FontWeight.Medium
				)
			}

			Spacer(modifier = Modifier.height(74.dp))

			Text(
				text = "Snap-a-Recipe",
				color = brandColor,
				style = MaterialTheme.typography.headlineLarge,
				fontWeight = FontWeight.Bold
			)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = "Turn your food photos into delicious recipes!",
				color = mutedText,
				textAlign = TextAlign.Center,
				style = MaterialTheme.typography.titleMedium
			)

			Spacer(modifier = Modifier.height(28.dp))

			Surface(
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(20.dp),
				color = cardColor
			) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(20.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = "Get Started",
						color = Color(0xFFFFF4D4),
						style = MaterialTheme.typography.headlineSmall,
						fontWeight = FontWeight.SemiBold
					)

					Spacer(modifier = Modifier.height(10.dp))

					Text(
						text = "Choose how to provide an image of your meal:",
						color = mutedText,
						style = MaterialTheme.typography.bodyLarge,
						textAlign = TextAlign.Center
					)

					Spacer(modifier = Modifier.height(20.dp))

					Text(
						text = "Recipe Language",
						color = mutedText,
						style = MaterialTheme.typography.bodyMedium
					)

					Spacer(modifier = Modifier.height(8.dp))

					Box(modifier = Modifier.fillMaxWidth()) {
						Button(
							onClick = { expanded = true },
							modifier = Modifier.fillMaxWidth(),
							colors = ButtonDefaults.buttonColors(
								containerColor = Color(0xFF3A4761),
								contentColor = Color.White
							),
							shape = RoundedCornerShape(10.dp)
						) {
							Row(
								modifier = Modifier.fillMaxWidth(),
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Text(text = selectedLanguage)
								Text(text = "v")
							}
						}

						DropdownMenu(
							expanded = expanded,
							onDismissRequest = { expanded = false },
							modifier = Modifier.fillMaxWidth(0.9f)
						) {
							languages.forEach { language ->
								DropdownMenuItem(
									text = { Text(language) },
									onClick = {
										selectedLanguage = language
										expanded = false
									}
								)
							}
						}
					}

					Spacer(modifier = Modifier.height(16.dp))

					Button(
						onClick = onTakePhoto,
						modifier = Modifier.fillMaxWidth(),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFFF4A609),
							contentColor = Color(0xFF1D1200)
						),
						shape = RoundedCornerShape(10.dp)
					) {
						Text(
							text = "Take a Photo",
							fontWeight = FontWeight.SemiBold,
							fontSize = 20.sp
						)
					}

					Spacer(modifier = Modifier.height(10.dp))

					Button(
						onClick = onUploadImage,
						modifier = Modifier.fillMaxWidth(),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFF9E4B13),
							contentColor = Color(0xFFFFF5E8)
						),
						shape = RoundedCornerShape(10.dp)
					) {
						Text(
							text = "Upload Image",
							fontWeight = FontWeight.SemiBold,
							fontSize = 20.sp
						)
					}

					if (resultText.isNotBlank() && resultText != "Idle") {
						Spacer(modifier = Modifier.height(14.dp))
						Text(
							text = resultText,
							color = Color(0xFFE7ECF9),
							style = MaterialTheme.typography.bodyMedium,
							textAlign = TextAlign.Center
						)
					}
				}
			}

			Spacer(modifier = Modifier.height(18.dp))
		}
	}
}
