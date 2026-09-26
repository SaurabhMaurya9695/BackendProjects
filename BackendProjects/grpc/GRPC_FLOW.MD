BEGINNER
│
├── What is RPC?
├── What is gRPC?
├── REST vs gRPC
├── HTTP/1.1 vs HTTP/2
└── Protocol Buffers
│
▼
FOUNDATION
│
├── .proto files
├── messages
├── fields
├── enums
├── services
├── RPC methods
└── code generation
│
▼
JAVA gRPC
│
├── Server
├── Channel
├── Stub
├── BlockingStub
├── AsyncStub
├── FutureStub
└── Status
│
▼
RPC TYPES
│
├── Unary
├── Server Streaming
├── Client Streaming
└── Bidirectional Streaming
│
▼
MICROSERVICES
│
├── Service-to-service communication
├── User Service
├── Product Service
├── Order Service
└── Chained RPC calls
│
▼
INTERMEDIATE
│
├── Metadata
├── Deadlines
├── Cancellation
├── Error handling
├── Interceptors
├── Validation
└── Exception → Status mapping
│
▼
ADVANCED
│
├── TLS
├── Authentication
├── Authorization
├── Health checking
├── Keepalive
├── Load balancing
├── Name resolution
├── Retry
├── Backoff
└── Service configuration
│
▼
PRODUCTION
│
├── Docker
├── Kubernetes
├── Observability
├── OpenTelemetry
├── Metrics
├── Distributed tracing
├── Performance
└── Production debugging



Some Important notice
1 :            
        gRPC
┌─────────┴─────────┐
│                   │
HTTP/2             Protobuf
transport          messages/IDL

2 : HTTP/2 - Responsible for the transport and which makes the grpc fast because conversion or call is happing based on 
    Binary format instead HTTP1 uses a binary -> text conversion which makes this HTTP1 slow (Human readble response)
    2.1 :  HTTP1 is slow and can't help a multiple connection to make a req, every time you need to spawn a multiple connection
        to make a request 
    2.2 : HTTP1.1 can hold a connection for still it is slow for large number of req to server 

3 : HTTP/2 Works :
    3.1 : Stream, message and frames
    3.2 : Stream - set of messages flows in bi-directional
    3.3 : Message - sequence of HTTP message
    3.4 : Frames - smallest kind of data - frame header and body which contains the payload etc
    3.5 : client -> streams + data + headers -> server
          client <- streams + data + headers <- server
    3.6 : Supports request/response multiplexing + bidirectional streaming 
    3.7 : increase latency 
            

4 : Protocol Buffers - Responsible for defining and serializing your data/service contract
    4.1 : A strongly typed language for defining the contract between services.
    4.2 : message User {
                int64 id = 1;
                string name = 2;
                string email = 3;
          }
    4.3 : This tells us:
            User
            ├── id       → number
            ├── name     → string
            └── email    → string
    4.4 : We can define the service too:
            service UserService {
                rpc GetUser(GetUserRequest)
                    returns (User);
            }
5 : .proto
    5.1 : DATA CONTRACT
          User
          GetUserRequest
          +
          SERVICE CONTRACT 
          UserService
          GetUser()

6 : The request travels - 
Java Client
│
│ getUser()
▼
gRPC Stub
│
▼
Serialize request
│
▼
HTTP/2
│
▼
Network
│
▼
gRPC Server
│
▼
UserService implementation
│
▼
User
│
▼
Serialize response
│
▼
HTTP/2
│
▼
Client

7 : The important dependencies will be:

gRPC
│
├── grpc-netty-shaded
├── grpc-protobuf
└── grpc-stub

Protobuf
│
└── protobuf-java

8 : When the maven builds this magic happens
                    user.proto
                    │
                    ▼
                    Protobuf Compiler
                    │
                    ▼
                    Generated Java
                    │
        ┌───────────┴───────────┐
        ▼                       ▼
        GetUserRequest.java        User.java

                    +
                    
         UserServiceGrpc.java

9 : What does UserServiceGrpc give us?
    9.1 : For the server, we'll use something conceptually like: UserServiceGrpc.UserServiceImplBase
    9.2 : For the client: UserServiceGrpc.UserServiceBlockingStub
    9.3 :                   UserServiceGrpc
                                │
                     ┌──────────┴──────────┐
                     ▼                     ▼
                     Server                Client
                     │                     │
                   ImplBase subclass   BlockingStub

10 :
        I want you to memorize this diagram:

                         .proto
                           │
                           │ code generation
                           ▼
                 ┌────────────────────┐
                 │ UserServiceGrpc    │
                 └─────────┬──────────┘
                           │
             ┌─────────────┴─────────────┐
             │                           │
             ▼                           ▼
      UserServiceImplBase            Client Stubs
             │                           │
             │                           │
             ▼                           ▼
      UserServiceImpl              UserServiceBlockingStub
             │                           │
             │                           │
             │                           ▼
             │                       Channel
             │                           │
             │                           │
             └───────── NETWORK ─────────┘
                           │
                         HTTP/2

11 : 
