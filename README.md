# Team Project for COM2020
- [Team members](#team-members)
- [Folder structure](#folder-structure)
- [Requirements](#requirements)
- [Meeting minutes](#meeting-minutes)
- [Jira](#jira)


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
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   └── java/
│       │       └── gameName/
│       │           └── gameFile.java
│       └── test/
│           └── java/
│               ├── gameName/
│               │   └── gameTestFile.java
│               └── resources/
│                   └── gameTestResources.csv
├── tel/
│   ├── telemetry.py
│   └── requirements.txt
├── meetings/
│   └── meeting_info
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
- Python: PEP8 style guide ([link](https://peps.python.org/pep-0008/))
- Java: Google Java style guide ([link](https://peps.python.org/pep-0008/))

## Meeting minutes 
Find meeting minutes and project specification [here](/meetings/)

## Jira
[Link to Jira Kanban board](https://comm2020-thespecialcharacters.atlassian.net/jira/software/projects/COMM2020/boards/2)
