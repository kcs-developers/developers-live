pipeline {
  agent any
  tools {
    gradle 'my_gradle'
  }
  environment {
    gitName = 'lango'
    gitEmail = 'xmun777@naver.com'
    githubCredential = 'git_cre'
    applicationGitAddress = 'https://github.com/start-dream-team/developers-live.git'
  }
  stages {
    stage('Checkout Github') {
      steps {
          checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: githubCredential, url: applicationGitAddress ]]])
          withCredentials([GitUsernamePassword(credentialsId: githubCredential, gitToolName: 'Default')]) {
            sh 'git submodule update --init --recursive'
          }
      post {
        failure {
          echo 'Application Repository clone failure'
        }
        success {
          echo 'Application Repository clone success'
        }
      }
    }
    stage('Gradle Build') {
      steps {
          sh 'gradle clean build -Penv=prod'
          }
      post {
        failure {
          echo 'Gradle jar build failure'
        }
        success {
          echo 'Gradle jar build success'
        }
      }
    }
  }
}