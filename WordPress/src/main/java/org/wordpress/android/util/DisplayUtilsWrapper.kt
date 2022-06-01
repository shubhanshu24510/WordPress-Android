package org.wordpress.android.util

import dagger.Reusable
import org.wordpress.android.viewmodel.ContextProvider
import javax.inject.Inject

@Reusable
class DisplayUtilsWrapper @Inject constructor(private val contextProvider: ContextProvider) {
    fun getDisplayPixelWidth() = DisplayUtils.getDisplayPixelWidth()

    fun isLandscapeBySize() =
            getDisplayPixelWidth() > DisplayUtils.getWindowPixelHeight(contextProvider.getContext())

    fun isLandscape() = DisplayUtils.isLandscape(contextProvider.getContext())

    fun isTablet() = DisplayUtils.isTablet(contextProvider.getContext()) ||
            DisplayUtils.isXLargeTablet(contextProvider.getContext())

    fun getDisplayPixelHeight() = DisplayUtils.getWindowPixelHeight(contextProvider.getContext())

    fun getDisplayPixelHeightOld() = DisplayUtils.getDisplayPixelHeight(contextProvider.getContext())

    fun isPhoneLandscape() = isLandscapeBySize() && !isTablet()
}
