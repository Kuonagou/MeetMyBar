package com.example.frontend.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.frontend.R

@Composable
fun SplashScreenBeer(navHostController: NavHostController) {
    val raw = R.raw.biere
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(raw))
    val progress by animateLottieCompositionAsState(composition = composition)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Agrandissement de l'animation avec Modifier.size()
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(200.dp) // Taille personnalisée
        )
        Text("MeetMyBar",fontSize = 30.sp, fontStyle = FontStyle.Italic,fontWeight = FontWeight.Bold)
    }
}