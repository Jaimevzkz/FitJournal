# FitJournal
Fit Journal is an application that serves as a digital gym journal. In this app, users can create their own workout routines by searching for exercises through a remote service and adding them to workouts with personalized names. Users can also track their progress in the gym by uploading images that will be stored in the cloud using Firebase Storage. In the Home tab, users can view a calendar displaying the days they have worked out. User management, including authentication and user data, is handled through the Google Firebase service.

# Requirements Catalog (Features)
-  Create profile: In the application, users can create profiles that will be stored in a cloud database, and it will be possible to log in on any device with the application.
- Modify profile: Users can add and modify various profile data to enhance their personalized experience in the application.
- Profile picture: Allows uploading a profile picture that will be saved in the cloud.
- Create a workout: Allows users to create a workout to which exercises will be added.
- Select parameters for each exercise: Enables adjusting parameters for each exercise in the routine, such as the number of sets, weights, etc. These data can be updated as needed.
- Search for an exercise: Enables searching for a specific exercise by entering its keyword. The application will consume data from an external API to return search results.
- Save an exercise: Once an exercise is found, it can be saved into a workout.
- Calendar: Allows users to view a calendar with the days on which they have worked out.
- Progress: Allows users to upload a photo of themselves as a reference for future tracking.

# Architecture
- **Clean architecture**: In order to have a structured, escalable and encapsulated project.
- **MVI pseudo-architecture**: Model-View-Intent has been used to structure the UI layer (MVI is been selected over MVVM due to design decisions (extensively explained in **_Memoria_FitJournal.pdf_** file in the root of the repository))
# Tech stack
Listed below are all the technologies used for the development of the project:
- [Kotlin](https://kotlinlang.org/): The programming language chosen for the development of the project due to the modern syntax and wide variety of features.
- [Jetpack Compose](https://developer.android.com/jetpack/compose): The new modern toolkit recommended by google. Jetpack compose makes coding a lot easier, its more intuitive and powerful.
- [Dagger Hilt](https://dagger.dev/hilt/): For dependency injection, enabling cleaner and more escalable code.
- Firebase
	- [Authentication](https://firebase.google.com/docs/auth): For user login.
	- [Firestore](https://firebase.google.com/docs/firestore): Used as a NoSQL database.
	- [Storage](https://firebase.google.com/docs/storage): Used to store profile and progress photos in an easy way.
- [Jetpack Data store preferences](https://developer.android.com/jetpack/androidx/releases/datastore): Used for simple data persistence.
- [Room](https://developer.android.com/training/data-storage/room): Used for saving user data locally.
- Community libraries:
	- [Compose Destinations](https://github.com/raamcosta/compose-destinations): A powerful add on developed  by the community for easier and safer navigation in Jetpack Compose.
	- [Calendar](https://github.com/kizitonwose/Calendar): Used as a calendar inside the app.
- [Coil](https://coil-kt.github.io/coil/compose/): Used for asynchronous photo loading.
- [Retrofit](https://square.github.io/retrofit/): Used for API rest.