package com.vzkz.common.general.fake

import com.vzkz.match.domain.StringGetter

class FakeStringGetter: StringGetter {
    var stringToReturn: String? = null
    override fun getString(resId: Int): String? {
       return stringToReturn
    }
}