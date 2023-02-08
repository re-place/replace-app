pipeline {
    agent any
    
    stages {
        stage('Frontend') {
            steps {
                docker.build(${ECR_FRONTEND}, '.')
                sh 'docker build -f ./web/src/angular/frontend.Dockerfile -t replace-frontend .'
                'Image noch taggen mit ecr-Command'
            }
        }
        stage('Backend') {
            steps {
                docker.build(${ECR_BACKEND}, '.')
                sh './gradlew publishImageToLocalRegistry'
                echo 'Image noch taggen mit ecr command'
            }
        }
        stage('Update Database') {
            when {
                branch 'test'
            }
            steps {
                sh 'gradle update'
            }
        }
        stage('Push into ecr-Repository') {
            when {
                branch 'test'
            }
            steps {
                docker.image(${ECR_FRONTEND}).push('latest')
                docker.image(${ECR_BACKEND}).push('latest')
            }
        }
        stage('Run') {
            when {
                branch 'test'
            }
            steps {
                echo 'run docker compose, so that it works with the pipeline finishing'
            }
        }
    }
}
