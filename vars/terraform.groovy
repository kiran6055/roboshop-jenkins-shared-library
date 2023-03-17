def call () {
  pipeline {

    options {
      ansiColor('xterm')
    }

    agent {
      node {
          label 'kiran'
      }
    }

    parameters {
      string(name: 'INFRA_ENV', defaultValue: '', description: 'Enter Env like dev or prod')
    }

    stages {

      stage('Terraform init') {
        steps {
          sh "terraform init -backend-config=env-${INFRA_ENV}/state.tfvars"
        }
      }

      stage('Terraform apply') {
        steps {
          sh "terraform apply -auto-approve -var-file=env-${INFRA_ENV}/main.tfvars"
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
