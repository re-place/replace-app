package replace

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringApp {

    object SpringBackend : Backend {
        override fun start(args: Array<String>) {
            val application = SpringApplication(SpringApp::class.java)
            application.setDefaultProperties(mapOf("server.port" to "8000"))
            application.run(*args)
        }
    }
}
