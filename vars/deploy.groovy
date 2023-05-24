def call () {
  pipeline {
    agent {
      label 'JenkinsAgent'
    }
    parameters {
      string(name: 'APP_ENV', defaultValue: '', description: 'Enter Env like dev or prod')
      choice(name: 'Action', choices: ['apply', 'destroy'], description: 'Enter COMPONENT name')
      choice(name: 'APP_VERSION', choices: ['apply', 'destroy'], description: 'Enter Version number')
    }
    stages {

      stage('RunDeployment') {
        steps {
          sh..
            aws ssm put-parameter --name "${APP_ENV}.${COMPONENT}.APP_VERSION" -- type "string" --value "${APP_VERSION}" --overwrite
        }
      }

    }
  }
}
