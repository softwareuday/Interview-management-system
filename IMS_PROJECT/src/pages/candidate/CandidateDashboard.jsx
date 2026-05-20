import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { dashboardAPI, jobAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import ThemeToggle from '../../components/common/ThemeToggle';
import { Briefcase, FileText, Calendar, Bookmark, TrendingUp, User, BookOpen, ArrowUpRight, Clock, CheckCircle, AlertCircle } from 'lucide-react';
import '../../styles/Dashboard.css';

const CandidateDashboard = () => {
  const [stats, setStats] = useState({ totalApplications: 0, activeApplications: 0, upcomingInterviews: 0, savedJobs: 0 });
  const [recentJobs, setRecentJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchStats();
    fetchRecentJobs();
  }, []);

  const fetchStats = async () => {
    try {
      const response = await dashboardAPI.getCandidateStats();
      setStats(response.data);
    } catch (err) {
      console.error(err);
      setError('Failed to load dashboard stats');
    } finally { setLoading(false); }
  };

  const fetchRecentJobs = async () => {
    try {
      const response = await jobAPI.browseJobs({ status: 'OPEN', page: 0, size: 3 });
      setRecentJobs(response.data);
    } catch (err) { console.error(err); }
  };

  const statCards = [
    { title: 'Total Applications', value: stats.totalApplications, icon: FileText, link: '/candidate/applications', color: '#3b82f6' },
    { title: 'Active Applications', value: stats.activeApplications, icon: TrendingUp, link: '/candidate/applications', color: '#22c55e' },
    { title: 'Upcoming Interviews', value: stats.upcomingInterviews, icon: Calendar, link: '/candidate/interviews', color: '#f59e0b' },
    { title: 'Saved Jobs', value: stats.savedJobs, icon: Bookmark, link: '/candidate/jobs', color: '#a855f7' }
  ];

  if (loading) return <div className="loading-spinner"><div className="spinner-large"></div></div>;

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <div className="dashboard-header">
          <div>
            <h1>Welcome back, Candidate!</h1>
            <p>Track your job search progress and discover new opportunities.</p>
          </div>
          <div className="header-actions">
            <ThemeToggle />
            <Link to="/candidate/jobs" className="btn btn-primary">Browse Jobs</Link>
          </div>
        </div>
        {error && <div className="error-alert">{error}</div>}

        {/* Stats Cards */}
        <div className="stats-grid">
          {statCards.map((stat, i) => (
            <Link to={stat.link} key={i} className="stat-card glass">
              <div className="stat-icon" style={{ backgroundColor: `${stat.color}15`, color: stat.color }}>
                <stat.icon size={28} />
              </div>
              <div className="stat-info">
                <h3>{stat.value}</h3>
                <p>{stat.title}</p>
              </div>
              <ArrowUpRight size={18} className="stat-arrow" />
            </Link>
          ))}
        </div>

        {/* Two column layout */}
        <div className="dashboard-grid">
          <div className="dashboard-card glass">
            <div className="card-header">
              <h3><Clock size={20} /> Recent Applications</h3>
              <Link to="/candidate/applications" className="link">View all</Link>
            </div>
            <div className="recent-list">
              {stats.totalApplications === 0 ? (
                <div className="empty-message">No applications yet. Start applying!</div>
              ) : (
                <p>Your applications will appear here.</p>
              )}
            </div>
          </div>
          <div className="dashboard-card glass">
            <div className="card-header">
              <h3><Briefcase size={20} /> Recommended for You</h3>
              <Link to="/candidate/jobs" className="link">View all</Link>
            </div>
            <div className="recommended-jobs">
              {recentJobs.length ? recentJobs.map(job => (
                <div key={job.id} className="job-item">
                  <div><strong>{job.title}</strong><br />{job.location || 'Remote'}</div>
                  <Link to={`/candidate/jobs`} className="btn-sm">Apply</Link>
                </div>
              )) : <div className="empty-message">No jobs available.</div>}
            </div>
          </div>
        </div>

        {/* Tips */}
        <div className="dashboard-card glass tips-card">
          <div className="card-header"><h3><BookOpen size={20} /> Pro Tips</h3></div>
          <div className="tips-grid">
            <div className="tip"><CheckCircle size={16} /> Tailor your resume for each job</div>
            <div className="tip"><CheckCircle size={16} /> Use the ATS scanner to improve match</div>
            <div className="tip"><CheckCircle size={16} /> Apply within 48 hours of posting</div>
            <div className="tip"><CheckCircle size={16} /> Follow up after one week</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CandidateDashboard;