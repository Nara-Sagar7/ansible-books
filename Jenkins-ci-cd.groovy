node('dockerbuild'){
    stage('Scm Checkout') {
        git 'https://github.com/Nara-Sagar7/Game-of-life.git'
    }
    stage('Build & Package') {
        sh 'mvn clean package'
    }
    stage('SonarqubeAnalisys') {
        withSonarQubeEnv('SONAR-6.7.4') {
            sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
        }
    }
    stage('DockerImageBuild') {
        sh 'docker build -t sagardevops/game:5.0.0 . '
    }
    stage('Docker Login & push') {
        withCredentials([string(credentialsId: 'dockerhubpwd', variable: 'Dockerhubpwd')]) {
          sh "docker login -u sagardevops -p ${Dockerhubpwd}"
        }
    }
    sshagent(['privatekey']) {
        def dockerrun = 'docker container run -p -d 8080:8080 --name gameofdocker sagardevops/game:5.0.0'
         sh "ssh -o StrictHostKeyChecking=no builder@10.128.15.206 ${dockerrun}"
    }
}    