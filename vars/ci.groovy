def call() {
  try {
    node('JenkinsAgent') {
      stage('checkout') {
        cleanWs()
        git branch: 'main', url: "https://github.com/kirankumar7163/${component}"
        sh 'env'
      }
      stage('compile/build') {
          common.compile
      }
      stage ('unit test') {
          common.unittest
      }
      stage('qualitycontrol') {
          SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
          SONAR_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.password  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
          wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
              sh "sonar-scanner -Dsonar.host.url=http://172.31.2.94:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=cart"
          }
      }
      stage('Upload Code to Centralized Place') {
        echo 'upload'
      }





    }
  } catch (Exception e) {
      common.email("Failed")
  }

}

