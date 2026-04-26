# Architecture Decisions - Tradeoffs & Reasoning

## 1. RDS Multi-AZ vs Aurora vs DynamoDB

| Decision | RDS Multi-AZ | Aurora | DynamoDB |
|----------|-------------|--------|----------|
| **Use Case** | Traditional SQL, predictable workload | High availability, auto-scaling | Serverless, highly variable workload |
| **Failover Time** | 1-2 minutes | <30 seconds | N/A (managed) |
| **Read Scaling** | Read replicas (async) | Auto-scaling replicas | Unlimited |
| **Cost** | Lower | Higher | Unpredictable, can be high |
| **Schema Flexibility** | Fixed | Fixed | Flexible (document DB) |
| **Consistency** | ACID (strong) | ACID (strong) | Eventually consistent (configurable) |
| **Recommended for HA** | ✅ YES | ✅✅ BEST | ❌ Only for specific workloads |

### Decision Matrix

**Choose Multi-AZ if:**
- Simple relational data model
- < 20 concurrent read replicas needed
- Budget conscious
- Predictable workload

**Choose Aurora if:**
- Need < 30s failover RTO
- Need auto-scaling read replicas
- Need point-in-time recovery
- Can afford premium

**Choose DynamoDB if:**
- Serverless is priority
- Highly variable workload
- Document-based data
- No complex joins needed

---

## 2. Blue-Green vs Canary vs Rolling Deployment

| Aspect | Blue-Green | Canary | Rolling |
|--------|-----------|--------|---------|
| **Downtime** | 0 seconds | 0 seconds | 0 seconds |
| **Rollback Speed** | Instant (< 2min) | Fast (2-5min) | Gradual (5-15min) |
| **Resource Usage** | 2x (both running) | 1.5x (5% + 95%) | 1.2x (overlap) |
| **Testing** | Full environment | Real traffic (5%) | Incremental |
| **Confidence** | Very high | High | Medium |
| **Complexity** | Medium | Medium | Low |
| **Best for** | Critical services | A/B testing | Non-critical apps |

### Decision Matrix

**Choose Blue-Green if:**
- Zero downtime is critical
- Have budget for 2x infrastructure
- Need instant rollback capability
- Deploying multiple times/day

**Choose Canary if:**
- Want real traffic validation
- Can tolerate 5% impact if bug
- Want to catch issues early
- Have good monitoring

**Choose Rolling if:**
- Budget is tight
- Downtime < 30min acceptable
- Simple service with no data concerns
- Team less experienced with deployments

---

## 3. RDS vs RDS Aurora vs Managed DynamoDB

### Cost Comparison (Production Scenario)

```
Scenario: 100GB database, 100 concurrent connections
Region: us-east-1

RDS PostgreSQL:
├─ Primary: db.r5.2xlarge × 1          = $1.176/hour
├─ Standby (Multi-AZ): db.r5.2xlarge × 1 = $1.176/hour
├─ Read Replicas: db.r5.xlarge × 2     = $0.588/hour
├─ Backup storage: 100GB × 30 days     = ~$30/month
└─ Total: ~$2,210/month

Aurora PostgreSQL:
├─ Primary + Replicas (managed): × 3   = $1.44/hour × 3 = $4.32/hour
├─ Storage: 100GB                      = $0.10/GB × 100 = $10/month
├─ Backup storage: Included
└─ Total: ~$3,155/month

RDS Multi-AZ (simpler):
├─ Primary: db.r5.xlarge               = $0.588/hour
├─ Standby (Multi-AZ): included        = $0.588/hour
├─ Read Replica: db.r5.large           = $0.294/hour
├─ Backup: 30 day retention            = ~$30/month
└─ Total: ~$1,584/month

Decision:
- Budget priority → RDS Multi-AZ
- Reliability priority → Aurora
- Balance → RDS Multi-AZ + 1 read replica
```

---

## 4. ALB vs NLB vs CloudFront

