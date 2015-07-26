# Quoi

Quoi de plus simple pour une application que de se configurer en lisant sa configuration depuis un fichier de propriétés ? Et bien dans le cas d'une application d'entreprise il se trouve que ce n'est pas si simple!

Les problèmes qui se posent sont les suivants:
  0. Comment lire des propriétés différentes selon la plateforme d'execution ?
  0. Comment protéger les valeurs sensibles tels que les mots de passe ?
  0. Comment permettre la modification des propriétés par les administrateurs après le déployement de l'appplication ?
  0. Comment valoriser certaines propriétés de manière dynamique à l'éxécution ?


Pour toutes les applications d'entreprise ces problèmatiques sont adressées avec plus ou moins d'élégance et plus ou moins de succès selon la culture et les compétences de l'équipe et aussi selon les technologies utilisées.


Ce projet est une démonstration de ce qu'il est possible de réaliser avec Spring et un peux d'organisation.

Pour le moment les points 1 et 4 sont adressé. Les autres points seront addressé dans un futur proche le temps que je mette au propre les solutions.


# Comment

### Spring’s PropertyPlaceholderConfigurer

Tout d'abord nous utiliseront le `PropertyPlaceholderConfigurer` pour valoriser des attributs dans nos fichiers de configuration xml et même dans le code source java en lisant des propriétés stoquées dans des fichiers `.properties`.

#### example
fichier: `db.properties`

	db.url = jdbc:mysql://localhost:3306/madatabase?profileSQL=true
	db.user = monlogin
	du.passwd = monpasswd

fichier: `applicationContext.xml`

	<context:property-placeholder location="classpath:config/db.properties" />

fichier: `database.xml`

	<bean id="dataSource" class="...">
		<property name="user" value="${db.user}" />
		<property name="password" value="${db.passwd}" />
	</bean>

fichier: `toto.java`

	@Value("${db.url}")
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}


A lui tous seul, `PropertyPlaceholderConfigurer` nous fournis déjà plusieurs services: La localisation et la lecture des fichiers `.properties` et l'extrapolation dans les fichiers de configurations et les sources java (i.e. remplacement des expressions `${key}` par la valeur correspondante). 


## Problème #1: Différentes properties selon la plateforme d'execution

C'est le problème le moins bien gérer. Ce problème n'est pas simple à apréhender par le tout venant je le concède, mais pour autant, avec les bons outils et la bonne organisation ce problème peut être résolu de manière élégante, simple et fiable.

Grace à la notion de profile (disponible à partir de Spring v3) il est possible de configurer Spring pour qu'il charge tel ou tel fichier de propriétés. Avec cette possibilité l'idée est d'avoir un fichier de configuration uniforme utilisant au maximum des expression `${key}` et d'avoir autant de fichier de propriétés que de plateforme cible. L'idée n'est pas nouvelle, ce qui est nouveau c'est le moyen de la mettre en oeuvre au travers la notion de profiles de Spring.

fichier: `applicationContext.xml`

	<!-- selon le profile demandé, Spring va executer l'un ou l'autre des blocks beans 
		 et donc lire l'un ou l'autre des fichiers de properties platform spécifique -->

	<beans profile="DEV INTEG QUALIF"> 	
		<context:property-placeholder location="classpath:config/services-DEV-INTEG-QUALIF.properties" />
	</beans>

	<beans profile="PROD"> 	
		<context:property-placeholder location="classpath:config/services-PROD.properties" />
	</beans>
	
	<beans profile="PREPROD">
		<context:property-placeholder location="classpath:config/services-PREPROD.properties" />
	</beans>


### Comment indiquer quel profile utiliser pour une execution

Il y a plusieurs façons d'indiquer quel profile doit être activé lors d'une execution:

- dans la ligne de commande: `-Dspring.profiles.active="PROD"`
- dans une variable d'environnement: `set SPRING_PROFILES_ACTIVE=PROD`
- dans une propriété de la servlet principale de Spring MVC.
- de manière programmatique: `ConfigurableEnvironment#setActiveProfiles(String... profiles)`

Bien sur le code d'initialisation du context ainsi que les fichiers de configuration Spring et les fichier properties de l'application devront être adaptés à la technique choisie.

Les tests unitaires dans `zc.study.spring.multipleplateformconfiguration` montrent comment le **problème #1** peut être résolu en utilisent plusieurs des techniques ci-dessus.


### Comment laisser le code découvrir lui-même quelle est la plateforme d'execution

Il est aussi possible, dans certains cas, de laisser le programme determiner seul quelle est la plateforme d'execution. Dans ce cas une fois le code de détection écrit et testé il n'y à plus rien à configurer au moment du lancement de l'application sur chaque plateforme, ce qui simplifie encore le deployement.

Pour ce faire il faut écrire une classe dérivée de `PropertySource<String>` comme ci-dessous:

	private static class CustomPropertySource extends PropertySource<String> {
		private String platform = null;
		
		public CustomPropertySource() { super("custom"); }

		@Override
		public String getProperty(String name) {
			if ("PLATFORM".equals(name)) {
	        	if (platform == null) platform = detectRunningPlatform();
	        	return platform;
			}

			return null;
		}
	    
		/**
		 * use any clever means to detect and categorize the running platform
		 */
		private String detectRunningPlatform() {
	    	// Often the running platform can be categorized by reading its IP address
	    	// other time it can be categorized by examining the file system (i.e. detecting or reading a file at well known place)
	    	
			return "PROD";
		}
	}



Et voila, mine de rien, nous venons de résoudre le **problème #4.**


## Encrypter les mots de passe

A suivre, le développement est en cours avec jasypt.





# references
http://www.summa-tech.com/blog/2009/04/20/6-tips-for-managing-property-files-with-spring
https://diarmuidmoloney.wordpress.com/2012/04/07/spring-environments/
http://stackoverflow.com/questions/9294187/how-to-add-properties-to-an-application-context
http://codesolid.com/spring-unit-testing-using-junit/
http://rpouiller.developpez.com/tutoriels/spring/tutoriel-tests-junit4-spring/
https://heuristicexception.wordpress.com/2008/10/09/secure-your-spring-application-configuration-with-jasypt/
http://www.jasypt.org/easy-usage.html