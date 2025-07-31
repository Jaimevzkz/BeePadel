# BeePadel
Welcome to BeePadel, the open source Pádel tracker you were looking for!

Track your pádel matches while you are playing with the WearOS watch version of the app and have all your matches logged in one place.
# Contributing
Feel free to open new issues with possible upgrades you would like to see in the future, as well as bugs found in the app.
# Tech Stack
- Built using [Kotlin](https://kotlinlang.org/) and [Jetpack Compose](https://developer.android.com/compose)
- [Navigation 3](https://developer.android.com/guide/navigation/navigation-3)
- Feature based multi module architecture
- MVI pseudo-architecture for presentation layers
- [Junit5](https://junit.org/) + [turbine](https://github.com/cashapp/turbine/) + [mockk](https://mockk.io/) for unit testing
- Jetpack Compose [UI testing]()
- [Koin](https://insert-koin.io/) for dependency injection
- Customized [Material 3](https://m3.material.io/) theme
- [Timber](https://github.com/JakeWharton/timber) for logging
- [SqlDelight](https://github.com/sqldelight/sqldelight) for local persistence
- Use of permissions and foreground services
- [Github actions](https://github.com/features/actions) in combination with a custom branching strategy for continous integration and continous deployment to the [Google Playstore](https://play.google.com/store)
