[![Gradle Build](https://github.com/JetBrains-Research/tasktracker-3/actions/workflows/build.yml/badge.svg)](https://github.com/JetBrains-Research/tasktracker-3/actions/workflows/build.yml)

# TaskTracker (Knowledge Observation and Learning Analytics)

**TaskTracker** is a powerful tool designed to run controlled experiments and collect data directly within JetBrains IDEs. Its primary advantage lies in its full customization: with flexible configuration files, **TaskTracker** can be adapted to suit any experimental scenario.

The tool's functionality extends beyond basic data collection. By gathering detailed and often hard-to-access information—such as change logs, code quality metrics, and user interaction patterns — **TaskTracker** empowers researchers to conduct deeper, more precise UX studies and behavioral analyses.

This level of insight opens up opportunities to:

- Improve experimental studies by providing richer, more actionable data.
- Streamline user testing for experimental features.
- Facilitate faster iteration and decision-making in UX and product development.
- **TaskTracker** is built with adaptability in mind, making it an essential resource for teams looking to conduct studies or improve development workflows in JetBrains IDEs.

Here we want to notice that the plugin and the server would not collect any of the user data outside given tasks.

The [plugin](ij-plugin) works in conjunction with the [server](ij-server), which is located in the same repository. The server receives, processes, and saves the data that was sent from the plugin side.

The previous versions of the tool:

- The first version - [repository](https://github.com/JetBrains-Research/task-tracker-plugin)
- The second version - [repository](https://github.com/samorojy/task-tracker-plugin/tree/revival)
