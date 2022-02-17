## PayPlaza ECR app 2.0

Version 2.0.0

The ECR application handles the merchant UI, QR-payments, and transaction result user guidance. The application is designated for Sunmi Android platform, but will work on other Android device and in the Android Emulator as well.

# Release notes

Release 2.0.0 includes a basic implementation of the [integration SDK](https://github.com/cmdotcom/android-pos-integration-sdk-kotlin) as an example.

# Getting started

## Hardware devices

* This app is designed and tested for Sunmi devices.
* This app has been tested in Sunmi P2 lite and Sunmi P2 Pro.

## Prerequisites

* Payplaza [Terminal](https://payplaza.com/terminals/) application must be installed in the device. Version 1.1.1 or higher.
* Android 7 (api 25) or higher.
* The device you'll use must have installed 'SunmiPayHardwareService'. Version v3.3.133 or higher.

## Installation

* Check if Java 8 (for Android Studio) and Java 11 (for Gradle) are installed. It can be done through **Android Studio**.

* Then, save your global credentials (optional):

  ```bash
  git config --global user.email "<your_user_name_here>@<your_domain>.com"
  git config --global user.password "<your_password_here>"
  ```

* Clone this repository and import into **Android Studio**

  ````bash
  git clone https://github.com/cmdotcom/sunmi-ecr-example-kotlin.git
  ````

## Development notes

* This application is implemented in [kotlin ](https://kotlinlang.org/)language (provided by **Android Studio**).
* Is a standard **Android Studio** project.
* The version of Android Studio used at the time of writing this is **Android Studio Artic Fox | 2020.3.1 Patch 4** 64 bits version
* *Android 7.1.1 (Nougat) SDK 25* must be installed in **Android Studio**
* The gradle version used for build the project is **7.0.2** 64 bits version

## Build notes

* Is up to development team configure build types. Debug build is available by default. The proper version of the [integration SDK](https://github.com/cmdotcom/android-pos-integration-sdk-kotlin) must be imported as a gradle dependency:

  * Repository to import dependencies:

    ````bash
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            maven { url 'https://jitpack.io' }
        }
    }
    ````
  
  * Debug:
  
    ````bash
    dependencies {
        // CM|PayPlaza Android POS Integration library
        implementation 'com.github.cmdotcom.android-pos-integration-sdk-kotlin:androidposintegrationsdk-debug:<version-tag>'
    }
    ````
  
  * Production:
  
    ````bash
    dependencies {
        // CM|PayPlaza Android POS Integration library
        implementation 'com.github.cmdotcom.android-pos-integration-sdk-kotlin:androidposintegrationsdk:<version-tag>'
    }
    ````


# Features

* Transactions.

  * Payment. Just select the desired amount and tap on check in the bottom-right of the numeric keypad. Follow the instructions to present the card and check the receipt.

  * Refund. Fill the previous from with refund password, operation STAN, amount to refund (never higher than the refunded payment) and select the date when the payment was done. Follow the instructions to present the card and check the receipt.

    ***Note**: STAN is provided in the receipt of the payment.

* Last receipt. Shows the receipt of the last operation.

* Day totals. Shows the operations done during the current day.

* Recovery process. Shows the operation statuses. It allows check the status of the last operation so user can decide if a refund is necessary or whatever action is needed.

  ***Note**: you can access each feature from the menu but recovery process. It is launched when Terminal application crashes or there is a pending receipt to be shown. For more details please check [SDK integration guide](https://github.com/cmdotcom/android-pos-integration-sdk-kotlin).
