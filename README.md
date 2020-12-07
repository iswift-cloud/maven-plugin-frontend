# maven-plugin-frontend

## maven-plugin-frontend

maven-plugin-frontend is maven frontend build plugin. It's run nodejs project install & build script and copy builded resources into jar.


## usage

```
<plugin>
    <groupId>cloud.iswift.framework</groupId>
    <artifactId>maven-plugin-frontend</artifactId>
    <version>1.0.0</version>
    <configuration>
        <!--frontend source dir; default is ${basedir} -->
        <sourceDir>${basedir}/src/main/frontend</sourceDir>
        <!--frontend dist dir; default is ${basedir}/dist -->
        <distDir>${basedir}/src/main/frontend/dist</distDir>
        <!--frontend install command; default is yarn install -->
        <installCommand>yarn install</installCommand>
        <!--frontend build command; default is yarn build -->
        <buildCommand>yarn build</buildCommand>
        <!-- frontend resources path in jar; default is static -->
        <target>static</target>
    </configuration>
    <executions>
        <execution>
            <id>frontend</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>frontend</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```