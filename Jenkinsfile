pipeline {
    agent any
    
    tools {
        nodejs 'default'
    }
    stages {
        stage('Frontend') {
            steps {
                sh 'docker build -f ./web/src/angular/Dockerfile -t replace-frontend .'
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
