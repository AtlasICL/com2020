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
│   ├── telemetry_app.py (telemetry entry point)
│   └── README.md (python-specific instructions for dev)
├── meetings/
│   └── ...
├── report/
│   └── ...
├── README.md
├── simulation_events.json
├── telemetry_events.json
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

