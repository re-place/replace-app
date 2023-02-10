pipeline {
    agent any
    
    stages {
        stage('Frontend') {
            steps {
                dir('web') {
                    sh 'docker build -t ${REPLACE_ECR_FRONTEND} .'
                }
            }
        }
        stage('Backend') {
            steps {
                sh 'cp ./infrastructure/src/jvmMain/resources/application_env.conf ./infrastructure/src/jvmMain/resources/application.conf'
                sh './gradlew -PktorImage=${REPLACE_ECR_BACKEND} publishImageToLocalRegistry'
            }
        }
        stage('Update Database') {
            // when {
            //     branch 'master'
            // }
            environment {
                REPLACE_DATABASE = credentials('DATABASE_CREDENTIALS')
            }
            agent {
                docker { image 'liquibase/liquibase:latest' }
            }
            steps {
                sh 'liquibase update --changelog-file=/infrastructure/src/jvmMain/resources/db/changelog-root.json --url=${REPLACE_DATABASE_URL} --username=${REPLACE_DATABASE_USR} --password=${REPLACE_DATABASE_PSW}'
            }
        }
        stage('Push into ecr-Repository') {
            // when {
            //     branch 'master'
            // }
            steps {
                sh 'docker push ${REPLACE_ECR_FRONTEND}:latest'
                sh 'docker push ${REPLACE_ECR_BACKEND}:latest'
            }
        }
        stage('Run') {
            // when {
            //     branch 'master'
            // }
            environment {
                REPLACE_DATABASE = credentials('DATABASE_CREDENTIALS')
            }
            steps {
                script {
                    docker.withServer('ssh://test.local') {
                        sh 'docker compose stop'
                        sh 'docker compose up --detach --pull always --remove-orphans'
                    }
                }
            }
        }
    }
}
