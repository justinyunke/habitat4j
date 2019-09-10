# Introduction

Habitat4J intends to simplify configuration management in a multiple
server, multiple application environment.  However, it is designed to be
easy to use -- even in a single application on a single server.

Habitat4J provides property management based on a two component model.  
The first component, called the "Server Identity," provides a system-wide
context for which applications load their properties.  The second
component, called the "Property List," provides an application-wide
property infrastructure that will load entries based on information in the
Server Identity component.

# Habitat4J Features
- XML-based configuration
- Property loading from filesystem or classpath resource
- Ability to set System and Security properties
- Name/value pairs, arrays, hashes, Javabean, Javabean array, and Javabean hash Property Values
- Optional property reloading at runtime - explicitly, on-the-fly, or per time interval
- Optional XSD Validation
- Optional Encoded and/or Encrypted Property Values
- Optional support for log4j

# Uses for Habitat4J include:

- Any environment running multiple applications</li>
- Staging, unit testing, and production server environments</li>
- Server farm environments</li>
