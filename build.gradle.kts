import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform") version "2.0.0-RC1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)

    jvm()
    macosArm64()
    macosX64()
    linuxX64()


    targets.configureEach {
        if (this is KotlinNativeTarget) {
            binaries {
                executable("main", listOf(NativeBuildType.RELEASE))
            }
            val irDumpDir = project.projectDir
                .resolve("build")
                .resolve("irDump")
                .resolve(this.targetName)
            irDumpDir.mkdirs()
            compilations.configureEach {
                this.compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.addAll(
                            "-Xsave-llvm-ir-after=Codegen",
                            "-Xsave-llvm-ir-directory=${irDumpDir.absolutePath}",
                            "-opt"
                        )
                    }
                }
            }
        }
    }

}
