call mvn -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,trace clean spring-boot:run -DallGames
pause