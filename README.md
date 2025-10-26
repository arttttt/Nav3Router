# Nav3 Router

A simple yet powerful Kotlin Multiplatform navigation library built on top of [Jetpack Navigation 3](https://developer.android.com/jetpack/androidx/releases/navigation3). Provides a clean, decoupled API for managing navigation state from your shared business logic (ViewModels, Presenters, etc.).

## Features

- **Kotlin Multiplatform Ready** - Share navigation logic between Android and iOS
- **Type-safe** - Full type safety with Kotlin's type system and `@Serializable`
- **Decoupled Architecture** - Separate navigation logic from UI for better testability
- **Command Pattern** - Queue-based system handles timing issues gracefully
- **Lifecycle-Aware** - Automatic setup/cleanup with proper lifecycle management
- **Testable** - Easy to test navigation logic in isolation

## Architecture

The library is built around three core architectural components:

```
┌─────────┐      ┌──────────────┐      ┌───────────┐
│ Router  │ ───► │ CommandQueue │ ───► │ Navigator │
└─────────┘      └──────────────┘      └───────────┘
     ▲                   │                     │
     │                   │                     ▼
ViewModel/BL        Buffers & Queues    Jetpack Nav3
                                          BackStack
```

### Core Components

- **`Router`** - High-level, platform-agnostic API for navigation. Use it from ViewModels or business logic to issue commands like `push`, `pop`, and `replaceStack`
- **`CommandQueue`** - Acts as a buffer, decoupling Router from Navigator. Queues commands when UI isn't ready (e.g., during configuration changes) and ensures main thread execution
- **`Navigator`** - Platform-specific implementation that executes commands. Translates abstract commands into direct manipulations of Navigation 3's NavBackStack

This architecture ensures:
- Navigation commands can be issued before UI is ready
- Commands are queued when navigator is unavailable
- Proper lifecycle management during configuration changes
- Thread-safe command execution on the main thread

## Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
// For shared module in KMP project
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.arttttt.nav3router:nav3router:1.0.0") // Check latest version
        }
    }
}

// For Android-only project
dependencies {
    implementation("io.github.arttttt.nav3router:nav3router:1.0.0")
}
```

## Quick Start

### 1. Define Your Screens

```kotlin
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen
    
    @Serializable
    data class Details(val itemId: String) : Screen
    
    @Serializable
    data object Settings : Screen
}
```

### 2. Set Up Navigation

#### Option A: Simple Setup (Router created automatically)

```kotlin
@Composable
fun App() {
    val backStack = rememberNavBackStack(Screen.Home)
    
    Nav3Host(
        backStack = backStack
    ) { backStack, onBack, router ->  // router provided by Nav3Host
        NavDisplay(
            backStack = backStack,
            onBack = onBack,
            entryProvider = entryProvider {
                entry<Screen.Home> {
                    HomeScreen(
                        onNavigateToDetails = { itemId ->
                            router.push(Screen.Details(itemId))
                        }
                    )
                }
                
                entry<Screen.Details> { screen ->
                    DetailsScreen(
                        itemId = screen.itemId,
                        onBack = { router.pop() }
                    )
                }
                
                entry<Screen.Settings> {
                    SettingsScreen()
                }
            }
        )
    }
}
```

#### Option B: With Dependency Injection (Recommended for larger apps)

```kotlin
@Composable
fun App() {
    // Get router from your DI container (Koin, Hilt, etc.)
    val router: Router<Screen> = koinInject()
    val backStack = rememberNavBackStack(Screen.Home)
    
    Nav3Host(
        backStack = backStack,
        router = router  // Pass your DI-provided router
    ) { backStack, onBack, _ ->
        NavDisplay(
            backStack = backStack,
            onBack = onBack,
            entryProvider = /* ... */
        )
    }
}

// In your ViewModel
class HomeViewModel(
    private val router: Router<Screen>
) : ViewModel() {
    
    fun openDetails(itemId: String) {
        router.push(Screen.Details(itemId))
    }
    
    fun openSettings() {
        router.push(Screen.Settings)
    }
}
```

## Router API

| Method | Description |
|--------|-------------|
| `push(vararg screens)` | Pushes one or more screens onto the stack |
| `pop()` | Removes the top screen. Triggers system back if it's the last screen |
| `replaceCurrent(screen)` | Replaces the current top screen with a new one |
| `replaceStack(vararg screens)` | Replaces the entire navigation stack with new screens |
| `popTo(screen)` | Navigates back to a specific screen, removing all screens above it |
| `clearStack()` | Removes all screens except the root |
| `dropStack()` | Keeps only the current screen, then triggers system back |

### Usage Examples

```kotlin
// Push single screen
router.push(Screen.Details("item-123"))

// Push multiple screens at once
router.push(
    Screen.Details("item-1"),
    Screen.Details("item-2"),
    Screen.Settings
)

// Replace current screen
router.replaceCurrent(Screen.Home)

// Navigate back
router.pop()

// Navigate back to specific screen
router.popTo(Screen.Home)

// Replace entire stack (useful for login/logout flows)
router.replaceStack(Screen.Login)

// Clear to root (useful for "Home" button)
router.clearStack()

// Make current screen the only one and exit
router.dropStack()
```