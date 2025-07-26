package com.vzkz.beepadel.navigation

import android.net.Uri
import androidx.navigation3.runtime.NavKey


typealias DeepLinkHandler = (Uri) -> NavKey?
