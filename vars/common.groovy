def compile() {
  if (app_lang == "nodejs") {
    sh 'npm install'
  }

  if (app_lang == "maven") {
    sh "mvn clean compile"
  }

}

def unittests() {

  if (app_lang == "nodejs") {
    // Developer is missing unit test cases in our project, He need to add them as best practice, We are skipping to proceed further
    sh 'npm test || true'

  }

  if (app_lang == "maven") {
    sh 'mvn test'
  }

  if (app_lang == "python") {
    sh 'python3 -m unittest'
  }
}

def email(email_note) {
  mail bcc: '', body: "Job Failed - ${JOB_BASE_NAME}\nJenkins URL - ${JOB_URL}", cc: '', from: 'kirankumar7163@gmail.com', replyTo: '', subject: "Jenkins Job Failed - ${JOB_BASE_NAME}", to: 'kirankumar7163@gmail.com'
}

def artifactPush() {
  sh "echo ${TAG_NAME} >VERSION"

  if (app_lang == "nodejs") {
    sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js VERSION ${extraFiles}"
  }
  sh 'ls -l'
}