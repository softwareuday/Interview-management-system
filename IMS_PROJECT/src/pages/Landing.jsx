import { Link } from 'react-router-dom';
import { Briefcase, Users, Calendar, TrendingUp, ArrowRight, CheckCircle, UserPlus, FileText, Video, Star, Award, Search, Zap, BarChart, Clock, Eye, Sparkles } from 'lucide-react';
import ThemeToggle from '../components/common/ThemeToggle';
import '../styles/Landing.css';

const Landing = () => {
  const features = [
    { icon: Sparkles, title: 'AI ATS Scanner', desc: 'Instant keyword scoring vs job description.' },
    { icon: BarChart, title: 'Smart Job Matching', desc: 'Candidates auto-matched to relevant roles.' },
    { icon: TrendingUp, title: 'Hiring Pipeline', desc: 'Visual funnel from apply to hire.' },
    { icon: Calendar, title: 'Interview Scheduler', desc: 'One-click scheduling with meeting links.' },
    { icon: Users, title: 'Candidate Profiles', desc: 'Resume upload, skills, LinkedIn sync.' },
    { icon: Zap, title: 'Role-Based Access', desc: 'Separate flows for recruiter & candidate.' }
  ];

  const steps = [
    { icon: UserPlus, title: 'Create Profile', desc: 'Register & upload your resume in 2 minutes.' },
    { icon: Search, title: 'Browse Jobs', desc: 'Search open roles, run ATS scan, apply.' },
    { icon: Star, title: 'Get Hired', desc: 'Track status, prepare for interviews.' }
  ];

  return (
    <div className="landing">
      {/* Sticky Navbar with glass effect on scroll */}
      <nav className="navbar glass" id="navbar">
        <div className="container">
          <div className="logo">Hire<span>IQ</span></div>
          <div className="nav-links">
            <a href="#features">Features</a>
            <a href="#howitworks">How it works</a>
            <Link to="/jobs">Browse Jobs</Link>
          </div>
          <div className="nav-actions">
            <ThemeToggle />
            <Link to="/login" className="btn btn-outline">Sign In</Link>
            <Link to="/register" className="btn btn-primary">Get Started</Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-bg"></div>
        <div className="container hero-grid">
          <div className="hero-text animate-left">
            <span className="badge glass">🤖 AI-Powered Recruitment</span>
            <h1>Hire Smarter.<br /><span className="gradient-text">Screen Faster.</span></h1>
            <p>The all‑in‑one ATS platform that matches the right talent to the right role — automatically.</p>
            <div className="hero-buttons">
              <Link to="/register" className="btn btn-primary btn-lg">Get Started Free</Link>
              <Link to="/jobs" className="btn btn-outline btn-lg">Browse Jobs →</Link>
            </div>
            <div className="trust-badge">
              <div className="avatar-stack">
                <div className="avatar">U</div>
                <div className="avatar">P</div>
                <div className="avatar">R</div>
              </div>
              <span>No credit card · Free forever · 500+ companies</span>
            </div>
          </div>
          <div className="hero-mockup animate-right">
            <div className="mockup-card glass">
              <div className="score-gauge">
                <svg width="100" height="100" viewBox="0 0 100 100">
                  <circle cx="50" cy="50" r="42" fill="none" stroke="#e2e8f0" strokeWidth="8"/>
                  <circle cx="50" cy="50" r="42" fill="none" stroke="#3b5bdb" strokeWidth="8" strokeDasharray="264" strokeDashoffset="58" strokeLinecap="round" transform="rotate(-90 50 50)"/>
                  <text x="50" y="50" textAnchor="middle" dominantBaseline="central" fontSize="18" fontWeight="700" fill="#3b5bdb">82%</text>
                </svg>
              </div>
              <div className="skills-row">
                <span className="skill-match">React ✓</span>
                <span className="skill-match">Spring Boot ✓</span>
                <span className="skill-miss">Docker ✗</span>
              </div>
              <div className="ats-score-text">ATS Score: <strong>82%</strong></div>
            </div>
            <div className="floating-badge top-right">✓ 142 Jobs Posted</div>
            <div className="floating-badge bottom-left">📊 ATS Analyzed</div>
          </div>
        </div>
      </section>

      {/* Stats Bar */}
      <section className="stats-bar glass">
        <div className="container stats-grid">
          <div className="stat-item"><strong>500+</strong><span>Companies</span></div>
          <div className="stat-item"><strong>12,000+</strong><span>Jobs Posted</span></div>
          <div className="stat-item"><strong>98%</strong><span>Match Rate</span></div>
          <div className="stat-item"><strong>3x</strong><span>Faster Hire</span></div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="features-section">
        <div className="container">
          <h2 className="section-title animate-up">Everything You Need to <span className="gradient-text">Hire the Best</span></h2>
          <div className="features-grid">
            {features.map((f, i) => (
              <div key={i} className={`feature-card glass stagger-${i+1} animate-scale`}>
                <f.icon size={36} color="var(--primary-600)" />
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section id="howitworks" className="howitworks-section">
        <div className="container">
          <h2 className="section-title animate-up">How It Works</h2>
          <div className="steps-tabs">
            <button className="active">For Candidates</button>
            <button>For Recruiters</button>
          </div>
          <div className="steps-grid">
            {steps.map((step, i) => (
              <div key={i} className={`step-card glass animate-${i === 0 ? 'left' : i === 1 ? 'up' : 'right'} stagger-${i+1}`}>
                <div className="step-number">{i+1}</div>
                <step.icon size={32} />
                <h3>{step.title}</h3>
                <p>{step.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ATS Scanner Preview */}
      <section className="ats-preview">
        <div className="container preview-grid">
          <div className="preview-left animate-left">
            <div className="preview-input glass">
              <textarea placeholder="Paste Job Description here..." rows={4}></textarea>
              <div className="upload-area">📎 Upload Resume (PDF/DOCX)</div>
              <button className="btn btn-primary">Scan Now</button>
            </div>
          </div>
          <div className="preview-right animate-right">
            <div className="preview-score glass">
              <div className="score-gauge-large">
                <svg width="140" height="140" viewBox="0 0 140 140">
                  <circle cx="70" cy="70" r="60" fill="none" stroke="#e2e8f0" strokeWidth="12"/>
                  <circle cx="70" cy="70" r="60" fill="none" stroke="#22c55e" strokeWidth="12" strokeDasharray="377" strokeDashoffset="83" strokeLinecap="round" transform="rotate(-90 70 70)"/>
                  <text x="70" y="70" textAnchor="middle" dominantBaseline="central" fontSize="24" fontWeight="700" fill="#22c55e">78%</text>
                </svg>
              </div>
              <p>Good Match</p>
              <div className="matched-skills">✅ Java ✅ Spring Boot ✅ REST APIs</div>
              <div className="missing-skills">❌ AWS ❌ Kubernetes</div>
              <div className="recommendation">Add AWS and GoLang to reach 91%</div>
            </div>
          </div>
        </div>
        <p className="preview-note">This is just a preview — sign up to scan your actual resume →</p>
      </section>

      {/* Role Cards */}
      <section className="role-cards">
        <div className="container role-grid">
          <div className="role-card candidate-card animate-left">
            <div className="role-icon">👤</div>
            <h2>I'm a Job Seeker</h2>
            <ul>
              <li>✓ Free resume ATS scanning</li>
              <li>✓ One-click job applications</li>
              <li>✓ Interview tracking dashboard</li>
              <li>✓ Real-time application status</li>
            </ul>
            <Link to="/register?role=candidate" className="btn btn-primary">Find Jobs →</Link>
          </div>
          <div className="role-card recruiter-card animate-right">
            <div className="role-icon">🏢</div>
            <h2>I'm a Recruiter</h2>
            <ul>
              <li>✓ Post unlimited job listings</li>
              <li>✓ AI-powered candidate screening</li>
              <li>✓ Hiring pipeline management</li>
              <li>✓ One-click interview scheduling</li>
            </ul>
            <Link to="/register?role=recruiter" className="btn btn-primary">Start Hiring →</Link>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container cta-content">
          <h2>Ready to Transform Your Hiring?</h2>
          <p>Join 500+ companies already using HireIQ</p>
          <div className="cta-buttons">
            <Link to="/register" className="btn btn-primary btn-lg">Get Started for Free</Link>
            <button className="btn btn-outline btn-lg">Schedule a Demo</button>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        <div className="container">
          <p>&copy; 2026 HireIQ. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default Landing;