package org.bravo.newsgrabber.util

fun findTags(message: String): List<String> {
    val chars = message.toCharArray()

    val tags = linkedSetOf<String>()

    var index = 0

    while (index < chars.size) {
        if (isTag(chars[index]) && isNotLastCharIn(index, chars)) {
            var buffer = ""

            index++

            while (index < chars.size && chars[index].isLetter()) {
                buffer += chars[index]
                index++
            }

            tags.add(buffer)
        }

        index++
    }

    return tags.toList()
}

fun isTag(char: Char): Boolean =
    char == '#' || char == '$'

fun isNotLastCharIn(index: Int, chars: CharArray) =
    chars.size > (index + 1) && chars[index + 1].isLetter()
