# News application for Android OS
REST application based on Clean Architecture to fetch and display data about the latest news articles.

The application supports:
* saving an article to bookmarks and managing current bookmarks;
* setting the country and article category;
* search articles by keywords or phrase;
* opening an article in a browser;
* sending a link to an article via messengers.
  
The **_full_** version of the application also supports displaying article details with actual image loading.

<p float="middle">
  <kbd> <img src="https://github.com/alexadler9/news-fetcher/assets/56451293/ff03e6e0-6a53-45e5-8e63-d7723d9bd3d4" height="320" /> </kbd>
  <kbd> <img src="https://github.com/alexadler9/news-fetcher/assets/56451293/5fba6367-075d-444d-a1a2-07b2f13799ab" height="200" /> </kbd>
  <kbd> <img src="https://github.com/alexadler9/news-fetcher/assets/56451293/61324a4f-025e-4ff6-af32-2463f8ecb2d9" height="200" /> </kbd>
  <kbd> <img src="https://github.com/alexadler9/news-fetcher/assets/56451293/65676885-976a-48e0-b886-3dc4bf5bee2d" height="200" /> </kbd>
  <kbd> <img src="https://github.com/alexadler9/news-fetcher/assets/56451293/65a44083-61d6-463d-85c7-a1f520116fbb" height="200" /> </kbd>
  <kbd> <img src="https://github.com/alexadler9/news-fetcher/assets/56451293/2671ae2c-a3dc-4b1e-8fa0-546cd12aeaf1" height="200" /> </kbd>
</p>

## Tech stack and open-source libraries
* **_Kotlin_** based, **_Coroutines_** for asynchronous (including channels and flows).
* Architecture:
  - **_MVI_** architecture (Model-View-Intent);
  - **_Repository_** pattern.
* **_Dagger_** & **_Hilt_** - for dependency injection.
* **_Jetpack_** libraries:
  - _Navigation with Safe Args plugin_ - navigation and data transfer between fragments;
  - _Paging_ for gradually data loading within RecyclerView;
  - _LiveData_ - notify domain layer data to views;
  - _ViewModel_ - UI related data holder, lifecycle aware;
  - _ViewBinding_ - simplified interaction with views;
  - _RecyclerView_ - view for providing a limited window into a data set.
* **_SharedPreferences_** - to store user preferences.
* **_Room_** - to store user bookmarks.
* **_ProductFlavor_** feature - to support multiple versions of the application:
  - _full_ - full application version with all available features;
  - _demo_ - limited application version: the feature of viewing article details is not available.
* **_Retrofit2_** & **_OkHttp3_** - construct the REST APIs and paging network data.
* **_Gson_** - JSON representation.
* **_Glide_** - loading images.
* Testing:
  - **_JUnit5_** & **_Mockito_** for unit tests;
  - **_Espresso_** for UI tests.
