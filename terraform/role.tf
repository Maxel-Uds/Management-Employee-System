resource "aws_iam_role_policy" "dynamo-policy" {
  name = "dynamo-policy"
  role = aws_iam_role.management-system-role.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "dynamodb:PutItem",
          "dynamodb:GetItem",
          "dynamodb:UpdateItem",
          "dynamodb:Query",
          "dynamodb:DeleteItem"
        ],
        Effect = "Allow",
        Resource = [
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.auth_user.name}",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.auth_user.name}/index/*",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.app_scopes.name}",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.company.name}",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.company.name}/index/*",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.owner.name}",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.owner.name}/index/*",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.employee.name}",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.employee.name}/index/*",
          "arn:aws:dynamodb:us-east-1:${var.aws_account}:table/${aws_dynamodb_table.used_refresh_token.name}"
        ]
      }
    ]
  })
}


resource "aws_iam_role_policy" "ses-policy" {
  name = "ses-policy"
  role = aws_iam_role.management-system-role.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "ses:sendEmail",
          "ses:sendRawEmail"
        ],
        Effect = "Allow",
        Resource = [
          "arn:aws:ses:us-east-1:${var.aws_account}:identity/*"
        ]
      }
    ]
  })
}

resource "aws_iam_role_policy" "sqs-policy" {
  name = "sqs-policy"
  role = aws_iam_role.management-system-role.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "sqs:DeleteMessage",
          "sqs:ReceiveMessage",
          "sqs:SendMessage"
        ],
        Effect = "Allow",
        Resource = [
          "arn:aws:sqs:us-east-1:${var.aws_account}:${aws_sqs_queue.company-creation-sqs.name}",
          "arn:aws:sqs:us-east-1:${var.aws_account}:${aws_sqs_queue.company-creation-dlq.name}",
          "arn:aws:sqs:us-east-1:${var.aws_account}:${aws_sqs_queue.delete-employee-sqs.name}",
          "arn:aws:sqs:us-east-1:${var.aws_account}:${aws_sqs_queue.delete-employee-dlq.name}"
        ]
      }
    ]
  })
}


resource "aws_iam_role" "management-system-role" {
  name = "management-system-role"
  description = "role para do componente management-employee-system"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          AWS = aws_iam_user.assume_role.arn
        }
        Condition = {
          StringEquals = {
            "sts:ExternalId": "75b256b1-e7a5-4939-b29d-ea12c319d11f"
          }
        }
      }
    ]
  })
}