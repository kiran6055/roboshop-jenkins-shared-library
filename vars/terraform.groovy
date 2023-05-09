def call () {
  pipeline {

//    options {
//      ansiColor('xterm')
//    }

    agent {
      node {
          label 'workstation'
      }
    }

    parameters {
      string(name: 'INFRA_ENV', defaultValue: '', description: 'Enter Env like dev or prod')
      choice(name: 'Action', choices: ['apply', 'destroy'], description: 'pick something')
    }

    stages {

      stage('Terraform init') {
        steps {
          sh "terraform init -backend-config=env-${INFRA_ENV}/state.tfvars"
        }
      }

      stage('Terraform apply') {
        steps {
          sh "terraform ${ACTION} -auto-approve -var-file=env-${INFRA_ENV}/main.tfvars"
        }

      }
    }

    post {
      always {
        cleanWs()
      }
    }

  }
}
