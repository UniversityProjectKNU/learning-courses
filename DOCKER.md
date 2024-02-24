Start container locally:
docker run -d -p 3306:3306 --net learning-courses-net --name mysqldb --env-file db-env-local -v mysqldb_volume:/var/lib/mysql mysql:8.0.31
(3307 - external port, 3306 - internal port)
docker run -d -p 8080:8080 --net learning-courses-net --name learning-courses --env-file app-env-local learning-courses
(8080 - external port, 8080 - internal port)

docker build -t learning-courses .
(. - path to the Dockerfile)

docker exec -it container_name bash
(enter container with name container_name)

mysql -u root -p
(for bash in container open mysql)\