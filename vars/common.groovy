def compile() {
  if (app_lang == "nodejs") {
    sh 'npm install'
  }

  if (app_lang == "maven") {
    sh "mvn clean compile"
  }
  if (app_lang == "golang") {
    sh "go mod init dispatch"
    sh "go get"
    sh "go build"
  }

  // this used to build the node JS in docker images below after -t which is copied from amazonecr like different componet start from number paster here tag-name which is already given variable
 sh "docker build -t 086083061026.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME} ."

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
  if (app_lang == "golang") {
    sh 'go test'
  }
}

def email(email_note) {
  mail bcc: '', body: "Job Failed - ${JOB_BASE_NAME}\nJenkins URL - ${JOB_URL}", cc: '', from: 'kiran6055@gmail.com', replyTo: '', subject: "Jenkins Job Failed - ${JOB_BASE_NAME}", to: 'kirandevopskumar@gmail.com'
}

def artifactPush() {
  // this is used to push the code to amazon ecr for that we need to create a ecr in amason with COMPONENT like shipping payment after that we need to copy the  1 link give below 3 link after that docker build in above copy after -t paste here to push the code
  sh "aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 086083061026.dkr.ecr.us-east-1.amazonaws.com"
  sh "docker push 086083061026.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}"

// this is used to push the articat to remote repository like sona nexus
//  sh "echo ${TAG_NAME} >VERSION"

//  if (app_lang == "nodejs") {
//    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js VERSION ${extraFiles}"
//  }
//
//  if (app_lang == "nginx" || app_lang == "python") {
//    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip * -x Jenkinsfile ${extraFiles}"
//  }

//  if (app_lang == "maven") {
//    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip * ${COMPONENT}.jar VERSION ${extraFiles}"
//  }

//  if (app_lang == "golang") {
//    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip * x Jenkinsfile ${extraFiles}"
//  }


//  NEXUS_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names nexus.password  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
//  NEXUS_USER = sh ( script: 'aws ssm get-parameters --region us-east-1 --names nexus.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
//  wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${NEXUS_PASS}", var: 'SECRET']]]) {
//    sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.12.164:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
//  }

}