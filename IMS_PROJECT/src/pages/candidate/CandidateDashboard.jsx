import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { dashboardAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import {
  Briefcase, FileText, Calendar, Bookmark, Search, TrendingUp,
  User, BookOpen, ArrowUpRight
} from 'lucide-react';
import '../../styles/Dashboard.css';

const CandidateDashboard = () => {
  const [stats, setStats] = useState({
    totalApplications: 0,
    activeApplications: 0,
    upcomingInterviews: 0,
    savedJobs: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => { fetchStats(); }, []);

  const fetchStats = async () => {
    try {
      const response = await dashboardAPI.getCandidateStats();
      setStats(response.data);
    } catch (err) {
      console.error('Error fetching stats:', err);
      setError('Failed to load dashboard stats');
    } finally { setLoading(false); }
  };

  const statCards = [
    { title: 'Total Applications', value: stats.totalApplications, icon: FileText, link: '/candidate/applications' },
    { title: 'Active Applications', value: stats.activeApplications, icon: TrendingUp, link: '/candidate/applications' },
    { title: 'Upcoming Interviews', value: stats.upcomingInterviews, icon: Calendar, link: '/candidate/interviews' },
    { title: 'Saved Jobs', value: stats.savedJobs, icon: Bookmark, link: '/candidate/jobs' }
  ];

  const quickActions = [
    { title: 'Browse Jobs', description: 'Explore thousands of job opportunities', icon: Search, link: '/candidate/jobs' },
    { title: 'My Applications', description: 'Track your application status', icon: FileText, link: '/candidate/applications' },
    { title: 'Interviews', description: 'View scheduled interviews', icon: Calendar, link: '/candidate/interviews' },
    { title: 'My Profile', description: 'Update your profile and resume', icon: User, link: '/candidate/profile' }
  ];

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="CANDIDATE" />
        <div className="dashboard-content">
          <div className="loading-container">
            <div className="spinner-large"></div>
            <p>Loading dashboard...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <div className="dashboard-header">
          <div className="header-text">
            <h1>Dashboard</h1>
            <p>Track your job search progress</p>
          </div>
          <Link to="/candidate/jobs" className="btn btn-primary">
            <Search size={20} /> Browse Jobs
          </Link>
        </div>

        {error && <div className="error-alert">{error}</div>}

        <div className="stats-grid">
          {statCards.map((stat, i) => (
            <Link to={stat.link} key={i} className="stat-card">
              <div className="stat-card-header">
                <div className="stat-icon" style={{
                  backgroundColor: 'var(--brown-100)',
                  color: 'var(--primary-700)'
                }}>
                  <stat.icon size={24} />
                </div>
                <ArrowUpRight size={18} className="stat-arrow" />
              </div>
              <div className="stat-body">
                <h2 className="stat-value">{stat.value}</h2>
                <p className="stat-label">{stat.title}</p>
              </div>
            </Link>
          ))}
        </div>

        <div className="quick-actions-section">
          <h2>Quick Actions</h2>
          <div className="actions-grid">
            {quickActions.map((action, i) => (
              <Link to={action.link} key={i} className="action-card">
                <div className="action-icon-wrapper" style={{ background: 'var(--gradient-primary)' }}>
                  <div className="action-icon" style={{ backgroundColor: 'var(--primary-700)' }}>
                    <action.icon size={28} strokeWidth={2} color="white" />
                  </div>
                </div>
                <div className="action-content">
                  <h3>{action.title}</h3>
                  <p>{action.description}</p>
                </div>
                <ArrowUpRight size={20} className="action-arrow" />
              </Link>
            ))}
          </div>
        </div>

        <div className="tips-section">
          <div className="tips-header">
            <BookOpen size={24} />
            <h2>Job Search Tips</h2>
          </div>
          <div className="tips-grid">
            <div className="tip-card">
              <h4>📝 Perfect Your Resume</h4>
              <p>Tailor your resume to match job descriptions and highlight relevant skills</p>
            </div>
            <div className="tip-card">
              <h4>🎯 Use ATS Score</h4>
              <p>Check your ATS match score to improve your application success rate</p>
            </div>
            <div className="tip-card">
              <h4>⚡ Apply Early</h4>
              <p>Early applicants have a higher chance of getting noticed</p>
            </div>
            <div className="tip-card">
              <h4>💬 Follow Up</h4>
              <p>Follow up on your applications after a week to show your interest</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CandidateDashboard;