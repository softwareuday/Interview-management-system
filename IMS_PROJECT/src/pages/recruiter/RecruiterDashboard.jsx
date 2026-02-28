import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { dashboardAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { Briefcase, Users, Calendar, FileText, Plus, TrendingUp, UserCheck, Clock, ArrowUpRight } from 'lucide-react';
import '../../styles/Dashboard.css';

const RecruiterDashboard = () => {
  const [stats, setStats] = useState({ activeJobs: 0, totalCandidates: 0, interviewsToday: 0, newApplications: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => { fetchStats(); }, []);

  const fetchStats = async () => {
    try {
      const response = await dashboardAPI.getRecruiterStats();
      setStats(response.data);
    } catch (err) {
      console.error('Error fetching stats:', err);
      setError('Failed to load dashboard stats');
    } finally { setLoading(false); }
  };

  const statCards = [
    { title: 'Active Jobs', value: stats.activeJobs, icon: Briefcase, color: 'var(--primary-700)', bg: 'var(--brown-100)', link: '/recruiter/jobs', change: '+12%' },
    { title: 'Total Candidates', value: stats.totalCandidates, icon: Users, color: 'var(--primary-700)', bg: 'var(--brown-100)', link: '/recruiter/applicants', change: '+23%' },
    { title: 'Interviews Today', value: stats.interviewsToday, icon: Calendar, color: 'var(--primary-700)', bg: 'var(--brown-100)', link: '/recruiter/interviews', change: '2 scheduled' },
    { title: 'New Applications', value: stats.newApplications, icon: FileText, color: 'var(--primary-700)', bg: 'var(--brown-100)', link: '/recruiter/applicants', change: 'Last 24h' }
  ];

  const quickActions = [
    { title: 'Post New Job', description: 'Create a new job posting', icon: Plus, link: '/recruiter/jobs/create', gradient: 'var(--gradient-primary)', iconBg: 'var(--primary-700)' },
    { title: 'Review Applicants', description: 'View and manage candidates', icon: UserCheck, link: '/recruiter/applicants', gradient: 'var(--gradient-primary)', iconBg: 'var(--primary-700)' },
    { title: 'Schedule Interview', description: 'Manage candidate interviews', icon: Clock, link: '/recruiter/interviews', gradient: 'var(--gradient-primary)', iconBg: 'var(--primary-700)' },
    { title: 'View Analytics', description: 'Check hiring metrics', icon: TrendingUp, link: '/recruiter/jobs', gradient: 'var(--gradient-primary)', iconBg: 'var(--primary-700)' }
  ];

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="RECRUITER" />
        <div className="dashboard-content"><div className="loading-container"><div className="spinner-large"></div><p>Loading dashboard...</p></div></div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <div className="dashboard-header">
          <div className="header-text"><h1>Dashboard</h1><p>Welcome back! Here's your hiring overview.</p></div>
          <Link to="/recruiter/jobs/create" className="btn btn-primary"><Plus size={20} />Post New Job</Link>
        </div>
        {error && <div className="error-alert">{error}</div>}
        <div className="stats-grid">
          {statCards.map((stat, i) => (
            <Link to={stat.link} key={i} className="stat-card">
              <div className="stat-card-header">
                <div className="stat-icon" style={{ backgroundColor: stat.bg, color: stat.color }}><stat.icon size={24} /></div>
                <ArrowUpRight size={18} className="stat-arrow" />
              </div>
              <div className="stat-body">
                <h2 className="stat-value">{stat.value}</h2>
                <p className="stat-label">{stat.title}</p>
              </div>
              <div className="stat-footer"><span className="stat-change">{stat.change}</span></div>
            </Link>
          ))}
        </div>
        <div className="quick-actions-section">
          <h2>Quick Actions</h2>
          <div className="actions-grid">
            {quickActions.map((action, i) => (
              <Link to={action.link} key={i} className="action-card">
                <div className="action-icon-wrapper" style={{ background: action.gradient }}>
                  <div className="action-icon" style={{ backgroundColor: action.iconBg }}><action.icon size={28} strokeWidth={2} /></div>
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
        <div className="recent-activity">
          <h2>Recent Activity</h2>
          <div className="activity-placeholder">
            <FileText size={48} strokeWidth={1.5} style={{ color: 'var(--text-muted)' }} />
            <p>Activity timeline coming soon</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RecruiterDashboard;