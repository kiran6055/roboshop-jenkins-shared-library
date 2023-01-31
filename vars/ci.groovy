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
                    script {
                      common.unittests
                    }
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

