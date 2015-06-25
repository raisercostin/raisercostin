Utility classes, JSP tags, Servlets, and extensions to different external API.

# Introduction #
DebuggingFilter is a servlet filter that add a "debug.jspInfo" attribute to the current request to be used in jsp pages, for debugging purpose.
# Samples #
See how the [debug info will look like](http://raisercostin.googlecode.com/files/jspinfo.jsp.html).
# Configuration #
## Configure web.xml ##
> Add this to your `WEB-INF/web.xml` file:
```
    <filter>
        <filter-name>debugging</filter-name>
        <filter-class>ro.raisercostin.web.DebuggingFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>debugging</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```
## Configure web.xml ##
> Add this to desired jsp file :
```
    ${debug.jspInfo}
```
## Configure logging ##
  1. To log the **request parametes** add in log4j.properties file :
```
    log4j.logger.ro.raisercostin.web.DebuggingFilter.PARAMETERS=debug
```
  1. To log the **request and header parameters** add in log4j.properties file :
```
    log4j.logger.ro.raisercostin.web.DebuggingFilter.REQUEST=debug
```
  1. To log all **environment parameters** add in log4j.properties file :
```
    log4j.logger.ro.raisercostin.web.DebuggingFilter.ALL=debug
```
## Configure maven dependencies ##
  1. Add this to your `pom.xml` file, inside `/project/dependencies/` :
```
        <dependency>
            <groupId>ro.raisercostin</groupId>
            <artifactId>raisercostin-jsp</artifactId>
            <version>1.0</version>
        </dependency>
```
  1. Add this to your `pom.xml` file, inside `/project/repositories/` :
```
        <repository>
            <id>raisercostin</id>
            <name>raisercostin</name>
            <url>http://raisercostin.googlecode.com/svn/maven2</url>
        </repository>
```