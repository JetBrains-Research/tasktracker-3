# Server

The server is written on the Ktor framework and uses a PostgreSQL database for data storage and processing. The server
also saves the data received from the plugin as directories in the specified path (customization of the path for saving
will be available later).

For a more convenient server setup for your needs, we provide the option to create a Docker container. To do this, you
can use the image named `daniilkarol/koala:latest`. You can also find a [docker compose](../docker-compose.yml) file that already provides a template to start a docker
container.

The server requires a certain number of environment variables, as follows:
- `HOST` - host address ([0.0.0.0]() by default)
- `PORT` - app port ([8080]() by default)
- `DB_URL` - postgres database url in format `postgresql://[user[:password]@][netloc][:port][/dbname]`
- `DB_USERNAME` - username for the database
- `DB_PASSWORD` - password for the database



