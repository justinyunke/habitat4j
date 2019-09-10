# 2019-09-10 [1.0.0] jpy
- Moved to Github productivityorg/habitat4j with Maven build setup.

# 200?-??-?? [1.0.0rc19] jpy
- No idea when this version was released.  It's been a while. :)

# 2007-03-09 [1.0.0rc18] jpy
- Corrected a bug in PropertyList.isPropertyABean() to avoid a Null-
  PointerException when globalPropertyBeanDefinitions are defined but
  there are no propertyBeanDefinitions.  Method now checks both
  Hashtables.
  
# 2007-02-25 [1.0.0rc17] jpy
- Viva Habitat4J Release Candidate 17!  :)
- Added add/remove/clearReloadablePropertyList(s) methods to
  AbstractPropertyListManager and PropertyListManager to control what
  Property Lists are allowed to be reloaded.  If none are specified, all
  Property Lists are allowed to be reloaded.  (The global features for
  reloading must be turned on for any of these to work.)
- Added "fileSystemPropertyListReloadPropertyLists" property-array to
  ApplicationPropertyListLoader (used by Habitat4JBootstrap), which
  uses the addReloadablePropertyList method in PropertyListManager to
  specify which Property Lists are allowed to be reloaded.
- Added ApplicationG JUnit test cases to test Habitat4JBootstrap and
  the above changes.
- After a reload occurs, reload handlers not defined in the 
  <definitions> section that are available in the "old"
  PropertyList will be transferred to the "new" PropertyList.  This can
  be disabled with the "featurePropertyListReloadDisableHandlerTransfer"
  feature.
- Added getReloadEventHandler() and hasReloadEventHandler() methods to
  PropertyList.
  
# 2006-08-16 [1.0.0rc16] jpy
- Viva Habitat4J Release Candidate 16!  :)
- Added "instanceOS" fixed parameter to ServerIdentity, which contains
  one of three values:  "windows", "unix", or "other".  The information
  is obtained via the InstanceOsHelper, which uses the System
  properties "os.name" value to detect the operating system under which
  the JVM is running.
  
# 2006-07-08 [1.0.0rc15] jpy
- Viva Habitat4J Release Candidate 15!  :)
- Modified Habitat4JBootstrapServletContextListener to work with
  JDK 1.3- by putting a toString() on a rethrown Exception.
- Fixed issue with showing encoded and/or encrypted values with
  PropertyList.toString() when using Property Bean Hashes.  Also wasn't
  passing in a null name parameter to the DecryptorIF implementations,
  and this has been fixed.
- Added support for serialization-generated clones of Property Bean
  objects as an alternative to the standard reflection-based mechanism.
  Added an attribute for <definition><property-bean ... copyMethod="x">
  where "x" = "reflect" or "serialize", which allows users of
  the convenient getPropertyBeanCopy(), getPropertyBeanArrayCopy(), and
  getPropertyBeanHashCopy() methods to select whether reflection or
  serialization should be used to produce the clone.  The "serialize" 
  option can only be used to copy Objects that are fully serializable,
  i.e. all components and superclasses must also be fully serializable.
  If this attribute is left out, reflection is used by default.
  The property-list.xsd (and alternate property-list) schemas have been
  modified with the new copyMethod attribute.
  
# 2006-03-15 [1.0.0rc14] jpy
- Viva Habitat4J Release Candidate 14!  :)
- Refactored PropertyList.toString() to use multi-line output for
  large property definitions, such as those with many array or hash
  entries.
- Refactored System and Security properties to not log an "error"
  when setting a property value matching what's already set.

# 2006-02-14 [1.0.0rc13] jpy
- Viva Habitat4J Release Candidate 13!  :)
- Added two wildcard capabilities to context parameters:
  <context parameter="*"> .. </context>
    This context will match if any value is provided for that
    context definition.
  <context parameter="!value"> .. </context>
    This context will match if any value does *not* match for
    that context definition.
- Added PROPERTY_LIST_FEATURE_SUPPRESS_PROPERTY_BEAN_COPY_WARNINGS
  feature to suppress WARN logging that occurs when PropertyBeans
  cannot be copied entirely.

# 2006-01-25 [1.0.0rc12] jpy
- Viva Habitat4J Release Candidate 12!  :)
- In AbstractBaseHandler, added secondary attempt to instantiate the
  XMLReader if XMLReaderFactory.createXMLReader(..) throws an
  Exception.  Might come in handy when JDK 1.4- or JAXB blocks
  Xerces.
