# Weather Application 🌤️

A modern Android weather application built using Kotlin, Jetpack components, and Clean Architecture principles. The app provides real-time weather updates using the OpenWeatherMap API.

## 🚀 Features

- **Real-time Weather:** Get current temperature, humidity, and wind speed.
- **Location Based:** Automatic weather updates based on the user's current GPS location.
- **Dependency Injection:** Powered by **Dagger Hilt** for modular and testable code.
- **Networking:** Robust API handling with **Retrofit** and **OkHttp**.
- **Modern UI:** Built with Material Design, ViewBinding, and Navigation Component.
- **Data Persistence:** Uses **DataStore** for saving user preferences (e.g., units).

## 🛠️ Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **DI:** [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture Layers
- **Jetpack Components:**
  - Navigation Component
  - ViewModel & LiveData
  - DataStore (Preferences)
  - ViewBinding
- **Image Loading:** [Glide](https://github.com/bumptech/glide)
- **Logging:** [Timber](https://github.com/JakeWharton/timber)

## 📦 Project Structure

The project is structured following Clean Architecture principles, ensuring a separation of concerns across distinct layers:
```text
app/
└── src/
    └── main/
        └── java/com/weather/app/
            ├── data/          # Data sources (Remote API via Retrofit, Local DataStore) & Repo Implementations
            ├── di/            # Hilt Dependency Injection Modules (NetworkModule, AppModule)
            ├── domain/        # Business logic (UseCases, Domain Models, Repository Interfaces)
            └── presentation/  # UI Layer (Fragments, ViewModels, ViewBinding, Adapters)
```

## ⚙️ Setup & Installation

1. **Clone the repository:** git clone [https://github.com/markjamesbautista/Weather-App.git](https://github.com/markjamesbautista/Weather-App.git)
2. **API Key Setup:**
    - Sign up at [OpenWeatherMap](https://openweathermap.org/api) and generate an API Key.
    - Open the project in Android Studio and navigate to the **NetworkModule** file (located under the modules package).
    - Find the API key property or constant inside your **NetworkModule and insert** your generated key there:
    ```kotlin
    private const val API_KEY = "your_api_key"
    ```

3. **Build & Run:**
    - Open the project in **Android Studio (Ladybug or newer)**.
    - Sync Gradle files.
    - Run on an emulator or physical device with **Location Services** enabled.

     