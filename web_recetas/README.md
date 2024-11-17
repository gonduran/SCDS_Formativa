# SCDS_Formativa_Frontend
Frontend de la web de recetas

# Comando SonarQube
# Modificar por comando que da la generacion del proyecto en SonarQube
mvn clean verify sonar:sonar "-Dsonar.projectKey=SCDS_FrontEnd" "-Dsonar.projectName='SCDS_FrontEnd'" "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=sqp_66f5b040abea16fd0bba02680eaa8dd469c9fabb"

# Comando sonar saltando pruebas unitarias
mvn clean verify sonar:sonar "-Dsonar.projectKey=SCDS_FrontEnd" "-Dsonar.projectName=SCDS_FrontEnd" "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=sqp_66f5b040abea16fd0bba02680eaa8dd469c9fabb" "-Dsonar.coverage.exclusions=**/*" "-Dsonar.coverage.enabled=false"