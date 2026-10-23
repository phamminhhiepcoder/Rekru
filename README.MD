# Recruitment Management System

A recruitment management system that streamlines employee management, department requests, job postings, candidate management, and AI-powered CV screening using Grok AI.
---

## 🚀 Features

### Recruitment Management System
- Admin Management: Manage user accounts, employee information, and roles and permissions
- Department Request Management: Create, view, and update recruitment requests
- Candidate Management: Add, update, and view candidate information
- Job Post Management: Create, update, and manage job postings
- AI-Powered CV Screening: Define recruitment criteria, analyze CVs using Grok AI, evaluate candidate suitability, and review screening results

### 🤖 Groq API Integration
- AI-powered CV analysis based on recruitment criteria
- Automated extraction of candidate information from uploaded PDF resumes
- Criteria-based evaluation of candidate qualifications, skills, and experience
- AI-generated candidate summaries, strengths, weaknesses, and suitability scores

### ☁ Media & Email Management
- Media files handling via Github
- Email notifications using Google Email Service:
  - Account registration to department employess and HRs
  - Reset passwords
  - Request information sending to candidate's email


---

## 🏗 System Architecture

```
Admin
 ├── User & Employee Management
 │    ├── Manage User Accounts
 │    ├── Manage Employee Information
 │    └── Assign Roles & Permissions
 │
 └── Dashboard


DEP (Department)
 ├── Request Management
 │    ├── Add New Request
 │    ├── View Detail Request
 │    └── Update Request
 │    └── View Requests
 │
 └── Dashboard
 └── Profile


HR (Human Resources)
 ├── Candidate Management
 │    ├── Add New Candidate
 │    ├── Update Candidate Information
 │    ├── View Candidates
 │    └── View Detail Candidate
 │
 ├── Post Management
 │    ├── Add New Job Post
 │    ├── Update Job Post Information
 │    ├── View Job Posts
 │    └── View Detail Job Post
 │
 ├── CV Screening Test
 │    ├── View Requests
 │    ├── Define Recruitment Criteria
 │    ├── Analyze CVs Using Grok AI
 │    ├── Evaluate Candidate Suitability
 │    └── View Screening Results
 │
 └── Dashboard
 └── Profile
```

---

## 🛠 Tech Stack

### Backend
- Spring Boot
- RESTful APIs

### AI Services
- Grok API

### Database
- MySQL

### External Services
- Github (File Management)
- Google Email Service

---

## ⚙ Installation

```bash
# Clone repository
git clone https://github.com/phamminhhiepcoder/Rekru.git

# Run project
Click Run Java in VS Code or Intellij IDEA
```

---

## 🔐 Environment Variables

Setup your keys in application.properties:

```
#Github Upload configuration
github.token=<token>
github.owner=<owner>
github.repo=<repo>
github.branch=<branch>


#Send email
spring.mail.username=<your-email>
spring.mail.password=<your-password>
#GROK
grok.api.key=<apiKey>
grok.api.url=<apiUrl>
grok.model=<model>
```

---

## Screenshots

---

## 📌 Future Improvements
- Multi-language support
- Mobile app integration
- Responsive UI Optimization

