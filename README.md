# DS Portal
## http://localhost:8080/ds-portal/index.html
### UI Migration Step
```
1.cd /{ui_project_path}
2.npm install
3.vi vue.config.js -> modify publicPath to ds-portal
4.npm run build
5.cp -r dist/* to /{project_path}/src/main/resources/static
```
### Bootstrap Step
```
1.cd /{project_path}
2.sh ./gradlew bootJar
3.cd bulid/libs
4.java -jar -Dspring.config.location=classpath:develop/ ocean-acs-portal-0.0.1-SNAPSHOT.jar
```
