package org.wordpress.android.ui.jetpackplugininstall.fullplugin

import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.ui.prefs.AppPrefsWrapper
import org.wordpress.android.util.config.JetpackInstallFullPluginFeatureConfig
import org.wordpress.android.util.extensions.isJetpackIndividualPluginConnectedWithoutFullPlugin
import javax.inject.Inject

class GetShowJetpackFullPluginInstallOnboardingUseCase @Inject constructor(
    private val jetpackInstallFullPluginFeatureConfig: JetpackInstallFullPluginFeatureConfig,
    private val appPrefsWrapper: AppPrefsWrapper,
) {
    fun execute(siteModel: SiteModel): Boolean =
        siteModel.id != 0 &&
                jetpackInstallFullPluginFeatureConfig.isEnabled() &&
                appPrefsWrapper.getShouldShowJetpackInstallOnboarding(siteModel.id) &&
                siteModel.isJetpackIndividualPluginConnectedWithoutFullPlugin()
}
