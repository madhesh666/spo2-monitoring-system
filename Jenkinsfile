
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
        stage('Publish HTML Report') {
    steps {
        publishHTML(target: [
            reportName: 'OxyCare HTML Report',
            reportDir: 'reports',
            reportFiles: 'index.html',
            keepAll: true,
            alwaysLinkToLastBuild: true,
            allowMissing: false
        ])
    }
}

stage('Copy Previous Build Artifact') {
    steps {
        copyArtifacts(
            projectName: env.JOB_NAME,
            selector: lastSuccessful(),
            filter: 'target/*.jar',
            fingerprintArtifacts: true
        )
        echo 'Previous successful build artifact copied'
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
