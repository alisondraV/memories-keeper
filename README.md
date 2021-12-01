# Memories Keeper

## Firebase

### Auth

Make sure to add the SHA-1 fingerprint to the Firebase console: 
- Windows - `keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android`
- Linux/MacOS - `keytool -list -v \ -alias androiddebugkey -keystore ~/.android/debug.keystore`