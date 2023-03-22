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

resource "aws_dynamodb_table_item" "employee_scopes" {
  table_name = aws_dynamodb_table.app_scopes.name
  hash_key   = aws_dynamodb_table.app_scopes.hash_key

  item = jsonencode({
    userType = {"S": "EMPLOYEE"}
  })
}

resource "aws_dynamodb_table_item" "admin_scopes" {
  table_name = aws_dynamodb_table.app_scopes.name
  hash_key   = aws_dynamodb_table.app_scopes.hash_key

  item = jsonencode({
    userType = {"S": "ADMIN"}
  })
}

resource "aws_dynamodb_table" "app_scopes" {
  provider           = aws.us-east-1
  name               = "app_scopes"
  hash_key           = "userType"
  read_capacity      = var.read_capacity
  write_capacity     = var.write_capacity

  attribute {
    name = "userType"
    type = "S"
  }
}

resource "aws_dynamodb_table" "company" {
  provider           = aws.us-east-1
  name               = "company"
  hash_key           = "id"
  read_capacity      = var.read_capacity
  write_capacity     = var.write_capacity

  attribute {
    name = "id"
    type = "S"
  }

  attribute {
    name = "document"
    type = "S"
  }

  attribute {
    name = "alias"
    type = "S"
  }

  global_secondary_index  {
    name = "company_document_index"
    hash_key = "document"
    write_capacity = var.read_capacity
    read_capacity = var.write_capacity
    projection_type = "ALL"
  }

  global_secondary_index  {
    name = "company_alias_index"
    hash_key = "alias"
    write_capacity = var.read_capacity
    read_capacity = var.write_capacity
    projection_type = "ALL"
  }
}

resource "aws_dynamodb_table" "owner" {
  provider           = aws.us-east-1
  name               = "owner"
  hash_key           = "id"
  read_capacity      = var.read_capacity
  write_capacity     = var.write_capacity

  attribute {
    name = "id"
    type = "S"
  }

  attribute {
    name = "document"
    type = "S"
  }

  attribute {
    name = "email"
    type = "S"
  }

  global_secondary_index  {
    name = "owner_email_index"
    hash_key = "email"
    write_capacity = var.read_capacity
    read_capacity = var.write_capacity
    projection_type = "ALL"
  }

  global_secondary_index  {
    name = "owner_document_index"
    hash_key = "document"
    write_capacity = var.read_capacity
    read_capacity = var.write_capacity
    projection_type = "ALL"
  }
}