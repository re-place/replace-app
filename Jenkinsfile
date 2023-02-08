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
                sh './gradlew -PktorImage=${REPLACE_ECR_BACKEND} publishImageToLocalRegistry'
            }
        }
        stage('Update Database') {
            // when {
            //     branch 'master'
            // }
            agent {
                docker { image 'liquibase/liquibase:latest' }
            }
            steps {
                // script {
                //     docker.withServer('ssh://test.local') {
                //         sh 'liquibase update --changelog-file=/infrastructure/src/jvmMain/resources/db/changelog-root.json --url=${REPLACE_DATABASE_URL} --username=${REPLACE_DATABASE_USER} --password=${REPLACE_DATABASE_PASSWORD}'
                //     }
                // }
                echo 'not working yet'
                //sh 'liquibase update --changelog-file=/infrastructure/src/jvmMain/resources/db/changelog-root.json --url=${REPLACE_DATABASE_URL} --username=${REPLACE_DATABASE_USER} --password=${REPLACE_DATABASE_PASSWORD}'
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
            steps {
                script {
                    docker.withServer('ssh://test.local') {
                        sh 'docker compose up --detach --pull always --remove-orphans'
                    }
                }
                //echo 'not running yet'
            }
        }
    }
}