| Feature | ALB | NLB | CloudFront |
|---------|-----|-----|-----------|
| **Layer** | Layer 7 (Application) | Layer 4 (Transport) | Layer 7 (CDN) |
| **Throughput** | 4Gbps | 100Gbps | Unlimited |
| **Latency** | < 100ms | < 10ms | Minimal (edge) |
| **Cost** | Low | Medium | Variable |
| **Use Case** | HTTP/HTTPS APIs | Gaming, IoT, extreme throughput | Static assets, DDoS |
| **Health Checks** | HTTP path | TCP port | Origin monitoring |

### Decision Matrix

**Use ALB if:**
- REST API, microservices
- Path-based routing needed
- Host-based routing needed
- Standard web application

**Use NLB if:**
- Ultra-high throughput (100Gbps+)
- Gaming (UDP)
- IoT (MQTT)
- Extreme performance

**Use CloudFront if:**
- Static assets (JS, CSS, images)
- Global CDN needed
- DDoS protection
- Content caching

**For HA Web App: ALB + CloudFront**
- ALB for application load balancing
- CloudFront for static assets + DDoS protection

---

## 5. ElastiCache vs DynamoDB vs RDS for Caching

| Aspect | ElastiCache (Redis) | DynamoDB | RDS with caching |
|--------|------------------|----------|-----------------|
| **Speed** | Sub-millisecond | Single-digit ms | 1-10ms |
| **Persistence** | Optional | Yes | N/A |
| **Use Case** | Session store, rate limit | NoSQL DB | Query cache |
| **HA** | Multi-AZ cluster | Managed | Multi-AZ (DB) |
| **Cost** | Low | Varies | Low (add-on) |

### Decision Matrix

**Use ElastiCache if:**
- Sub-millisecond latency required
- Session storage for stateful app
- Rate limiting
- Real-time leaderboards

**Use DynamoDB if:**
- NoSQL data model
- Serverless caching
- Variable workload

**Use RDS Query Cache if:**
- Simple caching
- Budget tight
- Data consistency critical

**For HA Web App: ElastiCache + RDS**
- ElastiCache: User sessions, cached queries
- RDS: Primary data store

---

## 6. Terraform vs CloudFormation vs CDK

| Aspect | Terraform | CloudFormation | CDK |
|--------|-----------|----------------|-----|
| **Language** | HCL (custom) | JSON/YAML | TypeScript/Python |
| **Learning Curve** | Medium | High | Low (familiar language) |
| **AWS Only** | No (multi-cloud) | Yes | Yes |
| **State Management** | Explicit (S3) | Implicit | Implicit (CDK) |
| **Modularity** | Great (modules) | Difficult (stacks) | Great (constructs) |
| **Community** | Large | Official AWS | Growing |

### Decision Matrix

**Use Terraform if:**
- Multi-cloud future
- Need maximum flexibility
- Large team, complex IaC
- Prefer declarative language

**Use CloudFormation if:**
- AWS only
- Want AWS official support
- Tight integration with AWS services

**Use CDK if:**
- Team comfortable with TypeScript/Python
- Want programmatic IaC
- Faster development

**Recommendation for HA App: Terraform**
- Multi-cloud capability
- Better state management
- Excellent module ecosystem
- Can be version-controlled easily

---

## 7. CloudWatch vs Prometheus vs DataDog

| Aspect | CloudWatch | Prometheus | DataDog |
|--------|-----------|-----------|---------|
| **Native to AWS** | Yes | No | No (3rd party) |
| **Cost** | Included (+ storage) | Free (self-hosted) | Expensive |
| **Learning Curve** | Low | Medium | Low |
| **Custom Metrics** | Yes | Yes (via Prometheus) | Yes |
| **Alerting** | Good | Good (AlertManager) | Excellent |
| **Visualization** | Good | Good (Grafana) | Excellent |

### Decision Matrix

**Use CloudWatch if:**
- AWS-only environment
- Minimal setup needed
- Budget conscious
- Acceptable UI/UX

**Use Prometheus if:**
- Multi-cloud
- Engineering team prefers open-source
- Grafana visualization preferred
- Cost critical (self-hosted)

**Use DataDog if:**
- Enterprise requirements
- Need AI-powered alerting
- Complex environment
- Budget available

