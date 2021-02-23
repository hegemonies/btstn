package org.bravo.newsgrabber.util

class UtilitiesDataProvider {

    object Data {
        val containsTags = "a b c #d \$e #f" to listOf("d", "e", "f")
        val notContainsTags = "test1 test2 test3" to emptyList<String>()
    }
}
