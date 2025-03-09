# elimu.ai Launcher 🚀

Custom Android launcher providing a clear instructional path (based on EGRA/EGMA skills). The applications are fetched from the [Appstore](https://github.com/elimu-ai/appstore)'s [Content Provider](https://github.com/elimu-ai/appstore/tree/master/app/src/main/java/ai/elimu/appstore/provider).

<kbd>![screenshot_20170619-205117](https://user-images.githubusercontent.com/15718174/27299219-ecf36596-552b-11e7-9fe9-09e5ca29d655.png)</kbd>

<kbd>![screenshot_20170625-145536](https://user-images.githubusercontent.com/15718174/27515871-41872c60-59ae-11e7-9b2d-3ca886d0d7f2.png)</kbd>

<kbd>![scr_20170619_211129_512](https://user-images.githubusercontent.com/15718174/27299402-95bea44c-552c-11e7-84ab-217cdca758e4.gif)</kbd>

See demo at https://www.youtube.com/watch?v=lLinDWyL564

## Pedagogy

(Also see https://github.com/elimu-ai/wiki/blob/master/PEDAGOGY.md)

> [!NOTE]
> Each app/game on the platform is categorized by which literacy/numeracy skill it teaches, and each skill (and its collection of apps) is gradually unlocked:

### Early Grade Reading Assessment (EGRA)

EGRA subtasks (represented by the _green_ spaceships in the launcher):

![egra_correlations](https://user-images.githubusercontent.com/15718174/27515885-74e0ca62-59ae-11e7-83c1-7ef12c0851ce.png)

### Early Grade Mathematics Assessment (EGMA)

EGMA subtasks (represented by the _blue_ spaceships in the launcher):

![egma_correlations](https://user-images.githubusercontent.com/15718174/27515894-91bfd4f2-59ae-11e7-9d87-5b03117c206f.png)

## Installation

> [!IMPORTANT]
> Note: This app depends on the [elimu.ai Appstore](https://github.com/elimu-ai/appstore) to be installed.

## Development 👩🏽‍💻

To install and run the launcher on an Android device during development, use the following commands:

    ./gradlew clean build
    adb install app/build/outputs/apk/debug/ai.elimu.launcher-<versionCode>-debug.apk
    adb shell am start -n ai.elimu.launcher.debug/ai.elimu.launcher.MainActivity

### Gradle Upgrade

```
./gradlew wrapper --gradle-version x.x.x
```

## Software Architecture

See https://github.com/elimu-ai/wiki/blob/master/SOFTWARE_ARCHITECTURE.md

---

<p align="center">
  <img src="https://github.com/elimu-ai/webapp/blob/main/src/main/webapp/static/img/logo-text-256x78.png" />
</p>
<p align="center">
  elimu.ai - Free open-source learning software for out-of-school children ✨🚀
</p>
<p align="center">
  <a href="https://elimu.ai">Website 🌐</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/elimu-ai/wiki#readme">Wiki 📃</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/orgs/elimu-ai/projects?query=is%3Aopen">Projects 👩🏽‍💻</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/elimu-ai/wiki/milestones">Milestones 🎯</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/elimu-ai/wiki#open-source-community">Community 👋🏽</a>
  &nbsp;•&nbsp;
  <a href="https://www.drips.network/app/drip-lists/41305178594442616889778610143373288091511468151140966646158126636698">Support 💜</a>
</p>
