variable "ami_id" { 
  type = "string",
  default = "ami-0ac019f4fcb7cb7e6"
}

variable "ins_type" { 
  type = "string",
  default = "m4.large" 
}

variable "key_name" { 
  type = "string",
  default = "" 
}

variable "PATH_TO_PRIVATE_KEY" {
  type = "string",
  default = ""
}