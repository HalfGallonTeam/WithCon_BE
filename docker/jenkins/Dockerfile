# 기존 Jenkins 이미지를 베이스로 사용
FROM jenkins/jenkins:lts

# Docker CLI와 Docker Compose를 설치
USER root
RUN apt-get update && \
    apt-get install -y apt-transport-https ca-certificates curl software-properties-common && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" && \
    apt-get update && \
    apt-get install -y docker-ce-cli && \
    curl -L "https://github.com/docker/compose/releases/download/v2.1.1/docker-compose-linux-x86_64" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

# Docker 그룹이 존재하지 않을 경우 생성
RUN groupadd -f docker

# Jenkins 사용자가 도커 데몬에 접근할 수 있도록 설정
RUN usermod -aG docker jenkins

USER jenkins
