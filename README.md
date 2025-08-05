# 📋 MyTasks - Personal Task Manager App

**MyTasks** is a modern, clean, and user-friendly Android app built using **Jetpack Compose** and **Firebase**. It helps users manage their personal tasks across different categories with priority labels. The app includes **user authentication**, real-time task storage, and dynamic UI updates.

---

## ✅ Features

### 🔐 Authentication
- **Register New Users** via Firebase Authentication (Email + Password)
- **Login Existing Users** with form validation
- **User Profile Data** (name and email) saved in Firebase Realtime Database
- **MVVM Architecture** used for authentication logic
- **Error messages and loading indicators** included for better UX
- **Auto-navigation** to main app after login

### 📁 Task Management
- Create tasks by **title**, **description**, and **priority**
- Organize tasks into custom **categories**
- **Edit and delete** tasks at any time
- **View task details** with support for updating
- **Priority-based color coding**: High (Red), Medium (Yellow), Low (Blue)
- Firebase Realtime Database used for live data sync
- Tasks are **sorted by priority**

### 🎨 UI Design
- Built using **Jetpack Compose** with Material 3
- Responsive layout and dark-themed cards
- Clean login and registration UI with white input fields and buttons
- Navigation between all screens using **Navigation Component**
- Cards display **title**, **description**, and **priority** clearly

---

## 🔧 Tech Stack

| Technology           | Use                                      |
|----------------------|-------------------------------------------|
| **Kotlin**           | Main development language                |
| **Jetpack Compose**  | Modern UI Toolkit                        |
| **Firebase Auth**    | User Login/Registration                  |
| **Firebase Realtime DB** | Task & profile data storage         |
| **MVVM Architecture**| State and data management                |
| **ViewModel + StateFlow** | Reactive UI updates                |

---

## 📸 Screenshots

### 🔐 Login Screen
![Login](https://github.com/Neha-Qasim/MyTasks/blob/6b5740ddb69da456b10cac872966ac84d6d4cf6a/Screenshot%202025-08-05%20221958.png)

### 🧾 Register Screen
![Register](screenshots/register.png)

### 📂 Categories
![Categories](screenshots/categories.png)

### 📋 Task List
![Task List](screenshots/task_list.png)

### ➕ Add Task
![Add Task](screenshots/add_task.png)

### 📄 Task Details
![Task Detail](screenshots/task_detail.png)

---

## 🚀 How to Run the Project

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/MyTasks.git
Open the project in Android Studio.

Connect your app to Firebase:

Add your google-services.json file in /app folder

Enable Authentication > Email/Password

Enable Realtime Database and set read/write permissions

Sync Gradle and Run the app on an emulator or device.

📦 MyTasks/
├── model/                # Task, Category models
├── data/                 # Firebase DAO & Repositories
├── auth/                 # Login & Registration Screens
├── viewmodel/            # TaskViewModel, AuthViewModel
├── Screens/              # UI Screens (Tasks, Add, Detail, Category)
├── navigation/           # AppNavGraph
└── MainActivity.kt       # Entry point

