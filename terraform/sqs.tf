resource "aws_sqs_queue" "company-creation-sqs" {
  name        = "company-creation-sqs"
  message_retention_seconds = var.message_retention_seconds
  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.company-creation-dlq.arn
    maxReceiveCount     = 4
  })
}

resource "aws_sqs_queue" "company-creation-dlq" {
  name = "company-creation-dlq"
}