# InlineDateRangeField Add-on for Vaadin 8

InlineDateRangeField is a UI component add-on for Vaadin 8 that provides the possibility to select a range of dates. The add-on is based on InlineDateField. 

The add-on actually contains two date range fields: DataRangeField and InlineDateRangeField. DateRangeField is mostly intended for internal use. InlineDateRangeField has a proper API suitable for data binding with Binder. This field should typically be used in applications.  


## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/daterangefield

## Building and running demo

git clone <url of the InlineDateRangeField repository>
mvn clean install
cd demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- JRebel Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine. 

### Debugging server-side

If you have not already compiled the widgetset, do it now by running vaadin:install Maven target for daterangefield-root project.

If you have a JRebel license, it makes on the fly code changes faster. Just add JRebel nature to your daterangefield-demo project by clicking project with right mouse button and choosing JRebel > Add JRebel Nature

To debug project and make code modifications on the fly in the server-side, right-click the daterangefield-demo project and choose Debug As > Debug on Server. Navigate to http://localhost:8080/daterangefield-demo/ to see the application.

### Debugging client-side

Debugging client side code in the daterangefield-demo project:
  - run "mvn vaadin:run-codeserver" on a separate console while the application is running
  - activate Super Dev Mode in the debug window of the application or by adding ?superdevmode to the URL
  - You can access Java-sources and set breakpoints inside Chrome if you enable source maps from inspector settings.
 
## Release notes

### Version 0.0.1-SNAPSHOT
- ...
- ...


## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

DateRangeField is written by Pontus Boström, pontus@vaadin.com

# Developer Guide

