package com.example.university.view.main.screens

//import androidx.compose.material.Switch
//import androidx.compose.material.SwitchDefaults
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.theme.Cocon
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.theme.PH
import com.example.university.theme.UXConstants
import com.example.university.theme.mint
import com.example.university.theme.pink
import com.example.university.view.main.MainScreens
import com.example.university.viewModel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

private val TAG = "SettingsView"

@Composable
fun SettingsScreen(
    onChangeColorScheme: () -> Unit,
    navController: NavHostController,
    vm: SettingsViewModel = koinViewModel(),
    showErrorMessage: (String) -> Unit,
) {
    val uiState by vm.uiState.collectAsState()
    if (uiState.haveErrorMessage) {
        showErrorMessage(uiState.errorMessage)
        vm.setHaveErrorMessage(false)
    }
    SettingsView(
        isPassNeeded = uiState.isPasswordNeeded,
        onIsPassNeededChange = {
            vm.setIsPasswordNeeded(it)
        },
        isRememberPresent = uiState.isRememberPresent,
        onIsRememberPresentChange = vm::setIsRememberPresent,
        currentColorScheme = uiState.currentColorScheme,
        onColorSchemeChange = {
            vm.setCurrentColorScheme(it)
            onChangeColorScheme()
        },
        onGoingToMain = {
            Log.i(TAG, "Перенаправление на главный экран")
            navController.navigate(MainScreens.Main.route)
        },
        studyQuantityPerDay = uiState.studyQuantityPerDay,
        onStudyQuantityChange = vm::setStudyQuantityPerDay,
        isAnimationsLong = uiState.isAnimationsLong,
        onIsAnimationsLongChange = vm::setIsAnimationsLong
    )
}

@Composable
fun SettingsView(
    isPassNeeded: Boolean,
    onIsPassNeededChange: (Boolean) -> Unit,
    isRememberPresent: Boolean,
    onIsRememberPresentChange: (Boolean) -> Unit,
    currentColorScheme: Int,
    onColorSchemeChange: (Int) -> Unit,
    onGoingToMain: () -> Unit,
    studyQuantityPerDay: Int,
    onStudyQuantityChange: (Int) -> Unit,
    isAnimationsLong: Boolean,
    onIsAnimationsLongChange: (Boolean) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(
                bottom = UXConstants.VERTICAL_PADDING,
                start = UXConstants.HORIZONTAL_PADDING,
                end = UXConstants.HORIZONTAL_PADDING
            ), verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(UXConstants.VERTICAL_PADDING)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.S_options),
                    style = MaterialTheme.typography.h5
                )
            }
            item {
                Text(
                    text = stringResource(id = R.string.S_base_options),
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 40.dp,
                            bottom = UXConstants.VERTICAL_PADDING
                        ),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.h6
                )
            }
            item {
                SwitchOption(
                    currentState = isPassNeeded,
                    description = stringResource(id = R.string.S_is_pass_needed),
                    action = onIsPassNeededChange,
                )
            }
            item {
                SwitchOption(
                    currentState = isRememberPresent,
                    description = stringResource(id = R.string.S_is_remember_present),
                    action = onIsRememberPresentChange
                )
            }
            item {
                SwitchOption(
                    currentState = isAnimationsLong,
                    description = stringResource(id = R.string.S_is_animations_long),
                    action = onIsAnimationsLongChange
                )
            }
            item {
                InputOption(currentValue = studyQuantityPerDay,
                    description = stringResource(id = R.string.S_study_quantity_per_day),
                    action = {
                        try {
                            onStudyQuantityChange(it.toInt())
                        } catch (e: Exception) {
                            onStudyQuantityChange(0)
                        }
                    })
            }
            item {
                Text(
                    text = stringResource(id = R.string.S_color_scheme),
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.h6
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // PH тема
                    colorTile(
                        bcColor = PH,
                        onClick = { onColorSchemeChange(0) },
                        isPicked = currentColorScheme == 0,
                        title = stringResource(id = R.string.PH_color_name)
                    )
                    // pink тема
                    colorTile(
                        bcColor = pink,
                        onClick = { onColorSchemeChange(1) },
                        isPicked = currentColorScheme == 1,
                        title = stringResource(id = R.string.pink_color_name)
                    )
                    // мятная тема
                    colorTile(
                        bcColor = mint,
                        onClick = { onColorSchemeChange(2) },
                        isPicked = currentColorScheme == 2,
                        title = stringResource(id = R.string.mint_color_name)
                    )
                }
            }
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 68.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(UXConstants.HORIZONTAL_PADDING)
                ) {
                    Image(
                        painter = painterResource(R.drawable.otter),
                        contentDescription = "literaly_me",
                        Modifier
                            .size(50.dp)
                            .clip(shape = MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = stringResource(id = R.string.S_signature_crop),
                        color = if (studyQuantityPerDay == 13) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                        fontFamily = Cocon,
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Left,
                    )
                }
            }
        }
        Button(
            onClick = onGoingToMain,
            modifier = Modifier
                .padding(vertical = UXConstants.VERTICAL_PADDING/2, horizontal = UXConstants.VERTICAL_PADDING)
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text(text = stringResource(id = R.string.exit))
        }
    }
}


@Composable
private fun SwitchOption(
    currentState: Boolean,
    description: String,
    action: (Boolean) -> (Unit),
) {
    // var checked by remember { mutableStateOf(currentState) }

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = description,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Left
        )

        Switch(
            checked = currentState,
            onCheckedChange = {
                Log.d(TAG, "Попытка установить настройку \"$description\" в состояние $it")
                action(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.primaryVariant,
                uncheckedThumbColor = MaterialTheme.colors.onError,
                uncheckedTrackColor = MaterialTheme.colors.onError.copy(alpha = 0.45f),
            ),
            //modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
fun InputOption(
    currentValue: Int,
    description: String,
    action: (String) -> (Unit),
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = description,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Left
        )
        TextField(
            value = currentValue.toString(),
            onValueChange = action,
            modifier = Modifier
                .width(75.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                unfocusedIndicatorColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.primary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                color = if (currentValue == 0) MaterialTheme.colors.secondaryVariant.copy(alpha = 0.4f)
                else MaterialTheme.colors.onBackground
            )
        )
    }
}

@Composable
fun colorTile(
    bcColor: Color,
    onClick: () -> (Unit),
    isPicked: Boolean = false,
    title: String,
) {
    //Log.i(TAG, isPicked.toString())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            Modifier
                .size(80.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(10.dp),
            color = bcColor,
            elevation = if (isPicked) 0.dp else UXConstants.ELEVATION
        ) {}
        Text(text = title, Modifier.padding(top = UXConstants.VERTICAL_PADDING.div(2)))
    }
}

@Preview(showBackground = true)
@Composable
fun InputOptionPreview() {
    KotobaCustomTheme(colorScheme = ColorScheme.PH.colors) {
        InputOption(currentValue = 10, description = "уфффффф", action = { })
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    KotobaCustomTheme(colorScheme = ColorScheme.mint.colors) {
        SettingsView(isPassNeeded = true,
            onIsPassNeededChange = { },
            isRememberPresent = false,
            onIsRememberPresentChange = { },
            currentColorScheme = 2,
            onColorSchemeChange = { },
            onGoingToMain = { /*TODO*/ },
            studyQuantityPerDay = 69,
            onStudyQuantityChange = { },
            isAnimationsLong = false,
            onIsAnimationsLongChange = {}
        )
    }
}