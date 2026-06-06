import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { dashboardAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import ThemeToggle from '../../components/common/ThemeToggle';
import { Briefcase, Users, Calendar, FileText, Plus, ArrowUpRight } from 'lucide-react';

const RecruiterDashboard = () => {
  const [stats, setStats] = useState({ activeJobs: 0, totalCandidates: 0, interviewsToday: 0, newApplications: 0 });

  useEffect(() => {
    dashboardAPI.getRecruiterStats().then(res => setStats(res.data)).catch(console.error);
  }, []);

  const statCards = [
    { title: 'Active Jobs', value: stats.activeJobs, icon: Briefcase, link: '/recruiter/jobs', color: '#3b82f6' },
    { title: 'Total Candidates', value: stats.totalCandidates, icon: Users, link: '/recruiter/applicants', color: '#22c55e' },
    { title: 'Interviews Today', value: stats.interviewsToday, icon: Calendar, link: '/recruiter/interviews', color: '#f59e0b' },
    { title: 'New Applications', value: stats.newApplications, icon: FileText, link: '/recruiter/applicants', color: '#a855f7' }
  ];

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <div className="dashboard-header">
          <div><h1>Recruiter Dashboard</h1><p>Manage your hiring process</p></div>
          <div className="header-actions"><ThemeToggle /><Link to="/recruiter/jobs/create" className="btn btn-primary"><Plus size={18} /> Post Job</Link></div>
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

export default RecruiterDashboard;