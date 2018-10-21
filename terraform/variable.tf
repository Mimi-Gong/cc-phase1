variable "ami_id" { 
  type = "string",
  default = "ami-0f65671a86f061fcd" 
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