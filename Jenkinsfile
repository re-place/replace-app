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
                cp './infrastructure/src/jvmMain/resources/application_env.conf' './infrastructure/src/jvmMain/resources/application.conf'
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
