# Patient Management System

A cloud-native, microservices-based patient management platform built with **Spring Boot** and deployed on AWS infrastructure through **Infrastructure as Code (AWS CDK)**, with **LocalStack** used to emulate the full AWS environment for local development and testing.

## Overview

This system is composed of independently deployable services — **Auth Service**, **Patient Service**, **Billing Service**, and **Analytics Service** — each owning its own data and responsibilities, and communicating through well-defined synchronous (REST/gRPC) and asynchronous (event-driven) channels. A central **API Gateway** acts as the single entry point for external clients, handling request routing, path rewriting, and JWT-based authentication before traffic reaches downstream services.

