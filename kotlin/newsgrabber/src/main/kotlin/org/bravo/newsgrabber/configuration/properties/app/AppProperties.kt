package org.bravo.newsgrabber.configuration.properties.app

import org.bravo.newsgrabber.model.FetchStrategy
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "app")
class AppProperties {

    @NotBlank
    var strategy: String = FetchStrategy.ALL.strategy

    lateinit var sources: List<String>
}