**Recommendation for HA App: CloudWatch + Grafana**
- CloudWatch for AWS metrics + logs
- Prometheus for application metrics
- Grafana for visualization

---

## 8. Secrets Management: Secrets Manager vs Parameter Store

| Aspect | Secrets Manager | Parameter Store |
|--------|-----------------|-----------------|
| **Automatic Rotation** | Yes | No |
| **Versioning** | Yes | Yes |
| **Encryption** | Yes (KMS) | Yes (KMS) |
| **Price** | $0.40/secret/month | Free tier, then $0.04/request |
| **Best For** | Database passwords, API keys | Configuration, non-sensitive |

### Decision Matrix

**Use Secrets Manager if:**
- Database credentials (rotate frequently)
- OAuth tokens (expiration needed)
- Certificate rotation
- Regulatory compliance (auditing)

**Use Parameter Store if:**
- Non-sensitive configuration
- Cost sensitive
- Simple app configuration
- No rotation needed

**For HA App: Use both**
- Secrets Manager: DB passwords, API keys
- Parameter Store: App configuration (database endpoint, cache endpoint)

---

## 9. IAM Roles vs Access Keys

| Aspect | IAM Roles | Access Keys |
|--------|-----------|------------|
| **Security** | Excellent | Good (if rotated) |
| **Rotation** | Automatic (tokens rotate) | Manual |
| **Audit Trail** | Detailed | Basic |
| **Best For** | Services, instances | Development only |

### Decision Matrix

**Use IAM Roles for:**
- EC2 instances (production)
- Lambda functions
- ECS tasks
- Any managed AWS service

**Use Access Keys for:**
- Local development only
- CI/CD systems (rotate frequently)
- Non-AWS systems

**For HA App: 100% IAM Roles**
- EC2 instances: Roles (not access keys)
- ECS tasks: Task roles
- Lambda: Execution roles
- Rotate: Every 30-90 days

---

## 10. VPC Design: Public vs Private Subnets

```
Recommended HA Architecture:

Public Subnet (1a, 1b, 1c):
├─ NAT Gateway (only this)
├─ ALB (for external traffic)
└─ Bastion Host (for SSH, if needed)

Private Subnet (1a, 1b, 1c):
├─ EC2 instances (application)
├─ ElastiCache
├─ RDS (no direct internet)
└─ Outbound via NAT Gateway

Benefits:
├─ RDS/Cache not exposed to internet
├─ EC2 instances don't have public IPs
├─ Outbound internet only via NAT
├─ No inbound from internet (except through ALB)
├─ Reduced attack surface
└─ Better security posture
```

---

## Recommendation Summary

### For Highly Available Web Application on AWS:

**Database Tier:**
- ✅ RDS Aurora (best HA) OR RDS Multi-AZ (budget option)
- ✅ 2 read replicas for read scaling
- ✅ 30-day backup retention
- ✅ Cross-region backup for DR

**Application Tier:**
- ✅ ALB with health checks
- ✅ ASG (3-10 instances, 3 AZs)
- ✅ Launch template with user data
- ✅ Security groups restricting inbound to ALB only

**Cache Tier:**
- ✅ ElastiCache Redis cluster mode
- ✅ Multi-AZ enabled
- ✅ Use for sessions + query cache

**Deployment:**
- ✅ Terraform for IaC
- ✅ Blue-green deployment with canary
- ✅ Automatic rollback on metrics threshold
- ✅ Jenkins/Harness pipeline with approval gates

**Monitoring:**
- ✅ CloudWatch for logs + metrics
- ✅ Prometheus for custom metrics
- ✅ Grafana for visualization
- ✅ SNS → PagerDuty for critical alerts

**Disaster Recovery:**
- ✅ Multi-region setup
- ✅ Route 53 health checks + failover
- ✅ RTO: 5 minutes
- ✅ RPO: 0 (sync replication)

**Secrets:**
- ✅ Secrets Manager for passwords + keys
- ✅ Parameter Store for configuration
- ✅ KMS encryption for all

**Total Cost:** ~$2,500-3,500/month
**Uptime SLA:** 99.99% (4 nines, ~43 seconds downtime/month)

---

*Last Updated: 2026-04-26*
