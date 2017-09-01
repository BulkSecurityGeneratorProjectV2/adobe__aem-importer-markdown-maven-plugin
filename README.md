# Markdown to AEM Maven Plugin

This Maven plugin allows the creation of AEM packages from Markdown files.

## Building

```bash
$ mvn clean install
```
## Configuring

Add following stuff to your `pom.xml`

```xml

  <build>
    <plugins>
      <plugin>
        <groupId>io.adobe.aem.markdown</groupId>
        <artifactId>markdown-aem-maven-plugin</artifactId>
        <version>${project.version}</version>
        <configuration>
          <root>/content/udp/en/open/source/markdown-aem-maven</root>
          <rootType>udp/components/structure/githubdocumentation</rootType>
          <type>udp/components/structure/githubdocumentation</type>
          <pageTemplate>/apps/udp/templates/importedgithubpage</pageTemplate>
          <rootTemplate>/apps/udp/templates/githubdocumentation</rootTemplate>
        </configuration>
        <executions>
          <execution>
            <phase>generate-sources</phase>
            <goals>
              <goal>package</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

## Parameters

- `root`
- `rootType`
- `type`
- `pageTemplate`
- `rootTemplate`

## Running

```bash
$ mvn markdown-aem-maven-plugin:package
```