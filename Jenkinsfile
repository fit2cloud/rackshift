pipeline {
    agent {
        node {
            label 'metersphere'
        }
    }
    options { quietPeriod(600) }
    environment {
        IMAGE_NAME = 'rackshift'
        IMAGE_PREFIX = 'registry.cn-qingdao.aliyuncs.com/x-lab'
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
                    cd ${WORKSPACE}/rackshift-dhcp-proxy
                    mvn clean install
                   '''
            }
        }
        stage('Docker build & push') {
            steps {
                sh '''
                    docker login registry.cn-qingdao.aliyuncs.com -u ${DOCKER_USR} -p ${DOCKER_PSW}
                    cd ${WORKSPACE}/rackshift-server
                    mvn clean install -DskipTests
                    docker build -t ${IMAGE_PREFIX}/${IMAGE_NAME}:v${BRANCH_NAME}-dev .
                    docker push ${IMAGE_PREFIX}/${IMAGE_NAME}:v${BRANCH_NAME}-dev

                    cd ${WORKSPACE}/rackshift-dhcp-proxy
                    docker build -t ${IMAGE_PREFIX}/rackshift-dhcp-proxy:v${BRANCH_NAME} .
                    docker push ${IMAGE_PREFIX}/rackshift-dhcp-proxy:v${BRANCH_NAME}
                   '''
            }
        }
    }
}
