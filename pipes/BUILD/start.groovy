import frmwork.tools.Configuracion
import frmwork.tools.Helm

// Funciona de arranque del proceso de compilacion
// devuelve 0 si OK  y <>0 si ERROR
def start() {
	log.info "[BUILD-start.groovy] PACKAGE"
	
	def conf = Configuracion.instance
	def nuevoTag = conf.versionEjecucion.toDeployArtifact()
	
	sh "pwd"
	sh "ls -la"
	configFileProvider(
			[configFile(fileId: 'backend-settings', variable: 'MAVEN_SETTINGS')]){
		sh "mvn versions:set -DnewVersion=${nuevoTag} -s $MAVEN_SETTINGS "
		sh "mvn package -DskipTests -s  $MAVEN_SETTINGS"
	}
	sh "ls -la ${WORKSPACE}/target"
	sh "cp ${WORKSPACE}/target/*.jar a/ot_mac0.jar"

	log.info("[BACKEND/build] Inicio de proceso helm")
	Helm.generarArtefactoByArtifactsHelm_Xml()
	log.info("[BACKEND/build] Fin de proceso helm")


	return 0; // devolveremos como error la fase de compilacion para que no continue el pipe
}
return this;