- Added "pragma" to definitions section in PropertyList.  Can be used
  to "switch" quickly between multiple configs.  PropertyList pragma
  definitions are checked first, then ServerIdentity pragma
  definitions.  The PROPERTY_LIST_FEATURE_SERVER_IDENTITY_PRAGMA_OVERRIDE
  feature needs to be set to allow PropertyList pragma definitions
  to override ServerIdentity pragma definitions.

# 2005-11-15 [1.0.0rc11] jpy
- Viva Habitat4J Release Candidate 11!  :)
- Bug Fix:  The <property-array> element had issues with mixed
  contexts.  Fix involved checking for "current context" more often.
- Added logging for the name of a classPath resource when an exception
  occurs.
- Added createServerIdentityPath() static helper method to
  Habitat4JBootstrap, which creates a semicolon-delimited path for
  server-identity files that may appear in different locations on
  different systems.

# 2005-10-06 [1.0.0rc10] jpy
- Viva Habitat4J Release Candidate 10!  :)
- Bug Fix:  The <context> "prefix" attribute was handled incorrectly.
  Anyone using relatively complex <context> sections using (a) the
  "prefix" attribute and (b) contextual attributes ("environment," etc.)
  should upgrade to 1.0.0rc10 immediately.
- Improved the PropertyBean copying code to use any existing "hasField"
  methods (no parameters and a boolean or Boolean return value) to decide
  whether a field should be copied.  If the bean does not have a
  "has" method corresponding to the "get"/"set" methods, the field will be
  copied by default.  Useful if you wish to avoid copying a field within a
  PropertyBean when using the *Copy property-retrieval methods.
  
# 2005-10-05 [1.0.0rc9] jpy
- Viva Habitat4J Release Candidate 9!  :)
- Bug Fix:  Replaced use of StringBuffer.indexOf(..) introduced in
  1.0.0rc5.  All previous versions since 1.0.0rc5 (inclusive) will not
  work in a J2SE 1.3.x environment, and this is corrected in 1.0.0rc9.
  Habitat4J experienced this problem in 0.9.13, when the version of Eclipse
  used at the time "caught" this problem with the 1.3 compatibility mode.
  Apparently, newer versions of Eclipse do not register the use of
  an .indexOf(..) call as a violation of 1.3 compatibility mode.
  This version of Habitat4J has been certified to work within
  both Sun JSE 1.3.1_16 and Sun J2SE 1.4.2_04 JRE environments.
  Future versions will be certified in both 1.3.x and 1.4.x before
  public release.  The compiler was modified to use the 1.3 classfile
  binary specification, as well.  If anyone needs to use Habitat4J in
  an environment prior to 1.3.1_16, the source may need to be built
  by the user.
- Removed a log entry that occurred if a PropertyBean's class was not
  found, reporting that the interface class "Object" was not implemented.
  This is related to removing the PropertyBeanIF class in 1.0.0rc8,
  and is only aesthetic.
  
# 2005-09-20 [1.0.0rc8] jpy
- Viva Habitat4J Release Candidate 8!  :)
- -- MAJOR CHANGE FOR PROPERTYBEAN USERS --
  After some consideration and a brief discussion with some users of
  Habitat4J, the PropertyBeanIF Java Interface has been removed from
  Habitat4J.  Now, any Object that implements public getter/setter
  methods and the default constructor (public w/no parameters)
  is usable as a PropertyBean in Habitat4J.  Deprecating the
  PropertyBeanIF Java Interface was considered, but the decision was
  made to remove it completely.
- Found several documentation references to PropertyBeanIF and changed
  accordingly.
- Modified PropertyList.toString() method so that properties are returned
  in sorted order.  PropertyHash and PropertyBeanHash properties now
  have their elements sorted by their keys, as well.  [Suggestion and
  code example provided by an anonymous Habitat4J user -- thanks!]

