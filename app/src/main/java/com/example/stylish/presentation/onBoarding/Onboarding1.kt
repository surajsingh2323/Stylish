/*  for practice without using navigation
package com.example.stylish.presentation.onBoarding


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stylish.R

@Composable
fun Onboard1 (
    totalSteps: Int, // steps index by default start from 0
    currentStep: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFF1F2937), // Dark gray
    inactiveColor: Color = Color(0xFFE5E7EB), // Light gray
    indicatorWidth: Dp = 24.dp,
    indicatorHeight: Dp = 4.dp,
    spacing: Dp = 8.dp,
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),

        // TopBar with progress and Skip
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(  // we can also use StepCounter in all screens by making reusable class
                    text = buildAnnotatedString { // allows multiple styles in one text
                        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                            append("${currentStep + 1}")
                        }
                        withStyle(style = SpanStyle(color = Color.Gray)) {
                            append("/")
                            append("$totalSteps")
                        }

                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                TextButton(onClick = onSkipClick) {
                    Text(
                        text = "Skip",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        },

        // BottomBar with indicators + Next button
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // gives max space b/w items in a row
                verticalAlignment = Alignment.CenterVertically // vertical centered inside row height(tallest child)
            ) {
                // Indicators
                Row( // Row for boxes or indicator components
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(totalSteps) { index -> // each index draw one indicator box
                        Box( // each indicator is a box
                            modifier = Modifier
                                .width(if (index == currentStep) indicatorWidth else indicatorHeight * 2)//short width for else
                                .height(indicatorHeight)
                                .background(
                                    color = if (index == currentStep) activeColor else inactiveColor,
                                    shape = RoundedCornerShape(indicatorHeight / 2)
                                )
                        )
                    }
                }

                // Next Button
                TextButton(
                    onClick = onNextClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFEF4444) // Red
                    )
                ) {
                    Text(
                        text = "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    ) { innerPadding -> // ensures ui doesn't overlap with system bars,so that bars will be visible
        //  Main Body Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.onboarding_img1),
                    contentDescription = null// because we don,t want to announce description like "its a image" etc
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Choose Products",
                    color = Color(0xFF1A1818),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
} */

package com.example.stylish.presentation.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stylish.Navigation.Routes
import com.example.stylish.R

@Composable
fun Onboard1(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Row with Skip and 1/3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "1/3",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Skip",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.LoginScreen)
                }
            )

        }

        // Spacer to push content to center
        Spacer(modifier = Modifier.weight(1f))

        // Image
        Image(
            painter = painterResource(R.drawable.onboarding_img1),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(height = 350.dp, width = 350.dp)
        )

        // Spacer between image and title
        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "Choose Products",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        // Spacer between title and description
        Spacer(modifier = Modifier.height(24.dp))
        // Description
        Text(
            text = "Discover a world of choices! Our app makes finding\n" +
                    "your perfect products simple and enjoyable.\n" +
                    "Browse effortlessly and pick what you love.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Spacer to push Next button to bottom
        Spacer(modifier = Modifier.weight(1f))

        // Bottom Row with Next button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Next",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.OnboardingScreen2)
                }
            )
        }
    }
}
