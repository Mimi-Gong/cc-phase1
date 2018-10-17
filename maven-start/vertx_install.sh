wget https://bintray.com/artifact/download/vertx/downloads/vert.x-3.5.4.tar.gz
sudo apt-get update
sudo apt-get upgrade -y
sudo apt-get install default-jdk -y
tar -zxf ~/vert.x-3.5.4.tar.gz
export PATH=~/vertx/bin:$
wget http://apache.spinellicreations.com/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
tar xzvf apache-maven-3.5.4-bin.tar.gz
export PATH=$PATH:/opt/apache-maven-3.5.4/bin
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64