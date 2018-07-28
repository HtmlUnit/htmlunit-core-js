# HtmlUnit - core-js

This project is a Rhino fork, maintained to support features needed by HtmlUnit.

It is split into two projects "htmlunit-rhino-fork", which contains the minimal changes to rhino,
and "htmlunit-core-js" which has the test cases and packaging.

Notice that "org.mozilla.*" is renamed to "net.sourceforge.htmlunit.corejs.*"

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You simply only need a local git client and gradle installation.


### Building

    1. Clone the two projects into sibling folders
    2. Create eclipse project for "htmlunit-rhino-fork", with 'src' 'testsrc' and 'toolsrc' folder as source folders, while having build/classes as the output folder
    3. In "htmlunit-rhino-fork", run:
        - git remote add upstream https://github.com/mozilla/rhino
        - git fetch upstream

        With proxy:
        - git config --global http.proxy http://proxyhost:port

        To unset proxy:
        - git config --global --unset http.proxy


## Running the tests

```
gradlew test
```

## Generating the JARs

```
gradlew jar
```

## Contributing

TODO
Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Deployment and Versioning

This part is intended for committer who are packaging a release.

* Check all your files are checked in
* Execute "gradlew clean test" to be sure all tests are passing
* Update the version number in pom.xml
* Execute "gradlew clean test" to be sure all tests are passing
* Commit the changes


* Build and deploy the artifacts 

```
   gradlew clean uploadArchives
```

* Go to [Sonatype staging repositories](https://oss.sonatype.org/index.html#stagingRepositories) and process the deploy
  - select the repository and close it - wait until the close is processed
  - release the package and wait until it is processed

* Create the version on Github
    * login to Github and open project https://github.com/HtmlUnit/htmlunit-core-js
    * click Releases > Draft new release
    * fill the tag and title field with the release number (e.g. 1.1.0)
    * write some notes about the release
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