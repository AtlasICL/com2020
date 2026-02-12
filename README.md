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
│   │   │           ├── gamefiles.java
│   │   │           └── ...
│   │   └── test/
│   │       ├── java/
│   │       │   └── WizardQuest/
│   │       │       ├── unit/
│   │       │       │   └── GameUnitTest.java
│   │       │       └── integration/
│   │       │           └── GameIntegrationTest.java
│   │       └── resources/
│   │           └── testResources.csv (placeholder)
│   └── pom.xml
├── telemetry/
│   ├── core/
│   │   ├── events.py
│   │   ├── logic.py
│   │   ├── parsing.py
│   │   └── export.py
│   ├── gui/
│   │   ├── gui.py
│   │   └── plotting.py
│   ├── auth/
│   │   └── auth.py
│   ├── schema.md (json schema)
│   ├── requirements.txt (for pip)
│   ├── telemetry_app.py
│   └── README.md (python-specific instructions for dev)
├── README.md
├── deployment_guide.md
└── .gitignore
```

## Requirements
The game module requires **Java 17+** and **Maven**.  
The telemetry module require **Python3**, and additional Python libraries, which may be
installed using `python -m pip install -r requirements.txt`.

## Style guide
- Python: [PEP8 style guide](https://peps.python.org/pep-0008/)
- Java: [Google Java style guide](https://peps.python.org/pep-0008/)

## Meeting minutes
Find meeting minutes and project specification [here](/meetings/)

