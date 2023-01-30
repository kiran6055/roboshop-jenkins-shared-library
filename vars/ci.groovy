def call() {
    pipeline {

        agent {
            label 'JenkinsAgent'
        }

        stages {

            stage('Compile/Build') {
                steps {
                    script {
                        common.compile()
                    }
                }
            }

            stage('Unit Tests') {
                steps {
                    echo 'Unit Tests'
                }
            }

            stage('Quality Control') {
                steps {
                    echo 'Quality Control'
                }
            }

            stage('Upload Code to Centralized Place') {
                steps {
                    echo 'Upload'
                }
            }


        }

    }
}

