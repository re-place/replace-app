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
                sh './gradlew publishImageToLocalRegistry'
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
