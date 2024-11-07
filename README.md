# EcoPaca-Analyzer

This is a Kotlin Multiplatform project targeting Android, iOS, and Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.  
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.  
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app, `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

---

## Story

This project involves the creation of an IoT device based on an **ESP32** that will monitor the conditions of **biodigester bales**, such as **temperature** and **humidity**, and also track their **location** using a **GPS module**. The device will send the collected data to a cloud database via **Firebase**, allowing users to receive **alerts** and view the information in a **mobile app** developed in **Kotlin Multiplatform**.

The system is designed to be an integrated monitoring tool, ensuring that the bales remain in optimal conditions and providing the ability to track their location when necessary, such as during transport operations or batch tracking.

## Mobile App Functionality

The mobile app provides an intuitive interface to visualize and monitor the data generated by the biodigester bales. The app integrates with Firebase to offer real-time access to critical information such as:

- **Temperature** and **Humidity**: Monitor the environmental conditions of each bale.
- **Location Tracking**: View the GPS location of bales during transport or storage.
- **Real-Time Alerts**: Receive notifications in case of abnormal conditions, ensuring timely intervention.
- **Data History**: Access historical data for analysis and to track the conditions of the bales over time.

With this functionality, users can ensure that the bales remain in optimal conditions and track their status and location, improving the management and maintenance of the biodigesters.

---

## Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
