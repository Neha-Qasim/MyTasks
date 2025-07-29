# 📋 MyTasks - Personal Task Manager App

**MyTasks** is a beginner-friendly and modular Android app built with **Jetpack Compose** 
that lets users organize tasks by categories, assign priorities, and manage them with an intuitive UI. It uses 
**Room database** for local storage and follows
**MVVM architecture** with reusable components.

---

## 🖼️ Preview

![image alt][image_url]

---

## 🚀 Features

- 📂 Category-based task management
- 📝 Add, edit, delete tasks
- 🗂️ Priority tagging (High, Medium, Low)
- ✔️ Mark tasks as completed
- ➕ Add & delete categories
- 🔁 Splash screen & smooth navigation
- 🧠 MVVM architecture
- 🧱 Room Database with DAO
- 💡 Reusable components (like `TaskCard`)
- 🧑‍🎨 Jetpack Compose Material 3 UI

---

## 🧱 Project Structure
mytasks/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/neha/mytasks/
│           │   ├── Screens/               # All Compose screens (UI)
│           │   │   ├── AddTaskScreen.kt
│           │   │   ├── CategoryScreen.kt
│           │   │   ├── SplashScreen.kt
│           │   │   ├── TaskDetailScreen.kt
│           │   │   └── TaskListScreen.kt
│           │   ├── data/                  # Room DB, DAOs, Repositories
│           │   │   ├── CategoryDao.kt
│           │   │   ├── CategoryRepository.kt
│           │   │   ├── TaskDao.kt
│           │   │   ├── TaskDatabase.kt
│           │   │   └── TaskRepository.kt
│           │   ├── model/                 # Data models
│           │   │   ├── Category.kt
│           │   │   ├── Task.kt
│           │   │   └── TaskPriority.kt
│           │   ├── navigation/            # Navigation graph
│           │   │   └── AppNavGraph.kt
│           │   ├── ui/theme/              # Material theme files
│           │   │   ├── Color.kt
│           │   │   ├── Theme.kt
│           │   │   └── Type.kt
│           │   ├── viewmodel/             # Business logic layer
│           │   │   └── TaskViewModel.kt
│           │   └── MainActivity.kt        # App entry point
│           └── res/                       # Resources (layouts, values, etc.)
│           └── AndroidManifest.xml
├── build.gradle.kts                       # Project-level Gradle script
├── settings.gradle.kts                    # Gradle settings
├── gradle.properties
├── gradlew / gradlew.bat                  # Gradle wrappers
└── README.md


---

## ⚙️ Tech Stack

| Layer        | Technology                        |
|--------------|------------------------------------|
| UI           | Jetpack Compose, Material 3        |
| Architecture | MVVM                                |
| Database     | Room DB                             |
| Language     | Kotlin                              |
| IDE          | Android Studio                      |

---


