# INSTALL

## debug vs release

If you switch build type from "debug" to "release", you also need to update the Appstore ContentProvider
in `app/src/main/AndroidManifest.xml`.
  * debug: `<uses-permission android:name="ai.elimu.appstore.debug.provider.READ" />`
  * release: `<uses-permission android:name="ai.elimu.appstore.provider.READ" />`
