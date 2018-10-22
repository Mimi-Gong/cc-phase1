wget https://bintray.com/artifact/download/vertx/downloads/vert.x-3.5.4.tar.gz  | tr -d '\r'
tar -zxf /home/ubuntu/vert.x-3.5.4.tar.gz | tr -d '\r'
export PATH="~/vertx/bin:$PATH"
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install -y default-jdk
sudo apt-get install -y maven
