node {
  stage 'Checkout'
  step([$class: 'GitHubCommitStatusSetter'])
  //Clean dir
  deleteDir()
  //Checkoute code from repository
  checkout scm
  
  stage 'Build'
  sh 'mvn clean install -DskipTests validate compile package jar:jar javadoc:javadoc'
  
  stage 'Publish Artifacts'
  step([$class: 'JavadocArchiver', javadocDir: 'target/site/apidocs'])
  sh 'mkdir - p target/release'
  sh 'cp target/*dependencies.jar target/release/XIVStats-Gatherer-Java.jar'
  sh 'cp config_example.xml target/release'
  sh 'cd target/release'
  sh 'zip -r XIVStats-Gatherer-Java-$POM_VERSION.zip .'
  sh 'cd ../..'
  archiveArtifacts 'target/*.jar,target/**/*.class'
  archiveArtifacts 'target/release/*.zip'
  
  stage 'Test'
  sh 'cp ~/xivstatsFiles/config.xml .'
  sh 'mvn test'
  sh 'rm config.xml'
  sh 'pip install --user codecov'
  sh 'codecov --token=96498141-ca0d-4144-af53-c04b593115ef'
  step([$class: 'JUnitResultArchiver', testResults: 'target/surefire-reports/*.xml'])
  step([$class: 'GitHubCommitStatusSetter'])
  
}
