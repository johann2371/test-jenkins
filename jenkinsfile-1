pipeline {
    agent any

    environment {
        IMAGE_NAME = "johannbrandon/mboapost"
        IMAGE_TAG  = "${BUILD_NUMBER}"
    }

    triggers {
        githubPush()
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/johann2371/test-jenkins.git'
            }
        }

        stage('Build & Tests') {
            steps {
                bat 'mvnw.cmd clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sq1') {
                    bat 'mvnw.cmd sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 11, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                bat """
                docker build -t %IMAGE_NAME%:%IMAGE_TAG% .
                docker tag %IMAGE_NAME%:%IMAGE_TAG% %IMAGE_NAME%:latest
                """
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-token',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat """
                    docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                    docker push %IMAGE_NAME%:%IMAGE_TAG%
                    docker push %IMAGE_NAME%:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Analyse OK – Image Docker publiée sur Docker Hub"
        }
        failure {
            echo "❌ Pipeline arrêté (Quality Gate ou build en échec)"
        }
    }
}
