[![Gradle Build](https://github.com/JetBrains-Research/tasktracker-3/actions/workflows/build.yml/badge.svg)](https://github.com/JetBrains-Research/tasktracker-3/actions/workflows/build.yml)

# TaskTracker 3.0

The main goal of this project is to create a plugin for intellij-based IDEs 
to be able to collect more rare and usually not accessible data like change logs, 
code quality and other. This will allow us to conduct better studies, produce more precise 
ux-studies and potentially speed up user-testing for experimental features. 



The plugin is capable of collecting the following data:
- Snapshots of all code changes.
- All activities that occurred in the IDE. More details about activity types you can find [here](ij-plugin/src/main/kotlin/org/jetbrains/research/tasktracker/tracking/activity/ActivityEvent.kt).
- Switching between file windows.
- Switching between tool and IDE plugin windows.
- Survey responses.
- Third-party logs/files specified in the [configuration](ij-plugin/src/main/kotlin/org/jetbrains/research/tasktracker/config/content/PluginInfoConfig.kt) by this [structure](ij-plugin/src/main/kotlin/org/jetbrains/research/tasktracker/config/content/Log.kt).


The plugin works in conjunction with the [server](ij-server), which is located in the same repository. The server receives, processes, and saves the data that was sent from the plugin side.

The settings of the plugin configuration and interaction with its server are stored [here](ij-plugin/src/main/resources/properties/actual). If this directory does not exist, it will be created with default properties and values.

Here we want to notice that the plugin will not collect any of the user data outside given tasks.

The previous versions of the plugin:

- The first version - [repository](https://github.com/JetBrains-Research/task-tracker-plugin)
- The second version - TODO: add a link

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

You can use the run IDE plugin configuration: [configuration file](./.run/Run%20IDE%20with%20Plugin.run.xml).
