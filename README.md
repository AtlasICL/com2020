# Team Project for COM2020
- [Team members](#team-members)
- [Folder structure](#folder-structure)
- [Requirements](#requirements)
- [Meeting minutes](#meeting-minutes)


## Team members:
| Name              | Email                  |
| ----------------- | ---------------------- |
| Harry Taylor      | ht555@exeter.ac.uk     |
| Luca Pacitti      | ljmp201@exeter.ac.uk   |
| Emre Acarsoy      | ea616@exeter.ac.uk     |
| Tom Croft         | tjc227@exeter.ac.uk    |
| Luca Croci        | lc1107@exeter.ac.uk    |
| Will Finney       | wjf207@exeter.ac.uk    |
| Kazybek Khairulla | kk598@exeter.ac.uk     |

## Folder structure
```
com2020/
├── game/
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   │       └── WizardQuest/
│   │   │           └── game.java
│   │   └── test/
│   │       ├── java/
│   │       │   └── WizardQuest/
│   │       │       ├── unit/
│   │       │       │   └── GameUnitTest.java
│   │       │       ├── integration/
│   │       │       │   └── GameIntegrationTest.java
│   │       │       └── e2e/
│   │       │           └── FullGameRunE2ETest.java
│   │       └── resources/
│   │           └── testResources.csv
│   └── pom.xml
├── telemetry/
│   ├── telemetry_app/
│   │   ├── __init__.py
│   │   ├── events.py
│   │   ├── logic.py
│   │   └── gui.py
│   ├── schema.md
│   ├── requirements.txt
│   └── telemetry_app.py
├── meetings/
│   ├── meeting_minutes.md
│   └── project_specification.pdf
├── README.md
└── .gitignore
```

## Requirements
The game module requires **Java** and **Maven**.  
The telemetry module require **Python3**, and additional Python libraries, which may be
installed using `python -m pip install -r requirements.txt`.

## CI/CD and testing
This project uses GitHub Actions for CI/CD and running automated tests.

## Style guide
- Python: [PEP8 style guide](https://peps.python.org/pep-0008/)
- Java: [Google Java style guide](https://peps.python.org/pep-0008/)

## Meeting minutes
Find meeting minutes and project specification [here](/meetings/)

