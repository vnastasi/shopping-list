# Shopping List App

[![Build Status](https://img.shields.io/github/actions/workflow/status/val/shopping-list/feature-pull-request.yml?branch=main&style=for-the-badge)](https://github.com/val/shopping-list/actions/workflows/feature-pull-request.yml)
<!-- TODO: Add other relevant badges, e.g., code coverage -->

A modern Android application for managing shopping lists.

<img src="docs/images/Screenshot_20250817_174114.png" alt="Screenshot_20250817_174114.png" width="320" />
<img src="docs/images/Screenshot_20250817_174204.png" alt="Screenshot_20250817_174204.png" width="320" />
<img src="docs/images/Screenshot_20250817_174619.png" alt="Screenshot_20250817_174619.png" width="320" />
<img src="docs/images/Screenshot_20250817_174632.png" alt="Screenshot_20250817_174632.png" width="320" />
<img src="docs/images/Screenshot_20250817_174653.png" alt="Screenshot_20250817_174653.png" width="320" />

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Cloning](#cloning)
  - [Building](#building)
- [Running Tests](#running-tests)
  - [Unit Tests](#unit-tests)
  - [Instrumented Tests](#instrumented-tests)
- [CI/CD](#cicd)
- [License](#license)

## Features

- Create and manage multiple shopping lists.
- Add items to lists.
- Mark items as purchased.

## Tech Stack

This project utilizes a modern Android development tech stack:

- <img src="https://cdn.simpleicons.org/kotlin" width="20" height="20" alt="Kotlin logo" /> **Kotlin**: Primary programming language.
- <img src="https://cdn.simpleicons.org/jetpackcompose" width="20" height="20" alt="Jetpack Compose logo" /> **Jetpack Compose**: For building the UI.
- <img src="https://cdn.simpleicons.org/dagger" width="20" height="20" alt="Dagger logo (for Hilt)" /> **Hilt**: For dependency injection.
- <img src="https://cdn.simpleicons.org/android" width="20" height="20" alt="Android logo (for Room)" /> **Room**: For local data persistence (likely, based on typical Android architecture).
- <img src="https://cdn.simpleicons.org/gradle" width="20" height="20" alt="Gradle logo" /> **Gradle Kotlin DSL**: For build scripts.
- <img src="https://cdn.simpleicons.org/materialdesign" width="20" height="20" alt="Material Design logo" /> **Material 3**: For UI components and theming.
<!-- TODO: Add or remove technologies as per the project's actual stack. -->

## Project Structure

The project follows a multi-module architecture to promote separation of concerns, scalability, and faster build times. Key modules include:

- `app`: The main application module, integrating all other modules.
- `database`: Handles local data storage.
- `domain/api`: Defines contracts for the domain layer.
- `domain/implementation`: Contains the business logic and use cases.
- `screen/overview`: UI and ViewModel for the overview screen.
- `screen/list-details`: UI and ViewModel for the list details screen.
- `screen/add-items`: UI and ViewModel for adding items.
- `build-logic`: Contains custom Gradle plugins and build conventions.

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

- Android Studio (latest stable version recommended)
- Java Development Kit (JDK) 21
- Git

### Cloning

1.  Clone the repository (please verify the URL):
    ```bash
    git clone https://github.com/vnastasi/shopping-list.git
    cd shopping-list
    ```

### Building

1.  Open the project in Android Studio.
2.  Allow Android Studio to sync the project with Gradle. This might take a few minutes.
3.  To build the debug version of the application, you can use the Gradle wrapper:
    ```bash
    ./gradlew assembleDebug
    ```
    Alternatively, use the "Build" menu in Android Studio.

## Running Tests

This project includes unit tests and instrumented (UI) tests.

### Unit Tests

To run all unit tests for the debug build type:

```bash
./gradlew testDebugUnitTest
```

### Instrumented Tests

Instrumented tests require a connected Android device or emulator.

1.  Ensure you have an emulator running or a device connected.
2.  To run all instrumented tests for the debug build type:
    ```bash
    ./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=md.vnastasi.shoppinglist.suite.AllSuite
    ```

## CI/CD

This project uses GitHub Actions for Continuous Integration. The workflow is defined in [`.github/workflows/feature-pull-request.yml`](./.github/workflows/feature-pull-request.yml).

The CI pipeline includes steps for:
- Building the application (`./gradlew assemble`)
- Running unit tests (`./gradlew testDebugUnitTest`, `./gradlew copyUnitTestExecData`)
- Running instrumented tests (`./gradlew connectedDebugAndroidTest`, `./gradlew copyInstrumentedTestExecData`)
- Code quality checks (`./gradlew projectHealth`, `./gradlew verifyCodeCoverage`, `./gradlew detekt`)
- Auto-merging dependency updates for Dependabot/Wrapperbot PRs.


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

---
*This README was generated with assistance from an AI tool.*
