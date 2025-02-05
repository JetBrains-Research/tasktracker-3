# Plugin

The plugin is capable of collecting the following data:
- Snapshots of all code changes.
- All activities that occurred in the IDE. More details about activity types you can find [here](src/main/kotlin/org/jetbrains/research/tasktracker/tracking/activity/ActivityEvent.kt).
- Switching between file windows.
- Switching between tool and IDE plugin windows.
- Survey responses.
- Third-party logs/files specified in the [configuration](src/main/kotlin/org/jetbrains/research/tasktracker/config/content/PluginInfoConfig.kt) by this [structure](src/main/kotlin/org/jetbrains/research/tasktracker/config/content/Log.kt).


The settings of the plugin configuration and interaction with its server are stored [here](src/main/resources/properties/actual). If this directory does not exist, it will be created with default properties and values.


## Getting started


### Developer mode

Just clone the repository and build the project:

```text
./gradlew build
```

Next, to run the plugin, run the `runIde` intellij task:

```text
./gradlew runIde
```

You can use the run IDE plugin configuration: [configuration file](../.run/Run%20IDE%20with%20Plugin.run.xml).
