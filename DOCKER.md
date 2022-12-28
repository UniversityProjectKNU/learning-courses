docker run -d -p 3307:3306 --net learning-courses-net --name mysqldb --env-file mysql-env -v /C/Users/Admin/IntelliJProjects/learning-courses/.other/db:/var/lib/mysql mysql:8.0.31
(3307 - external port, 3306 - internal port)

docker build -t learning-courses .
(. - path to the Dockerfile)

docker run -d -p 8080:8080 --name learning-courses --net learning-courses-net --env-file app-env learning-courses 
(8080 - external port, 8080 - internal port)
