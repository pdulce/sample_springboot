// Funciona de arranque del proceso de compilacion
// devuelve 0 si OK  y <>0 si ERROR
def start() {
    log.info "PRUEBAS UNITARIAS"
    int result = 0;
    
	configFileProvider([configFile(fileId: 'backend-settings', variable: 'MAVEN_SETTINGS')]) {
			sh "mvn test -s ${MAVEN_SETTINGS} "
	}
	junit allowEmptyResults: true, healthScaleFactor: 1.0, keepLongStdio: true, testResults: "**/target/surefire-reports/*.xml"
    return result; 
}
return this;
