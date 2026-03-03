package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceRollerTheme {
                DiceRollerApp()
            }
        }
    }
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {

    // 🎲 Dice state
    var result by remember { mutableIntStateOf(1) }
    var rollCount by remember { mutableIntStateOf(0) }
    var scaleTarget by remember { mutableStateOf(1f) }

    // 👥 Multiplayer state
    var player1Score by remember { mutableIntStateOf(0) }
    var player2Score by remember { mutableIntStateOf(0) }
    var currentPlayer by remember { mutableIntStateOf(1) }
    var winnerText by remember { mutableStateOf("") }

    // 🎬 Animation
    val animatedScale by animateFloatAsState(
        targetValue = scaleTarget,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
        label = "diceScale"
    )

    // 🎲 Dice image selector
    val imageResource = when (result) {
        1 -> R.drawable.dice_1_xml
        2 -> R.drawable.dice_2_xml
        3 -> R.drawable.dice_3_xml
        4 -> R.drawable.dice_4_xml
        5 -> R.drawable.dice_5_xml
        else -> R.drawable.dice_6_xml
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🎯 Current Player Turn
        Text(
            text = "🎯 Player $currentPlayer Turn",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🎲 Dice Image
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = result.toString(),
            modifier = Modifier.scale(animatedScale)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🎲 Result Text
        Text(
            text = "You rolled: $result",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔢 Roll Counter
        Text(
            text = "Total Rolls: $rollCount",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 👥 Player Scores
        Text(
            text = "👤 Player 1 Score: $player1Score",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "👤 Player 2 Score: $player2Score",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🎲 Roll Button
        Button(
            onClick = {

                // Generate dice
                result = (1..6).random()
                rollCount++

                // Add score
                if (currentPlayer == 1) {
                    player1Score += result
                    currentPlayer = 2
                } else {
                    player2Score += result
                    currentPlayer = 1
                }

                // 🏆 Winner check (first to 50)
                winnerText = when {
                    player1Score >= 50 -> "🏆 Player 1 Wins!"
                    player2Score >= 50 -> "🏆 Player 2 Wins!"
                    else -> ""
                }

                // 🎬 Trigger animation
                scaleTarget = 1.3f
                scaleTarget = 1f
            }
        ) {
            Text(
                text = "🎲 Roll Dice",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🏆 Winner Display
        if (winnerText.isNotEmpty()) {
            Text(
                text = winnerText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerApp() {
    DiceRollerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFE3F2FD)
        ) {
            DiceWithButtonAndImage(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}