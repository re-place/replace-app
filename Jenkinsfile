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
<<<<<<< HEAD
            when {
                expression { GIT_BRANCH == 'origin/master' }
            }
=======
            // when {
            //     branch 'master'
            // }
>>>>>>> d492275 (Configuration of Callback)
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
<<<<<<< HEAD
            when {
                expression { GIT_BRANCH == 'origin/master' }
            }
=======
            // when {
            //     branch 'master'
            // }
>>>>>>> d492275 (Configuration of Callback)
            steps {
                sh 'docker push ${REPLACE_ECR_FRONTEND}:latest'
                sh 'docker push ${REPLACE_ECR_BACKEND}:latest'
            }
        }
        stage('Run') {
<<<<<<< HEAD
            when {
                expression { GIT_BRANCH == 'origin/master' }
            }
=======
            // when {
            //     branch 'master'
            // }
>>>>>>> d492275 (Configuration of Callback)
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
