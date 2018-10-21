wget https://bintray.com/artifact/download/vertx/downloads/vert.x-3.5.4.tar.gz
tar -zxf ~/vert.x-3.5.4.tar.gz
sudo apt-get update
sudo apt-get upgrade -y
sudo apt-get install default-jdk -y
export PATH=~/vertx/bin:$
export PATH=/usr/bin:/bin
sudo apt-get update && sudo apt-get install maven -y