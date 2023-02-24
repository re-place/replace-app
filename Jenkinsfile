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
            when {
                expression { GIT_BRANCH == 'origin/master' }
                expression { GIT_BRANCH == 'origin/dev' }
            }
            environment {
                STAGING_DATABASE = credentials('STAGING_DATABASE_CREDENTIALS')
                TEST_DATABASE = credentials('TEST_DATABASE_CREDENTIALS')
            }
            agent {
                docker { image 'liquibase/liquibase:latest' }
            }
            steps {
                script {
                    if(GIT_BRANCH == 'origin/master'){
                        REPLACE_DATABASE_URL = '${TEST_DATABASE_URL}'
                        REPLACE_DATABASE = ${TEST_DATABASE}
                    }
                    if(GIT_BRANCH == 'origin/dev'){
                        REPLACE_DATABASE_URL = '${STAGING_DATABASE_URL}'
                        REPLACE_DATABASE = ${STAGING_DATABASE}
                    }
                }
                
                sh 'liquibase update --changelog-file=/infrastructure/src/jvmMain/resources/db/changelog-root.json --url=${REPLACE_DATABASE_URL} --username=${REPLACE_DATABASE_USR} --password=${REPLACE_DATABASE_PSW}'
            }
        }
        stage('Push into ecr-Repository') {
            when {
                expression { GIT_BRANCH == 'origin/master' }
                expression { GIT_BRANCH == 'origin/dev' }
            }
            steps {
                script {
                    if(GIT_BRANCH == 'origin/master'){
                        ECR_TAG = '${TEST_TAG}'
                    }
                    if(GIT_BRANCH == 'origin/dev'){
                        ECR_TAG = '${STAGING_TAG}'
                    }
                }

                sh 'docker push ${REPLACE_ECR_FRONTEND}:${ECR_TAG}'
                sh 'docker push ${REPLACE_ECR_BACKEND}:${ECR_TAG}'
            }
        }
        stage('Run') {
            when {
                expression { GIT_BRANCH == 'origin/master' }
                expression { GIT_BRANCH == 'origin/dev' }
            }
            environment {
                STAGING_DATABASE = credentials('STAGING_DATABASE_CREDENTIALS')
                TEST_DATABASE = credentials('TEST_DATABASE_CREDENTIALS')
            }
            steps {
                script {
                    if(GIT_BRANCH == 'origin/master'){
                        REPLACE_DATABASE_URL = '${TEST_DATABASE_URL}'
                        REPLACE_DATABASE = ${TEST_DATABASE}
                        REPLACE_DOCKER_ENV = 'ssh://test.local'
                        REPLACE_OAUTH_CALLBACK = '${TEST_OAUTH_CALLBACK}'
                        REPLACE_OAUTH_CLIENT_ID = '${TEST_OAUTH_CLIENTID}'
                        REPLACE_OAUTH_CLIENT_SECRET = '${TEST_OAUTH_SECRET}'
                    }
                    if(GIT_BRANCH == 'origin/dev'){
                        REPLACE_DATABASE_URL = '${STAGING_DATABASE_URL}'
                        REPLACE_DATABASE = ${STAGING_DATABASE}
                        REPLACE_DOCKER_ENV = 'ssh://staging.local'
                        REPLACE_OAUTH_CALLBACK = '${STAGING_OAUTH_CALLBACK}'
                        REPLACE_OAUTH_CLIENT_ID = '${STAGING_OAUTH_CLIENTID}'
                        REPLACE_OAUTH_CLIENT_SECRET = '${STAGING_OAUTH_SECRET}'
                    }
                }

                script {
                    docker.withServer(${REPLACE_DOCKER_ENV}) {
                        sh 'docker compose stop'
                        sh 'docker compose up --detach --pull always --remove-orphans'
                    }
                }
            }
        }
    }
}
