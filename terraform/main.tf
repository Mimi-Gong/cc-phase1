provider "aws" {
  region = "us-west-2"
}

resource "aws_security_group" "sg" {
  name        = "allow_all"
  description = "Allow all inbound traffic"

  ingress {
    from_port   = 0
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
  }
  tags {
    Name = "CC-SG"
    Project = "Phase1"
  }
}

resource "aws_instance" "instance" {
  ami           = "${var.ami_id}"
  instance_type = "${var.ins_type}"
  key_name = "${var.key_name}"
  provisioner "file" {
    source="setting.sh"
    destination="/tmp/setting.sh"
  }
  provisioner "remote-exec" {
  inline=[
    "chmod +x /tmp/setting.sh",
    "sudo /tmp/setting.sh"
  ]
  }
  connection {
    user="ubuntu"
    private_key="${file("${var.PATH_TO_PRIVATE_KEY}")}"
  }
  tags {
    Name = "CC-instance"
    Project = "Phase1"
  }
}


resource "aws_network_interface_sg_attachment" "sg_attachment" {
  security_group_id    = "${aws_security_group.sg.id}"
  network_interface_id = "${aws_instance.instance.primary_network_interface_id}"
}