"""
This module contains the core functionality for parsing and validating 
JSON-formatted event logs. 

The telemetry.core.events defines the Event objects which are created 
upon parsing JSON, and relevant Enums.

The telemetry.core.parsing module contains the logic for parsing game 
output json files. This module is responsible for reading the event log
JSON files, and instantiating the ValidEvent object which corresponds 
to the event parsed.
"""