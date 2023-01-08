pipeline {
    agent any
    
    tools {
        nodejs 'default'
    }
    stages {
        stage('Frontend Test') {
            steps {
                echo 'Not Yet Done'
            }
        }
        stage('Backend Test') {
            steps {
                echo 'Not yet done'
            }
        }
        stage('Frontend') {
            steps {
                echo 'Not Yet Done'
            }
        }
        stage('Backend') {
            steps {
                sh 'docker build -f ./Dockerfile -t replace-backend .'
            }
        }
        stage('Deploy') {
            when {
                branch 'Test'
            }
            steps {
                echo 'Not Yet Done'
            }
        }
    }
}
