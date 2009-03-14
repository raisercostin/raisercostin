@echo off
set CLASSPATH="d:\home\.m2\repository\com\google\gwt\gwt-user\1.5.3\gwt-user-1.5.3.jar";"d:\home\.m2\repository\com\google\gwt\gwt-dev\1.5.3\gwt-dev-1.5.3-windows.jar";"D:\workspace\raisercostin-googlecode-projects\org.raisercostin.maven\gwt\src\main\java";"D:\workspace\raisercostin-googlecode-projects\org.raisercostin.maven\gwt\src\main\resources";"D:\workspace\raisercostin-googlecode-projects\org.raisercostin.maven\gwt\classes";"D:\workspace\raisercostin-googlecode-projects\org.raisercostin.maven\gwt\target\classes";


"d:\system\apps\jdk6\jre\bin\java" -Xmx512m -cp %CLASSPATH%  com.google.gwt.dev.GWTCompiler  -gen "D:\workspace\raisercostin-googlecode-projects\org.raisercostin.maven\gwt\target\.generated" -logLevel INFO -style OBF -out "D:\workspace\raisercostin-googlecode-projects\org.raisercostin.maven\gwt\target\gwt-1.2" org.raisercostin.maven.gwt.Module
