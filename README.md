fjfj
# BeePadel
Welcome to BeePadel, the open source Pádel tracker you were looking for!

- Track your pádel matches while you are playing, with your phone or a WearOS watch and have all your matches logged in one place. 
- With the ability to import/export your match data, you'll have the ability to never loose your data. 
- Connect with Strava so that your matches are automatically uploaded and get more reach.
## Download
[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="80">](https://play.google.com/store/apps/details?id=com.vzkz.beepadel)
## Module Architecture
```
BeePadel
├── app
├── wear
│   ├── app
│   ├── match
│   │   ├── data
│   │   ├── domain
│   │   ├── presentation
├── build-logic
│   ├── convention
├── common
│   ├── general
│   ├── sharedTest
├── core
│   ├── connectivity
│   │   ├── data
│   │   ├── domain
│   ├── data
│   ├── database
│   │   ├── data
│   │   ├── domain
│   ├── domain
│   ├── notification
│   │   ├── data
│   │   ├── domain
│   ├── preferences
│   ├── presentation
│   │   ├── desingsystem
│   │   ├── desingsystem_wear
│   │   ├── ui
│   ├── 
├── match
│   ├── data
│   ├── domain
│   ├── presentation
├── settings
│   ├── data
│   ├── domain
│   ├── presentation
```

## Tech Stack
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
