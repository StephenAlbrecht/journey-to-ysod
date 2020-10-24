# Journey to Ysod

This is a Sci-fi re-skin of Pixel Dungeon, with new quests, weapons, sprites, and more.

## Building the Project
### Software Prerequisites
* JetBrains IntelliJ IDEA (optional, but recommended)
* Java 8 (newer versions may not work with the Android sdkmanager)
* Android Studio (for Android SDK). Make sure to install it so the SDK exists on your machine.

### Build Instructions
1. Accept Android SDK licenses
   1. Navigate to the `sdkmanager` (e.g. `~/Android/Sdk/tools/bin/sdkmanager`)
   2. Run `yes | ./sdkmanager --licenses`
2. Create `local.properties` the project root directory with the following contents:
    ```
    sdk.dir=<Path to Android SDK, e.g. "../../Android/Sdk">
    ```
3. Open the project with IntelliJ by opening the `build.gradle` file as a Project. All the dependencies should download automatically.

### Running the Project
To run the game on desktop, run the class `com.watabou.pixeldungeon.desktop.DesktopLauncher` in the `desktop/src/` directory

Saved games will be located at:

| Platform | Directory
|:---------|:---------
| Windows  | `<user home>/Saved Games/`
| Linux    | `~/.watabou/pixel-dungeon/`
| Mac      | `Library/Application Support/Pixel Dungeon/`

