def call() {
  if (!env.sonar_extra_opt) {
    env.sonar_extra_opt = " "
  }
  try {
    node('JenkinsAgent') {

      stage('checkout') {
        cleanWs()
        git branch: 'main', url: "https://github.com/kirankumar7163/${component}"
        sh 'env'
      }

      stage('compile/build') {
        common.compile()
      }

      stage('Unit Tests') {
        common.unittests()
      }

      stage('Quality Control') {
        SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
        SONAR_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.password  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
        wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
          //sh "sonar-scanner -Dsonar.host.url=http://172.31.2.94:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=${component} -Dsonar.qualitygate.wait=true ${sonar_extra_opt}"
          sh "echo sonar scan"
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

