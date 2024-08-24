# Sprint 1 #

## Etape 1: Mise en place du serveur RHEL8
Code vagrant: https://github.com/delinel/Marry/tree/main/Vagrant/ionos

**Erreur rencontrée**
```
CentOS Stream 8 - AppStream                                                                                         0.0  B/s |   0  B     00:00    
Errors during downloading metadata for repository 'appstream':
  - Curl error (6): Couldn't resolve host name for http://mirrorlist.centos.org/?release=8-stream&arch=x86_64&repo=AppStream&infra=stock [Could not resolve host: mirrorlist.centos.org]
Error: Failed to download metadata for repo 'appstream': Cannot prepare internal mirrorlist: Curl error (6): Couldn't resolve host name for http://mirrorlist.centos.org/?release=8-stream&arch=x86_64&repo=AppStream&infra=stock [Could not resolve host: mirrorlist.centos.org]
```
**Solution**
```
sudo sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-*
sudo sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*
```

- Update packages and install basic tools
```
sudo yum update -y
sudo yum install epel-release -y
```

- Create the admin user to execute operations
```
ssh root@192.168.1.80
adduser -d /home/dely -s /bin/bash dely     #Create user
usermod -aG wheel dely                      #Add user to the root group
passwd dely                                 #Set password for user
```
- Permit user to user sudo before command without password
```
visudo
```
Add at the end of file
```
dely ALL=(ALL) NOPASSWD:ALL
```
- Exit terminal and reconnect with the new user
```
exit
ssh dely@192.168.1.80
```

**Personnalize the workspace**
- Edit the bashrc file
```
vi ~/.bashrc
```
- Add the variables
```
PS1='\[\033[00m\][\[\033[32m\]\u\[\033[36m\]@\[\033[33m\]\h\[\033[00m\]:\[\033[36m\]\w\[\033[00m\]]\[\033[00m\]\$' ==> prompt color
export HISTTIMEFORMAT="%F %T : " ==> horodatage of commands
```
```
source ~/.bashrc
```

**Generate the public and private keys**
- Edit the bashrc file
```
ssh-keygen -t rsa -b 4096
```

**Install java 17**
```
sudo yum install java-17-openjdk
```
- Content of file /etc/profile.d/java.sh
```
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-17.0.6.0.9-0.3.ea.el8.x86_64
export PATH="$JAVA_HOME/bin:$PATH"
```
- Set java environment variables
```
sudo chmod +x /etc/profile.d/java.sh
source /etc/profile.d/java.sh
java --version
```

**Install maven**
```
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
sudo tar xzf apache-maven-3.9.6-bin.tar.gz -C /opt
sudo mv /opt/apache-maven-3.9.6 /opt/maven
sudo vi /etc/profile.d/maven.sh
```
- Content of file /etc/profile.d/maven.sh
```
export M2_HOME=/opt/maven
export PATH=${M2_HOME}/bin:${PATH}
```

- Set maven environment variables
```
sudo chmod +x /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh
mvn --version
```

**Install Docker**

```
sudo yum install -y yum-utils device-mapper-persistent-data lvm2 
sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
sudo yum install docker-ce
sudo systemctl enable docker
sudo usermod -aG docker dely
```
- Set folder for the projects
```
sudo mkdir /projects    ## for the files code
sudo mkdir /docker      ## docker folder
sudo mkdir /logs        ## logs folder
sudo chown -R dely:dely /projects
```
- Set data-root for docker
 edit file **/etc/docker/daemon.json**
```
{
  "data-root": "/docker"
}
```
- Start docker service
```
sudo systemctl start docker
sudo systemctl status docker
docker --version
```

## Etape 2: Clone project through ssh and build artefact

- Copy the public key in github setting
```
cat ~/.ssh/id_rsa.pub
```
Github > Profile > Settings > SSH & GPG Keys > New SSH Key

![alt text](image.png)

- Clone the project
```
sudo yum install git
cd /projects
git clone git@github.com:delinel/frontend-devops.git
cd frontend-devops
```
- Build artefact
```
mvn clean package
```





