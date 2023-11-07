# json-parser

[//]: # (sub title about what this code does)
A simple JSON parser written in Java.

[//]: # (sub title about how to use this code)
## How to use
* Attached with code 4 files of the required types; json, yaml, xml, and csv.
* The code defines the 4 test files automatically and parses them.
* The code prints the parsed data in the console.

[//]: # (structure of the code)
## Structure
* The code is divided into 3 main classes:
  * `Main` class: the main class that runs the code. 
    * It also reads the files of all types.
    * Defines the logic for JSON parsing.
    * Defines the logic for YAML parsing.
    * Declares and uses the classes for CSV parsing and YAML parsing.
  * `CSVParse` class: the class that parses the CSV string.
  * `XMLParser` class: the class that parses the XML string.
  * `XMLNode` class: the class that defines the XML node for internal usage only.