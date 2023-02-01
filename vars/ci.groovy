def call() {
    try {

        pipeline {

            agent {
                label 'JenkinsAgent'
            }

            stages {

                stage('Compile/Build') {
                    steps {
                        script {
                            common.compile()
                        }
                    }
                }

                stage('Unit Tests') {
                    steps {
                        script {
                            common.unittests()
                        }
                    }
                }

                stage('Quality Control') {
                    environment {
                        SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                        SONAR_PASS = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.password  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                    }
                    steps {

                        sh "sonar-scanner -Dsonar.host.url=http://172.31.2.94:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=$SONAR_PASS} -Dsonar.projectKey=cart"
                    }
                }

                stage('Upload Code to Centralized Place') {
                    steps {
                       echo 'Upload'
                    }
                }


            }

        }
    } catch (Exception e) {
      common.email("Failed")
    }

}

