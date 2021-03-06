package com.example.wallet.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.viewmodel.transactions.CategoryStatisticsViewModel
import com.example.wallet.model.viewmodel.transactions.CategoryStatisticsViewModelFactory
import java.util.*

private const val DividerLengthInDegrees = 1.8f

/**
 * A donut chart that animates when loaded.
 */

@Composable
fun CategoryStatisticsScreen(
    navController: NavHostController,
    categoryId: Int,
    categoryName: String,
    categoryIcon: String,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {

    val viewModel: CategoryStatisticsViewModel = viewModel(
        factory = CategoryStatisticsViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    )
    viewModel.setCategoryId(categoryId)
    val expenseAmount = viewModel.expenseAmount.value
    val incomeAmount = viewModel.incomeAmount.value
    var expenseItems = viewModel.expanseState.value
    var incomeItems = viewModel.incomeState.value
    var dataLoaded = viewModel.dataLoaded.value
    val listOfButtons = listOf<String>("Expense", "Income")

    while (!dataLoaded) {
        return
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 15.dp), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Category: $categoryIcon $categoryName", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.White
                )
            )
        }
        LazyRow(modifier = Modifier.padding(start = 60.dp)) {
            items(listOfButtons) { element ->
                OutlinedButton(onClick = {
                    viewModel.typeOfTransactionsToDisplay.value =
                        element.lowercase(Locale.getDefault())
                }, Modifier.padding(20.dp)) {
                    Text(text = element)
                }
            }
        }

        var transactionsToShow =
            if (viewModel.typeOfTransactionsToDisplay.value.equals("expense")) expenseItems else incomeItems
        var totalAmount =
            if (viewModel.typeOfTransactionsToDisplay.value.equals("expense")) expenseAmount else incomeAmount
        PieChartBase(
            transactions = transactionsToShow,
            colors = { item -> item.color },
            amounts = { item -> item.transaction.amount.toFloat() },
            totalAmount = totalAmount,
            list = {
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    items(transactionsToShow) { item ->
                        val itemId = item.transaction.id
                        TransactionStatisticsRow(
                            color = item.color,
                            categoryIcon = item.transaction.categoryIcon,
                            categoryName = item.transaction.categoryName,
                            walletName = item.transaction.walletName,
                            date = item.transaction.date,
                            location = item.transaction.location,
                            amount = item.transaction.amount,
                            comments = item.transaction.comments,
                            type = item.transaction.type,
                            editClickAction = { navController.navigate("transactionDetails/$itemId") }) {

                        }
                    }
                }
            })
    }
}

@Composable
fun TransactionStatisticsRow(
    color: Color,
    categoryIcon: String,
    categoryName: String,
    walletName: String,
    date: String,
    location: String?,
    amount: Int,
    comments: String?,
    type: String,
    editClickAction: () -> Unit,
    deleteClickAction: () -> Unit
) {
    Row {
        Spacer(
            Modifier
                .size(10.dp, 20.dp)
                .background(color = color)
        )
        ReusableRow(
            categoryIcon = categoryIcon,
            categoryName = categoryName,
            walletName = walletName,
            date = date,
            location = location,
            amount = amount,
            comments = comments,
            type = type,
            editClickAction = editClickAction,
            deleteClickAction = deleteClickAction
        )
    }

}

@Composable
fun AnimatedCircle(
    proportions: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val currentState = remember {
        MutableTransitionState(AnimatedCircleProgress.START)
            .apply { targetState = AnimatedCircleProgress.END }
    }
    val stroke = with(LocalDensity.current) { Stroke(12.dp.toPx()) }
    val transition = updateTransition(currentState)
    val angleOffset by transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = LinearOutSlowInEasing
            )
        }
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            360f
        }
    }
    val shift by transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = CubicBezierEasing(0f, 0.75f, 0.35f, 0.85f)
            )
        }
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            30f
        }
    }

    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = shift - 90f
        proportions.forEachIndexed { index, proportion ->
            val sweep = proportion * angleOffset
            drawArc(
                color = colors[index],
                startAngle = startAngle + DividerLengthInDegrees / 2,
                sweepAngle = sweep - DividerLengthInDegrees,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke
            )
            startAngle += sweep
        }
    }
}

fun <E> List<E>.extractProportions(selector: (E) -> Float): List<Float> {
    val total = this.sumOf { selector(it).toDouble() }
    return this.map { (selector(it) / total).toFloat() }
}


@Composable
fun <T> PieChartBase(
    transactions: List<T>,
    colors: (T) -> Color,
    amounts: (T) -> Float,
    totalAmount: Int,
    list: @Composable () -> Unit
) {
    Column {
        Box(Modifier.padding(16.dp)) {
            val accountsProportion = transactions.extractProportions { amounts(it) }
            val circleColors = transactions.map { colors(it) }
            AnimatedCircle(
                accountsProportion,
                circleColors,
                Modifier
                    .height(300.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = totalAmount.toString(),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        list()
    }

}


enum class AnimatedCircleProgress { START, END }


