def call () {
  pipeline {
    agent {
      label 'JenkinsAgent'
    }
    parameters {
      string(name: 'APP_ENV', defaultValue: '', description: 'Enter Env like dev or prod')
      string(name: 'component', defaultValue: '', description: 'Enter COMPONENT name')
      string(name: 'APP_VERSION', defaultValue: '', description: 'Enter Version number')
    }

    options {
      ansiColor('xterm')
    }

    environment {
      ssh=credentials('ssh')
    }

    stages {

      stage('Run Deployment') {
        steps {
          sh '''
            aws ssm put-parameter --name "${APP_ENV}.${component}.APP_VERSION" --type "String" --value "${APP_VERSION}" --overwrite
            #this is for immutable approach
            aws autoscaling start-instance-refresh --auto-scaling-group-name ${APP_ENV}-${component} --preferences '{"InstanceWarmup": 240, "MinHealthyPercentage": 50, "SkipMatching": false}'
           
           
            # this is for mutable approach
            aws ec2 describe-instances  --filters "Name=tag:Name,Values=${APP_ENV}-${component}" | jq ".Reservations[].Instances[].PrivateIpAddress" >/tmp/hosts
            ansible-playbook -i /tmp/hosts deploy.yml -e component=${component} -e env=${APP_ENV} -e ansible_user=${ssh_USR} -e ansible_password=${ssh_PSW}

          '''
        }
      }

    }
  }
}
