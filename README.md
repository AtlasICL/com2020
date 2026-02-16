# Team Project for COM2020
- [Team members](#team-members)
- [Style guide](#style-guide)
- [Meeting minutes](#meeting-minutes)
- [Build instructions](#build-instructions)
- [Software Bill of Materials (SBOM)](#software-bill-of-materials-sbom)
  - [Game (Java) dependencies](#game-java-dependencies)
  - [Telemetry App (Python) dependencies](#telemetry-app-python-dependencies)
  - [External dependencies](#external-dependencies)
  - [System dependencies](#system-dependencies)
  - [Dependency Licences](#dependency-licences)
- [Folder structure](#folder-structure)



## Team members
| Name              | Email                  |
| ----------------- | ---------------------- |
| Emre Acarsoy      | ea616@exeter.ac.uk     |
| Luca Croci        | lc1107@exeter.ac.uk    |
| Tom Croft         | tjc227@exeter.ac.uk    |
| Kazybek Khairulla | kk598@exeter.ac.uk     |
| Luca Pacitti      | ljmp201@exeter.ac.uk   |
| Harry Taylor      | ht555@exeter.ac.uk     |
| Will Finney       | wjf207@exeter.ac.uk    |


## Style guide
- Python: [PEP8 style guide](https://peps.python.org/pep-0008/)
- Java: [Google Java style guide](https://peps.python.org/pep-0008/)

## Meeting minutes
Find meeting minutes and project specification [here](/meetings/)

## Build instructions
For the deployment guide, see [this file](deployment_guide.md).

# Software Bill of Materials (SBOM) 

|                        |                       |
| ---------------------- | --------------------- |
| **Project:**           | WizardQuest (com2020) |
| **Version:**           | 0.0.9                 |
| **Licence:**           | MIT                   |
| **SBOM last updated:** | 2026-02-15            |


## Game (Java) dependencies

**Language:** Java 17  
**Build System:** Apache Maven  

| Type    | Dependency              | Group ID                       | Version |
| ------- | ----------------------- | ------------------------------ | ------- |
| Runtime | jackson-databind        | com.fasterxml.jackson.core     | 2.20.0  |
| Runtime | jackson-datatype-jsr310 | com.fasterxml.jackson.datatype | 2.20.0  |
| Testing | junit-jupiter           | org.junit.jupiter              | 5.10.1  |
| Build   | maven-compiler-plugin   | org.apache.maven.plugins       | 3.11.0  |
| Build   | maven-surefire-plugin   | org.apache.maven.plugins       | 3.2.5   |
| Build   | exec-maven-plugin       | org.codehaus.mojo              | 3.3.0   |


## Telemetry App (Python) dependencies

**Language:** Python 3.13  
**Package Manager:** pip

| Type    | Dependency | Version     |
| ------- | ---------- | ----------- |
| Runtime | matplotlib | most recent |
| Runtime | numpy      | most recent |
| Runtime | pandas     | most recent |
| Runtime | seaborn    | most recent |
| Runtime | requests   | most recent |
| Testing | pytest     | most recent |


## External dependencies

| Service          | Provider |
| ---------------- | -------- |
| OAuth 2.0 (OIDC) | Google   |

Requires `OIDC_ISSUER`, `OIDC_CLIENT_ID`, and `OIDC_CLIENT_SECRET` environment variables.


## System dependencies

| Requirement    | Details                   |
| -------------- | ------------------------- |
| Java Runtime   | JDK 17+                   |
| Python Runtime | Python 3.13               |
| Network        | Required for Google OAuth |


# Licensing

## Project Licence

This project is released under an MIT License. 

## Dependency Licences

| Dependency              | Licence         | Link                                                                                  |
| ----------------------- | --------------- | ------------------------------------------------------------------------------------- |
| jackson-databind        | Apache 2.0      | [License here](https://github.com/FasterXML/jackson-databind/blob/3.x/LICENSE)        |
| jackson-datatype-jsr310 | Apache 2.0      | [License here](https://github.com/FasterXML/jackson-datatypes-misc/blob/3.x/LICENSE)  |
| junit-jupiter           | EPL 2.0         | [License here](https://central.sonatype.com/artifact/org.junit.jupiter/junit-jupiter) |
| maven-compiler-plugin   | Apache 2.0      | [License here](https://www.apache.org/licenses/)                                      |
| maven-surefire-plugin   | Apache 2.0      | [License here](https://www.apache.org/licenses/)                                      |
| exec-maven-plugin       | Apache 2.0      | [License here](https://www.apache.org/licenses/)                                      |
| matplotlib              | PSF (BSD-style) | [License here](https://pypi.org/project/matplotlib/)                                  |
| numpy                   | BSD 3-Clause    | [License here](https://pypi.org/project/numpy/)                                       |
| pandas                  | BSD 3-Clause    | [License here](https://pypi.org/project/numpy/)                                       |
| seaborn                 | BSD             | [License here](https://pypi.org/project/seaborn/)                                     |
| requests                | Apache 2.0      | [License here](https://pypi.org/project/requests/)                                    |
| pytest                  | MIT             | [License here](https://pypi.org/project/pytest/)                                      |

All dependency licences are compatible with this project's MIT licence.


# Folder structure
```
com2020/
├── game/
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   │       └── WizardQuest/
│   │   │           └── ...
│   │   └── test/
│   │       ├── java/
│   │       │   └── WizardQuest/
│   │       │       ├── unit/
│   │       │       │   └── ...
│   │       │       └── integration/
│   │       │           └── ...
│   │       └── resources/
│   │           └── ...
│   └── pom.xml
├── telemetry/
│   ├── core/
│   │   └── ...
│   ├── gui/
│   │   └── ...
│   ├── auth/
│   │   └── ...
│   ├── schema.md (json schema)
│   ├── requirements.txt (for pip)
│   ├── telemetry_app.py
│   └── README.md (python-specific instructions for dev)
├── event_logs/
│   ├── telemetry_events.json
│   └── simulation_events.json
├── README.md
├── deployment_guide.md
└── .gitignore
```