pipeline {
    agent any

    environment {
        IMAGE_TAG = "${GIT_BRANCH == 'master' ? 'staging' : GIT_BRANCH == 'dev' ? 'latest' : GIT_BRANCH}"
        REPLACE_DOCKER_ENV = "${GIT_BRANCH == 'master' ? 'ssh://staging.local' : 'ssh://test.local'}"
        REPLACE_DATABASE_URL = "${GIT_BRANCH == 'master' ? STAGING_DATABASE_URL : TEST_DATABASE_URL}"
        REPLACE_OAUTH_AUTHORIZEURL = "${GIT_BRANCH == 'master' ? STAGING_OAUTH_AUTHORIZE : TEST_OAUTH_AUTHORIZE}"
        REPLACE_OAUTH_ACCESSTOKENURL = "${GIT_BRANCH == 'master' ? STAGING_OAUTH_ACCESSTOKEN : TEST_OAUTH_ACCESSTOKEN}"
        REPLACE_OAUTH_CALLBACK = "${GIT_BRANCH == 'master' ? STAGING_OAUTH_CALLBACK : TEST_OAUTH_CALLBACK}"
        REPLACE_OAUTH_CLIENTID = "${GIT_BRANCH == 'master' ? STAGING_OAUTH_CLIENTID : TEST_OAUTH_CLIENTID}"
        REPLACE_OAUTH_SECRET = "${GIT_BRANCH == 'master' ? STAGING_OAUTH_SECRET : TEST_OAUTH_SECRET}"
        CREDENTIALS = "${GIT_BRANCH == 'master' ? 'STAGING_DATABASE_CREDENTIALS' : 'TEST_DATABASE_CREDENTIALS'}"
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
                sh './gradlew -PktorImage=${REPLACE_ECR_BACKEND} -PktorTag=${IMAGE_TAG} publishImageToLocalRegistry'
            }
        }
        stage('Update Database') {
            when {
                expression { GIT_BRANCH == 'master' || GIT_BRANCH == 'dev' }
            }
            environment {
                REPLACE_DATABASE = credentials("${CREDENTIALS}")
            }
            agent {
                docker { image 'liquibase/liquibase:latest' }
            }
            steps {
                sh 'liquibase update --changelog-file=/infrastructure/src/jvmMain/resources/db/changelog-root.json --url=${REPLACE_DATABASE_URL} --username=${REPLACE_DATABASE_USR} --password=${REPLACE_DATABASE_PSW}'
            }
        }
        stage('Push into ecr-Repository') {
            when {
                expression { GIT_BRANCH == 'master' || GIT_BRANCH == 'dev' }
            }
            steps {
                sh 'docker push ${REPLACE_ECR_FRONTEND}:${IMAGE_TAG}'
                sh 'docker push ${REPLACE_ECR_BACKEND}:${IMAGE_TAG}'
            }
        }
        stage('Run') {
            when {
                expression { GIT_BRANCH == 'master' || GIT_BRANCH == 'dev' }
            }
            environment {
                REPLACE_DATABASE = credentials("${CREDENTIALS}")
            }
            steps {
                script {    
                    docker.withServer("${REPLACE_DOCKER_ENV}") {
                        sh 'docker compose stop'
                        sh 'docker compose up --detach --pull always --remove-orphans'
                    }
                }
            }
        }
    }
}
