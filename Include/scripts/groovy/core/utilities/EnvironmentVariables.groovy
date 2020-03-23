package core.utilities

public class EnvironmentVariables {
	
	static void populateKatalonProfile(){
		addGlobalVariableAtRunTime("uidp_environment", System.getenv("UIDP_ENV"))
		addGlobalVariableAtRunTime("uidp_client_id", System.getenv("UIDP_CLIENT_ID"))
		addGlobalVariableAtRunTime("uidp_client_secret", System.getenv("UIDP_CLIENT_SECRET"))
		addGlobalVariableAtRunTime("uidp_scope", System.getenv("UIDP_SCOPE"))
		addGlobalVariableAtRunTime("uidp_grant_type", System.getenv("UIDP_GRANT_TYPE"))
		addGlobalVariableAtRunTime("selvregistrering_passord", System.getenv("SELVREG_PASSWORD"))
		addGlobalVariableAtRunTime("selvregistrering_pin", System.getenv("SELVREG_PIN"))
	}
	
	static void addGlobalVariableAtRunTime(String name, def value){
		GroovyShell shell = new GroovyShell()
		MetaClass mc = shell.evaluate("internal.GlobalVariable").metaClass
		String getterName = "get" + name.capitalize()
		mc.'static'."$getterName" = { -> return value }
		mc.'static'."$name" = value
	}
	
}
