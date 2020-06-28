**_Notes:_**
 1.  Video recording can also be enabled when enable.fullscreen.mode is true/false
 2. Check video record play in client network
 3. Sequential execution for video recording
 4. below plugin can be un commented to use explicit java ReportGenerator class to generate master cucumber report.
 
 
  <!--	<plugin>
  						<groupId>org.codehaus.mojo</groupId>
  						<artifactId>exec-maven-plugin</artifactId>
  						<version>1.6.0</version>
  						<executions>
  							<execution>
  								<id>ReportGenerator</id>
  								<phase>test</phase>
  								<goals>
  									<goal>java</goal>
  								</goals>
  								<configuration>
  									<classpathScope>test</classpathScope>
  									<mainClass>ReportGenerator</mainClass>
  									<arguments>
  										<argument>${project.build.directory}/cucumber-report.json</argument>
  									</arguments>
  								</configuration>
  							</execution>
  						</executions>
  					</plugin>-->
 