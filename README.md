# CineVault 🎬

A modern, elegant Android movie discovery and management app built with Jetpack Compose. CineVault helps you explore, organize, and track your favorite movies with a beautiful medieval-themed cinema aesthetic.

## 📱 Features

### Core Features

- **Movie Discovery**: Browse popular, top-rated, and upcoming movies
- **Search**: Search for movies by title, director, or year
- **Movie Details**: View comprehensive movie information including:
  - Cast and crew details
  - Trailers and videos
  - Overview and ratings
  - Similar movie recommendations
- **Personal Collections**:
  - Favorites list
  - Watchlist
  - Watched movies tracking
- **User Profile**:
  - Customizable profile with bio
  - View your movie collections
  - Track your movie statistics

### User Experience

- **Authentication**: Secure login and registration with Firebase
  - Email/Password authentication
  - Google Sign-In
  - Facebook Sign-In
- **Personalization**:
  - Dark mode support
  - Multi-language support (English & Arabic)
  - Customizable profile
- **Onboarding**: Smooth onboarding experience for new users
- **Splash Screen**: Beautiful medieval-styled splash screen with gradient background

## 🛠️ Tech Stack

### Core Technologies

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - UI components and theming

### Architecture

- **MVVM (Model-View-ViewModel)** - Clean architecture pattern
- **Repository Pattern** - Data layer abstraction
- **StateFlow & Flow** - Reactive state management

### Libraries & Frameworks

#### Navigation

- **Voyager** - Type-safe navigation library for Compose

#### Networking

- **Retrofit** - HTTP client for API calls
- **Gson** - JSON serialization/deserialization

#### Database

- **Room** - Local database for offline storage
- **Firebase Firestore** - Cloud database for user data
- **Firebase Authentication** - User authentication

#### Image Loading

- **Coil** - Image loading library for Compose

#### UI Components

- **Material Icons Extended** - Extended icon set
- **Accompanist** - Additional Compose utilities

#### Other

- **DataStore** - Preferences storage
- **Coroutines** - Asynchronous programming
- **YouTube Player** - Video playback

## 📁 Project Structure

```
app/src/main/java/com/example/moviestime/
├── data/
│   ├── datastore/          # DataStore for preferences
│   ├── local/              # Room database
│   ├── model/              # Data models
│   ├── notification/       # Notification handling
│   ├── remote/             # API services and DTOs
│   └── repository/         # Repository implementations
├── ui/
│   ├── components/         # Reusable UI components
│   ├── navigation/         # Navigation screens (Voyager)
│   ├── screens/            # Screen composables
│   └── theme/              # App theming and fonts
├── utils/                  # Utility classes
├── viewmodel/              # ViewModels
└── MainActivity.kt         # Main activity
```

## 🎨 Design

### Theme

- **Cinema-Inspired Design**: Dark burgundy and gold color scheme
- **Medieval Aesthetic**: Elegant typography with PlayFair for headings and Inter for body text
- **Custom Fonts**:
  - PlayFair Display (Headings)
  - Inter (Body text)
- **Gradient Backgrounds**: Rich gradients throughout the app
- **Noisy Texture**: Subtle texture overlays for depth

### Color Palette

- **Primary**: Burgundy (#9E1938)
- **Secondary**: Gold (#E8C547)
- **Background**: Dark (#171311)
- **Surface**: Card color (#231F1C)

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 24+ (minimum)
- Firebase project setup

### Setup Instructions

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd Capstone-Project-DEPI
   ```

2. **Firebase Setup**

   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Download `google-services.json` and place it in `app/` directory
   - Enable the following Firebase services:
     - Authentication (Email/Password, Google, Facebook)
     - Firestore Database

3. **API Key Setup**

   - Get your TMDB API key from [The Movie Database](https://www.themoviedb.org/settings/api)
   - Add your API key to `local.properties`:
     ```properties
     TMDB_API_KEY=your_api_key_here
     ```

4. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Run the app on an emulator or physical device

## 📱 Screens

### Main Screens

- **Splash Screen**: Medieval-styled welcome screen
- **Onboarding**: First-time user introduction
- **Login/Register**: Authentication screens
- **Home**: Featured movies and categories
- **Discover**: Movie search and genre exploration
- **Profile**: User profile and collections
- **Movie Details**: Comprehensive movie information
- **Settings**: App preferences and account management
- **Edit Profile**: Profile customization

## 🔐 Authentication

The app supports multiple authentication methods:

- Email/Password
- Google Sign-In
- Facebook Sign-In

User data is stored securely in Firebase Firestore.

## 🌍 Internationalization

Currently supports:

- English
- Arabic (العربية)

Language can be changed in Settings.

## 🎯 Key Features Implementation

### Movie Management

- **Favorites**: Add/remove movies from favorites
- **Watchlist**: Save movies to watch later
- **Watched**: Mark movies as watched
- All data is synced with Firebase Firestore

### Search & Discovery

- Real-time search with debouncing
- Genre-based filtering
- Category browsing (Popular, Top Rated, Upcoming)

### Offline Support

- Room database for local caching
- Offline movie data access

## 🧪 Testing

To run tests:

```bash
./gradlew test
```

## 📝 License

This project is part of a Capstone project for DEPI.

## 👥 Contributors

- Tarik Lotfy
- Yousef Sabry
- Yousef ElBasuony
- Mohamed ElBoraey

## 🙏 Acknowledgments

- [The Movie Database (TMDB)](https://www.themoviedb.org/) for movie data API
- Firebase for backend services
- All open-source library contributors
  -ENG/ Ahmed Khwaa

---
