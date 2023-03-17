provider "aws" {
  alias  = "us-east-1"
  region = "us-east-1"
}

variable "read_capacity" {
  default = 1
}

variable "write_capacity" {
  default = 1
}