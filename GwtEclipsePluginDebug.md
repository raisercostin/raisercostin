# Introduction #
> For easy debugging on the issues in gwt I built it with jars that contains java sources as well.

> I also fixed an issue regarding jsp compiling in gwt if sources are at least java 1.5 by using jakarta-tomcat-5.0.30 dependency instead of jakarta-tomcat-5.0.28 (what is currently to be found on all maven repositories).

# Configure jsp compiler for 1.5 sources #
  1. add to your web.xml file (like is explained here http://firstclassthoughts.co.uk/java/tomcat_5_eclipse_source_1.5.html).
```
<servlet>
    <servlet-name>jsp</servlet-name>
    <servlet-class>org.apache.jasper.servlet.JspServlet</servlet-class>
    <init-param>
        <param-name>compilerSourceVM</param-name>
        <param-value>1.5</param-value>
    </init-param>
    <init-param>
        <param-name>compilerTargetVM</param-name>
        <param-value>1.5</param-value>
    </init-param>
    <load-on-startup>3</load-on-startup>
</servlet>
```
  1. This will not work on jakarta-tomcat-5.0.28 but only after you upgrade GWT to use jakarta-tomcat-5.0.30
> > You can download the GWT compiled with this dependency from http://raisercostin.googlecode.com/files/gwt-windows-1.6.4.raisercostin.zip
  1. Configure the GWT eclipse plugin with this new release
> > Window>Preferences>Google>WebToolkit>Add... and select the unarchived folder with the zip downloaded previously.