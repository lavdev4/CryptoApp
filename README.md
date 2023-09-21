# CryptoApp
📈🏦💳🪙 Курс криптовалют в реальном времени

<details>
  <summary><b>🏞️Скриншоты</b></summary>
    <p align="center">
      <img width="20%" height="20%" src="https://github.com/lavdev4/CryptoApp/assets/103329075/a9d62289-9dbe-404b-89ff-0e5fb88fa7c4">
    </p>
    <p align="center">
      <img width="20%" height="20%" src="https://github.com/lavdev4/CryptoApp/assets/103329075/7f820b93-a555-4e8b-a19a-751c7e87bfb9">
    </p>
    <p align="center">
      <img width="40%" height="40%" src="https://github.com/lavdev4/CryptoApp/assets/103329075/3d893c54-1ef0-465e-b36f-867d44bafbc5">
    </p>
    <p align="center">
      <img width="40%" height="40%" src="https://github.com/lavdev4/CryptoApp/assets/103329075/40cedd67-d4b6-4f63-a308-56e08b0606ce">
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
