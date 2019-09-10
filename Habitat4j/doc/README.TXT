--------------------------------------------------------------------------
Document: README.TXT
Version:  $Id: README.TXT,v 1.16 2005/09/03 22:48:19 cvs Exp $
Author:   Justin Yunke <habitat4j@productivity.org>
URL:      http://www.productivity.org/projects/habitat4j/
--------------------------------------------------------------------------
I.  HABITAT4J INTRODUCTION
--------------------------------------------------------------------------
Habitat4J is an XML-based property file management system for Java.
It relies on a two component model:

- ServerIdentity Component a.k.a. XML-based "ServerIdentity File"

  This file defines a server-wide set of parameters that all
  applications residing on that server will utilize to load
  a subset of properties in the PropertyList (see below).
  
- PropertyList Component(s) a.k.a. XML-based "PropertyList File(s)"

  This file contains Property values that are organized by context.
  Contexts will be loaded depending on the ServerIdentity.

--------------------------------------------------------------------------
II.  HABITAT4J FEATURES
--------------------------------------------------------------------------
Habitat4J provides the following for your application:

- The following Property types are supported:

  + Standard Name/Value Pair String Properties
  + String Array Properties (list of Strings associated with a property
    name)
  + String Hash Properties (hash of Strings associated with a property
    name, with entries that can be looked up by an additional key)
  + Javabean Properties (reuse your own or create new)
  + Javabean Array Properties (list of Javabeans associated with a
    property name)
  + Javabean Hash Properties (hash of Javabeans associated with a
    proeprty name, with entries that can be looked up by an additional
    key)
  + System Properties
  + Security Properties

- The option is available to decode and/or decrypt values (in any of the
  above Property types).  Decoder and Decryptor classes are not
  provided by Habitat4J, but an interface is given to write your own
  implementations that utilize popular or proprietary encoding and
  encryption mechanisms.  One sample Decoder and one sample Decryptor
  class is provided with the Habitat4J JUnit tests.

- The option is available to have all ServerIdentity and PropertyList files
  validated against XSD schemas.

- The option to safely reload property files based on changing timestamps,
  either by explicitly telling Habitat4J to check for a new timestamp,
  or automatically on-the-fly, or after an interval of time has passed.
  
Habitat4J's code includes:
  
- Straight-forward SAX-based parsing implementation.

- Support for System.out/System.err and Log4J-based logging.

- An abstract class implementation so your application can customize
  Habitat4J.
  
- The Habitat4JBootstrapServlet, available in the Habitat4JWeb
  distribution, which provides an implementation for initializing
  or "boostrapping" Habitat4J for a J2EE web application.
  
--------------------------------------------------------------------------
III.  HABITAT4J DISTRIBUTION
--------------------------------------------------------------------------
The Habitat4J distribution consists of the following:

  habitat4j-x.y.z.jar -- the binary and source distribution of Habitat4J
  habitat4j-x.y.z-bin.jar -- the binary-only distribution of Habitat4J
  habitat4j-x.y.z-src.jar -- the source-only distribution of Habitat4J
  habitat4j-x.y.z-doc.jar -- the documentation distribution of Habitat4J
  habitat4j-x.y.z-junit.jar -- the source and binary distribution of
    Habitat4J JUnit tests

  habitat4jweb-x.y.z.jar -- the binary distribution of Habitat4JWeb
  habitat4jweb-x.y.z-src.jar -- the source distribution of Habitat4JWeb
  habitat4jweb-x.y.z-src.jar -- the source distribution of Habitat4JWeb

--------------------------------------------------------------------------
IV.  HABITAT4J REQUIREMENTS
--------------------------------------------------------------------------
The habitat4j implementations depend on the following 3rd-party JARs:

  xerces XML API (commonly called xml-apis.jar)
  xerces Implementation (commonly called xercesImpl.jar)
  SAX Implementation (typically included with xerces)
  log4j (optional)

In addition, the habitat4j-x.y.z-junit.jar also depends on the following
3rd-party JAR:

  junit
  
The habitat4jweb-x.y.z.jar included with the Habitat4J distributions
depends on an implementation of javax.servlet.*, which is typically
included in a Servlet or J2EE container implementation.
  
--------------------------------------------------------------------------
V.  HABITAT4J SPECIAL THANKS
--------------------------------------------------------------------------
Special thanks to:

	Spyhouse -- http://twincities.citysearch.com/profile/34708756
--------------------------------------------------------------------------
End of Habitat4J README
--------------------------------------------------------------------------
jpy
