def call () {
  pipeline {
    agent {
      label 'JenkinsAgent'
    }
    parameters {
      string(name: 'APP_ENV', defaultValue: '', description: 'Enter Env like dev or prod')
      string(name: 'Action', defaultValue: '', description: 'Enter COMPONENT name')
      string(name: 'APP_VERSION', defaultValue: '', description: 'Enter Version number')
    }
    stages {
      stage('Run Deployment') {
        steps {
          sh '''
            aws ssm put-parameter --name "${APP_ENV}.${component}.APP_VERSION" --type "String" --value "${APP_VERSION}" --overwrite
          '''
        }
      }

    }
  }
}
