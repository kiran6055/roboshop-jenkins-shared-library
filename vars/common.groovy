def compile() {
  if (app_lang == "nodejs") {
    sh 'npm install'
  }

  if (app_lang == "maven") {
    sh 'mvn package'
  }


  if (app_lang == "golang") {
    sh 'go mod init dispatch'
    sh 'go get'
    sh 'go build'

  }

}

def unittests() {
  if (app_lang == "nodejs") {
    // developer is ,missing testcases in our projecct, so we are skipping test cases
    sh 'npm test'
  }


  if (app_lang == "maven") {
    sh 'mvn test'
  }

  if (app_lang == "python") {
    sh 'python3 -m unittest'
  }

}

def email(email_note) {
  mail bcc: '', body: 'Test', cc: '', from: 'kirankumar7163@gmail.com', replyTo: 'kirankumar.nagaraja@gmail.com', subject: 'Test From Jenkins', to: ''
}
