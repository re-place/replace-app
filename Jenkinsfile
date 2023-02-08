pipeline {
    agent any
    
    stages {
        stage('Frontend') {
            steps {
                //docker.build(${ECR_FRONTEND}, '.')
                sh 'docker build -f ./web/Dockerfile -t ${ECR_FRONTEND} .'
            }
        }
        stage('Backend') {
            steps {
                sh 'gradle publishImageToLocalRegistry'
            }
        }
        stage('Update Database') {
            when {
                branch 'master'
            }
            steps {
                sh 'gradle update'
            }
        }
        stage('Push into ecr-Repository') {
            when {
                branch 'master'
            }
            steps {
                docker.image(${ECR_FRONTEND}).push('latest')
                docker.image(${ECR_BACKEND}).push('latest')
            }
        }
        stage('Run') {
            when {
                branch 'master'
            }
            steps {
                echo 'run docker compose, so that it works with the pipeline finishing'
            }
        }
    }
}
