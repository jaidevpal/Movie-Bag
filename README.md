# movie-bag

This project is completely based on a public API.

System is design on _MVVM architecture_, which clearly result on smooth performance.

There are mainly four Activity screens placed inside UI package:
1. Main activity(Launcher)
2. Movie Profile
3. Movie Reviews
4. Movie Casts

UI is mainly focused on easy to understand layouts and options which results in better user experience.

Datas are fetched from the API link in form of JSON and converted in GSON format, which is later Stored by the respected data model classes as per the requirement and passed on further to the activities in form of user readable format in the layout.

All the Data models are placed inside Data Model Directory, and API related networking files such as Retrofit client class and interface are placed inside the API directory.
As Retrofit Client and Interface are the main network class, the contain the API link and Key.
API_Interface file helps us to reach the API directories as per requirement.
All the activity classes are not placed in a separate directories.

I have created an click Interface class which helps to take user's click position from Recycler_Adapter classes of rsepected activity, and passes it to the onItemClick mehod from the Click Interface class, that help helps to perform click based action right from the activity class itself.

All the Images are shown using Picasso library.
The dotIndicators below all the image viewers are also integrated using a 3rd Party library of tommybuonomo/dotsindicator (Main Reason behind using this library is due to it's cool animated dot movments).

Whole User Interface design of the system is totally based on Google's own Material Component.

