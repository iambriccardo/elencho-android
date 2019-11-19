package com.riccardobusetti.unibztimetable.utils

import java.util.regex.Pattern

object StringUtils {

    fun isMatchingPartially(regex: String, string: String) =
        Pattern.compile(regex).matcher(string).find()
}