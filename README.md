# Playlist Maker

Мобильное Android-приложение **Playlist Maker** для поиска музыкальных треков, просмотра деталей треков и управления настройками приложения. Проект разработан в рамках спринтов 3–6 курса **«Ассоциированные программы: Андроид-разработка»** на платформе Яндекс Практикум.

---

## Стек технологий и архитектура

- **Язык программирования:** Kotlin (`2.0.21`), JVM Target 11
- **Минимальная версия SDK:** `minSdk = 29` (Android 10.0), `targetSdk = 35`, `compileSdk = 35`
- **UI-фреймворк:** Jetpack Compose + Material 3 (`Compose BOM 2024.11.00`)
- **Навигация:** Jetpack Navigation Compose (`PlaylistHost` + `NavHost` + `Screen` enum) и явные/неявные `Intent`
- **Архитектура:** Clean Architecture (`data` / `domain` / `ui`) + MVVM (`ViewModel`, `StateFlow`, `SearchState`)
- **Асинхронность:** Kotlin Coroutines (`viewModelScope`, `Dispatchers.IO`, `StateFlow`)
- **Внедрение зависимостей (DI):** Koin (`koin-android`, `koin-androidx-compose`) и фабрика `Creator`
- **Тестирование:** JUnit 4 + `kotlinx-coroutines-test`

---

## Структура проекта по спринтам

1. **Спринт 3 (`feature/sprint-03`):**
   - Создана структура проекта `Playlist Maker` (`ru.yandex.practicum.playlistmaker`).
   - Свёрстан главный экран `MainScreen` на Jetpack Compose в соответствии с макетом Figma (шапка на синем фоне `#3772E7`, скруглённый контейнер со списком разделов: «Поиск», «Плейлисты», «Избранное», «Настройки»).
   - Реализована навигация между `MainActivity`, `SearchActivity` и `SettingsActivity` через явные `Intent`.

2. **Спринт 4 (`feature/sprint-04`):**
   - Реализован экран поиска `SearchScreen` с полем ввода `OutlinedTextField`, плейсхолдером, иконкой поиска и кнопкой очистки введённого запроса (`Icons.Default.Clear`), сохранением состояния через `rememberSaveable`.
   - Реализован экран настроек `SettingsScreen` с переключателем тёмной темы и тремя действиями на базе неявных `Intent` и строковых ресурсов (`values/strings.xml`, `values-ru/strings.xml`):
     - «Поделиться приложением» (`Intent.ACTION_SEND`, `text/plain`)
     - «Написать разработчикам» (`Intent.ACTION_SENDTO`, `mailto:`)
     - «Пользовательское соглашение» (`Intent.ACTION_VIEW`, ссылка на оферту Практикума)
   - Реализована единая Compose-навигация в `PlaylistHost` с использованием `NavHost`, `NavHostController` и `enum class Screen`.

3. **Спринт 5 (`feature/sprint-05`):**
   - Выполнено разделение проекта на слои Clean Architecture:
     - `data`: `NetworkClient`, DTO (`BaseResponse`, `TrackDto`, `TracksSearchRequest`, `TracksSearchResponse`), `Storage`, `RetrofitNetworkClient`, `TracksRepositoryImpl`, DI-модули Koin (`repositoryModule`, `viewModelModule`).
     - `domain`: модель `Track`, интерфейсы `TracksRepository` и `TrackSearchInteractor`, реализация `TrackSearchInteractorImpl`.
     - `ui`: `SearchViewModel`, `TrackViewModel`, `SearchState` (`Initial`, `Searching`, `Success`, `Fail`), `SearchScreen`, `TrackListItem`, `DetailsScreen`.
   - Подключён DI-фреймворк Koin в классе `MyApplication`.

4. **Спринт 6 (`feature/sprint-06`):**
   - Завершена интеграция `SearchViewModel` с корутинами (`viewModelScope.launch(Dispatchers.IO)`), индикатором загрузки `CircularProgressIndicator`, отображением найденных треков в `LazyColumn` с `TrackListItem` и разделителями `HorizontalDivider`, обработкой пустого результата и ошибок поиска, а также переходом на экран деталей трека `DetailsScreen`.
   - Подготовлена ветка `review-midterm` для промежуточной проверки проекта по чек-листу спринтов 3–6.

---

## Инструкция по сборке и запуску

### Требования к окружению
- **JDK:** 17 или выше
- **Android SDK:** API 35 (`platforms;android-35`, `build-tools;35.0.0`)
- **Android Studio:** Ladybug (2024.2.1) или новее

### Сборка через терминал
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/Igor3675/yandex-practicum-mobile-development.git
   cd yandex-practicum-mobile-development
   ```
2. Убедитесь, что переменная `ANDROID_HOME` указывает на ваш Android SDK (или укажите `sdk.dir` в файле `local.properties`).
3. Соберите отладочный APK и запустите модульные тесты:
   ```bash
   ./gradlew assembleDebug testDebugUnitTest
   ```
   На Windows (PowerShell / CMD):
   ```powershell
   .\gradlew.bat assembleDebug testDebugUnitTest
   ```
4. Для запуска на подключённом устройстве или эмуляторе выполните:
   ```bash
   ./gradlew installDebug
   ```
   или откройте проект в **Android Studio** и нажмите **Run (`Shift + F10`)**.
