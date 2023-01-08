pipeline {
    agent any
    
    stages {
        stage('Frontend') {
            steps {
                sh 'docker build -f ./web/src/angular/frontend.Dockerfile -t replace-frontend .'
            }
        }
        stage('Backend') {
            steps {
                sh 'docker build -f ./backend.Dockerfile -t replace-backend .'
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
