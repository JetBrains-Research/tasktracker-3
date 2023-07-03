[![Gradle Build](https://github.com/JetBrains-Research/tasktracker-3/actions/workflows/build.yml/badge.svg)](https://github.com/JetBrains-Research/tasktracker-3/actions/workflows/build.yml)

# TaskTracker 3.0

The main goal of this project is to create a plugin for intellij-based IDEs 
to be able to collect more rare and usually not accessible data like change logs, 
code quality and other. This will allow us to conduct better studies, produce more precise 
ux-studies and potentially speed up user-testing for experimental features. 


We want to develop a plugin with the following functionality:

- [ ] It will be able to ask small, singular, questions to user in popup window;
- [ ] It will be able to suggest users to participate in two types of studies:
  - [ ] “Questionnaire” study – which will open questionnaire in side window of IDE& Questionnaire 
   in this case could be any web content, for example diary record or some attention experiment.
  - [ ] “Task” study – which will open file inside ide with some task and will record the process of task solution
  - [ ]“Replay” study – which will play a given set of actions in the IDE and users will be asked a few questions about it.
- [ ] It will be able to configure IDE in arbitrary pre-defined way;
- [ ] It will be able to suggest this task based on IDE logs – on given time or after a particular event.
- [ ] Send all resulting data to the remote server.

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
