# BeePadel
Welcome to BeePadel, the open source Pádel tracker you were looking for!

Track your pádel matches while you are playing with the WearOS watch version of the app and have all your matches logged in one place.
# Contributing
Feel free to open new issues with possible upgrades, as well as bugs found in the app.
## Build from source
In order to build BeePadel from source:
- Clone this repository
- Add this lines to the `local.properties` (if strava functionality is not needed, just fill with dummy values):
```kotlin
strava_client_id=<client-id>
strava_client_secret="<client-secret>"
```
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
- [Github actions](https://github.com/features/actions) in combination with a custom branching strategy for continuous integration and continuous deployment to the [Google Play Store](https://play.google.com/store)
- [Ktor](https://ktor.io/) for https requests
- [Strava API](https://developers.strava.com/) for 'Connect to Strava'