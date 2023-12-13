### Android приложение для поиска вакансий на популярной площадке hh.ru

#### Стек: MVVM + Clean Architecture | Kotlin, Coroutines, LiveData, Koin, Databinding, Navigation components, Room, Dagger2, Glide, Retrofit2, GSON, SQLite

### [Скачать "Talent Trove"](https://github.com/menta1/practicum-android-diploma/releases/download/pet_proj/Talent.Trove.apk)

####Приложение умеет 

#### Screenshots:
<img src="https://github.com/menta1/practicum-android-diploma/assets/81776684/3332b947-bccc-4422-b625-df5b61d7a96c" width="250">
<img src="https://github.com/menta1/practicum-android-diploma/assets/81776684/cdaa2434-d777-4a24-9866-8479df9b050e" width="250">
<img src="https://github.com/menta1/practicum-android-diploma/assets/81776684/2399586c-0494-43f7-9196-9c1299bc39bc" width="250">
<img src="https://github.com/menta1/practicum-android-diploma/assets/81776684/c84c8b50-e018-4b4b-a31d-91ad74f4992a" width="250">
<img src="https://github.com/menta1/practicum-android-diploma/assets/81776684/13621e79-2adc-4d47-93d7-0104a380167d" width="250">
<img src="https://github.com/menta1/practicum-android-diploma/assets/81776684/36b50b84-707d-498b-a86b-a56b6171bce3" width="250">

Приложение поддерживает устройства, начиная с Android 8.0 (minSdkVersion = 26)

Приложение предоставляет следующую функциональность:

- Поиск вакансий;
- Указание фильтров для поиска;
- Просмотр деталей отдельной вакансии;
- И добавление вакансий в список "Избранного".

**Главный экран - экран поиска вакансий**

На этом экране пользователь может искать вакансии по любому непустому набору слов поискового запроса. Результаты поиска представляют собой список, содержащий краткую информацию о вакансиях.

**Особенности экрана**** :**

- По умолчанию, поиск происходит по всей доступной базе вакансий без учёта региона, отрасли компании и уровня зарплаты и валюты.
- Приложение не хранит историю поиска, поэтому между перезапусками приложения текст в поле ввода не обязан сохраняться.
- При вводе нового текста в поле ввода мы осуществляем новый поиск с debounce в 2000 миллисекунд.
- В отдельном элементе списка может быть картинка логотипа компании, которую нужно дополнительно загрузить. В процессе загрузки картинки и в случае ошибки загрузки этой картинки мы показываем плейсхолдер. Также плейсхолдер отображается, если информации о картинке нет.
- В зависимости от пришедших с сервера данных информация о вакансии может отображаться несколькими способами:
  - "От XX";
  - "До XX";
  - "От XX до XX";
  - "Зарплата не указана".
- Если в вакансии указана зарплата, то числа должны отображаться с разбиением на разряды (то есть 1 000 000, 12 345 678).
- Зарплата в вакансии может быть указана в разной валюте, не только в рублях. Вот полный список возможных валют:
  - Российский рубль (RUR / RUB)
  - Белорусский рубль (BYR)
  - Доллар (USD)
  - Евро (EUR)
  - Казахстанский тенге (KZT)
  - Украинская гривна (UAH)
  - Азербайджанский манат (AZN)
  - Узбекский сум (UZS)
  - Грузинский лари (GEL)
  - Киргизский сом (KGT)
- (\*) В целях экономии трафика пользователей загрузка результатов поиска должна происходить постранично (paging) по 20 элементов за раз. Запрос на следующую страницу должен происходить, когда пользователь доскроллил до последнего доступного элемента списка (или чуть раньше).

**Фильтрация - набор экранов фильтров поиска**

Используя настройки фильтра, пользователь может уточнить некоторые параметры поиска, который осуществляется на экране "Поиск". Фильтр позволяет указать:

- Место работы - регион, населённый пункт, указанный в вакансии как рабочая локация.
- Отрасль - сфера деятельности организации, разместившей вакансию.
- Уровень зарплаты - уровень ЗП, соответствующий указанному в вакансии.
- Возможность скрывать вакансии, для которых не указана ЗП.