# 2005-09-13 [1.0.0rc7] jpy
- Viva Habitat4J Release Candidate 7!  :)
- Improved *Copy code for PropertyBeanIF implementations.  Added these
  three feature configurations, defaults shown in parentheses:
    PROPERTY_LIST_FEATURE_PROPERTY_BEAN_STRING_OBJECT_COPY (false)
    PROPERTY_LIST_FEATURE_PROPERTY_BEAN_WRAPPER_OBJECT_COPY (false)
    PROPERTY_LIST_FEATURE_PROPERTY_BEAN_CLONEABLE_OBJECT_COPY (true)
  Under most situations, it shouldn't be necessary to clone String
  objects and primitive wrapper Objects, but the above features turn on
  that behavior.  Objects in PropertyBeanIF implementations that 
  are instanceof Cloneable will have a reflection-based clone()
  method invocation performed by default.  These changes should
  improve the performance of 1.0.0rc6 and result in less extraneous
  Objects left to the gc().  Added JUnit test data to verify Cloneable
  Objects will indeed clone() properly.
- Cleaned up some textual inconsistencies in Habitat4JFeatures.  May
  affect code if the static constant references weren't used in an
  application that uses Habitat4J.
- Cleaned up some comments.

# 2005-09-10 [1.0.0rc6] jpy
- Viva Habitat4J Release Candidate 6!  :)
- Starting to find obscure bugs relating to Object manipulation within
  Habitat4J, as shown below:
- Bug fix:  PropertyHash and PropertyBeanHash now handle the
  instance attribute values for "replace" and "revise" correctly
  when dealing with their Hash values.  Previous to this release,
  revised PropertyHash values generated the WARN log entry that the
  revised item would be skipped because it was already defined.
  Revised PropertyBeanHash entries would correctly NOT replace the
  entire Hash, but would result in each revised bean being completely
  replaced -- even if only a single bean field was modified.  With this
  release, the instance "revise" value for PropertyHash and
  PropertyBeanHash means both the entire Hash *and* its elements are
  able to be revised.  A WARN log entry is now only generated if, in
  "replace" mode, the same Hash key is listed more than once within
  a single <property-hash> or <property-bean-hash> element.
- Bug fix:  PropertyBeanIF, DecoderIF, and DecryptorIF interfaces
  were improperly handled if (a) any of these interfaces were extended
  before being used by a Class, or (b) the interfaces were implemented
  in a SuperClass.  Before rc6, the developer would have to make sure
  an "implements" for one of the Habitat4J interfaces occurs at the
  lowest subclass level.  Modified the SampleCommProtocolBean,
  EveryParameterTypeBean, and SampleLDAPBean JUnit test implementations
  to test this fix.
- Bug fix:  Previous to rc6, the PropertyBeanIF copying logic would
  not create copies (clones) of the String, BigInteger, BigDecimal,
  and numeric wrapper classes.
- Enhanced the PropertyBeanIF copying logic to use the "clone()" method
  (via Reflection) of Objects implementing the Cloneable Java Interface.
  It's important to note that the PropertyBeanIF copying logic is limited
  and is really only meant to be used for PropertyBeanIF implementations
  (a) containing supported Habitat4J types and (b) conforming to the
  "get" or "is" getter method prefixes and "set" setter method prefix.
  If PropertyBeanIF implementations that have other application logic
  in them are used and copied using Habitat4J, results may vary.  It's
  probably best to keep PropertyBeanIF implementations as simple as
  the application allows.
- Improved logging relating to PropertyBeanIF copying logic.

# 2005-09-06 [1.0.0rc5] jpy
- Viva Habitat4J Release Candidate 5!  :)
- Bug fix:  PropertyArray's instance attribute had no effect prior
  to this version.  JUnit test added to verify.
- Bug fix:  PropertyList.getPropertyBeanArray(..) now handles empty
  bean arrays correctly by throwing the PropertyNotAvailableException.
  Only affects those using their own custom extensions of
  AbstractPropertyListManager.
- Bug fix:  The alternate XSD schemas in
  org.productivity.java.habitat4j.common.xsd had missing tags.
- Bug fix:  The *Copy method for PropertyBeanIF implementations
  were not handling boolean "is"-style getters appropriately.
  JUnit tests enhanced to test the copies of all supported PropertyBeanIF
  implementation types.
- Added support for BigInteger and BigDecimal types within a
  PropertyBeanIF implementation.
- Added Habitat4J Version log message in ServerIdentityManager upon
  initialize(..).
- Introduced PropertyHash and PropertyBeanHash property types.
- Added support for "prepend" to complement the "append" instance
  flag for PropertyArray and PropertyBeanArray property types.
