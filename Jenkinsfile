pipeline {
    agent {
        node {
            label 'metersphere'
        }
    }
    options { quietPeriod(600) }
    environment { 
        IMAGE_NAME = 'rackshift'
        IMAGE_PREFIX = 'registry.cn-qingdao.aliyuncs.com/rackshift'
    }
    stages {
        stage('Build') {
            steps {
                sh "rm -rf ${WORKSPACE}/rackshift-server/src/main/resources/static"
                sh "mkdir -p ${WORKSPACE}/rackshift-server/src/main/resources/static"
                sh "cd ${WORKSPACE}/rackshift-web"
                sh "cnpm install"
                sh "cnpm run build"
                sh "cp -r ${WORKSPACE}/rackshift-web/dist/* ${WORKSPACE}/rackshift-server/src/main/resources/static"
                sh "cd ${WORKSPACE}/rackshift-server"
                sh "mvn clean install -DskipTests"
            }
        }
        stage('Docker build & push') {
            steps {
                sh "docker build -t ${IMAGE_PREFIX}/${IMAGE_NAME}:v${BRANCH_NAME}-dev ."
                sh "docker push ${IMAGE_PREFIX}/${IMAGE_NAME}:v${BRANCH_NAME}-dev"
            }
        }
    }
}