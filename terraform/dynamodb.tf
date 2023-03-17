resource "aws_dynamodb_table_item" "auth_client" {
  table_name = aws_dynamodb_table.auth_user.name
  hash_key   = aws_dynamodb_table.auth_user.hash_key

  item = <<ITEM
    {
      "id": {"S": "d2eb9f49-9508-4a34-b42b-742740fb362b"},
      "username": {"S": "d1c2eef0-32cb-4568-a544-862172dd6641"},
      "password": {"S": "$2a$10$3TQGeQe5mhTSyOD5Xi6R3.8s/ufXGW1dLE2fW17MsOSBUNwIHU7i2"},
      "userType": {"S": "ADMIN"},
      "document": {"S": "0000000"},
      "scopes":   {"SS": ["fullAccess:application"]},
      "payload":  {"S": "{\"id\":\"d1c2eef0-32cb-4568-a544-862172dd6641\",\"userType\":\"ADMIN\",\"document\":\"0000000\"}"}
    }
    ITEM
}

resource "aws_dynamodb_table" "auth_user" {
  provider           = aws.us-east-1
  name               = "auth_user"
  hash_key           = "id"
  read_capacity      = var.read_capacity
  write_capacity     = var.write_capacity

  attribute {
    name = "id"
    type = "S"
  }

  attribute {
    name = "username"
    type = "S"
  }

  global_secondary_index  {
    name = "auth_user_username_index"
    hash_key = "username"
    write_capacity = var.read_capacity
    read_capacity = var.write_capacity
    projection_type = "ALL"
  }
}