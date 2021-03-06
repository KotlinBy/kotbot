import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

/**
 * Base configuration for **Kotbot** projects.
 *
 * @author Ruslan Ibragimov
 */
class KotbotPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        val kotlinVersion: String by project

        dependencies {
            "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
            "implementation"("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
            "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinCoroutines")

            "testImplementation"(junitApi)
            "testRuntimeOnly"(junitEngine)
            "testImplementation"(mockk)
        }

        repositories {
            mavenCentral()
            maven { url = uri("https://repo.kotlin.link") }
        }

        tasks.withType<KotlinJvmCompile>().configureEach {
            kotlinOptions {
                jvmTarget = kotbotJvmTarget
                languageVersion = "1.4"
                freeCompilerArgs = freeCompilerArgs + listOf("-progressive")
            }
        }

        tasks.withType<JavaCompile>().configureEach {
            sourceCompatibility = kotbotJvmTarget
            targetCompatibility = kotbotJvmTarget
        }

        Unit
    }
}
