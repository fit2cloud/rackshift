pipeline {
    agent {
        node {
            label 'rackshift'
        }
    }
    options { quietPeriod(600) }
    environment { 
        IMAGE_NAME = 'rackshift'
        IMAGE_PREFIX = 'registry.cn-qingdao.aliyuncs.com/rackshift'
    }
    stages {
        stage('Build/Test') {
            steps {
                configFileProvider([configFile(fileId: 'rackshift-maven', targetLocation: 'settings.xml')]) {
                    sh "mvn clean package --settings ./settings.xml"
                }
            }
        }
        stage('Docker build & push') {
            steps {
                sh "docker build --build-arg MS_VERSION=\${TAG_NAME:-\$BRANCH_NAME}-\${GIT_COMMIT:0:8} -t ${IMAGE_NAME}:\${TAG_NAME:-\$BRANCH_NAME} ."
                sh "docker tag ${IMAGE_NAME}:\${TAG_NAME:-\$BRANCH_NAME} ${IMAGE_PREFIX}/${IMAGE_NAME}:\${TAG_NAME:-\$BRANCH_NAME}"
                sh "docker push ${IMAGE_PREFIX}/${IMAGE_NAME}:\${TAG_NAME:-\$BRANCH_NAME}"
            }
        }
    }
}