# AEM Importer for Markdown Maven Plugin

[![CircleCI](https://circleci.com/gh/adobe/aem-importer-markdown-maven-plugin.svg?style=svg)](https://circleci.com/gh/adobe/aem-importer-markdown-maven-plugin)

* Are you using Adobe Experience Manager?
* Are you using Apache Maven?
* Do you write (or generate) documentation in Markdown?
* Would you like to serve your Markdown documentation using AEM?
* Would you like to integrate the conversion process into your Maven build?

If you've answered all questions with yes, you've come to the right place.

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
        <groupId>com.adobe.aem</groupId>
        <artifactId>aem-importer-markdown-maven-plugin</artifactId>
        <version>1.11-SNAPSHOT</version>
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

| Parameter | Description |
| --- | --- |
| `root` | this is the path in AEM where your pages are being created. Make sure not to overwrite important existing content |
| `rootType` | this is the `sling:resourceType` of the root page |
| `type` | this is the `sling:resourceType` for all imported pages |
| `pageTemplate` | page template path for the root page |
| `rootTemplate` | page template path for imported pages |
| `branches` | key-value pairs of branches and the root path of the local checkout |
| `docs` | list of patterns of paths or file names that should be imported |

## Running

```bash
$ mvn aem-importer-markdown-maven-plugin:package
```

### Releasing

This project is configured to deploy to [Maven Central](https://repo1.maven.org/maven2/) via [Sonatype OSS](https://oss.sonatype.org/content/groups/public/com/adobe/aem/aem-importer-markdown/). 
If you want to release yourself, first create an account in the [Sonatype JIRA](https://issues.sonatype.org) and open a new issue, requesting access to the group `com.adobe.aem`. 
Please reference `trieloff` and `adobe-bot` in your request, so that we can confirm your permission.

In the next step, edit your `~/.m2/settings.xml` to include a new `<server>` section:

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username><!-- your Sonatype username --></username>
      <password><!-- your Sonatype password --></password>
    </server>
  </servers>
</settings>
```

### Deploying a Build

To deploy a build, use the pre-configured Maven Release Plugin with following commands:

```bash
$ mvn release:clean release:prepare
$ mvn release:perform
```

Releases can only be performed when you

* have commit permissions on this repository
* have access to the Sonatype group


## License/Copyright

Copyright 2017 Adobe Systems Incorporated. All rights reserved.
This file is licensed to you under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0
