package org.bravo.newsgrabber.properties.app

import org.bravo.newsgrabber.model.FetchStrategy
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.validation.constraints.NotBlank

@Configuration
@ConfigurationProperties(prefix = "app")
class AppProperties {

    @NotBlank
    val strategy: String = FetchStrategy.ALL.strategy

    val sources: List<String> = emptyList()
}
