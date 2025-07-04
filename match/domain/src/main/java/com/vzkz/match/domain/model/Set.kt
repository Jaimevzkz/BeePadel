package com.vzkz.match.domain.model

import java.util.UUID

data class Set(
    val setId: UUID,
    val gameList: List<Game>,
)