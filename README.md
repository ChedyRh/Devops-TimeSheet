# Devops-TimeSheet
Application des techniques DevOps sur un projet Spring Boot.

Outils utilisés : 
Maven, 
Log4j, 
JUnit, 
Sonar, 
Nexus, 
Jenkins, 
Docker/Docker hub

le travail effectuer est effectuer sur le service employee  : 
- creation du classe des test EmployeTest et saisie des tests unitaire
- Construction du projet en utilisant Maven
- journalisation en utilisant Log4j
- Analyse et amelioration du qualité du code en utulisant Sonar
- déploiement sous Nexus
- Génération de l'image Docker et push sur docker hub
- Construction d'une pipeline jenkins qui récupere le code du repository Git permet d'automatiser les 5 étape précédentes et qui se déclanche aprés tout push éffectuer au niveau de repository 
et envoi un email en cas d'échec

le pipeline Jenkins
pipeline {
    agent any
    
    environment{
      dockerImage='' 
      registry = '........'
      registryCredential = "dockerHub"
    }
    stages {
        stage('Git Checkout') {
            steps {
                echo  'pulling...'
                git branch:'.......', url:'.........'

                }

            }
        stage('Maven build') {
            steps{
                bat "mvn clean install"
            }
            
        }
        stage('Sonar') {
            steps{
                bat "mvn package sonar:sonar"
            }
            
        }
        stage('Nexus') {
            steps{
                bat " mvn package deploy:deploy-file -DgroupId=tn.esprit.spring -DartifactId=Timesheet -Dversion=2.0 -DgeneratePom=true -Dpackaging=war -DrepositoryId=deploymentRepo -Durl=http://localhost:8081/repository/maven-releases/ -Dfile=target/Timesheet-2.0.war "
                 }
                
             }
              stage('Building image') {
            steps{
                 script {
                    dockerImage = docker.build registry
                    }
                 }
            }
        stage('Push Image to DockerHub') {
            steps{    
                 script {
                          docker.withRegistry( '', registryCredential ) {
                         dockerImage.push()
                        }
                    }
                }
            }
        }
        post {
            failure{
                emailext attachLog: true, body: 'Build error in ur pipeline', subject: 'Build error in ur pipeline', to: 'exple@gmail.com'
            }
            always{
                cleanWs()
            }
        }
    }

