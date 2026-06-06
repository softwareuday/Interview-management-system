import { Link } from 'react-router-dom';
import { Briefcase, Users, Calendar, TrendingUp, ArrowRight, CheckCircle, UserPlus, FileText, Video, Star, Award, Search, Zap, BarChart, Sparkles } from 'lucide-react';
import ThemeToggle from '../components/common/ThemeToggle';
import '../styles/Landing.css';

const Landing = () => {
  const features = [
    { icon: Sparkles, title: 'AI ATS Scanner', desc: 'Get instant match scores, missing skills, and recommendations against any job description.' },
    { icon: BarChart, title: 'Smart Matching', desc: 'Our algorithm matches the right candidates to the right roles automatically.' },
    { icon: TrendingUp, title: 'Hiring Pipeline', desc: 'Visual funnel from application to offer – track every stage.' },
    { icon: Calendar, title: 'Interview Scheduler', desc: 'One‑click scheduling with video, phone, or in‑person options.' },
    { icon: Users, title: 'Candidate Profiles', desc: 'Upload resumes, manage skills, and track application history.' },
    { icon: Zap, title: 'Role‑Based Access', desc: 'Separate dashboards for candidates and recruiters.' }
  ];

  return (
    <div className="landing">
      <nav className="navbar glass">
        <div className="container">
          <div className="logo">Interview<span>Portal</span></div>
          <div className="nav-actions">
            <ThemeToggle />
            <Link to="/login" className="btn btn-outline">Sign In</Link>
            <Link to="/register" className="btn btn-primary">Get Started</Link>
          </div>
        </div>
      </nav>

      <section className="hero-section">
        <div className="container hero-grid">
          <div className="hero-text">
            <h1>Find Your Dream Job or <span className="gradient-text">Hire Top Talent</span></h1>
            <p>The all‑in‑one interview management system that connects talented candidates with leading companies.</p>
            <div className="hero-buttons">
              <Link to="/jobs" className="btn btn-primary btn-lg">Browse Jobs</Link>
              <Link to="/register?role=recruiter" className="btn btn-outline btn-lg">Hire Talent</Link>
            </div>
          </div>
          <div className="hero-stats glass">
            <div className="stat-item"><strong>1000+</strong><span>Active Jobs</span></div>
            <div className="stat-item"><strong>500+</strong><span>Companies</span></div>
            <div className="stat-item"><strong>98%</strong><span>Satisfaction</span></div>
          </div>
        </div>
      </section>

      <section className="features-section">
        <div className="container">
          <h2 className="section-title">Everything You Need to Hire the Best</h2>
          <div className="features-grid">
            {features.map((f, i) => (
              <div key={i} className="feature-card glass">
                <f.icon size={36} />
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="cta-section">
        <div className="container">
          <h2>Ready to Transform Your Hiring?</h2>
          <p>Join thousands of companies and candidates already using InterviewPortal.</p>
          <Link to="/register" className="btn btn-primary btn-lg">Get Started Free</Link>
        </div>
      </section>

      <footer className="footer">
        <div className="container">
          <p>&copy; 2026 InterviewPortal. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default Landing;