- Ensured PropertyHash and PropertyBeanHash property types support
  encoding and encryption, and that the PropertyList.toString() method
  does not display encoded/encrypted data.
- Introduced the ability to substitute ServerIdentity pragma definitions
  into PropertyList field values.  This is done by setting a pragma
  like "foo" to "bar," and then referring to it anywhere in a PropertyList
  value via "${pragma:foo}" (without the quotes).  The entire variable,
  from the "$" to the closing "}" (inclusive) will be replaced with "bar."
  Multiple variables can be substituted within a single value.  If a
  pragma definition is not available, an empty space is substituted.
- Minor clean-ups, including moving some hardcoded statics into
  Habitat4JConstants and some JavaDoc clean-up.

# 2005-08-25 [1.0.0rc4] jpy
- Viva Habitat4J Release Candidate 4!  :)
- Added support to Habitat4JBootstrap via ApplicationPropertyListLoader
  to load files placed into a heirarchy, so properties can be organized
  in a directory structure.  For instance, if the basePath specified by
  ApplicationPropertyListResourceLocation is "foo" and the array item
  in the properties-bootstrap.xml is "bar/file" then the file
  "foo/bar/properties-file.xml" will be loaded.  Works for both file
  and class resources.
- Added getPropertyBeanCopy and getPropertyBeanArrayCopy to
  PropertyListManager, AbstractPropertyListManager, and PropertyList.
  This allows an application to utilize and alter copies (clones) of
  PropertyBean instances without affecting the original instances.
- Added JUnit tests to validate the *Copy methods above.
- Added logging support to PropertyList.
- Fixed a bad manifest entry in the habitat4jweb jar provided in
  Habitat4J 1.0.0rc3. Cleaned up other manifest entries.
- Minor documentation and logging fixes.

# 2005-03-29 [1.0.0rc3] jpy
- Viva Habitat4J Release Candidate 3!  :)
- BUG FIX - DecoderIF/DecryptorIF - now calls initialize() method
  for both modes specified by alwaysNewInstance.  Prior to this fix,
  only the "true" mode called initialize().  Modified the initialize()
  method in DecoderIF and DecryptorIF, adding a parameter for
  propertyListName.
- Added Habitat4JBootstrapServletContextListener as another mechanism for
  application bootstrap.  This is only supported in the Web Deployment
  Descriptor version 2.3 and higher, which relates to a J2EE version
  of 1.3.  For earlier versions of J2EE, it's still best to bootstrap
  with the Habitat4JBootstrapServlet or manually with Habitat4JBootstrap.
- As of this release, Habitat4J's context matching is case-INsensitive.
  To set a more strict mode of operation, the Habitat4JFeature
  PROPERTY_LIST_FEATURE_CASE_SENSITIVE_CONTEXT_MATCHING can be set to
  a "true" value.
- Repaired some errant displaying of encrypted/encoded values that
  was introduced when the PropertyNotAvailableException capability was
  added.
- Added support to PropertyList for viewing PropertyBean values with
  PropertyListManager.toDisplayString() when a method returning a
  boolean value is named "isXX()" instead of "getXX()."
- Turned on more of the Java compiler error/warnings in Eclipse and
  removed some extraneous code based on its recommendations.

# 2005-03-24 [1.0.0rc2] jpy
- Viva Habitat4J Release Candidate 2!  :)
- Modified Habitat4JBootstrap so it can be passed a Map of bootstrap
  parameters.  Modified Habitat4JBootstrapServlet to simply wrap the new
  functionality in Habitat4JBootstrap.
- Modified getBasePath() in ApplicationPropertyListResourceLocation
  PropertyBean implementation to back-out the "null means empty" change
  made in 1.0.0rc1.  Modified ApplicationPropertyListLoader to properly
  handle base paths when they are defined as null.
- Modified setBasePath() in ApplicationPropertyListResourceLocation to
  remove any trailing slashes (but still allow a "root" path).
- Fixed small typo in AbstractServerIdentityManager that caused an
  Exception if "mode" is given a null value.

# 2005-03-23 [1.0.0rc1] jpy
- Viva Habitat4J Release Candidate 1!  :)
- Added ServerIdentityManager.setPragmaDefinition(..) in order to be
  able to tweak pragma definitions during a bootstrap process.  Also
  modified AbstractServerIdentityManager and ServerIdentity classes to
  implement this.
