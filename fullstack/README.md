# 🚀 Interview Management System – AI-Powered ATS Platform

A full-stack **recruitment platform** that connects candidates and recruiters.  
Candidates can browse jobs, apply (guest or registered), get AI‑driven ATS scores, and track applications.  
Recruiters can post jobs, manage applicants, schedule interviews, and scan resumes with an intelligent ATS.

## ✨ Key Features

### 👤 Candidate Side
- **Guest applications** – apply without an account (name + email + resume).
- **AI ATS Scanner** – upload a PDF/DOCX resume, compare against any job description, and receive:
    - Match score (0–100)
    - Matched skills ✅
    - Missing skills ❌
    - Actionable recommendations
- **Dashboard** – track applications, upcoming interviews, saved jobs.
- **Profile & resume management** – update personal info, skills, LinkedIn URL.

### 🏢 Recruiter Side
- **Job management** – create, edit, close, delete job postings.
- **Applicant tracking** – view all applicants, filter by job, update status (Applied → Shortlisted → Interview Scheduled → Selected/Rejected).
- **Recruiter‑side ATS** – manually trigger a scan on any applicant; store the score and keywords in the database.
- **Interview scheduling** – one‑click scheduling with date, time, mode (Video / Phone / In‑person), meeting link, and remarks.
- **Hiring pipeline overview** – visual funnel (Applications → Screened → Interviewed → Offers → Hired).

### 🔐 Authentication & Authorization
- JWT‑based authentication (stateless).
- Role‑based access: `CANDIDATE`, `RECRUITER`.
- Guest applications stored with `guest_name` / `guest_email`.

### 📊 Technical Highlights
- **File parsing** – Apache Tika extracts text from PDF / DOCX resumes.
- **Keyword‑based ATS fallback** – stop‑word removal, meaningful keyword matching (can be upgraded to Google Gemini AI).
- **Glassmorphism UI** – modern, responsive, dark/light mode toggle.
- **Production ready** – deployed on Render (backend) with PostgreSQL.

---

## 🛠️ Tech Stack

| Layer       | Technology                                                      |
|-------------|-----------------------------------------------------------------|
| **Frontend**| React 18, Vite, React Router, Axios, Lucide Icons, plain CSS    |
| **Backend** | Spring Boot 3.5.6, Spring Security, JWT, JPA/Hibernate          |
| **Database**| PostgreSQL (Render) / MySQL (local)                            |
| **File Storage**| Local filesystem (`uploads/resumes/`)                      |
| **Deployment**| Render (backend), Vercel/Netlify (frontend)                    |

---

## 🚀 Getting Started (Local Development)

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL or PostgreSQL (local)

### 1. Clone the repository
```bash
git clone https://github.com/softwareuday/Interview-management-system.git
cd Interview-management-system