package org.bravo.newsgrabber.util

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class UtilitiesTest {

    @Test
    fun `success find tags`() {
        val (actualMessage, expectedTags) = UtilitiesDataProvider.Data.containsTags
        val actualTags = findTags(actualMessage)

        Assertions.assertThat(actualTags).isEqualTo(expectedTags)
    }

    @Test
    fun `success not found tags`() {
        val (actualMessage, expectedTags) = UtilitiesDataProvider.Data.notContainsTags
        val actualTags = findTags(actualMessage)

        Assertions.assertThat(actualTags).isEqualTo(expectedTags)
    }
}
