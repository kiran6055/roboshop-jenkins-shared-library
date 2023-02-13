def call () {
  pipeline {

    agent {
      node {
          label 'kiran'
      }
    }

    parameters {
      string(name: 'Infra env', defaultValue: '', description: 'enter env like dev or prod')
    }
    stages {
      stage('terraform init') {
        sh "terraform init -backend-config=env-${INFRA_ENV}/state.tfvars"
      }
      stage('tarraform apply') {
        sh "terraform apply -auto-approve -var-file=env-${INFRA_ENV}/main.tfvars"
      }

    }
  }

  post {
    always {
      cleanWS()
    }
  }
}

