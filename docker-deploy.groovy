node('docker') {
    stage('scm Checkout') {
        git 'https://github.com/Nara-Sagar7/Game-of-life.git'
    }
    stage('Maven Build') {
        sh 'mvn clean package'
    }
    stage('sonarcube Analysis') {
         withSonarQubeEnv('SONAR-6.7.4') {
              sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
         }
    }
}