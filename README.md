# Markdown to AEM Maven Plugin

This Maven plugin allows the creation of AEM packages from Markdown files.

## Dependencies

- https://git.corp.adobe.com/adobe-io/unified-dev-portal/pull/129
- build https://git.corp.adobe.com/adobe-io/unified-dev-portal/ branch: `standalone-importer`

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
          <branches>
            <master>path/to/master/checkout</master>
            <develop>path/to/develop/checkout</develop>
          </branches>
          <pages>
            <page>README.md</page>
            <page>docs*</page>
          </pages>  
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

- `root`: this is the path in AEM where your pages are being created. Make sure not to overwrite important existing content
- `rootType`: this is the `sling:resourceType` of the root page
- `type`: this is the `sling:resourceType` for all imported pages
- `pageTemplate`: page template path for the root page
- `rootTemplate`: page template path for imported pages
- `branches`: key-value pairs of branches and the root path of the local checkout
- `docs`: list of patterns of paths or file names that should be imported

## Running

```bash
$ mvn markdown-aem-maven-plugin:package
```
