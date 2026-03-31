

/* package com.example.stylish.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf  // we have imported one getter and one setter for it
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSavable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.HistoricalChange
import androidx.compose.ui.tooling.preview.Preview

/************************************STATE HOISTING**************************************/
@Composable
fun DisplayCounter(count:Int,onCountChange:()->Unit) { // Child function
   // var count = 0 , stateless
    // saveable store state temporary,fails after multiple rotation
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){
        Text("$count")
        Button(onClick = {onCountChange()}) { // count+1,will only math but doesn't update count.Instead use count = count+1
            Text("Increase")
        }
    }

}

@Composable
fun CounterApp(modifier: Modifier){ // Parent function
    var count by remember { mutableStateOf(0) }
    DisplayCounter(count) { count++ } // { } is onCountChange function

}

@Composable
@Preview(showBackground = true)
fun CounterPrev() {

}  */
/*************************************SPLASH SCREEN*****************************************/

package com.example.stylish.Presentation.Splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.stylish.R

@Composable
fun SplashScreen(
    onFinish: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 3000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.White,
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.brand_name_icon),
                contentDescription = "EcoMart Logo",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "EcoMart",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(alphaAnim.value)
            )
        }
    }
}