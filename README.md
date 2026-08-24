# HR App — Android клиент

Мобильное приложение для подбора персонала на платформе Android. Соединяет соискателей и работодателей — от просмотра вакансий до общения в чате.

---

## Стек технологий

| Категория | Технология |
|---|---|
| Язык | Kotlin |
| UI | Jetpack Compose |
| Архитектура | Clean Architecture (MVVM) |
| DI | Hilt (Dagger) |
| Сеть | Retrofit + OkHttp |
| Сериализация | Kotlinx Serialization |
| Навигация | Navigation Compose |
| Локальное хранилище | DataStore Preferences |
| Асинхронность | Coroutines + Flow |
| Минимальный SDK | API 24 (Android 7.0) |
| Целевой SDK | API 34 (Android 14) |

---

## Архитектура

Проект построен по принципу Clean Architecture с тремя слоями:

```
app/src/main/java/com/example/hr_app/
├── data/
│   ├── api/              # Retrofit интерфейсы для каждого модуля
│   ├── dto/
│   │   ├── requests/     # DTO исходящих запросов
│   │   └── responses/    # DTO входящих ответов
│   ├── local/            # TokenStorage (DataStore)
│   ├── mappers/          # Конвертация DTO → Domain модели
│   └── repositories/     # Реализации репозиториев
├── domain/
│   ├── models/           # Доменные модели и enum-классы
│   ├── repositories/     # Интерфейсы репозиториев
│   └── usecases/         # Use Cases по модулям
├── presentation/
│   ├── components/       # Переиспользуемые UI компоненты
│   ├── navigation/       # NavGraph и маршруты
│   ├── screens/          # Экраны по модулям
│   └── theme/            # Цвета, типографика, тема
└── di/                   # Hilt модули
```

---

## Функционал

### Соискатель
- Регистрация и вход в аккаунт
- Просмотр списка вакансий с поиском
- Просмотр деталей вакансии
- Создание и редактирование резюме
- Прикрепление PDF файла к резюме
- Отклик на вакансию с выбором резюме
- Отслеживание статусов откликов
- Чат с работодателем после принятия отклика
- Настройки профиля

### Работодатель
- Регистрация и вход в аккаунт
- Создание и редактирование вакансий
- Управление статусами вакансий (открыта/закрыта/черновик)
- Просмотр откликов на вакансии
- Принятие или отклонение кандидатов
- Чат с соискателем после принятия отклика
- Настройки профиля

---

## Требования

- Android Studio Hedgehog или новее
- JDK 21
- Android устройство или эмулятор с API 24+
- Запущенный [backend сервер](https://github.com/Ma-x-1m/hr-app-backend)

---

## Запуск проекта

### Шаг 1 — Клонируй репозиторий

```bash
git clone https://github.com/Ma-x-1m/hr-app-mobile.git
cd hr-app-mobile
```

### Шаг 2 — Открой проект в Android Studio

```
File → Open → выбери папку hr-app-mobile/
```

Дождись окончания синхронизации Gradle.

### Шаг 3 — Настрой адрес сервера

Открой файл `app/src/main/java/com/example/hr_app/di/NetworkModule.kt` и укажи адрес backend сервера:

```kotlin
// Для эмулятора Android
baseUrl = "http://10.0.2.2:8080/"

// Для реального устройства — укажи IP компьютера в локальной сети
baseUrl = "http://192.168.1.XXX:8080/"
```

> Узнать IP компьютера в Windows: выполни `ipconfig` в PowerShell и найди `IPv4-адрес` в разделе Wi-Fi или Ethernet адаптера.

### Шаг 4 — Убедись что backend запущен

Backend должен быть запущен и отвечать по адресу `http://127.0.0.1:8080`. Инструкция по запуску backend находится в [hr-app-backend](https://github.com/Ma-x-1m/hr-app-backend).

### Шаг 5 — Запусти приложение

Подключи устройство или запусти эмулятор, затем нажми **Run** в Android Studio.

---

## Навигация

Приложение использует двухуровневую навигацию:

```
rootNavController          — основная навигация (auth, detail экраны)
└── MainScreen
    └── tabNavController   — навигация по вкладкам BottomBar
```

**BottomBar для соискателя:**
Вакансии → Резюме → Отклики → Чаты → Профиль

**BottomBar для работодателя:**
Вакансии → Чаты → Профиль

---

## Структура экранов

| Экран | Роль | Описание |
|---|---|---|
| LoginScreen | Все | Вход в аккаунт |
| RegisterScreen | Все | Регистрация |
| VacanciesListScreen | Соискатель | Список вакансий |
| VacancyDetailScreen | Соискатель | Детали вакансии + отклик |
| MyResumesScreen | Соискатель | Список резюме |
| ResumeEditScreen | Соискатель | Создание/редактирование резюме |
| MyApplicationsScreen | Соискатель | Мои отклики |
| MyVacanciesScreen | Работодатель | Мои вакансии |
| VacancyEditScreen | Работодатель | Создание/редактирование вакансии |
| VacancyApplicationsScreen | Работодатель | Отклики на вакансию |
| ConversationsScreen | Все | Список чатов |
| ChatScreen | Все | Переписка |
| ProfileScreen | Все | Профиль пользователя |
| SettingsScreen | Все | Настройки |

---

## Особенности реализации

**Авторизация** — JWT токен сохраняется в DataStore и автоматически добавляется к каждому запросу через `AuthInterceptor`.

**Polling чата** — сообщения обновляются каждые 5 секунд через `while(isActive) + delay(5000)` в `viewModelScope`.

**Загрузка PDF** — файл выбирается через `ActivityResultContracts.GetContent()`, конвертируется из Uri в File через `ContentResolver` и отправляется как multipart запрос.

**Автообновление списков** — экраны со списками обновляются при каждом возврате через `repeatOnLifecycle(Lifecycle.State.RESUMED)`.

---

## Возможные проблемы

**Ошибка подключения к серверу:**
- Убедись что backend запущен
- Проверь что устройство и компьютер в одной Wi-Fi сети
- Проверь что IP адрес в `NetworkModule.kt` указан верно
- На Windows добавь правило брандмауэра для порта 8080:
  ```bash
  netsh advfirewall firewall add rule name="Ktor 8080" dir=in action=allow protocol=TCP localport=8080
  ```

**Ошибка сборки Hilt MissingBinding:**
- Убедись что все Use Cases имеют аннотацию `@Inject constructor`

---

## Ссылки

- [Backend сервер (Ktor)](https://github.com/Ma-x-1m/hr-app-backend)
- [Документация API](https://github.com/Ma-x-1m/hr-app-backend/blob/main/docs/api_documentation.md)
