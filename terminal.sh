adb devices: Lists all connected physical devices and running emulators.
adb connect <IP_Address>:5555: Connects to a device wirelessly over a Wi-Fi network.
adb pair <IP_Address>:<Port>: Pairs a device for wireless debugging (Android 11+).

./gradlew assembleDebug: Generates a quick, unsigned APK for local testing.
./gradlew assembleRelease: Compiles a release-ready APK (requires configuring a signing key).
./gradlew bundleRelease

adb install path/to/app.apk: Installs the specified APK file onto a connected device.
adb install -r path/to/app.apk: Reinstalls or updates an existing app while preserving its cached user data.
adb install -t path/to/app.apk


adb logcat | grep com.your.package.name