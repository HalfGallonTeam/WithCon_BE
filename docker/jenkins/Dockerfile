# 기존 Jenkins 이미지를 베이스로 사용
FROM jenkins/jenkins:lts

# Docker CLI와 Docker Compose를 설치
USER root
RUN yum update -y && \
    yum install -y yum-utils device-mapper-persistent-data lvm2 && \
    yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo && \
    yum install -y docker-ce docker-ce-cli containerd.io && \
    curl -L "https://github.com/docker/compose/releases/download/v2.1.1/docker-compose-linux-x86_64" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

# Jenkins 사용자가 도커 데몬에 접근할 수 있도록 설정
RUN usermod -aG docker jenkins

USER jenkins
