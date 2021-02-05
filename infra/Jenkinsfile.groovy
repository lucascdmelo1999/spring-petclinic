node('master'){
    def app
    try{
       stage('Limpeza do cache') {
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true,
                    patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                               [pattern: '.propsfile', type: 'EXCLUDE']])
 
       }
 
        stage('Compilação do maven e Teste'){
            checkout scm
            sh './mvnw clean compile package test'
 
        }
       /*stage('SonarQube analysis') {
			    withSonarQubeEnv('sonarqube') {
				    sh './mvnw sonar:sonar -Dsonar.projectKey=sonar-report -Dsonar.host.url=https://host.docker.internal:9000/ -Dsonar.login=d2a6fc0ef08bc0595bd44bbcc265b3b6fd4efd07'

			    }
            }*/
            stage('logout'){
                sh 'docker logout'
            }
            stage('login'){
                sh 'docker login -u admin -p admin localhost:8083'
            }
            stage("Build dockerfile"){​​​​​​​​
                sh 'docker build -f ./Dockerfile.cp -t localhost:8083/petclinic:latest .'

            }​​​​​​​​
            stage('Push image') {​​​​​​​​
                sh 'docker push localhost:8083/petclinic:latest'
            }​​​​​​​​
 
        
        
 
    }catch (exec)
    {
        currentBuild.result = 'FAILURE'
        throw new Exception(exec)
        
    }finally{
       
 
      
       
    } 
}