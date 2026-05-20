import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { dashboardAPI, jobAPI, applicationAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import ThemeToggle from '../../components/common/ThemeToggle';
import { 
  Briefcase, Users, Calendar, FileText, Plus, TrendingUp, 
  UserCheck, Clock, ArrowUpRight, Eye, BarChart, Zap, 
  CheckCircle, XCircle, AlertCircle, Video, Phone, MapPin 
} from 'lucide-react';
import '../../styles/Dashboard.css';

const RecruiterDashboard = () => {
  const [stats, setStats] = useState({ 
    activeJobs: 0, 
    totalCandidates: 0, 
    interviewsToday: 0, 
    newApplications: 0 
  });
  const [recentApplicants, setRecentApplicants] = useState([]);
  const [todayInterviews, setTodayInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchStats();
    fetchRecentApplicants();
    fetchTodayInterviews();
  }, []);

  const fetchStats = async () => {
    try {
      const response = await dashboardAPI.getRecruiterStats();
      setStats(response.data);
    } catch (err) {
      console.error('Error fetching stats:', err);
      setError('Failed to load dashboard stats');
    } 
  };

  const fetchRecentApplicants = async () => {
    try {
      const response = await applicationAPI.getAllRecruiterApplications();
      const sorted = [...response.data]
        .sort((a,b) => new Date(b.appliedAt) - new Date(a.appliedAt))
        .slice(0, 4);
      setRecentApplicants(sorted);
    } catch (err) {
      console.error('Error fetching applicants:', err);
    }
  };

  const fetchTodayInterviews = async () => {
    try {
      const response = await dashboardAPI.getRecruiterInterviews?.() || [];
      const today = new Date().toISOString().split('T')[0];
      const todays = response.filter(i => i.interviewDate === today);
      setTodayInterviews(todays);
    } catch (err) {
      console.error('Error fetching interviews:', err);
    } finally {
      setLoading(false);
    }
  };

  const statCards = [
    { title: 'Active Jobs', value: stats.activeJobs, icon: Briefcase, link: '/recruiter/jobs', color: '#3b82f6', change: '+12%' },
    { title: 'Total Candidates', value: stats.totalCandidates, icon: Users, link: '/recruiter/applicants', color: '#22c55e', change: '+23%' },
    { title: 'Interviews Today', value: stats.interviewsToday, icon: Calendar, link: '/recruiter/interviews', color: '#f59e0b', change: 'scheduled' },
    { title: 'New Applications', value: stats.newApplications, icon: FileText, link: '/recruiter/applicants', color: '#a855f7', change: 'last 24h' }
  ];

  const quickActions = [
    { icon: Plus, label: 'Post a New Job', link: '/recruiter/jobs/create' },
    { icon: Users, label: 'Review Applicants', link: '/recruiter/applicants' },
    { icon: Calendar, label: 'Schedule an Interview', link: '/recruiter/interviews' }
  ];

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="RECRUITER" />
        <div className="dashboard-content">
          <div className="loading-spinner"><div className="spinner-large"></div></div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        {/* Header */}
        <div className="dashboard-header">
          <div>
            <h1>Welcome back, Recruiter!</h1>
            <p>Manage your job postings and track hiring progress.</p>
          </div>
          <div className="header-actions">
            <ThemeToggle />
            <Link to="/recruiter/jobs/create" className="btn btn-primary">
              <Plus size={18} /> Post New Job
            </Link>
          </div>
        </div>

        {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

        {/* Stats Cards Row */}
        <div className="stats-grid">
          {statCards.map((stat, i) => (
            <Link to={stat.link} key={i} className="stat-card glass">
              <div className="stat-icon" style={{ backgroundColor: `${stat.color}15`, color: stat.color }}>
                <stat.icon size={28} />
              </div>
              <div className="stat-info">
                <h3 className="stat-number">{stat.value}</h3>
                <p>{stat.title}</p>
                <span className="stat-change">{stat.change}</span>
              </div>
              <ArrowUpRight size={18} className="stat-arrow" />
            </Link>
          ))}
        </div>

        {/* Two Column Layout */}
        <div className="dashboard-grid">
          {/* Left Column: Hiring Pipeline + Recent Applicants */}
          <div className="dashboard-left">
            {/* Hiring Pipeline (Funnel) */}
            <div className="dashboard-card glass">
              <div className="card-header">
                <h3><BarChart size={20} /> Hiring Pipeline</h3>
                <span className="badge">This Month</span>
              </div>
              <div className="pipeline-bars">
                <div className="pipeline-item">
                  <span>Applications</span>
                  <div className="bar"><div className="fill" style={{ width: '100%' }}></div></div>
                  <strong>147</strong>
                </div>
                <div className="pipeline-item">
                  <span>Screened</span>
                  <div className="bar"><div className="fill" style={{ width: '60%' }}></div></div>
                  <strong>89</strong>
                </div>
                <div className="pipeline-item">
                  <span>Interviewed</span>
                  <div className="bar"><div className="fill" style={{ width: '35%' }}></div></div>
                  <strong>52</strong>
                </div>
                <div className="pipeline-item">
                  <span>Offers Made</span>
                  <div className="bar"><div className="fill" style={{ width: '12%' }}></div></div>
                  <strong>18</strong>
                </div>
                <div className="pipeline-item">
                  <span>Hired</span>
                  <div className="bar"><div className="fill" style={{ width: '7%' }}></div></div>
                  <strong>11</strong>
                </div>
              </div>
            </div>

            {/* Recent Applicants Table */}
            <div className="dashboard-card glass">
              <div className="card-header">
                <h3><Users size={20} /> Recent Applicants</h3>
                <Link to="/recruiter/applicants" className="link">View all →</Link>
              </div>
              <div className="applicants-table">
                {recentApplicants.length === 0 ? (
                  <div className="empty-message">No applicants yet.</div>
                ) : (
                  <table>
                    <thead>
                      <tr><th>Candidate</th><th>Job Title</th><th>ATS Score</th><th>Status</th></tr>
                    </thead>
                    <tbody>
                      {recentApplicants.map(app => (
                        <tr key={app.applicationId}>
                          <td>
                            <div className="candidate-cell">
                              <div className="avatar-sm">{app.candidateName?.charAt(0)}</div>
                              <span>{app.candidateName}</span>
                            </div>
                          </td>
                          <td>{app.jobTitle}</td>
                          <td>
                            {app.atsScore ? (
                              <span className={`score-badge ${app.atsScore >= 70 ? 'high' : app.atsScore >= 50 ? 'medium' : 'low'}`}>
                                {app.atsScore}%
                              </span>
                            ) : (
                              <span className="score-na">—</span>
                            )}
                          </td>
                          <td><span className="status-pill">{app.status}</span></td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </div>
            </div>
          </div>

          {/* Right Column: Quick Actions + Today's Interviews */}
          <div className="dashboard-right">
            <div className="dashboard-card glass">
              <div className="card-header"><h3><Zap size={20} /> Quick Actions</h3></div>
              <div className="quick-actions">
                {quickActions.map((action, i) => (
                  <Link to={action.link} key={i} className="action-btn">
                    <action.icon size={20} />
                    <span>{action.label}</span>
                    <ArrowUpRight size={16} />
                  </Link>
                ))}
              </div>
            </div>

            <div className="dashboard-card glass">
              <div className="card-header">
                <h3><Calendar size={20} /> Today's Interviews</h3>
                <span className="badge">{todayInterviews.length}</span>
              </div>
              <div className="today-interviews">
                {todayInterviews.length === 0 ? (
                  <div className="empty-message">No interviews scheduled today.</div>
                ) : (
                  todayInterviews.map(iv => (
                    <div key={iv.id} className="interview-item">
                      <div className="interview-time">{iv.interviewTime}</div>
                      <div className="interview-details">
                        <strong>{iv.candidateName}</strong>
                        <span>{iv.position}</span>
                        <div className="mode-badge">
                          {iv.mode === 'VIDEO' && <Video size={14} />}
                          {iv.mode === 'PHONE' && <Phone size={14} />}
                          {iv.mode === 'IN_PERSON' && <MapPin size={14} />}
                          {iv.mode}
                        </div>
                      </div>
                      <a href={iv.meetingLink} target="_blank" rel="noopener noreferrer" className="btn-sm">Join</a>
                    </div>
                  ))
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RecruiterDashboard;