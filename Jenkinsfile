pipeline {
    agent any

    environment {
        IMAGE_TAG = GIT_BRANCH == 'origin/master' ? 'latest' : GIT_BRANCH == 'origin/dev' ? 'staging' : GIT_BRANCH
        REPLACE_DOCKER_ENV = GIT_BRANCH == 'origin/master' ? 'ssh://test.local' : 'ssh://staging.local'
        REPLACE_DATABASE_URL = GIT_BRANCH == 'origin/master' ? '${TEST_DATABASE_URL}' : '${STAGING_DATABASE_URL}'
        REPLACE_OAUTH_CALLBACK = GIT_BRANCH == 'origin/master' ? '${TEST_OAUTH_CALLBACK}' : '${STAGING_OAUTH_CALLBACK}'
        REPLACE_OAUTH_CLIENT_ID = GIT_BRANCH == 'origin/master' ? '${TEST_OAUTH_CLIENTID}' : '${STAGING_OAUTH_CLIENTID}'
        REPLACE_OAUTH_CLIENT_SECRET = GIT_BRANCH == 'origin/master' ? '${TEST_OAUTH_SECRET}' : '${STAGING_OAUTH_SECRET}'
    }

    stages {
        stage('Frontend') {
            steps {
                dir('web') {
                    sh 'docker build -t ${REPLACE_ECR_FRONTEND}:${IMAGE_TAG} .'
                }
            }
        }
        stage('Backend') {
            steps {
                sh 'cp ./infrastructure/src/jvmMain/resources/application_env.conf ./infrastructure/src/jvmMain/resources/application.conf'
                sh './gradlew -PktorImage=${REPLACE_ECR_BACKEND}:${IMAGE_TAG} publishImageToLocalRegistry'
            }
        }
        stage('Update Database') {
            when {
                expression { GIT_BRANCH == 'origin/master' || GIT_BRANCH == 'origin/dev' }
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
                        REPLACE_DATABASE = ${TEST_DATABASE}
                    }
                    if(GIT_BRANCH == 'origin/dev'){
                        REPLACE_DATABASE = ${STAGING_DATABASE}
                    }
                }
                
                sh 'liquibase update --changelog-file=/infrastructure/src/jvmMain/resources/db/changelog-root.json --url=${REPLACE_DATABASE_URL} --username=${REPLACE_DATABASE_USR} --password=${REPLACE_DATABASE_PSW}'
            }
        }
        stage('Push into ecr-Repository') {
            when {
                expression { GIT_BRANCH == 'origin/master' || GIT_BRANCH == 'origin/dev' }
            }
            steps {
                sh 'docker push ${REPLACE_ECR_FRONTEND}:${IMAGE_TAG}'
                sh 'docker push ${REPLACE_ECR_BACKEND}:${IMAGE_TAG}'
            }
        }
        stage('Run') {
            when {
                expression { GIT_BRANCH == 'origin/master' || GIT_BRANCH == 'origin/dev' }
            }
            environment {
                STAGING_DATABASE = credentials('STAGING_DATABASE_CREDENTIALS')
                TEST_DATABASE = credentials('TEST_DATABASE_CREDENTIALS')
            }
            steps {
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
