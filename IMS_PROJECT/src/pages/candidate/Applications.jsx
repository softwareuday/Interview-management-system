import { useState, useEffect } from 'react';
import { applicationAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { FileText, Search, TrendingUp, Calendar, Briefcase, CheckCircle, XCircle, Clock, Star, AlertCircle, Eye } from 'lucide-react';
import { APPLICATION_STATUS, STATUS_LABELS } from '../../constants';
import '../../styles/Applications.css';
import { API_BASE_URL } from '../../constants';

const Applications = () => {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  useEffect(() => { fetchApplications(); }, []);

  const fetchApplications = async () => {
    try {
      const response = await applicationAPI.getCandidateApplications();
      setApplications(response.data);
    } catch (err) {
      console.error('Error fetching applications:', err);
      setError('Failed to load applications');
    } finally { setLoading(false); }
  };

  const filteredApplications = applications.filter(app => {
    const matchesSearch = app.jobTitle.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         (app.companyName?.toLowerCase() || '').includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || app.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const getStatusColor = (status) => {
    switch (status) {
      case APPLICATION_STATUS.APPLIED: return 'status-applied';
      case APPLICATION_STATUS.SHORTLISTED: return 'status-shortlisted';
      case APPLICATION_STATUS.INTERVIEW_SCHEDULED: return 'status-interview';
      case APPLICATION_STATUS.SELECTED: return 'status-selected';
      case APPLICATION_STATUS.REJECTED: return 'status-rejected';
      default: return 'status-applied';
    }
  };

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="CANDIDATE" />
        <div className="dashboard-content"><div className="loading-container"><div className="spinner-large"></div><p>Loading applications...</p></div></div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <div className="page-header">
          <div><h1>My Applications</h1><p>Track your job application status</p></div>
          <div className="header-stats">
            <div className="stat-chip"><FileText size={18} /><span>{applications.length} Total Applications</span></div>
            <div className="stat-chip"><Clock size={18} /><span>{applications.filter(a => a.status === APPLICATION_STATUS.APPLIED).length} In Review</span></div>
          </div>
        </div>
        {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}
        <div className="applications-filters">
          <div className="search-box">
            <Search size={20} />
            <input type="text" placeholder="Search by job title or company..." value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)} className="search-input" />
          </div>
          <div className="status-filter-buttons">
            <button className={`status-filter-btn ${statusFilter === 'ALL' ? 'active' : ''}`}
              onClick={() => setStatusFilter('ALL')}>All ({applications.length})</button>
            {Object.keys(APPLICATION_STATUS).map(status => (
              <button key={status} className={`status-filter-btn ${statusFilter === status ? 'active' : ''}`}
                onClick={() => setStatusFilter(status)}>
                {STATUS_LABELS[status]} ({applications.filter(a => a.status === status).length})
              </button>
            ))}
          </div>
        </div>
        {filteredApplications.length === 0 ? (
          <div className="empty-state">
            <FileText size={64} strokeWidth={1.5} />
            <h3>No applications found</h3>
            <p>{searchTerm || statusFilter !== 'ALL' ? 'Try adjusting your filters' : 'You haven\'t applied to any jobs yet. Start browsing!'}</p>
          </div>
        ) : (
          <div className="applications-list">
            {filteredApplications.map(app => (
              <div key={app.applicationId} className="application-card">
                <div className="application-header">
                  <div className="job-info">
                    <div className="company-icon"><Briefcase size={24} /></div>
                    <div><h3>{app.jobTitle}</h3><p className="company-name">{app.companyName || 'Company'}</p></div>
                  </div>
                  <span className={`status-badge ${getStatusColor(app.status)}`}>
                    {app.status === APPLICATION_STATUS.APPLIED && <FileText size={16} />}
                    {app.status === APPLICATION_STATUS.SHORTLISTED && <Star size={16} />}
                    {app.status === APPLICATION_STATUS.INTERVIEW_SCHEDULED && <Calendar size={16} />}
                    {app.status === APPLICATION_STATUS.SELECTED && <CheckCircle size={16} />}
                    {app.status === APPLICATION_STATUS.REJECTED && <XCircle size={16} />}
                    {STATUS_LABELS[app.status]}
                  </span>
                </div>
                <div className="application-body">
                  {app.atsScore !== null && (
                    <div className="ats-score-section">
                      <div className="ats-label"><TrendingUp size={18} /><span>ATS Match Score</span></div>
                      <div className="ats-progress">
                        <div className="progress-bar"><div className={`progress-fill ${app.atsScore >= 70 ? 'high' : app.atsScore >= 50 ? 'medium' : 'low'}`} style={{ width: `${app.atsScore}%` }}></div></div>
                        <span className="ats-value">{app.atsScore}%</span>
                      </div>
                      <p className="ats-hint">
                        {app.atsScore >= 70 ? 'Excellent match!' : app.atsScore >= 50 ? 'Good match.' : 'Update your resume to better match the job requirements.'}
                      </p>
                    </div>
                  )}
                  <div className="application-timeline">
                    {['APPLIED', 'SHORTLISTED', 'INTERVIEW_SCHEDULED', 'SELECTED'].map((step, idx) => (
                      <div key={step} className={`timeline-item ${['APPLIED', 'SHORTLISTED', 'INTERVIEW_SCHEDULED', 'SELECTED'].indexOf(app.status) >= idx ? 'completed' : ''}`}>
                        <div className="timeline-dot"></div>
                        <div className="timeline-content">
                          <span className="timeline-label">{STATUS_LABELS[step]}</span>
                          <span className="timeline-date">
                            {step === 'APPLIED' ? new Date(app.appliedAt).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }) :
                             step === app.status ? 'Current' : 'Pending'}
                          </span>
                        </div>
                      </div>
                    ))}
                  </div>
                  {app.statusNotes && (
                    <div className="status-notes"><strong>Note from recruiter:</strong><p>{app.statusNotes}</p></div>
                  )}
                </div>
                <div className="application-footer">
                  <div className="footer-info"><Calendar size={14} /><span>Applied {new Date(app.appliedAt).toLocaleDateString()}</span></div>
                  {app.resumeUrl && (
                    <a href={`${API_BASE_URL}${app.resumeUrl}`} target="_blank" rel="noopener noreferrer" className="btn-view-resume">
                      <Eye size={16} /> View Resume
                    </a>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Applications;