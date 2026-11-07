
pipeline {
    agent any

    tools {
        maven 'Maven-3.10'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'OxyCare source code checked out'
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean test package'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('Archive JAR') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar',
                    excludes: 'target/original-*.jar'
            }
        }
    }
}
