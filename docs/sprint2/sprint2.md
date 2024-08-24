# Sprint2

## Etape 1: Installer Nexus

- Adding service account
```
sudo useradd --system --no-create-home nexus
```
- Creating necessory folder for Nexus
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





