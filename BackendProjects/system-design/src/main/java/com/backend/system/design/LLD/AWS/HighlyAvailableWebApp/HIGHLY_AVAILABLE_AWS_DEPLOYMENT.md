# Highly Available Web Application Deployment on AWS - Complete Guide

## Your Answer Analysis

### ✅ What You Got Right
1. Using **Jenkins/Harness pipeline** for CI/CD ✅
2. **Infrastructure creation first** (IaC) ✅
3. **Blue-green deployment** for zero downtime ✅
4. **Load balancers** for distribution ✅
5. **API Gateway** for API management ✅

### ❌ What's Missing (Critical)
1. **Database high availability** (RDS Multi-AZ, read replicas)
2. **Auto-scaling** (EC2, RDS)
3. **Caching layer** (ElastiCache, Redis)
4. **Monitoring & alerting** (CloudWatch, Prometheus)
5. **Disaster recovery & backup strategy**
6. **Secrets management** (AWS Secrets Manager, Parameter Store)
7. **VPC, subnets, security groups, NAT gateways**
8. **Health checks** (ELB health checks)
9. **Database migration strategy** (with zero downtime)
10. **Rollback strategy** (if deployment fails)

---

## Table of Contents
1. [Complete AWS HA Architecture](#complete-aws-ha-architecture)
2. [Deployment Pipeline](#deployment-pipeline)
3. [Database High Availability](#database-high-availability)
4. [Infrastructure as Code](#infrastructure-as-code)
5. [Blue-Green Deployment](#blue-green-deployment)
6. [Monitoring & Observability](#monitoring--observability)
7. [Disaster Recovery](#disaster-recovery)
8. [Interview Q&A](#interview-qa)

---

## Complete AWS HA Architecture

### High-Level Design
```
┌─────────────────────────────────────────────────────────────┐
│                        AWS REGION                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Route 53 (DNS + Health Checks + Failover)          │  │
│  │  Health checks to ELB + standby region              │  │
│  └──────────┬───────────────────────────────────────────┘  │
│             │                                               │
│  ┌──────────▼───────────────────────────────────────────┐  │
│  │  CloudFront (CDN) + WAF (DDoS Protection)           │  │
│  └──────────┬───────────────────────────────────────────┘  │
│             │                                               │
│  ┌──────────▼───────────────────────────────────────────┐  │
│  │  API Gateway (API Management + Rate Limiting)       │  │
│  └──────────┬───────────────────────────────────────────┘  │
│             │                                               │
│  ┌──────────▼───────────────────────────────────────────┐  │
│  │  Application Load Balancer (ALB)                     │  │
│  │  Health checks every 5s, draining 30s               │  │
│  │  Sticky sessions enabled for stateful apps          │  │
│  └──────────┬───────────────────────────────────────────┘  │
│             │                                               │
│  ┌──────────▼───────────────────────────────────────────┐  │
│  │  Auto Scaling Group (ASG)                            │  │
│  │  ├─ Min: 3 (one per AZ)                             │  │
│  │  ├─ Max: 10                                          │  │
│  │  ├─ Target: 70% CPU utilization                     │  │
│  │  └─ Launch template + health checks                 │  │
│  │                                                      │  │
│  │  ┌─────────┬─────────┬─────────┐                    │  │
│  │  │ Availability Zone 1                              │  │
│  │  │ ┌──────┐ ┌──────┐                                │  │
│  │  │ │ EC2  │ │ EC2  │  (Public Subnet)             │  │
│  │  │ └──────┘ └──────┘                                │  │
│  │  └─────────┬─────────┬─────────┘                    │  │
│  │  │ Availability Zone 2                              │  │
│  │  │ ┌──────┐ ┌──────┐                                │  │
│  │  │ │ EC2  │ │ EC2  │  (Public Subnet)             │  │
│  │  │ └──────┘ └──────┘                                │  │
│  │  └─────────┬─────────┬─────────┘                    │  │
│  │  │ Availability Zone 3                              │  │
│  │  │ ┌──────┐ ┌──────┐                                │  │
│  │  │ │ EC2  │ │ EC2  │  (Public Subnet)             │  │
│  │  │ └──────┘ └──────┘                                │  │
│  │  └─────────────────────┘                            │  │
│  │                                                      │  │
│  │  All instances run app via Docker + init scripts    │  │
│  └──────────┬───────────────────────────────────────────┘  │
│             │                                               │
│  ┌──────────▼────────────────────────────────────────────┐ │
│  │  ElastiCache (Redis Cluster)                         │ │
│  │  ├─ Multi-AZ enabled                                 │ │
│  │  ├─ Automatic failover                               │ │
│  │  └─ Session store + application cache                │ │
│  └──────────┬────────────────────────────────────────────┘ │
│             │                                               │
│  ┌──────────▼────────────────────────────────────────────┐ │
│  │  RDS PostgreSQL (DB)                                 │ │
│  │  ├─ Multi-AZ: Primary + Standby (sync replication)   │ │
│  │  ├─ Read Replicas: 2 (for read scaling)              │ │
│  │  ├─ Automated backups: 30-day retention              │ │
│  │  ├─ Encryption at rest + TLS in transit              │ │
│  │  └─ Parameter groups + option groups configured      │ │
│  └──────────┬────────────────────────────────────────────┘ │
│             │                                               │
│  ┌──────────▼────────────────────────────────────────────┐ │
│  │  RDS Aurora (Cluster)  [Alternative to Multi-AZ]     │ │
│  │  ├─ Auto-scaling read replicas                        │ │
│  │  ├─ Continuous backup to S3                           │ │
│  │  ├─ Point-in-time recovery                            │ │
│  │  └─ Faster failover (< 30 seconds)                    │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Data Tier                                          │   │
│  │  ├─ S3: Static assets, backups, logs                │   │
│  │  ├─ EBS snapshots: Automated, retained 30 days      │   │
│  │  ├─ DynamoDB: Session store (if no Redis)           │   │
│  │  └─ Glacier: Long-term backup retention             │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Monitoring & Logging                              │   │
│  │  ├─ CloudWatch: Metrics, logs, alarms               │   │
│  │  ├─ X-Ray: Distributed tracing                      │   │
│  │  ├─ CloudTrail: Audit logs                          │   │
│  │  ├─ VPC Flow Logs: Network monitoring               │   │
│  │  └─ SNS: Notifications to PagerDuty, Slack          │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Security                                           │   │
│  │  ├─ IAM: Roles, policies, least privilege           │   │
│  │  ├─ Security Groups: Inbound/outbound rules         │   │
│  │  ├─ Secrets Manager: DB passwords, API keys         │   │
│  │  ├─ ACM: SSL/TLS certificates (auto-renewal)        │   │
│  │  ├─ VPC Endpoints: Private access to AWS services   │   │
│  │  └─ KMS: Encryption keys management                 │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│                 Secondary AWS Region (DR)                    │
│  Same setup as primary, Route 53 fails over if needed       │
└──────────────────────────────────────────────────────────────┘
```

---

## Deployment Pipeline

### Jenkins/Harness Pipeline Stages

```
┌──────────────────────────────────────────────────────────────┐
│                  DEPLOYMENT PIPELINE                         │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  STAGE 1: BUILD & TEST (Developer commits to Git)           │
│  ├─ Webhook triggered on push                               │
│  ├─ Code checkout                                           │
│  ├─ Build (Maven/Gradle)                                    │
│  ├─ Unit tests                                              │
│  ├─ Integration tests (with embedded DB)                    │
│  ├─ SAST (SonarQube, Checkmarx)                             │
│  ├─ Docker build & scan image (Trivy)                       │
│  └─ Push to ECR (private registry)                          │
│         │                                                   │
│         ▼                                                   │
│  STAGE 2: DEPLOY TO STAGING                                │
│  ├─ Terraform: Update staging infrastructure               │
│  ├─ Pull latest Docker image from ECR                      │
│  ├─ Update ECS task definition                             │
│  ├─ Deploy to ECS (rolling update)                         │
│  ├─ Smoke tests (API health checks)                        │
│  ├─ Performance tests                                      │
│  ├─ Security tests (DAST)                                  │
│  └─ Manual approval gate                                   │
│         │                                                   │
│         ▼                                                   │
│  STAGE 3: BLUE-GREEN DEPLOYMENT TO PROD                    │
│  │                                                          │
│  ├─ GREEN environment: New version launched (parallel)      │
│  │  ├─ New EC2 instances (from ASG)                        │
│  │  ├─ New RDS connections (read from primary)             │
│  │  ├─ New ElastiCache connections                        │
│  │  └─ Health checks: Must pass 100%                       │
│  │                                                          │
│  ├─ Traffic shift (Canary/Linear/All-at-once)              │
│  │  ├─ Option 1: Canary - 5% traffic to GREEN for 5min   │
│  │  ├─ Option 2: Linear - 10% every 5 min until 100%      │
│  │  ├─ Option 3: All-at-once - Instant switch             │
│  │  └─ Monitor metrics: Error rate, latency, CPU          │
│  │                                                          │
│  ├─ BLUE environment still running (rollback-ready)        │
│  │  ├─ If GREEN has errors > threshold:                    │
│  │  │  ├─ Automatic rollback to BLUE                      │
│  │  │  ├─ Route 53 failover                                │
│  │  │  ├─ ASG scales down GREEN                            │
│  │  │  └─ Incident alert to oncall                        │
│  │  └─ If GREEN succeeds:                                 │
│  │     ├─ Route 53 updates to GREEN (100%)                │
│  │     ├─ ASG terminates BLUE instances                    │
│  │     └─ CloudWatch metrics logged                        │
│  │                                                          │
│  └─ Total downtime: 0 seconds ✅                           │
│         │                                                   │
│         ▼                                                   │
│  STAGE 4: POST-DEPLOYMENT VALIDATION                       │
│  ├─ Full regression tests                                  │
│  ├─ Database consistency checks                            │
│  ├─ Logs analysis (errors, warnings)                       │
│  ├─ Security scan of new code                              │
│  └─ Rollback plan documented (if needed)                   │
│         │                                                   │
│         ▼                                                   │
│  STAGE 5: CLEANUP                                          │
│  ├─ Archive logs & metrics                                 │
│  ├─ Update documentation                                   │
│  ├─ Notify stakeholders                                    │
│  └─ Close related tickets                                  │
│                                                              │
└──────────────────────────────────────────────────────────────┘

Total Deployment Time: 10-15 minutes
Rollback Time: < 2 minutes
```

### Pipeline Configuration (Harness/Jenkins YAML)
```yaml
# Pseudo-code for Harness Pipeline

pipeline:
  name: "Deploy Highly Available App"
  
  stages:
    - stage: Build
      steps:
        - checkout: git
        - build: mvn clean package
        - test: mvn integration-test
        - scan: sonarqube-scan
        - docker:
            build: true
            push_to: ecr
            
    - stage: Deploy to Staging
      steps:
        - terraform:
            action: plan
            vars: environment=staging
        - deploy:
            service: app-staging
            strategy: rolling
            health_check: /health
            
    - stage: Manual Approval
      type: approval
      approvers: [lead-engineer]
      
    - stage: Blue-Green Production Deployment
      steps:
        - terraform:
            action: apply
            vars: environment=prod
        - blue_green_deploy:
            blue_version: current
            green_version: new
            traffic_shift:
              strategy: canary
              canary_percentage: 5
              canary_duration: 5m
              increment: 20
              increment_duration: 2m
            health_check:
              path: /health
              interval: 5s
              timeout: 3s
              healthy_threshold: 3
              unhealthy_threshold: 2
            rollback_condition:
              error_rate_threshold: 1%
              p99_latency_threshold: 500ms
              
    - stage: Post Deployment
      steps:
        - verify: smoke-tests
        - monitor: cloudwatch-alarms
        - notify: slack
```

---

## Database High Availability

### Option 1: RDS Multi-AZ (Synchronous Replication)

```
┌─────────────────────────────────────────────────┐
│              RDS Multi-AZ Setup                 │
├─────────────────────────────────────────────────┤
│                                                 │
│  Availability Zone A          Availability Zone B
│  ┌───────────────────┐        ┌───────────────┐
│  │ Primary DB        │        │ Standby DB    │
│  │ (Read/Write)      │        │ (No access)   │
│  │                   │        │               │
│  │ - Accepts writes  │        │ - Hot standby │
│  │ - Replication lag │        │ - Same config │
│  │   = 0 (sync)      │        │               │
│  │                   │        │               │
│  │ Synchronous       │───────▶│ Synchronous   │
│  │ Replication       │        │ Replication   │
│  └───────────────────┘        └───────────────┘
│         │                            △
│         │                            │
│         │                    On failure: 
│         │                    Automatic failover
│         │                    (1-2 minutes)
│         │                            │
│         └────────────────────────────┘
│          RDS handles failover
│
│  Benefits:
│  ✅ Zero data loss (sync replication)
│  ✅ Automatic failover (RDS managed)
│  ✅ DNS endpoint remains same
│  ✅ Application needs no code changes
│
│  Trade-off:
│  ❌ Slightly higher latency (2-3 AZs)
│  ❌ Cannot read from standby
│
└─────────────────────────────────────────────────┘
```

### Option 2: RDS Aurora (Recommended)

```
┌──────────────────────────────────────────────────┐
│              Aurora Cluster Setup                │
├──────────────────────────────────────────────────┤
│                                                  │
│  ┌──────────────────────────────────────────┐   │
│  │  Aurora Cluster (Shared Storage Layer)   │   │
│  │                                          │   │
│  │  ┌────────┐  ┌────────┐  ┌────────┐     │   │
│  │  │Primary │  │Replica │  │Replica │     │   │
│  │  │Writer  │  │Reader  │  │Reader  │     │   │
│  │  │        │  │        │  │        │     │   │
│  │  │AZ-1    │  │AZ-2    │  │AZ-3    │     │   │
│  │  └────────┘  └────────┘  └────────┘     │   │
│  │      │          │           │           │   │
│  │      └──────────┴───────────┘           │   │
│  │           All point to                  │   │
│  │      Shared Cluster Storage              │   │
│  │  (6-way replicated, durability)         │   │
│  │                                          │   │
│  │  Single Logical Volume                   │   │
│  │  ├─ Data replicated 6 ways               │   │
│  │  ├─ Quorum read/write (4/6)              │   │
│  │  └─ Self-healing on failure              │   │
│  └──────────────────────────────────────────┘   │
│                                                  │
│  Advantages over Multi-AZ:                      │
│  ✅ Read replicas auto-scaling (up to 15)       │
│  ✅ Faster failover (< 30 seconds)              │
│  ✅ Can read from replicas (read scaling)       │
│  ✅ Better performance                          │
│  ✅ Continuous backup to S3                     │
│                                                  │
│  Example read endpoints:                        │
│  - mydb-instance.xxx.rds.amazonaws.com (writer)│
│  - mydb-instance-ro.xxx.rds.amazonaws.com      │
│    (read-only, load balances across replicas)   │
│                                                  │
└──────────────────────────────────────────────────┘
```

### Read Replicas Strategy

```
Primary DB         Read Replicas
(Writes)          (Reads)
   │                 ├─ Replica-1 (Async)
   │                 ├─ Replica-2 (Async)
   │                 └─ Replica-3 (Async)
   │
   ├─ Transaction logs → Replicas
   │
   Application Strategy:
   - Write queries → Primary endpoint
   - Read queries → Read replica endpoint (rotated)
   - Analytics → Separate read replica
```

### Database Schema Migration (Zero Downtime)

```
Problem: Need to add column to 10M row table
         Cannot lock table, would cause downtime

Solution: Expand-Contract Pattern

Step 1: EXPAND (Add new column, keep old code running)
  ├─ Add new_column to table (nullable, no default)
  ├─ Old code: Still writes to old_column
  ├─ New code: Writes to BOTH old_column and new_column
  ├─ Background job: Backfill new_column from old_column
  │  (runs gradually, doesn't lock table)
  └─ No downtime ✅

Step 2: VERIFY (Data consistency checks)
  ├─ Compare old_column vs new_column
  ├─ If mismatch < 0.1%, proceed
  └─ If mismatch > 0.1%, rollback

Step 3: CONTRACT (Switch to new column only)
  ├─ Deploy code: Read from new_column only
  ├─ Monitor for errors
  ├─ If errors, fallback to Step 2
  └─ Remove old_column in future deployment

Total downtime: 0 seconds ✅
```

---

## Infrastructure as Code

### Terraform Structure

```
terraform/
├── environments/
│   ├── staging/
│   │   ├── terraform.tfvars
│   │   ├── backend.tf
│   │   └── variables.tfvars
│   └── production/
│       ├── terraform.tfvars
│       ├── backend.tf
│       └── variables.tfvars
│
├── modules/
│   ├── vpc/
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   ├── asg/
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   ├── rds/
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   ├── alb/
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   └── elasticache/
│       ├── main.tf
│       ├── variables.tf
│       └── outputs.tf
│
├── main.tf (orchestrates modules)
├── variables.tf (global variables)
├── outputs.tf (outputs)
└── backend.tf (S3 + DynamoDB for state)

Key Principles:
✅ State file in S3 with versioning enabled
✅ DynamoDB table for state locking
✅ Separate state per environment
✅ Modules for reusability
✅ Secrets in Secrets Manager (not in code)
✅ Plan before apply (human approval)
```

### Example: RDS Terraform

```hcl
# modules/rds/main.tf

resource "aws_db_instance" "postgres" {
  # Basic config
  identifier     = "${var.environment}-postgres"
  engine         = "postgres"
  engine_version = "14.7"
  instance_class = var.instance_class

  # High Availability
  multi_az = true  # Enable Multi-AZ
  
  # Storage
  allocated_storage = 100
  storage_encrypted = true
  kms_key_id        = aws_kms_key.rds.arn
  
  # Backup & Maintenance
  backup_retention_period = 30
  backup_window          = "03:00-04:00"
  maintenance_window     = "sun:04:00-sun:05:00"
  skip_final_snapshot    = false
  
  # Network
  db_subnet_group_name   = aws_db_subnet_group.private.name
  publicly_accessible    = false
  vpc_security_group_ids = [aws_security_group.rds.id]
  
  # Performance
  performance_insights_enabled    = true
  enabled_cloudwatch_logs_exports = ["postgresql"]
  
  # Credentials
  username = var.db_username
  password = random_password.db_password.result
  
  # Monitoring
  monitoring_interval = 60
  monitoring_role_arn = aws_iam_role.rds_monitoring.arn
  
  tags = {
    Name = "${var.environment}-postgres"
  }
}

# Read replicas
resource "aws_db_instance" "read_replica" {
  count              = 2
  identifier         = "${var.environment}-postgres-replica-${count.index + 1}"
  replicate_source_db = aws_db_instance.postgres.identifier
  instance_class     = var.replica_instance_class
  
  # Must be in different AZ
  availability_zone = var.read_replica_azs[count.index]
  
  skip_final_snapshot = false
  
  tags = {
    Name = "${var.environment}-postgres-replica-${count.index + 1}"
  }
}

# Store password in Secrets Manager
resource "aws_secretsmanager_secret" "db_password" {
  name = "${var.environment}/rds/master-password"
}

resource "aws_secretsmanager_secret_version" "db_password" {
  secret_id     = aws_secretsmanager_secret.db_password.id
  secret_string = random_password.db_password.result
}

# Outputs
output "rds_endpoint" {
  value = aws_db_instance.postgres.endpoint
}

output "read_replica_endpoints" {
  value = aws_db_instance.read_replica[*].endpoint
}
```

---

## Blue-Green Deployment

### Detailed Flow

```
Time 0: Current state (BLUE running)
┌──────────────────────────────────────┐
│ Route 53: Points to BLUE             │
│                                      │
│ ALB                                  │
│  ├─ Target Group BLUE (5 instances)  │
│  └─ Health: 5/5 healthy ✅           │
│                                      │
│ ASG BLUE: Min=3, Max=10, Desired=5   │
└──────────────────────────────────────┘

Time 1: GREEN environment launched
┌──────────────────────────────────────┐
│ Route 53: Still points to BLUE       │
│                                      │
│ ALB                                  │
│  ├─ Target Group BLUE (5 instances)  │
│  │   └─ Health: 5/5 healthy ✅       │
│  │                                   │
│  └─ Target Group GREEN (scaling up)  │
│      ├─ Instance 1: Starting         │
│      ├─ Instance 2: Starting         │
│      └─ Instance 3: Starting         │
│                                      │
│ All new code deployed to GREEN       │
│ Full regression testing on GREEN     │
└──────────────────────────────────────┘

Time 2: GREEN ready (100% health checks pass)
┌──────────────────────────────────────┐
│ Route 53: Still points to BLUE       │
│                                      │
│ ALB                                  │
│  ├─ Target Group BLUE (5 instances)  │
│  │   └─ Health: 5/5 healthy ✅       │
│  │                                   │
│  └─ Target Group GREEN (5 instances) │
│      └─ Health: 5/5 healthy ✅       │
│                                      │
│ Both environments running!            │
│ Ready for traffic shift              │
└──────────────────────────────────────┘

Time 3: Traffic shift (Canary: 5%)
┌──────────────────────────────────────┐
│ Route 53: Points to ALB              │
│                                      │
│ ALB Listener rules:                  │
│  ├─ 95% traffic → BLUE TG            │
│  └─ 5% traffic → GREEN TG            │
│                                      │
│ Monitoring:                          │
│  ├─ GREEN error rate: < 0.5% ✅      │
│  ├─ GREEN p99 latency: < 500ms ✅    │
│  └─ GREEN CPU: < 75% ✅              │
│                                      │
│ Canary succeeds, proceed to next     │
└──────────────────────────────────────┘

Time 4: Traffic shift (Linear: 50%)
┌──────────────────────────────────────┐
│ ALB Listener rules:                  │
│  ├─ 50% traffic → BLUE TG            │
│  └─ 50% traffic → GREEN TG           │
│                                      │
│ Monitoring every 30 seconds:         │
│  ├─ GREEN error rate: < 0.5% ✅      │
│  ├─ GREEN p99 latency: < 500ms ✅    │
│  └─ GREEN CPU: < 75% ✅              │
│                                      │
│ Metrics good, increment to 100%      │
└──────────────────────────────────────┘

Time 5: Traffic shift (100% to GREEN)
┌──────────────────────────────────────┐
│ ALB Listener rules:                  │
│  └─ 100% traffic → GREEN TG          │
│                                      │
│ BLUE instances still running          │
│ (rollback ready for 30 minutes)      │
│                                      │
│ Monitoring metrics:                  │
│  ├─ Error rate: Baseline ✅          │
│  ├─ Latency: Normal ✅               │
│  ├─ CPU: Normal ✅                   │
│  └─ Database connections: OK ✅      │
└──────────────────────────────────────┘

Time 6: Rollback window (if needed)
If issues detected in GREEN:
  1. Automated rollback:
     ALB: 100% traffic → BLUE TG
  2. BLUE serves traffic again
  3. GREEN instances scaled down
  4. Incident logged & alert sent
  5. Total downtime: 0 seconds ✅

Time 7: Cleanup (after rollback window expires)
If GREEN stable for 30 min:
  ├─ Route 53: Confirm GREEN endpoint
  ├─ ASG BLUE: Scale down to 0
  ├─ Delete BLUE Target Group
  ├─ Delete BLUE security group
  └─ Archive BLUE metrics & logs
```

### Deployment Metrics (CloudWatch)

```
Custom Metrics to Monitor During Deployment:

1. Error Rate
   ├─ BLUE: 0.1% (baseline)
   ├─ During shift:
   │  ├─ GREEN at 5% traffic: 0.05% ✅ (Lower is suspicious, could be sampling)
   │  ├─ GREEN at 25% traffic: 0.1% ✅ (Matches BLUE)
   │  └─ GREEN at 100% traffic: 0.1% ✅ (Stable)
   └─ Threshold to rollback: > 1%

2. P99 Latency
   ├─ BLUE: 400ms
   ├─ During shift:
   │  ├─ GREEN at 5%: 410ms ✅
   │  ├─ GREEN at 50%: 420ms ✅
   │  └─ GREEN at 100%: 415ms ✅
   └─ Threshold to rollback: > 1000ms

3. CPU Utilization
   ├─ BLUE: 60%
   ├─ During shift:
   │  ├─ At 5%: 62% ✅
   │  ├─ At 50%: 65% ✅
   │  └─ At 100%: 63% ✅
   └─ Threshold: ASG scales up if > 80%

4. Memory Usage
   ├─ BLUE: 70%
   └─ Should remain stable during shift

5. Database Connections
   ├─ BLUE: 50/100
   ├─ GREEN: Should match when at 100%
   └─ Threshold: > 90% usage = scale up

6. Cache Hit Ratio
   ├─ Should remain > 85%
   └─ If drops, might indicate cache warming issue
```

---

## Monitoring & Observability

### CloudWatch Dashboard

```yaml
Dashboard: "Application-Health"

Widgets:
  1. System Health (Top row)
     ├─ Application Error Rate (%)
     ├─ API Latency (p50, p95, p99)
     ├─ Active Connections
     └─ Deployment Status

  2. Infrastructure (Middle row)
     ├─ EC2 CPU Utilization (%)
     ├─ EC2 Memory Usage (%)
     ├─ Network In/Out (bytes/sec)
     └─ Disk I/O Operations

  3. Database (Lower row)
     ├─ RDS CPU Utilization
     ├─ RDS Connections
     ├─ Replication Lag (Multi-AZ)
     └─ Query Performance

  4. Cache (Lower row)
     ├─ ElastiCache Hit Rate
     ├─ ElastiCache Memory Usage
     ├─ Evictions/sec
     └─ Network Bytes In/Out

  5. Alerts (Status bar)
     ├─ Critical: Red
     ├─ Warning: Yellow
     └─ OK: Green
```

### Log Aggregation Strategy

```
Application Logs:
  ├─ CloudWatch Logs (all containers)
  ├─ Log Groups by service:
  │  ├─ /app/api-service
  │  ├─ /app/worker-service
  │  └─ /app/batch-service
  │
  ├─ Log Streams by instance:
  │  ├─ i-0x1234567890abcdef
  │  └─ i-0y9876543210zyxwvu
  │
  └─ Retention: 30 days

Infrastructure Logs:
  ├─ VPC Flow Logs (network traffic)
  ├─ ALB Access Logs (HTTP requests)
  ├─ CloudTrail (API calls)
  └─ RDS Logs (slow queries, errors)

Log Analysis:
  ├─ CloudWatch Insights (SQL-like queries)
  ├─ Alerts on error patterns
  ├─ Anomaly detection
  └─ Integration with SIEM
```

### Alerting Strategy

```
Critical Alerts (PagerDuty escalation):
  ├─ Error rate > 1%
  ├─ API p99 latency > 1000ms
  ├─ Database failover happened
  ├─ ASG hitting max capacity
  ├─ Deployment failed
  └─ Certificate expiring in 7 days

Warning Alerts (Slack notification):
  ├─ Error rate > 0.5%
  ├─ API p99 latency > 500ms
  ├─ CPU utilization > 80%
  ├─ Database replication lag > 1s
  ├─ Disk usage > 80%
  └─ Memory usage > 85%

Info Alerts (Dashboard only):
  ├─ Deployment started
  ├─ Traffic shift to 50%
  ├─ ASG scaling event
  └─ Backup completed
```

---

## Disaster Recovery

### RTO/RPO targets

```
RTO (Recovery Time Objective): How quickly to recover
RPO (Recovery Point Objective): How much data loss acceptable

For Highly Available Application:
├─ RTO: 5 minutes (auto-failover, no manual intervention)
├─ RPO: 0 minutes (zero data loss, sync replication)

Failure Scenarios & Recovery:

1. Single EC2 instance fails (1/5)
   ├─ RTO: < 30 seconds (ELB health check + ASG replacement)
   ├─ RPO: 0 (stateless, no data)
   └─ Action: Automatic

2. Single AZ fails (entire AZ down)
   ├─ RTO: < 1 minute
   │  ├─ ELB removes instances from AZ-1
   │  ├─ ASG launches replacement in AZ-2 or AZ-3
   │  └─ DB failover to standby (if primary was in AZ-1)
   ├─ RPO: 0 (Multi-AZ RDS)
   └─ Action: Automatic

3. Database instance fails
   ├─ RTO: 1-2 minutes (Multi-AZ failover)
   ├─ RPO: 0 (sync replication)
   └─ Action: RDS automatic failover

4. Region failure (entire region down)
   ├─ RTO: 5-10 minutes
   ├─ Recovery steps:
   │  ├─ Route 53 health check detects failure
   │  ├─ Route 53 failover to secondary region
   │  ├─ DNS propagation: 30-60 seconds
   │  ├─ Application starts in secondary region
   │  ├─ Database replica promotes to primary
   │  └─ Traffic flows to secondary region
   ├─ RPO: 5 minutes (asynchronous replication lag)
   └─ Action: Automatic (Route 53 failover policy)

5. Data corruption / application bug
   ├─ RTO: 30 minutes
   ├─ Recovery steps:
   │  ├─ Backup detected 1 hour before incident
   │  ├─ RDS point-in-time recovery to good state
   │  ├─ Redeploy application from Git tag
   │  ├─ Validate data integrity
   │  └─ Perform blue-green deployment
   ├─ RPO: 1 hour (hourly backup frequency)
   └─ Action: Manual (incident commander initiated)
```

### Backup Strategy

```
Database Backups:
  ├─ Automated daily backups (30-day retention)
  ├─ Retention: 30 days
  ├─ Location: S3 (encrypted)
  ├─ Point-in-time recovery: Last 7 days
  └─ Cross-region backup replication (for region failover)

Application Code:
  ├─ Git repository (GitHub Enterprise)
  ├─ Backup: All commits replicated
  └─ Disaster recovery: Full repo accessible

Configuration:
  ├─ Terraform state: S3 + versioning
  ├─ Backup: Daily snapshots to Glacier
  └─ Recovery: Restore from tagged release

Secrets:
  ├─ AWS Secrets Manager (encrypted)
  ├─ Backup: AWS manages (multi-region)
  └─ Rotation: Automatic every 30 days
```

---

## Interview Q&A

### Q1: Why Multi-AZ is not enough?

**Answer:**
```
Multi-AZ only protects against single AZ failure:
  ├─ PROTECTS: Server crash, network issue in one AZ
  ├─ PROTECTS: RDS instance failure (auto-failover)
  └─ DOES NOT PROTECT: Regional outage (entire AWS region down)

Regional outage examples:
  ├─ Power grid failure (happened 2021, Virginia)
  ├─ Networking infrastructure issue (happened 2020, Singapore)
  ├─ Cooling system failure (happened 2019, Ireland)

Solution:
  ├─ Multi-region setup (active-passive or active-active)
  ├─ Route 53 failover policy
  ├─ Replicated database (cross-region)
  └─ Cross-region backup

RTO with multi-region: 5 minutes
RTO with multi-AZ only: 60+ minutes (manual intervention)
```

### Q2: How does Blue-Green deployment ensure zero downtime?

**Answer:**
```
Traditional deployment (Blue only):
  1. Deploy new code to servers
  2. Servers restart → Traffic drops during restart
  3. Old requests might fail
  4. Downtime: 1-5 minutes

Blue-Green deployment:
  1. Keep BLUE running (serving 100% traffic)
  2. Launch GREEN with new code (parallel environment)
  3. Test GREEN fully
  4. Switch traffic: ALB reroutes to GREEN
  5. BLUE remains running (instant rollback)

Key differences:
  ├─ No server restart while serving traffic
  ├─ Full testing before traffic switch
  ├─ Instant rollback if issues
  └─ DNS doesn't change (ALB target group changes)

Downtime: 0 seconds (only DNS update, cached in browser)
```

### Q3: How does Auto-Scaling maintain high availability?

**Answer:**
```
Auto-Scaling Group (ASG) Configuration:
  ├─ Min instances: 3 (one per AZ minimum)
  ├─ Desired: 5 (normal load)
  ├─ Max: 10 (peak load)

Scaling triggers:
  ├─ Scale UP if:
  │  ├─ CPU > 70% (for 2 consecutive minutes)
  │  ├─ Memory > 80%
  │  └─ Custom metric (queue depth, request count)
  │
  └─ Scale DOWN if:
     ├─ CPU < 30% (for 5 consecutive minutes)
     └─ Other metrics below threshold

High Availability achieved by:
  ├─ Instances spread across 3 AZs
  ├─ If one AZ fails:
  │  ├─ Instances removed from failed AZ
  │  ├─ ASG launches replacement in healthy AZ
  │  └─ Min 3 instances maintained
  │
  ├─ Health checks:
  │  ├─ ELB health check: /health endpoint every 5s
  │  ├─ If unhealthy, instance terminated
  │  └─ ASG launches replacement
  │
  └─ If instance crashes:
     ├─ Health check fails
     ├─ Instance terminated
     ├─ Replacement launched
     └─ RTO: < 30 seconds

Example scenario:
  ├─ 5 instances running (AZ-1: 2, AZ-2: 2, AZ-3: 1)
  ├─ AZ-1 fails
  ├─ Both instances in AZ-1 unhealthy
  ├─ ASG terminates them
  ├─ ASG launches 2 replacements in AZ-2
  ├─ Now: AZ-2: 4, AZ-3: 1 (total 5)
  └─ High availability maintained ✅
```

### Q4: How do you handle database migrations with zero downtime?

**Answer:**
```
Scenario: Add a column to 10M-row table

Wrong approach (causes downtime):
  1. ALTER TABLE ADD COLUMN
  2. Table locks during migration
  3. Can't write to table for 10+ minutes
  4. Downtime: 10+ minutes ❌

Correct approach (Expand-Contract):

PHASE 1: EXPAND (Add new column)
  ├─ ALTER TABLE ADD COLUMN new_col (nullable)
  ├─ No default value (avoids full table lock)
  ├─ Deployment: Old code running
  ├─ Old code: Still writes old_col
  ├─ New code: Not deployed yet
  ├─ Background job starts:
  │  ├─ SELECT * FROM table WHERE new_col IS NULL
  │  ├─ Update batches of 1000 rows
  │  ├─ Backfill: new_col = compute_value(old_col)
  │  └─ Takes 30 minutes, no locking
  └─ Zero downtime ✅

PHASE 2: DUAL WRITE (Deploy new code)
  ├─ Deployment: New code
  ├─ New code logic:
  │  ├─ Reads: Use new_col
  │  ├─ Writes: Write to BOTH old_col AND new_col
  │  └─ Backward compatible
  ├─ Monitor for errors
  └─ If errors, rollback (old code still reads old_col)

PHASE 3: CONTRACT (Remove old column)
  ├─ After 1 week (confidence period)
  ├─ Deployment: Remove old_col references
  ├─ New code: Reads new_col only
  ├─ Monitor for errors
  └─ If errors, we have rollback ✅

PHASE 4: CLEANUP
  ├─ After 1 more week
  ├─ ALTER TABLE DROP COLUMN old_col
  └─ Done ✅

Total downtime: 0 seconds
Recovery window: 1-2 weeks
```

### Q5: How do you monitor during blue-green deployment?

**Answer:**
```
Monitoring during traffic shift:

Pre-deployment metrics (baseline):
  ├─ Error rate: 0.1%
  ├─ P50 latency: 100ms
  ├─ P95 latency: 300ms
  ├─ P99 latency: 400ms
  ├─ CPU: 60%
  ├─ Memory: 70%
  └─ DB connections: 50/100

During canary (5% traffic to GREEN):
  ├─ Alert thresholds (automatic rollback if exceeded):
  │  ├─ Error rate > 1.0% (10x baseline)
  │  ├─ P99 latency > 1000ms (2.5x baseline)
  │  ├─ CPU > 90%
  │  └─ Memory > 95%
  │
  ├─ Monitoring interval: Every 10 seconds
  ├─ Canary duration: 5 minutes
  ├─ If GREEN metrics OK: Proceed to next phase
  └─ If GREEN metrics bad: Automatic rollback

During linear shift (10% → 50% → 100%):
  ├─ Increment: 20% every 2 minutes
  ├─ Health check between increments:
  │  ├─ GREEN error rate vs BLUE
  │  ├─ GREEN latency vs BLUE
  │  ├─ Resource utilization
  │  └─ Custom business metrics (if any)
  │
  └─ If degradation detected: Stop and rollback

Custom metrics to monitor:
  ├─ API response time by endpoint
  ├─ Database query latency
  ├─ Cache hit/miss ratio
  ├─ Queue processing delay
  ├─ Business metrics:
  │  ├─ Payment success rate
  │  ├─ Login success rate
  │  └─ Feature flag evaluation time

Rollback triggers:
  ├─ Automatic:
  │  ├─ Error rate > threshold (1%)
  │  ├─ Latency > threshold (1000ms p99)
  │  ├─ Resource exhaustion (99% CPU/Memory)
  │  └─ Exception spike in logs
  │
  └─ Manual:
     ├─ Oncall engineer observes issue
     ├─ Clicks "Rollback" in deployment dashboard
     ├─ ALB reverts to BLUE
     └─ Takes < 2 minutes

Post-deployment validation:
  ├─ Full regression test suite (30 min)
  ├─ Check database integrity
  ├─ Verify logs for errors
  └─ Smoke tests of critical paths
```

---

## Summary: Your Original Answer vs Complete Answer

### Your Answer:
```
✅ Jenkins/Harness pipeline
✅ Creates infrastructure first
✅ Blue-green deployment
✅ Zero downtime
✅ Load balancers
✅ API Gateway
```

### Complete HA Deployment:
```
✅ Jenkins/Harness pipeline (with detailed stages)
✅ Infrastructure as Code (Terraform)
✅ Multi-AZ setup (all components)
✅ Database high availability (Multi-AZ or Aurora)
✅ Read replicas for scaling
✅ Blue-green deployment (with canary + automatic rollback)
✅ Auto-scaling (ASG for EC2)
✅ Load balancers (ALB) with health checks
✅ API Gateway (rate limiting, authentication)
✅ Caching layer (ElastiCache/Redis)
✅ Monitoring & alerting (CloudWatch, PagerDuty)
✅ Distributed logging (CloudWatch Logs)
✅ Disaster recovery (multi-region, Route 53 failover)
✅ Database migration strategy (expand-contract)
✅ Secrets management (AWS Secrets Manager)
✅ VPC, security groups, NAT gateways
✅ SSL/TLS certificates (ACM)
✅ Backup & restore strategy (RDS, EBS snapshots)
```

---

## Interview Tips

### ✅ DO:
1. **Start high-level**: "My deployment involves multiple layers..."
2. **Show you understand trade-offs**: "We use Aurora instead of Multi-AZ because..."
3. **Discuss failure scenarios**: "If an AZ fails, here's what happens..."
4. **Mention monitoring**: "We monitor error rate and latency during shift..."
5. **Discuss data consistency**: "We use expand-contract for schema changes..."

### ❌ DON'T:
1. **Skip database HA**: "We just use RDS" (incomplete)
2. **Forget monitoring**: "We deploy and hope it works" (no confidence)
3. **Ignore edge cases**: "Multi-AZ is enough" (doesn't handle region failure)
4. **Miss disaster recovery**: "We don't have a rollback plan"

### Follow-up Questions to Prepare For:
1. "What if the database itself becomes a bottleneck?"
   → Read replicas, caching, sharding
2. "How do you handle database schema changes?"
   → Expand-contract pattern
3. "What's your RTO/RPO?"
   → Define clearly (e.g., RTO 5min, RPO 0)
4. "How do you monitor the deployment?"
   → CloudWatch metrics, custom alarms
5. "What happens if both primary and secondary region fail?"
   → More than 2 regions, or backup restoration

---

*Last Updated: 2026-04-26*
*Ready for your AWS system design interview! 🚀*