- Added Habitat4JBootstrap.initializeServerIdentity(..) methods in order
  to support bootstrapping the Server-Identity file as a separate step.
- Added Habitat4JBootstrapServlet.doBootstrapInitializeServerIdentity(),
  .doBootstrapInitializePropertyLists() and .doBootstrapInitialize()
  methods for ease in subclassing the Habitat4JBootstrapServlet to do
  things like tweak ServerIdentity pragma definitions.
- Modified getPrefix(), getSuffix(), and getBasePath() in
  ApplicationPropertyListResourceLocation PropertyBean implementation so
  that they return "" if the protected value is null.
- Implemented PropertyListReloadInhibitor, which provides the ability to
  globally turn off/on all automatic/manual reloading of PropertyList
  files.  It is normally called via the
  PropertyListManager.inhibitReload() and .uninhibitReload() methods.
  A long value (in milliseconds) can optionally be provided to
  .inhibitReload(long) in order to specify an "expiration" for the
  reload inhibition as a fail-safe in case the .uninhibitReload() never
  gets called by the application due to a RunTimeException or other
  unexpected issues.
- Changed "enum" variable to "enumeration" based on recommendation
  by Eclipse.
- Added serialVersionUID private static final long variables to all
  Serializable implementations based on recommendation by Eclipse.

# 2004-11-16 [0.9.28] jpy
- Added ServerIdentityFileNotFoundException.
- Added check to ensure ServerIdentity cannot be loaded more than once
  unless the SERVER_IDENTITY_FEATURE_CAN_BE_RELOADED Habitat4JFeature
  is set to a boolean true value.
- Added reset() method to ServerIdentity.

# 2004-11-11 [0.9.27] jpy
- Added Bootstrap initializer classes:  Habitat4JBootstrap and
  the J2EE HttpServlet wrapper Habitat4JBootstrapServlet.  Also
  added Habitat4JBootstrapException.
- Added ApplicationPropertyListLoader, used by Habitat4JBootstrap.
- Added ApplicationPropertyListReloadConfiguration and
  ApplicationPropertyListResourceLocation implementations of
  PropertyBeanIF, which are both used by ApplicationPropertyListLoader.
- Moved habitat4j-examples.html file into the doc/Habitat4J directory
  (one level up).
- Added habitat4j-bootstrap.html file to the doc/Habitat4J directory,
  which provides plain-ol'-documentation for use of Habitat4JBootstrap
  and Habitat4JBootstrapServlet.
- Added habitat4j-compat.html file to the doc/Habitat4J directory,
  which provides plain-ol'-documentation for use of the XML Schema
  Compatibility code, which allows Habitat4J to adapt to foreign
  XML "property file" schemas.
- Refactored compat.XMLMap to compat.AbstractXMLMap.
- Removed "log4j.jar" out of dependency list in Manifest.MF for
  Habitat4J.
- Fixed path in doc\Habitat4J\util\sivalidate.bat ServerIdentity XML
  file validator and removed the dependency on log4j.

# 2004-10-03 [0.9.26] jpy
- Began supplemental project "Habitat4JWeb," which provides utility
  classes to use in a J2EE application.  The binary and source
  implementations are provided separately to avoid classloader
  issues for those using Habitat4J outside of a web container.
- Cleaned up some logging relating to the reloading code.
- Corrected problem with Reload-on-the-Fly and classpath-based
  Properties.  Important ONLY if you use Reload-on-the-Fly.
- The server-identity.xml file can now be provided as a semicolon-
  delimited list.  The first one in the list found to exist on the
  filesystem will be the one used.  Code uses File.exists().
- Added removePropertyList and renamePropertyList methods to
  AbstractPropertyListManager and PropertyListManager.
- Added ApplicationEonProductionTestCase to validate thread-safe behavior
  of Habitat4J.
- Fixed clear() method to ServerIdentityRole().
- Added "global" definitions for PropertyBean, Decoder, Decryptor,
  and ReloadEventHandler so developers can programmatically define these
  definitions in code vs. separately in each Property-List XML file.
- Development started on compatibility code for adapting Habitat4J to
  pre-existing XML schemas.
- Added ApplicationFonDev1TestCase to validate compatibility code.
- Added DecoderException and DecryptorException, and modified 
  DecoderIF and DecryptorIF to throw these exceptions for better error
  handling. If you've used these interfaces, take note that they've
  changed considerably.
