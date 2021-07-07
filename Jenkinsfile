pipeline {
    agent {
        node {
            label 'metersphere'
        }
    }
    options { quietPeriod(600) }
    environment { 
        IMAGE_NAME = 'rackshift'
        ORG = 'x-lab'
        IMAGE_PREFIX = 'registry.cn-qingdao.aliyuncs.com/rackshift'
        DOCKER = credentials('x-lab')
    }
    stages {
        stage('Build') {
            steps {
                sh '''
                    rm -rf ${WORKSPACE}/rackshift-server/src/main/resources/static
                    mkdir -p ${WORKSPACE}/rackshift-server/src/main/resources/static
                    cd ${WORKSPACE}/rackshift-web
                    cnpm install
                    cnpm run build
                    cp -r ${WORKSPACE}/rackshift-web/dist/* ${WORKSPACE}/rackshift-server/src/main/resources/static
                    mvn clean install -DskipTests
                   '''
            }
        }
        stage('Docker build & push') {
            steps {
                sh '''
                    docker login registry.cn-qingdao.aliyuncs.com -u ${DOCKER_USR} -p ${DOCKER_PSW}
                    cd ${WORKSPACE}/rackshift-server
                    docker build -t ${IMAGE_PREFIX}/${ORG}/${IMAGE_NAME}:v${BRANCH_NAME}-dev .
                    docker push ${IMAGE_PREFIX}/${ORG}/${IMAGE_NAME}:v${BRANCH_NAME}-dev
                   '''
            }
        }
    }
}