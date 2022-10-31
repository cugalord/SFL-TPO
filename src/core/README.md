# SFL-Core

## 1. Description

The module SFL-Core contains the implementation of the lowest level
core functionality of the SFL project. It contains classes that are 
used to fetch data from configs, connecting to the database and a 
public facing API that contains the functions used in other sections 
of the project.

## 2. Structure

The module structure is as follows:
- The `ConfigLoader` directory contains the implementation of fetching data 
  and storing it into a DBData object:
  - The `ConfigLoader.java` file contains the implementation of the 
    config loader.
  - The `DBData.java` file contains the POD (plain-old-data) implementation
    of the database url parameters, which are filled by the ConfigLoader.
- The `DBCore` directory contains the implementation of data connectivity and
  the public facing API:
  - The `DBCore.java` file contains the backend implementation of a database
    connection manager.
  - The `DBAPI.java` file contains the public facing API of a database 
    manipulation tool.
- The `config.xml` file contains the database url parameters, such as ip, 
  port and schema name.