- Added PropertyNotAvailableException, and modified PropertyList and
  AbstractPropertyListManager to support it.  PropertyListManager
  also modified, yet continues to throw away all exceptions and return
  default values for all properties.

# 2004-08-26 [0.9.25] jpy
- IMPORTANT:  Reload code repaired.  Removed race condition caused when
  reload feature is used in a multi-threaded application, and removed a
  NullPointer condition.
- Added convenient PropertyListManager.setFeature(String,int) method,
  handy for setting the reload interval.

# 2004-08-20 [0.9.24] jpy
- IMPORTANT: Habitat4J no longer requires log4j, and by default logs
  to System.out/System.err.  To switch to log4j, use the selectLog4j()
  method of the Habitat4JLogger Singleton class.  Similarly, to switch
  back to System.out/System.err mode, use the selectSystem() method.

# 2004-08-18 [0.9.23] jpy
- IMPORTANT: Fixed issue in PropertyListHandler for improper handling of
  null String properties (empty elements) that resulted in ERROR logging.
- Added PGP signatures for all files, and added PGP Public Key to
  docs/pgp directory.
- Added information about habitat4j-announce and habitat4j-support
  mailing lists in FAQ.
- Added "yes" as an additional synonym for "true" for boolean properties,
  and "no" for "false."
- Added getPropertyAsInt(..) and isPropertyTrue(..) to PropertyList,
  AbstractPropertyListManager (_ prefix), and PropertyListManager.
- Added JUnit tests to verify above.
- Added extra logging for reflection errors.

# 2004-08-07 [0.9.22] jpy
- Added loadPropertyList() method to AbstractPropertyListManager /
  PropertyListManager which checks to see if the given path argument
  is recognized by the ClassLoader and uses the
  loadPropertyListFromResource method().  If not, it assumes it's a file
  and uses the loadPropertyListFromFile() method.
- Modified JUnit tests to verify loadPropertyList() method.
- Made all "public" methods in AbstractPropertyListManager and
  AbstractServerIdentityManager "protected" since they will never be
  accessed outside of a wrapper class.

# 2004-08-06 [0.9.21] jpy
- Fixed ServerIdentity.toString() issues with NullPointerException.
- Cleaned up some JavaDoc.
- Added version and modifiedBy attributes to server-identity element.
- Added ability to specify name of a PropertyList in the XML file in
  the property-list element.
- Modified JUnit tests to cover two additions above.

# 2004-07-27 [0.9.20] jpy
- Cleaned up ServerIdentityManager to match conventions in
  PropertyListManager.
- Added convenient accessor methods in ServerIdentityManager to get to
  data in ServerIdentity object.
- Added JUnit tests to validate that ServerIdentity XML files are being
  read accurately.
- Added ServerIdentityValidator utility class (with main method) to be
  used for validating ServerIdentity files outside of applications.
- Modified ServerIdentity to only create new Hashtable() instances
  when needed.

# 2004-07-22 [0.9.19] jpy
- Fixed NullPointerException caused deep in the XML parsing
  code if a PropertyBean's ID is invalid.
- Removed check for a valid currentPropertyBean in the
  PropertyBeanArray handling code, which was listing a bogus errors in
  the logs.
- Modified some debugging text.

# 2004-07-21 [0.9.18] jpy
- Fixed an important problem with context where a context element contains
  an attribute that does NOT appear in the server-identity.xml file.
  Now, any context that has an attribute NOT in the server-identity.xml
  file will be ignored.

# 2004-07-19 [0.9.17] jpy
- Renamed ReloadIF to ReloadEventHandlerIF.
- Renamed XSD definitions element from reload to reload-event-handler,
  and added some use="optional" entries for the version/modifiedBy/
  reloadSerial property-list attributes.
- Added PropertyListManager accessors for getPropertyListVersion(),
  getPropertyListModifiedBy(), and getPropertyListReloadSerial() and
  extended ApplicationB test case to test them.
- Removed all instances of the "default list" constant in
  PropertyListManager -- this class should only be a facade for 
  AbstractPropertyListManager.
- Cleaned up some JavaDoc files.

# 2004-07-18 [0.9.16] jpy
- Added "version," "modifiedBy," and "reloadSerial" attributes to
  the property-list element.
