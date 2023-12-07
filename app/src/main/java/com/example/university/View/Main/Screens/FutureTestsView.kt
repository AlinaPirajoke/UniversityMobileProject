package com.example.university.View.Main.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.university.View.Main.MainScreens
import com.example.university.ViewModel.FutureTestsViewModel
import com.example.university.theme.KotobaCustomTheme
import com.example.university.theme.PHColors
import com.example.university.theme.UXConstants
import org.koin.androidx.compose.koinViewModel

private const val TAG = "FutureTestsView"

@Composable
fun FutureTestsScreen(
    navController: NavHostController,
    vm: FutureTestsViewModel = koinViewModel()
){
    val uiState by vm.uiState.collectAsState()
    FutureTestsView(dateQuantityList = uiState.dateQuantity, onPick = {date, quantity ->
        if (quantity > 0) {
            Log.i(TAG, "Перенаправление на экран выбора количества слов для тестирования")
            navController.navigate("${MainScreens.PickQuantity.route}/${date}")
        }
    })
}

@Composable
fun FutureTestsView(
    dateQuantityList: List<Pair<String, Int>>,
    onPick: (String, Int) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(
                start = UXConstants.HORIZONTAL_PADDING,
                top = UXConstants.VERTICAL_PADDING,
                end = UXConstants.HORIZONTAL_PADDING,
            )
    ) {
        Text(
            text = "День и количество слов, которые нужно будет повторить",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.secondaryVariant
        )
        CreateGrid(
            dateQuantityList,
            onPick
        )
    }
}

@Composable
fun CreateGrid(dateQuantity: List<Pair<String, Int>>, onClick: (String, Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        Modifier.padding(top = UXConstants.VERTICAL_PADDING),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(0.dp, 15.dp, 0.dp, 15.dp)
    ) {
        dateQuantity.forEachIndexed { index, data ->
            if (index == 0) {
                val text = data.first + " (сегодня)"
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CreateListCard(
                        dateQuantity = data.copy(first = text),
                        onClick = onClick
                    )
                }
            } else item(span = { GridItemSpan(1) }) {
                CreateListCard(
                    dateQuantity = data,
                    onClick = onClick
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateListCard(
    dateQuantity: Pair<String, Int>,
    onClick: (String, Int) -> Unit
) {

    Card(shape = RoundedCornerShape(15.dp), elevation = 4.dp,
        //modifier = Modifier
        //.fillMaxWidth()
        //.height(150.dp)
        //.padding(padding)
        onClick = { onClick(dateQuantity.first, dateQuantity.second) }
    ) {

        Box(Modifier.padding(20.dp)) {

            Column(
                Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = dateQuantity.second.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 0.dp),
                    //style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 40.sp,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = dateQuantity.first,
                    Modifier
                        .padding(top = 5.dp, bottom = 0.dp),
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FutureTestsPreview() {
    KotobaCustomTheme(colorScheme = PHColors) {
        FutureTestsView(
            dateQuantityList = listOf("a" to 1, "b" to 2, "2023-12-12" to 5),
            onPick = {i, d -> }
        )
    }
}