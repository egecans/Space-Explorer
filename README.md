# Space Explorer Android App

A modern Android application showcasing SpaceX launch data, implemented with **Clean Architecture**, **MVVM**, and **modularization**. The app supports offline caching, smooth navigation, and clean UI with responsive layouts.

---

## Project Architecture & Structure

- **Clean Architecture** applied with modular separation into:
  - `domain` — business logic, use cases, repository interfaces, and domain models.
  - `data` — Retrofit API service, Room database, repository implementations, and data mappers.
  - `di` — Dependency Injection module managing Hilt bindings and provisioning implementations.
  - `app` — Presentation layer including UI, ViewModels, application entrypoint, and navigation.
  - `common` — Shared utilities and extensions used across modules.

- **Module Dependency Graph:**
  - app → di → data → domain
  - app → domain
  - app → common
  - di → domain
  - di → data
  - data → domain


- This structure **decouples the `app` from `data`** by routing dependencies through the `di` module, aligning better with clean architecture principles.

- **MVVM (Model-View-ViewModel)** pattern separates UI from business logic.

- **Single Activity Design Pattern** with Jetpack **Fragments** for navigation and UI screens.

- UI state management with **sealed classes** representing Loading, Success, Error, and NoInternet states.

- Uses **Kotlin Coroutines** and **StateFlow** for asynchronous data streams and reactive UI.

---

## Key Features & Technologies

- **Networking:** Retrofit with Gson converter and OkHttp logging interceptor for SpaceX API integration.

- **Local Caching:** Room database provides offline support with cache validation.

- **Dependency Injection:** Hilt modules and bindings centralized in `di` module to avoid dependency cycles.

- **UI Components:**
  - Responsive XML layouts using ConstraintLayout and ScrollView.
  - RecyclerView displaying launch list with card-style items.
  - Detail screen showing launch and rocket info with clickable links.
  - Custom SpaceX-inspired theming.

- **App Icon:** Custom adaptive launcher icon matching SpaceX branding.

- **Error Handling:** User-friendly handling of loading, error, and offline states.

- **Testing:** Unit tests for repository, use cases, and ViewModels with mocked dependencies.

---

## Module Overview

| Module   | Responsibilities                                      | Dependencies                   |
|----------|-------------------------------------------------------|-------------------------------|
| `domain` | Business logic, domain models, repository interfaces, use cases | None                          |
| `data`   | API services, Room database, repository implementations, mappers | Depends on `domain`            |
| `di`     | Hilt modules and DI bindings providing implementations | Depends on `domain`, `data`   |
| `app`    | UI, ViewModels, navigation, application entrypoint     | Depends on `domain`, `di`, `common` |
| `common` | Shared utilities and extensions                        | None                          |

---

## Development Highlights

- Strict modular separation to enforce clean dependency rules.
- DI centralized in `di` module to prevent `app` → `data` direct coupling.
- Single Activity with Jetpack Navigation and Safe Args.
- UI state sealed classes for robust UI rendering logic.
- Offline caching and network-aware data fetching.
- Kotlin Coroutines and StateFlow for reactive programming.
- Comprehensive unit testing covering modules independently.
- SpaceX-inspired custom theming and adaptive launcher icon.

---

## How to Run

1. Clone the repo.
2. Open in Android Studio Arctic Fox or later.
3. Sync Gradle to download dependencies.
4. Run the app on an emulator or physical device with internet.

---

## Future Improvements

- UI animations and transitions.
- Cache invalidation strategies.
- Integration/UI testing.
- Accessibility and localization.
- Feature-based modularization as app grows.
- CI/CD automation.

---

## Contact

For questions or feedback, contact [Your Name] at [your.email@example.com].

---

*This project demonstrates modern Android development best practices with a clean, modular, and maintainable architecture.*
