# JEEWS-SempicLesYeux

Application de gestion d'albums photos et d'annotation des photos
utilisant les technologies du web sémantique.

## Démarrer l'Application

Suivre les instructions ci-dessous pour exécuter l'application sur votre environnement

### Prérequis

Télécharger le serveur d'application [Apache TomEE Plume](https://www.apache.org/dyn/closer.cgi/tomee/tomee-8.0.0/apache-tomee-8.0.0-plume.tar.gz)

Dans le fichier *conf/tomcat-users.xml*, ajouter la balise :

```
<user password="admin" roles="manager-script,admin" username="admin"/>
```

Télécharger la dernière version du serveur SPARQL [Apache Jena Fuseki](https://jena.apache.org/download/), à placer dans le même répertoire que *SempicRDF*

Il est également nécessaire d'avoir un serveur MySQL qui tourne, dans lequel vous aurez créé une nouvelle base de données nommée *SempicDB*

### Exécution

Lancer l'application depuis le Framework de votre choix, ou en exécutant les commandes :

```
cd JEEWSAppli/
mvn package tomee:exec
jar -jar target/JEEWSAppli-1-0-exec.jar
```

## Auteurs

* **Benjamin Poulet**
* **Adem Gurbuz**
* **Edouard Delatre**
