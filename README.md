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

### Setting up to Deploy

This project is configured to deploy to [Adobe Artifactory](https://artifactory.corp.adobe.com/artifactory/webapp/#/artifacts/browse/tree/General/maven-markdown-tools-snapshot/io/adobe/udp/markdown-importer). In order to deploy yourself, you need to update your `settings.xml` with following snippets:

```xml
  <servers>
    <server>
      <username><!-- replace with your LDAP user name --></username>
      <password><!-- replace with your API key from Artifactory --></password>
      <id>central</id>
    </server>
    <server>
      <username><!-- replace with your LDAP user name --></username>
      <password><!-- replace with your API key from Artifactory --></password>
      <id>snapshots</id>
    </server>
  </servers>
```

and

```xml
    <profile>
      <repositories>
        <repository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>maven-markdown-tools-release</name>
          <url>https://artifactory.corp.adobe.com:443/artifactory/maven-markdown-tools-release</url>
        </repository>
        <repository>
          <snapshots />
          <id>snapshots</id>
          <name>maven-markdown-tools-snapshot</name>
          <url>https://artifactory.corp.adobe.com:443/artifactory/maven-markdown-tools-snapshot</url>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>maven-markdown-tools-release</name>
          <url>https://artifactory.corp.adobe.com:443/artifactory/maven-markdown-tools-release</url>
        </pluginRepository>
        <pluginRepository>
          <snapshots />
          <id>snapshots</id>
          <name>maven-markdown-tools-snapshot</name>
          <url>https://artifactory.corp.adobe.com:443/artifactory/maven-markdown-tools-snapshot</url>
        </pluginRepository>
      </pluginRepositories>
      <id>artifactory-markdown</id>
    </profile>
```

and

```xml
    <activeProfiles>
        <activeProfile>artifactory-markdown</activeProfile>
    </activeProfiles>
```

### Deploying a Build

```bash
$ mvn deploy
```