**Особенности экранов**** :**

- Параметры фильтра не являются обязательными - пользователь может уточнить любой параметр из предложенных, а может не указывать ничего. В случае, если указан хотя бы один из параметров он должен учитываться при последующих поисковых запросах на экране "Поиск". Параметры фильтра, которые пользователь не уточнял, в поисковом запросе участвовать не должны.
- Настройки параметров фильтра должны сохраняться даже после закрытия приложения.
- Поиск по отраслям компании ведётся сразу по всем элементам дерева отраслей, без разделения на категории по уровням вложенности.
- Экраны фильтрации отображаются поверх нижней навигации.
- Если у пользователя выбрана страна поиска вакансий, то список регионов на экране выбора региона поиска ограничивается регионами указанной страны.
- Если пользователь выбрал город до выбора страны, то страна должна подставляться автоматически.
- Кнопка "Сбросить" появляется, если пользователь указал хотя бы одно значение фильтров.
- Кнопка "Применить" появляется, если пользователь указал фильтр, отличающийся от предыдущего. Нажатие на кнопку "Применить" приводит к сохранению выбранных настроек фильтра и применению фильтра для всех последующих запросов на поиск вакансий до изменения фильтра.
- Все настройки фильтра сохраняются автоматически сразу после изменения.

**Экран просмотра деталей вакансии**

Нажав на элемент списка найденных вакансий (а так же в списке закладок и похожих вакансий), пользователь попадает на экран с подробным описанием вакансии. Помимо уровня ЗП, требуемого опыта и графика работы пользователь может на этом экране увидеть:

- Информацию о работодателе
- Подробное описание вакансии
- Перечень требуемых ключевых навыков
- Контактную информацию

Также пользователь может ознакомиться со списком похожих вакансий, поделиться ссылкой на данную вакансию, а также связаться с работодателем через указанные контакты.

**Особенности экрана**** :**

- Любая часть описания деталей вакансии опциональна, то есть из сети может не прийти какое-то из ожидаемых полей. В этом случае программа должна корректно работать и отображать те данные, которые у неё есть.
- Подробное описание вакансии приходит в HTML-формате, напрямую отобразить полученное по сети поле не получится.
- При нажатии на элемент списка похожих вакансий требуется открыть новый экран деталей вакансий, который связан с предыдущим (на предыдущий экран можно вернуться, нажав кнопку Back).
- Отображение указанной зарплаты и валюты должно происходить аналогично выдаче поиска вакансий.
- При нажатии на указанный адрес электронной почты должен открываться диалог с предложением написать email на этот адрес.
- При нажатии на указанный номер телефона приложение должно открыть приложение для звонка, в котором уже будет отображён номер телефона.
- Отображение списка похожих вакансий должно происходить аналогично отображению списка вакансий на экране поиска.
- Экраны деталей вакансии отображаются поверх нижней навигации.

**Экран избранных вакансий**

Пользователь может добавлять вакансии в "Избранное", чтобы иметь возможность быстро вернуться к заинтересовавшему его предложению. Добавить вакансию в "избранное" (или удалить из "избранного") можно на экране "Вакансия". На экране списка избранных вакансий пользователь может удалить вакансию из закладок. Все вакансии, добавленные в закладки, можно увидеть на отдельном экране в приложении.

**Особенности экрана**** :**

- Вакансии, добавленные в "избранное" можно просматривать без подключения к интернету. Если нет интернета, вместо логотипа компании можно показывать плейсхолдер и не отображать список похожих вакансий.
- Если пользователь добавляет вакансию в закладки, она должна сразу появиться на экране списка закладок.
- (\*) Список избранных вакансий может содержать довольно много вакансий, поэтому для ускорения загрузки экрана можно добавить пагинацию для выдачи избранных вакансий.

**Экран информации о команде разработчиков**

На экране отображается статический список людей, участвовавших в разработке приложения.
