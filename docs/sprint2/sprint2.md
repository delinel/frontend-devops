# Sprint2

## Etape 1: Add Nexus container

- Adding service account
```
sudo useradd --system --no-create-home nexus
```
- Creating necessory folder for Nexus (persistent volume)
```
sudo mkdir -p /nexus/data
sudo chown -R 200 /nexus/data
```
- Create compose file for nexus
```
cd /projects/frontend-devops
vi files/docker-compose_nexus.yml
```
- Run container through compose file
```
docker compose -f files/docker-compose_nexus.yml up -d
sudo firewall-cmd --permanent --add-port=8081/tcp
sudo firewall-cmd --reload
```

- Verify the volumes (persist data)
```
docker inspect nexus-app -f "{{ .Mounts }}"
```
**open nexus on browser http://192.168.1.80:8081**
- Authenticate with admin account, the default admin password is in the file **/nexus/data/admin.password**
- Create two repositories **Release** and **Snapshot**
![alt text](image.png)



## Etape 2: Add Jenkins container

- Adding service account jenkins
```
sudo adduser -d /home/jenkins -s /bin/bash jenkins
sudo usermod -aG wheel,docker jenkins
#### Add visudo #####
jenkins ALL=(ALL) NOPASSWD:ALL
```
- Check the groups of user
```
id jenkins
#### or #####
groups jenkins
```
- Creating necessory folder for jenkins (persistent volume)
```
sudo mkdir -p /jenkins/data
sudo chown -R jenkins:jenkins /jenkins
```
- Create Dockerfile for jenkins to customize uid and gid
```
vi files/Dockerfile_jenkins
cd files
```
- Build our jenkins image with specifics values ARG and push images to registry
```
docker build -t myjenkins:1.0.0 --build-arg USER_ID=1002 --build-arg GROUP_ID=1002 -f Dockerfile_jenkins .
docker tag myjenkins:1.0.0 delinel/devops:myjenkins-1.0.0
docker push delinel/devops:myjenkins-1.0.0
```
- Create compose file for jenkins
```
cd /projects/frontend-devops
vi files/docker-compose_jenkins.yml
```
- Run container through compose file
```
docker compose -f files/docker-compose_jenkins.yml up -d
sudo firewall-cmd --permanent --add-port=8084/tcp
sudo firewall-cmd --reload
```

- Verify the volumes (persist data)
```
docker inspect jenkins-app -f "{{ .Mounts }}"
```
**open nexus on browser http://192.168.1.80:8084 or http://192.168.1.80:8084/jenkins (if 404 Not found)**
- Authenticate with admin account, the default admin password is in the file **/jenkins/data/secrets/initialAdminPassword**
- Install necessary plugins **(nexus-artifact-uploader, blue ocean, maven, java jdk, git, pipeline, rbac stratégies, metrics, cloudbees, ssh, Docker Pipeline, ...)**
- Configure tools (Maven, ...)
- Create users and roles https://medium.com/@srghimire061/how-to-manage-users-and-roles-in-jenkins-fe6a7a8be344#:~:text=Log%20in%3A%20enter%20your%20username,then%20click%20Create%20User%20button.
- Create user in Nexus who make upload of artifact
- we should add a Jenkins credential of the kind "Username with password" with a valid login to our Nexus instance, and let's give it an ID of "nexus-credentials."
- We can create two types of job to run pieline (**Build Maven Project** or **Pipeline**)

### Build Maven Project
- Source Code Management
![image](https://github.com/user-attachments/assets/bb356c9e-61ee-42ec-a676-4a981e99e7c8)

- Build
![image](https://github.com/user-attachments/assets/fbc4f48a-73cf-4792-9298-41b2693a28aa)

- Post Steps
![image](https://github.com/user-attachments/assets/b9c47a0a-97fe-45da-8d30-f080fb0cc131)

![image](https://github.com/user-attachments/assets/aa6ad722-1a40-4afb-b35a-1c0d579fe83b)


### Pipeline
- Just write the Pipeline script
https://github.com/delinel/frontend-devops/blob/develop/files/Jenkinsfile


  
  
source url: https://dzone.com/articles/jenkins-publish-maven-artifacts-to-nexus-oss-using

## Etape 3: Upload artifact trough Maven
- Edit the **pom.xml** file with the following settings
```
  <distributionManagement>
    <repository>
      <id>nexus-rele</id>
      <name>Releases</name>
      <url>http://192.168.1.80:8081/repository/Release-rep/</url>
    </repository>
    <snapshotRepository>
      <id>nexus-snap</id>
      <name>Snapshot</name>
      <url>http://192.168.1.80:8081/repository/Snapshot-rep/</url>
    </snapshotRepository>
  </distributionManagement>
```
where
- **id**: custum repository name
- **name**: type of repository
- **url**: nexus url repository
  
- Edit or create **~/.m2/settings.xml** file to define credentials user who upload file in Nexus
```
<settings>
  <servers>
    <server>
        <id>nexus-rele</id>
        <username>user-nexus</username>
        <password>xxxxxxxx</password>
    </server>
    <server>
        <id>nexus-snap</id>
        <username>user-nexus</username>
        <password>xxxxxxxx</password>
    </server>
  </servers>
</settings>
```
- Test upload trough Maven
```
mvn deploy
```
Browse Nexus Repository and check new artifact
