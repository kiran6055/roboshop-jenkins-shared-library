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

    options {
      ansiColor('xterm')
    }

    stages {
      stage('Run Deployment') {
        steps {
          sh '''
            aws ssm put-parameter --name "${APP_ENV}.${component}.APP_VERSION" --type "String" --value "${APP_VERSION}" --overwrite
            aws ec2 describe-instances  --filters "Name=tag:Name,Values=${APP_ENV}-${component}" | jq ".Reservations[].Instances[].PrivateIpAddress" >/tmp/hosts
            
            ansible-playbook -i /tmp/hosts deploy.yml -e component=${component} -e env=${APP_ENV}

          '''
        }
      }

    }
  }
}
