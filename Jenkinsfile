pipeline {
   agent any
   stages {
      stage ('Init') {
         steps {
            echo "Testing ..."
         }
      }
      stage ('Build') {
         steps {
            echo "Building ..."
            sh 'mvn clean package'
         }
         post {
            success {
               echo 'Archiving artifacts'
               archiveArtifacts artifacts:'**/target/*.jar'
            }
         }
      }
      stage ('Deploy') {
         steps {
            echo "Deploying ...."
            sh '**/target/java -jar schoolbus-reservation-system-0.0.1-SNAPSHOT.jar'
         }
      }
   }
}