def err = null
try {
  node {
    stage ('Checkout') {
      //Clean dir
      deleteDir()
      //Checkoute code from repository
      checkout scm
    }

    stage ('Build') {
      sh 'mvn clean install -DskipTests validate compile package jar:jar javadoc:javadoc'
    }

    stage ('Generate Artifacts') {
      pom = readMavenPom file: 'pom.xml'
      $POM_VERSION = pom.version
      step([$class: 'JavadocArchiver', javadocDir: 'target/site/apidocs'])
      sh 'mkdir - p target/release'
      sh 'cp target/*.jar target/release/XIVStats-Gatherer-Java.jar'
      sh 'cp config_example.xml target/release'
      sh 'cd target/release && zip -r XIVStats-Gatherer-Java-$POM_VERSION.zip .'
      sh 'cd ../..'
      archiveArtifacts 'target/*.jar,target/**/*.class'
      archiveArtifacts 'target/release/*.zip'
    }

    stage ('Test') {
      try {
        sh 'cp ~/xivstatsFiles/config.xml .'
        sh 'mvn test'
      }
      finally {
        sh 'rm config.xml'
        sh 'pip install --user codecov'
        sh 'codecov --token=96498141-ca0d-4144-af53-c04b593115ef'
        step([$class: 'JUnitResultArchiver', testResults: 'target/surefire-reports/*.xml'])
      }
    }

    stage('SonarQube analysis') {
      // requires SonarQube Scanner 2.8+
      def scannerHome = tool 'SonarQube Scanner 2';
      println scannerHome;
      withSonarQubeEnv('ReidWeb SonarQube') {
        sh "${scannerHome}/bin/sonar-scanner"
      }
    }

  }
} catch (caughtError) { //End of Try
  err = caughtError
  currentBuild.result = "FAILURE"
} finally {
  (currentBuild.result != "ABORTED") && node("master") {
    // Send e-mail notifications for failed or unstable builds.
    // currentBuild.result must be non-null for this step to work.
    step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: emailextrecipients([[$class: 'CulpritsRecipientProvider'], [$class: 'RequesterRecipientProvider']])])

    /* Must re-throw exception to propagate error */
    if (err) {
      throw err
    }
  }
}
