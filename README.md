# EnergyLevels

EnergyLevels is a native Android application designed to help you build and track healthy micro-habits. The app, formerly known as MicroHabits, allows users to set goals, discover food recipes and exercise programs, and receive recommendations tailored to their daily energy levels.

The project consists of an Android client built with Jetpack Compose and a Python-based Flask API that connects to a MySQL database.

## Features

- **Goal Management**: Create, view, and update personal health goals related to food and exercise.
- **Activity Discovery**: Explore a variety of food recipes and exercise programs, complete with detailed instructions, ingredients, and steps.
- **Daily Energy Tracking**: Log your daily energy level (High, Medium, Low) to get personalized activity suggestions.
- **Detailed Views**: Access comprehensive information for recipes, including ingredients and cooking steps, and for exercises, including duration, difficulty, and individual movements.
- **Favorites System**: Save your favorite recipes and exercises for quick and easy access.
- **Focus Map**: Prioritize new habits by mapping them based on their potential impact and the likelihood of completion.
- **Custom UI**: A clean, modern interface built entirely with Jetpack Compose, featuring a custom design system with unique fonts and color schemes.

## Technology Stack

- **Frontend (Android App)**
  - **Language**: Kotlin
  - **UI**: Jetpack Compose
  - **Navigation**: Navigation Compose
  - **Networking**: Volley
  - **Image Loading**: Coil 3
  - **Architecture**: MVVM-like pattern with services and state holders.

- **Backend (API)**
  - **Framework**: Flask
  - **Language**: Python
  - **Database Connector**: `mysql-connector-python`

- **Database**
  - MySQL

## Architecture

The application follows a client-server architecture:

- **Android Client (`/app`)**: A native Android application that provides the user interface and handles client-side logic. It communicates with the backend via a REST API to fetch and manage data.
- Full tree view:
```text
C:.
│   MainActivity.kt         # Contains logic to initiate the home screen when the app is opened, also sets up the base for the navigation items and bottom navigation
│
├───api                     # Holds constants and functions to set up a connection with the API
│
├───archive                 # Content which was used in previous versions
│
├───components              # Jetpack Compose components which can be used in different situations and on different pages
│
├───data                    # Holds a variable object which contains all the global variables to be accessed more easily
│
├───helpers                 # Helper functions which help to achieve certain goals (like turning a class into a Map or JSONObject)
│
├───models
│   ├───classes             # Contains newly made classes
│   │
│   ├───deleteLater         # Classes which are no longer in use and could possibly be deleted later
│   │
│   ├───enums               # Enums made for certain situations, for example to help measure user's energy levels
│   │
│   └───superclasses        # Contains super classes which the normal classes can use to be set up more easily
│
├───screens
│   ├───baseFunctionality   # Contains the screens made for the basic functionality before the main functionality was created
│   │
│   ├───details             # Specific details screen to display more content for certain activities
│   │
│   ├───main                # Screens which are expected to be used the most, these screens are also the items displayed in the bottom navigation bar
│   │
│   └───secondary           # Remaining screens which are not in the bottom navigation bar, but still hold important content and functionalities
│
├───services                # Services to help execute certain back-end functionalities
│
└───ui                      # Contains settings to help set the styling and look of the app
```

- **Flask API (`/API`)**: A lightweight Python server that exposes RESTful endpoints for the Android app. It handles all CRUD (Create, Read, Update, Delete) operations by interacting directly with the MySQL database.

## Getting Started

To run this project, you need to set up both the backend server and the Android application.

### 1. Backend Setup

The backend is a Flask API that connects to a MySQL database.

**Prerequisites:**
- Python 3
- Flask
- MySQL Server

**Steps:**

1.  **Set up the Database:**
    - Ensure your MySQL server is running.
    - Create a new database named `micro_habits`.
    - The API connects using the credentials `user='root'` and an empty password, connecting to `localhost`. If your configuration is different, update the `get_db_connection` function in `API/app.py`.
    - You will need to create the database schema and tables (e.g., `user`, `goal`, `food_recipe`, `exercise_program`) manually, as a schema file is not included.

2.  **Install Dependencies:**
    ```sh
    pip install Flask mysql-connector-python
    ```

3.  **Run the Flask Server:**
    ```sh
    cd API
    python app.py
    ```
    The server will start on `http://0.0.0.0:5000`.

### 2. Android App Setup

The Android app communicates with the Flask API to function correctly.

**Prerequisites:**
- Android Studio
- An Android device or emulator

**Steps:**

1.  **Clone the Repository:**
    ```sh
    git clone https://github.com/ilse0806/energylevels.git
    ```

2.  **Open in Android Studio:**
    - Open Android Studio and select "Open" or "Import Project".
    - Navigate to the cloned `energylevels` directory and open it.

3.  **Update API Endpoint URL:**
    - Open the file `app/src/main/java/com/example/microhabits/api/DatabaseConstants.kt`.
    - **This is a critical step:** You must replace the hardcoded IP address with the local IP address of the machine running your Flask backend.
      
      For example, if your computer's local IP is `192.168.1.10`, change the constants to:
      ```kotlin
      object DatabaseConstants {
          const val GET_TABLE_URL = "http://192.168.1.10:5000/get_table/"
          const val GET_ROW_URL = "http://192.168.1.10:5000/get_row/"
          // ... and so on for all other URLs
      }
      ```

4.  **Build and Run:**
    - Let Android Studio sync the Gradle files.
    - Build and run the application on your selected emulator or physical device.
