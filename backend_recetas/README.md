# SCDS_Formativa_Backend
Backend de la web de recetas

# Comando SonarQube
# Modificar por comando que da la generacion del proyecto en SonarQube
mvn clean verify sonar:sonar "-Dsonar.projectKey=SCDS_BackEnd" "-Dsonar.projectName=SCDS_BackEnd" "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=sqp_38a8983e3b8606ccee5372a8c6cd5c687399ba0e"

# Comando sonar saltando pruebas unitarias
mvn clean verify sonar:sonar "-Dsonar.projectKey=SCDS_BackEnd" "-Dsonar.projectName=SCDS_BackEnd" "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=sqp_38a8983e3b8606ccee5372a8c6cd5c687399ba0e" "-Dsonar.coverage.exclusions=**/*" "-Dsonar.coverage.enabled=false"