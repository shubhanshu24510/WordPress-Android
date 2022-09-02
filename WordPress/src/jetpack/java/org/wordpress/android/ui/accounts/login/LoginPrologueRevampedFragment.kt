package org.wordpress.android.ui.accounts.login

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline.Rectangle
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import org.wordpress.android.R.drawable
import org.wordpress.android.R.string
import org.wordpress.android.ui.accounts.login.SlotsEnum.Buttons
import org.wordpress.android.ui.accounts.login.SlotsEnum.ClippedBackground
import org.wordpress.android.ui.accounts.login.components.ButtonsColumn
import org.wordpress.android.ui.accounts.login.components.JetpackLogo
import org.wordpress.android.ui.accounts.login.components.PrimaryButton
import org.wordpress.android.ui.accounts.login.components.SecondaryButton
import org.wordpress.android.ui.accounts.login.components.SplashBox
import org.wordpress.android.ui.compose.theme.AppTheme
import org.wordpress.android.util.extensions.showFullScreen

class LoginPrologueRevampedFragment : Fragment() {
    private lateinit var loginPrologueListener: LoginPrologueListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                LoginScreenRevamped(
                        onWpComLoginClicked = loginPrologueListener::showEmailLoginScreen,
                        onSiteAddressLoginClicked = loginPrologueListener::loginViaSiteAddress,
                )
            }
        }

        requireActivity().window.showInFullScreen()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is LoginPrologueListener) { "$context must implement LoginPrologueListener" }
        loginPrologueListener = context
    }

    private fun Window.showInFullScreen() {
        // Set Translucent Status Bar
        this.showFullScreen()

        // Set Translucent Navigation Bar
        setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
    }

    companion object {
        const val TAG = "login_prologue_revamped_fragment_tag"
    }
}

/**
 * These slots are utilized below in a subcompose layout in order to measure the size of the buttons composable. The
 * measured height is then used to create a clip shape for the blurred background layer. This allows the background
 * composable to be aware of its sibling's dimensions within a single frame (i.e. it does not trigger a recomposition).
 */
enum class SlotsEnum { Buttons, ClippedBackground }

@Composable
private fun LoginScreenRevamped(
    onWpComLoginClicked: () -> Unit,
    onSiteAddressLoginClicked: () -> Unit,
) {
    Box {
        SplashBox()
        Image(
                painter = painterResource(drawable.bg_jetpack_login_splash_top_gradient),
                contentDescription = stringResource(string.login_prologue_revamped_content_description_top_bg),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 292.dp)
        )
        JetpackLogo(
                modifier = Modifier
                        .padding(top = 60.dp)
                        .size(60.dp)
                        .align(Alignment.TopCenter)
        )
        SubcomposeLayout { constraints ->
            val buttonsPlaceables = subcompose(Buttons) @Composable {
                ButtonsColumn {
                    PrimaryButton(onClick = onWpComLoginClicked)
                    SecondaryButton(onClick = onSiteAddressLoginClicked)
                }
            }.map { it.measure(constraints) }

            val buttonsHeight = buttonsPlaceables[0].height
            val buttonsClipShape = object : Shape {
                override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Rectangle {
                    return Rectangle(
                            Rect(
                                    bottom = size.height,
                                    left = 0f,
                                    right = size.width,
                                    top = size.height - buttonsHeight,
                            )
                    )
                }
            }

            val clippedBackgroundPlaceables = subcompose(ClippedBackground) @Composable {
                SplashBox(
                        modifier = Modifier.clip(buttonsClipShape),
                        textModifier = Modifier.composed {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                blur(15.dp, BlurredEdgeTreatment.Unbounded)
                            } else {
                                // On versions older than Android 12 the blur modifier is not supported,
                                // so we make the text transparent to have the buttons stand out.
                                alpha(0.5f)
                            }
                        }
                )
            }.map { it.measure(constraints) }

            layout(constraints.maxWidth, constraints.maxHeight) {
                clippedBackgroundPlaceables.forEach { it.placeRelative(0, 0) }
                buttonsPlaceables.forEach { it.placeRelative(0, constraints.maxHeight - buttonsHeight) }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Preview(showBackground = true, device = Devices.PIXEL_4_XL, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewLoginScreenRevamped() {
    AppTheme {
        LoginScreenRevamped(
                onWpComLoginClicked = {},
                onSiteAddressLoginClicked = {}
        )
    }
}
