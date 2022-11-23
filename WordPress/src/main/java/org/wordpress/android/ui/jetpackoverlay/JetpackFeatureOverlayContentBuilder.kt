package org.wordpress.android.ui.jetpackoverlay

import org.wordpress.android.R
import org.wordpress.android.ui.jetpackoverlay.JetpackFeatureRemovalOverlayUtil.JetpackFeatureOverlayScreenType
import org.wordpress.android.ui.jetpackoverlay.JetpackFeatureRemovalOverlayUtil.JetpackFeatureOverlayScreenType.SITE_CREATION
import org.wordpress.android.ui.jetpackoverlay.JetpackFeatureRemovalPhase.PhaseFour
import org.wordpress.android.ui.jetpackoverlay.JetpackFeatureRemovalPhase.PhaseNewUsers
import org.wordpress.android.ui.jetpackoverlay.JetpackFeatureRemovalPhase.PhaseOne
import org.wordpress.android.ui.jetpackoverlay.JetpackFeatureRemovalPhase.PhaseThree
import org.wordpress.android.ui.jetpackoverlay.JetpackFeatureRemovalPhase.PhaseTwo
import javax.inject.Inject

class JetpackFeatureOverlayContentBuilder @Inject constructor() {
    fun build(params: JetpackFeatureOverlayContentBuilderParams): JetpackFeatureOverlayUIState {
        return when (params.currentPhase) {
            is PhaseOne -> getStateForPhaseOne(params,params.feature!!)
            PhaseTwo -> TODO()
            PhaseThree -> TODO()
            PhaseFour -> TODO()
            PhaseNewUsers -> TODO()
        }
    }
    private fun getStateForPhaseOne(
        params: JetpackFeatureOverlayContentBuilderParams,
        feature: JetpackFeatureOverlayScreenType
    ): JetpackFeatureOverlayUIState {
        val componentVisibility = JetpackFeatureOverlayComponentVisibility.PhaseOne()
        val content = when (feature) {
            JetpackFeatureOverlayScreenType.STATS -> getStateForPhaseOneStats(params.isRtl)
            JetpackFeatureOverlayScreenType.NOTIFICATIONS -> getStateForPhaseOneNotifications(params.isRtl)
            JetpackFeatureOverlayScreenType.READER -> getStateForPhaseOneReader(params.isRtl)
            SITE_CREATION -> TODO()
        }
        return JetpackFeatureOverlayUIState(componentVisibility, content)
    }

    private fun getStateForPhaseOneStats(rtl: Boolean): JetpackFeatureOverlayContent {
        return JetpackFeatureOverlayContent(
                illustration = if (rtl) R.raw.jp_stats_rtl else R.raw.jp_stats_left,
                title = R.string.wp_jetpack_feature_removal_overlay_phase_one_title_stats,
                caption = R.string.wp_jetpack_feature_removal_overlay_phase_one_description_stats,
                primaryButtonText = R.string.wp_jetpack_feature_removal_overlay_switch_to_new_jetpack_app,
                secondaryButtonText = R.string.wp_jetpack_continue_to_stats
        )
    }

    private fun getStateForPhaseOneReader(rtl: Boolean): JetpackFeatureOverlayContent {
        return JetpackFeatureOverlayContent(
                illustration = if (rtl) R.raw.jp_reader_rtl else R.raw.jp_reader_left,
                title = R.string.wp_jetpack_feature_removal_overlay_phase_one_title_reader,
                caption = R.string.wp_jetpack_feature_removal_overlay_phase_one_description_reader,
                primaryButtonText = R.string.wp_jetpack_feature_removal_overlay_switch_to_new_jetpack_app,
                secondaryButtonText = R.string.wp_jetpack_continue_to_reader
        )
    }

    private fun getStateForPhaseOneNotifications(rtl: Boolean): JetpackFeatureOverlayContent {
        return JetpackFeatureOverlayContent(
                illustration = if (rtl) R.raw.jp_notifications_rtl else R.raw.jp_notifications_left,
                title = R.string.wp_jetpack_feature_removal_overlay_phase_one_title_notifications,
                caption = R.string.wp_jetpack_feature_removal_overlay_phase_one_description_notifications,
                primaryButtonText = R.string.wp_jetpack_feature_removal_overlay_switch_to_new_jetpack_app,
                secondaryButtonText = R.string.wp_jetpack_continue_to_notifications
        )
    }
}

data class JetpackFeatureOverlayContentBuilderParams(
    val currentPhase: JetpackFeatureRemovalPhase,
    val isRtl: Boolean = true,
    val feature: JetpackFeatureOverlayScreenType?
)
