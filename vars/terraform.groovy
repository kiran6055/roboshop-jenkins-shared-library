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
  }
}