import { Link, useNavigate } from 'react-router-dom';
import { Briefcase, Users, Calendar, TrendingUp, ArrowRight, CheckCircle } from 'lucide-react';
import '../styles/Landing.css';

const Landing = () => {
  const navigate = useNavigate();

  return (
    <div className="landing">
      {/* Hero Section */}
      <section className="hero">
        <div className="hero-content">
          <div className="hero-text">
            <h1 className="hero-title">
              Find Your Dream Job or
              <span className="gradient-text"> Hire Top Talent</span>
            </h1>
            <p className="hero-subtitle">
              Complete interview management system connecting talented candidates with leading companies
            </p>
            <div className="hero-buttons">
              <button 
                onClick={() => navigate('/register')} 
                className="btn btn-hero-primary"
              >
                Get Started
                <ArrowRight size={20} />
              </button>
              <button 
                onClick={() => navigate('/login')} 
                className="btn btn-hero-outline"
              >
                Sign In
              </button>
            </div>
          </div>
          <div className="hero-image">
            <div className="floating-card card-1">
              <Briefcase size={24} />
              <span>1000+ Jobs</span>
            </div>
            <div className="floating-card card-2">
              <Users size={24} />
              <span>500+ Companies</span>
            </div>
            <div className="floating-card card-3">
              <Calendar size={24} />
              <span>Easy Scheduling</span>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features">
        <div className="features-header">
          <h2>Why Choose Our Platform?</h2>
          <p>Everything you need to manage your hiring process</p>
        </div>

        <div className="features-grid">
          {/* For Recruiters */}
          <div className="feature-card recruiter-card">
            <div className="feature-icon recruiter-icon">
              <Briefcase size={32} />
            </div>
            <h3>For Recruiters</h3>
            <ul className="feature-list">
              <li><CheckCircle size={16} /> Post unlimited jobs</li>
              <li><CheckCircle size={16} /> ATS-powered resume screening</li>
              <li><CheckCircle size={16} /> Schedule interviews easily</li>
              <li><CheckCircle size={16} /> Track application status</li>
              <li><CheckCircle size={16} /> Analytics dashboard</li>
            </ul>
            <Link to="/register?role=recruiter" className="btn btn-primary btn-block">
              Join as Recruiter
            </Link>
          </div>

          {/* For Candidates */}
          <div className="feature-card candidate-card">
            <div className="feature-icon candidate-icon">
              <Users size={32} />
            </div>
            <h3>For Candidates</h3>
            <ul className="feature-list">
              <li><CheckCircle size={16} /> Browse thousands of jobs</li>
              <li><CheckCircle size={16} /> Get instant ATS match scores</li>
              <li><CheckCircle size={16} /> Track application progress</li>
              <li><CheckCircle size={16} /> Interview preparation tools</li>
              <li><CheckCircle size={16} /> Profile management</li>
            </ul>
            <Link to="/register?role=candidate" className="btn btn-primary btn-block">
              Join as Candidate
            </Link>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="stats">
        <div className="stat-item">
          <TrendingUp size={40} />
          <h3>1000+</h3>
          <p>Active Jobs</p>
        </div>
        <div className="stat-item">
          <Users size={40} />
          <h3>500+</h3>
          <p>Companies</p>
        </div>
        <div className="stat-item">
          <CheckCircle size={40} />
          <h3>5000+</h3>
          <p>Successful Hires</p>
        </div>
        <div className="stat-item">
          <Calendar size={40} />
          <h3>98%</h3>
          <p>Satisfaction Rate</p>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta">
        <h2>Ready to Get Started?</h2>
        <p>Join thousands of companies and candidates today</p>
        <div className="cta-buttons">
          <Link to="/register" className="btn btn-primary btn-lg">
            Sign Up Now
            <ArrowRight size={20} />
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        <p>&copy; 2026 Interview Portal. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default Landing;