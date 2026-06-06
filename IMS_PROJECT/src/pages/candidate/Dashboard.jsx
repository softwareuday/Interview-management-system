import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { dashboardAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import ThemeToggle from '../../components/common/ThemeToggle';
import { Briefcase, FileText, Calendar, Bookmark, TrendingUp, ArrowUpRight } from 'lucide-react';

const Dashboard = () => {
  const [stats, setStats] = useState({ totalApplications: 0, activeApplications: 0, upcomingInterviews: 0, savedJobs: 0 });

  useEffect(() => {
    dashboardAPI.getCandidateStats().then(res => setStats(res.data)).catch(console.error);
  }, []);

  const statCards = [
    { title: 'Total Applications', value: stats.totalApplications, icon: FileText, link: '/candidate/applications', color: '#3b82f6' },
    { title: 'Active Applications', value: stats.activeApplications, icon: TrendingUp, link: '/candidate/applications', color: '#22c55e' },
    { title: 'Upcoming Interviews', value: stats.upcomingInterviews, icon: Calendar, link: '/candidate/interviews', color: '#f59e0b' },
    { title: 'Saved Jobs', value: stats.savedJobs, icon: Bookmark, link: '/candidate/jobs', color: '#a855f7' }
  ];

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <div className="dashboard-header">
          <div><h1>Candidate Dashboard</h1><p>Track your job search progress</p></div>
          <div className="header-actions"><ThemeToggle /></div>
        </div>
        <div className="stats-grid">
          {statCards.map((stat, i) => (
            <Link to={stat.link} key={i} className="stat-card glass">
              <div className="stat-icon" style={{ backgroundColor: `${stat.color}15`, color: stat.color }}><stat.icon size={28} /></div>
              <div className="stat-info"><h3>{stat.value}</h3><p>{stat.title}</p></div>
              <ArrowUpRight size={18} className="stat-arrow" />
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;