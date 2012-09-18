This is meant for project committers:

- Steps to make this project:

  - Get latest rhino version (currently 1.7R4)
  - Rename LICENSE.txt to MPL-2.0.txt
  - Add htmlunit-core-js LICENSE.txt
  - Make eclipse project, with 'src' 'testsrc' and 'toolsrc' folder as source folders, while
    making build/classes as the output folder 
  - Add 'lib' and 'build' to svn:ignore
  - Add 'htmlunittestsrc' which contains 'build.xml' and the test cases

- To run HtmlUnit fork tests:
    ant -f htmlunit.xml test

- To run original Rhino tests:
    ant junit-all

- Deploy snapshot:
  - Ensure that you have pgp installed and that you create own keys, see
      https://docs.sonatype.org/display/Repository/How+To+Generate+PGP+Signatures+With+Maven

  - Configure your Maven settings.xml, see
      https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide

  - Run:
      ant -f htmlunit.xml deploy-snapshot


- To manually generate rhinoDiff.txt:
    - git clone https://github.com/mozilla/rhino.git
    - cd rhino
    - git checkout Rhino1_7R4_RELEASE
    - apply the changes
    - git diff >rhinoDiff.txt


- To release:
  1- Change version in pom.xml and htmlunit.xml
  2- Make sure rhinoDiff.txt is updated
  3- ensure that you have pgp installed and that you create own keys (see
      https://docs.sonatype.org/display/Repository/How+To+Generate+PGP+Signatures+With+Maven)
  
  4- Configure your Maven settings.xml, see
      https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide

  5- Deploy the release files into Sonatype staging repository:
      ant -f htmlunit.xml jar-all

      - for 2.11, run the below:

      mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=pom.xml -Dfile=build/htmlunit-core-js-2.11.jar
      mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=pom.xml -Dfile=build/htmlunit-core-js-2.11-sources.jar -Dclassifier=sources
      mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=pom.xml -Dfile=build/htmlunit-core-js-2.11-javadoc.jar -Dclassifier=javadoc

  6- Go to https://oss.sonatype.org/index.html#stagingRepositories and release the repository

TODO:
    - GPG automated from ant (in "bundle" target), if possible
