/**
 * Copyright 2018 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.aem.markdown;

import io.adobe.udp.markdownimporter.*;
import io.adobe.udp.markdownimporter.mappings.MarkdownMappings;
import io.adobe.udp.markdownimporter.services.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "package", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class PackageMojo extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;

    @Parameter(defaultValue = "", property = "design")
    private String design;

    @Parameter(defaultValue = "", property = "root", required = true)
    private String root;

    @Parameter(defaultValue = "*", property = "pages")
    private List<String> pages;

    @Parameter(defaultValue = "", property = "branches", required = true)
    private Map<String, String> branches;

    @Parameter(defaultValue = "", property = "mappings")
    private Map<String, String> componentMappings;

    @Parameter(defaultValue = "", property = "rootTemplate", required = true)
    private String rootTemplate;

    @Parameter(defaultValue = "", property = "pageTemplate", required = true)
    private String pageTemplate;

    @Parameter(defaultValue = "${project.artifactId}", property = "package")
    private String packageName;

    @Parameter(defaultValue = "${project.name}", property = "title")
    private String title;

    @Parameter(defaultValue = "", property = "rootType", required = true)
    private String rootType;

    @Parameter(defaultValue = "", property = "type", required = true)
    private String type;

    @Parameter(defaultValue = "${project.groupId}", property = "group")
    private String group;

    @Parameter(defaultValue = "${project.version}", property = "version")
    private String version;

    @Parameter(defaultValue = "${project.build.directory}", property = "output", required = true)
    private File targetDir;

    private Map<String, String> getDefaultMappings() {
        Map<String, String> mappings = new HashMap<String, String>();

        mappings.put("com.vladsch.flexmark.ast.Node", "io.adobe.udp.markdownimporter.mappings.MarkdownParagraphMapper");
        mappings.put("com.vladsch.flexmark.ast.IndentedCodeBlock",
                "io.adobe.udp.markdownimporter.mappings.IndentedCodeBlockMapper");
        mappings.put("com.vladsch.flexmark.ast.FencedCodeBlock",
                "io.adobe.udp.markdownimporter.mappings.MarkdownFencedCodeBlockMapper");
        mappings.put("com.vladsch.flexmark.ext.tables.TableBlock",
                "io.adobe.udp.markdownimporter.mappings.MarkdownTableMapper");
        mappings.put("com.vladsch.flexmark.ast.Heading",
                "io.adobe.udp.markdownimporter.mappings.MarkdownHeadlineMapper");
        mappings.put("com.vladsch.flexmark.ext.front.matter.YamlFrontMatterBlock",
                "io.adobe.udp.markdownimporter.mappings.FrontMatterMapper");

        return mappings;
    }

    public void execute() throws MojoExecutionException {

        InputConfig config = new InputConfig();

        config.setRootPath(root);

        List<String> branchNames = new Vector<String>();
        if (branches.keySet().isEmpty()) {
            branchNames.add("master");
        } else {
            branchNames.addAll(branches.keySet());
        }
        config.setBranches(branchNames);

        config.setPages(pages);

        Map<String, String> mappings = getDefaultMappings();
        mappings.putAll(componentMappings);
        config.setComponentMappings(mappings);

        config.setRootTemplate(rootTemplate);
        config.setPageTemplate(pageTemplate);
        config.setRootTitle(title);
        config.setPackageName(packageName);
        config.setGroup(group);
        config.setVersion(version);
        config.setRootPageResourceType(rootType);
        config.setPageResourceType(type);
        config.setDesignPath(design);

        List<String> workingDirs = new Vector<String>();

        for (String branchName : branches.keySet()) {
            workingDirs.add(branchName + ":" + branches.get(branchName));
        }

        config.setWorkingDirs(workingDirs);

        MarkdownMappings.configure(config.getComponentMappings());
        MarkdownParserService markdownParserService = new MarkdownParserServiceImpl();
        GithubLinkService githubLinkService = new GithubLinkServiceImpl();
        FileSystemPathService pathService = new FileSystemPathServiceImpl();
        WorkdirMarkdownImporter i = new WorkdirMarkdownImporter(markdownParserService, githubLinkService, pathService,
                config);

        i.processGithubPage();
        Map<String, PageData> pages = i.getPageData();
        Map<String, File> images = i.getImages();
        RootPageData root = new RootPageData(config);
        pages.put(config.getRootPath(), root);

        try {
            Importer.generatePackage(pages, images, config);

            File zipFile = new File(config.getPackageName() + ".zip");

            zipFile.renameTo(new File(targetDir, zipFile.getName()));
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot generate content package", e);
        }
    }
}
