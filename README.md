# movie-bag
This project is completely based on a public API.

System is design on MVVM architecture, which clearly result on smooth performance.

There are mainly four Activity screens:
1. Main activity(Launcher)
2. Movie Profile
3. Movie Reviews
4. Movie Casts

UI is mainly focused on easy to understand layouts and options which results in better user experience.

Datas are fetched from the API link in form of JSON and converted in GSON format, which is later Stored by the respected data model classes as per the requirement and passed on further to the activities in form of user readable format in the layout.

All the Data models are placed inside Data Model Directory, and API related networking files such as Retrofit client class and interface are placed inside the API directory.
