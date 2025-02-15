package org.wordpress.android.ui.mysite.personalization

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import org.wordpress.android.R
import org.wordpress.android.ui.compose.components.MainTopAppBar
import org.wordpress.android.ui.compose.components.NavigationIcons
import org.wordpress.android.ui.compose.components.buttons.WPSwitch
import org.wordpress.android.ui.compose.theme.AppTheme

@AndroidEntryPoint
class PersonalizationActivity : ComponentActivity() {
    private val viewModel: PersonalizationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                viewModel.start()
                PersonalizationScreen(viewModel.uiState.observeAsState())
            }
        }
    }

    @Composable
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    fun PersonalizationScreen(uiState: State<List<DashboardCardState>?>) {
        Scaffold(
            topBar = {
                MainTopAppBar(
                    title = stringResource(id = R.string.personalization_screen_title),
                    navigationIcon = NavigationIcons.BackIcon,
                    onNavigationIconClick = onBackPressedDispatcher::onBackPressed,
                )
            },
            content = {
                PersonalizationContent(uiState.value ?: emptyList())
            }
        )
    }

    @Composable
    fun PersonalizationContent(cardStateList: List<DashboardCardState>, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(id = R.string.personalization_screen_description),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                    )
                }
                items(cardStateList.size) { index ->
                    val cardState = cardStateList[index]
                    DashboardCardStateRow(
                        cardState = cardState,
                        viewModel::onCardToggled
                    )
                }

                item {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        text = stringResource(id = R.string.personalization_screen_footer_cards),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardCardStateRow(
    cardState: DashboardCardState,
    onCardToggled: (cardType: CardType, enabled: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(.9f)
            ) {
                Text(
                    text = stringResource(id = cardState.title),
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(id = cardState.description),
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                )
            }
            Spacer(Modifier.width(8.dp))
            WPSwitch(
                checked = cardState.enabled,
                onCheckedChange = {
                    onCardToggled(cardState.cardType, it)
                },
                modifier = Modifier
                    .weight(.1f)
            )
        }
        Divider(
            thickness = 0.5.dp,
            modifier = Modifier
                .padding()
        )
    }
}

