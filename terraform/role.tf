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
          "dynamodb:Query"
        ],
        Effect = "Allow",
        Resource = [
          "arn:aws:dynamodb:us-east-1:821986558514:table/auth_user",
          "arn:aws:dynamodb:us-east-1:821986558514:table/auth_user/index/*"
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