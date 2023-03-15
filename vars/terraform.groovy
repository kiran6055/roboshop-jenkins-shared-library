def call () {
  pipeline {

    agent {
      node {
          label 'kiran'
      }
    }

    parameters {
      string(name: 'INFRA_ENV', defaultValue: '', description: 'enter env like dev or prod')
    }
    stages {
      steps('terraform init') {
        sh "terraform init -backend-config=env-${INFRA_ENV}/state.tfvars"
      }
    }

    }
  }
