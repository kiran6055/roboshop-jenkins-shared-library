def compile() {
    if (app_lang == "nodejs") {
        sh 'npm install'
    }


    if (app - lang == "java") {
        sh 'mvn package'
    }

}