- Added reload block flag in PropertyList to ensure the load can't happen
  twice simultaneously.
- Added preExecute, postExecute, and onFailure methods via the ReloadIF
  definition, so application code can prepare for/validate a Property-List
  before and after a reload occurs or fails.
- Removed Habitat4JFeatures for "Always New Instance."  An attribute now
  specifies whether definition objects are instantiated for every use.
- JUnit TestCases for reloading capability added, but one is disabled in
  Habitat4JApplicationsDETestSuite, since it takes > 120 seconds to
  confirm.
- Added a setFeature() parameter for boolean values.

# 2004-07-15 [0.9.15] jpy
- Modified code that generated a null pointer if XSD validation is turned
  off.
- Modified reload code to reset the 'last loaded' timer when the interval
  has been reached and the file hasn't changed.

# 2004-07-14 [0.9.14] jpy
- Small modifications for JUnit support via build.xml ANT script,
  not yet released.

# 2004-07-13 [0.9.13] jpy
- Changed StringBuffer.indexOf() to a separate implementation to avoid
  compatibility issues with Java versions < 1.4.x
- Removed newer Exception(Throwable) constructor implementations to avoid
  compatibility issues with Java versions < 1.4.x
- Removed newer StringBuffer.append(Stringbuffer) references to avoid
  compatibility issues with Java versions < 1.4.x
- Added ability to log with a certain priority when throwing exceptions.

# 2004-07-05 [0.9.12] jpy
- Renamed ServerRole to ServerIdentityRole to maintain consistency.
- Recoded decoder & decryptor to only instantiate a max of one each,
  with Habitat4JFeature to override.
- Renamed JUnit test cases to be consistent with documentation.
- Cleaned up some error logging/exception handling.

# 2004-07-03 [0.9.11] jpy
- Added more JUnit test cases.
- Rewrote how currentPropertyName is done in AbstractPropertyListManager
- Added PropertyList methods to query whether a property was
  decoded or decrypted at load time.
- Modified PropertyList.toString() to ensure property values that were
  decoded or decrypted at load time are not displayed.
- Added a queueing mechanism to set System and Security properties
  after the load has successfully completed.

# 2004-07-01 [0.9.10] jpy
- Implemented two new initialization modes to make a total of three.
  The default mode (FILE) is to use a server-identity.xml file.  Second
  mode (JVM) loads the Server-Identity object with configuration from
  the JVM's System properties.  The third mode (NULL) loads a blank
  Server-Identity object.

# 2004-06-21 [0.9.9] jpy
- Started contextual JUnit tests
- Fixed bugs in PropertyListHandler.setPropertyListContext() and the
  invoker overridden methods.
- Changed toDisplayString() methods to the standard toString()
- Reorganized test data

# 2004-06-20 [0.9.8] jpy
- Added support for 14 more types of PropertyBean parameters.
- Fixed some erroneous JUnit test data.

# 2004-06-20 [0.9.7] jpy
- Removed HOWTO.TXT, added habitat4j-examples.html
- Reorganized distribution, added habitat4j-x.y.z-junit.jar
 
# 2004-06-19 [0.9.6] jpy
- Minor corrections to property-list.xsd

# 2004-06-19 [0.9.5] jpy
- Added encoder and encryptor support to all value elements
- Renamed encoding to encoder, encrypting to encryptor
- IMPORTANT: Removed hard-coded prefix delimiter - you'll need to
  append your own "." this to your "prefix" attribute if that's the
  delimiter you're using
- Added "package" attribute to <definitions>
- Changed some constantly reused String instances to StringBuffer in
  PropertyListHandler

# 2004-06-19 [0.9.4] jpy
- Added "encodingId" and "encryptionId" attributes to name/value
  properties
- Added encoding and encryption definitions
- Changed <property-bean-definitions> element to generic <definitions>
- Added "instance" attribute for array, property-bean and
  property-bean-array

# 2004-06-15 [0.9.3] jpy
- Added <property-bean-array> element and support for Bean array property
  values

# 2004-06-14 [0.9.2] jpy
- Added <property-bean> element and support for Bean property values

# 2004-06-14 [0.9.1] jpy
- Added <property-array> element and support for String array property
  values
- Cleaned up various Javadoc comments
- Added more test cases to Application A JUnit

# 2004-06-14 [0.9.0] jpy
- Habitat4J is born!
