# Build and verification status

## Environment checks

- Java runtime: OpenJDK 21.0.12 detected.
- `javac`: detected.
- Gradle executable: not installed.
- Gradle distribution download: attempted; DNS resolution for `services.gradle.org`
  failed in this execution environment.
- A Gradle 8.12.1 bootstrap launcher is included for machines with internet access.
- GitHub Actions workflow provisions Gradle 8.12.1 and Java 21.

## Static checks performed

- All JSON resources parse successfully.
- Required Gradle, metadata, workflow, and entrypoint files exist.
- `javac` was run in parser-check mode, but compilation cannot resolve Fabric,
  Minecraft, or LWJGL classes because dependencies are unavailable. No Java syntax
  diagnostics were reported before dependency-resolution errors.

## Not verified

- A real Gradle build has **not** completed.
- The mod has **not** been launched in Minecraft.
- No compiled JAR is included.
- API-level compile/runtime compatibility remains unverified until GitHub Actions
  runs successfully. Do not treat the project as a tested release until then.

## Dependency coordinates

The selected toolchain coordinates are published for Minecraft 1.21.5:
- Fabric Loom 1.10.5
- Yarn 1.21.5+build.1
- Fabric Loader 0.16.14
- Fabric API 0.119.5+1.21.5

The Yarn mapping and Loom plugin artifact exist in the Fabric Maven repository.
Fabric's 1.21.5 announcement recommends Loom 1.10 and Gradle 8.12.
