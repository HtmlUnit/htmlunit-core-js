### Latest release Version 4.21.0 / December 28, 2025

# HtmlUnit - core-js

This project is a Rhino fork, maintained to support features needed by HtmlUnit.

It is split into two projects "htmlunit-rhino-fork", which contains the minimal changes to rhino,
and "htmlunit-core-js" which has the test cases and packaging.

Notice that "org.mozilla.*" is renamed to "org.htmlunit.corejs.*"

[![Maven Central Version](https://img.shields.io/maven-central/v/org.htmlunit/htmlunit-core-js)](https://central.sonatype.com/artifact/org.htmlunit/htmlunit-core-js)

:heart: [Sponsor](https://github.com/sponsors/rbri)

### Project News

**[Developer Blog](https://htmlunit.github.io/htmlunit-blog/)**

[HtmlUnit@mastodon](https://fosstodon.org/@HtmlUnit) | [HtmlUnit@bsky](https://bsky.app/profile/htmlunit.bsky.social) | [HtmlUnit@Twitter](https://twitter.com/HtmlUnit)

#### Version 5

Work on HtmlUnit - core-js 5.0 has started. This new major version will require **JDK 17 or higher**.


#### Legacy Support (JDK 8)

If you need to continue using **JDK 8**, please note that versions 4.x will remain available as-is. However,
**ongoing maintenance and fixes for JDK 8 compatibility are only available through sponsorship**.

Maintaining separate fix versions for JDK 8 requires significant additional effort for __backporting__, testing, and release management.

**To enable continued JDK 8 support**, please contact me via email to discuss sponsorship options. Sponsorship provides:

- __Backporting__ security and bug fixes to the 4.x branch
- Maintaining compatibility with older Java versions
- Timely releases for critical issues

Without sponsorship, the 4.x branch will not receive updates. Your support ensures the long-term __sustainability__ of this project across multiple Java versions.

## Start HtmlUnit - core-js Development

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You simply only need a local maven installation.


### Building

Create a local clone of the repository and you are ready to start.

Open a command line window from the root folder of the project and call

```
mvn compile
```

### Running the tests

```
mvn test
```

## Contributing

Pull Requests and and all other Community Contributions are essential for open source software.
Every contribution - from bug reports to feature requests, typos to full new features - are greatly appreciated.

## Deployment and Versioning

This part is intended for committer who are packaging a release.

* Check all your files are checked in
* Execute these mvn commands to be sure all tests are passing and everything is up to data

```
   mvn versions:display-plugin-updates
   mvn versions:display-dependency-updates
   mvn -U clean test
```

* Update the version number in pom.xml and README.md
* Commit the changes


* Build and deploy the artifacts 

```
   mvn -up clean deploy
```

* Go to [Maven Central Portal](https://central.sonatype.com/) and process the deploy
  - publish the package and wait until it is processed

* Create the version on Github
    * login to Github and open project https://github.com/HtmlUnit/htmlunit-core-js
    * click Releases > Draft new release
    * fill the tag and title field with the release number (e.g. 4.0.0)
    * append 
        * htmlunit-core-js-4.x.x.jar
        * htmlunit-core-js-4.x.x.jar.asc 
        * htmlunit-core-js-4.x.x.pom
        * htmlunit-core-js-4.x.x.pom.asc 
        * htmlunit-core-js-4.x.x-javadoc.jar
        * htmlunit-core-js-4.x.x-javadoc.jar.asc
        * htmlunit-core-js-4.x.x-sources.jar
        * htmlunit-core-js-4.x.x-sources.jar.asc
    * and publish the release 

* Update the version number in pom.xml to start next snapshot development
* Update the htmlunit pom to use the new release

## Authors

* **Mozilla Rhino Team**
* **HtmlUnit Team**

## License

This project is licensed under the Apache 2.0 License

## Acknowledgments

Many thanks to all of you contributing to HtmlUnit/Rhino in the past.

## Development Tools

Special thanks to:

<a href="https://www.jetbrains.com/community/opensource/"><img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg" alt="JetBrains" width="42"></a>
<a href="https://www.jetbrains.com/idea/"><img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA_icon.svg" alt="IntelliJ IDEA" width="42"></a>  
**[JetBrains](https://www.jetbrains.com/)** for providing IntelliJ IDEA under their [open source development license](https://www.jetbrains.com/community/opensource/) and

<a href="https://www.eclipse.org/"><img src="https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-foundation-grey-orange.svg" alt="Eclipse Foundation" width="80"></a>  
Eclipse Foundation for their Eclipse IDE

<a href="https://www.syntevo.com/smartgit/"><img src="https://www.syntevo.com/assets/images/logos/smartgit-8c1aa1e2.svg" alt="SmartGit" width="54"></a>  
to **[Syntevo](https://www.syntevo.com/)** for their excellent [SmartGit](https://www.smartgit.dev/)!
