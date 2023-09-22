# CryptoApp
📈🏦💳🪙 Курс криптовалют в реальном времени

<details>
  <summary><b>🏞️Скриншоты</b></summary>
    <p align="center">
      <img width="30%" height="30%" src="https://github.com/lavdev4/CryptoApp/assets/103329075/c26eadba-e8fc-4e7a-8fde-89cdecd08d44">
    </p>
    <p align="center">
      <img width="30%" height="30%" src="https://github.com/lavdev4/CryptoApp/assets/103329075/df509cda-d92c-4123-b329-d6db299ae913">
    </p>
    <p align="center">
      <img width="60%" height="60%" src="https://github.com/lavdev4/CryptoApp/assets/103329075/9b0a2580-f49c-4caf-8d02-da5ac54d6a08">
    </p>
    <p align="center">
      <img width="60%" height="60%" src="https://github.com/lavdev4/CryptoApp/assets/103329075/6011f349-9465-4ca8-849a-0ba1a58f1d4d">
    </p>
</details>

## Описание: 
В приложении реализована постраничная загрузка из сети с периодическим обновлением данных на загруженных страницах и кэшированем в базу данных. При обновлении данных загружаются и обновляются только те страницы, которые уже были загружены. Постраничная загрузка реализована при помощи Paging 3, а регулярное обновление данных выполняет Work Manager. 
Для отображения данных реализованы два экрана: экран со списком криптовалют и экран с подробной информацией о выбранной криптовалюте. На экране, содержащем подробную информацию, обновление информации происходит так же, как на экране со списком валют, в реальном времени. 
**Работа выполнена в рамках Clean Architecture и с применением Di.**

## Особенности:
- Постраничная загрузка
- Регулярное обновление данных
- Кэширование
- Анимация переходов между экранами
- Clean architecture

## Использованные инструменты:
Dagger 2, Room, Retrofit 2, Picasso, Paging 3, Work Manager, Android Transitions, Kotlin Flow
