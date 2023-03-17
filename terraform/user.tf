resource "aws_iam_user" "assume_role" {
  name = "assume_role"
}

resource "aws_iam_access_key" "assume_role" {
  user = aws_iam_user.assume_role.name
}

data "aws_iam_policy_document" "assume_role_ro" {
  statement {
    effect    = "Allow"
    actions   = ["sts:AssumeRole"]
    resources = [aws_iam_role.management-system-role.arn]
  }
}

resource "aws_iam_user_policy" "assume_role_ro" {
  name   = "policy-to-assume-role"
  user   = aws_iam_user.assume_role.name
  policy = data.aws_iam_policy_document.assume_role_ro.json
}