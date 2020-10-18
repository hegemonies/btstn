package org.bravo.apiproxy

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.date.*
import org.bravo.apiproxy.property.databaseHost
import org.bravo.apiproxy.property.databasePassword
import org.bravo.apiproxy.property.databasePort
import org.bravo.apiproxy.property.databaseUsername
import org.bravo.apiproxy.route.news
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    Database.connect(
        url = "jdbc:postgresql://$databaseHost:$databasePort/bravo_news",
        driver = "org.postgresql.Driver",
        user = databaseUsername,
        password = databasePassword
    )

    // You can see metrics used MBeans plugin in JVisualVM
    // install(DropwizardMetrics) {
    //     val reporter = Slf4jReporter.forRegistry(registry)
    //         .outputTo(log)
    //         .convertRatesTo(TimeUnit.SECONDS)
    //         .convertDurationsTo(TimeUnit.MILLISECONDS)
    //         .build();
    //     reporter.start(10, TimeUnit.SECONDS);
    // }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    install(AutoHeadResponse)

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }


    install(CachingHeaders) {
        options { outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> CachingOptions(
                    CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60),
                    expires = null as? GMTDate?
                )
                else -> null
            }
        }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    /* todo: htts for future
    install(HSTS) {
        includeSubDomains = true
    }

    // https://ktor.io/servers/features/https-redirect.html#testing
    if (!testing) {
        install(HttpsRedirect) {
            // The port to redirect to. By default 443, the default HTTPS port.
            sslPort = 443
            // 301 Moved Permanently, or 302 Found redirect.
            permanentRedirect = true
        }
    }
    */

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        news()

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
