import { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { jobAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { Plus, Search, Briefcase, MapPin, DollarSign, Users, Calendar, Edit, Trash2, XCircle, Eye, Filter, AlertCircle, CheckCircle } from 'lucide-react';
import '../../styles/Jobs.css';

const Jobs = () => {
  const location = useLocation();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('ALL');

  useEffect(() => {
    fetchJobs();
    if (location.state?.message) {
      setSuccessMessage(location.state.message);
      setTimeout(() => setSuccessMessage(''), 5000);
      window.history.replaceState({}, document.title);
    }
  }, [location]);

  const fetchJobs = async () => {
    try {
      const response = await jobAPI.getRecruiterJobs();
      setJobs(response.data);
    } catch (err) {
      console.error('Error fetching jobs:', err);
      setError('Failed to load jobs');
    } finally { setLoading(false); }
  };

  const handleCloseJob = async (jobId) => {
    if (!window.confirm('Are you sure you want to close this job posting? Candidates will no longer be able to apply.')) return;
    try {
      await jobAPI.closeJob(jobId);
      setJobs(jobs.map(job => job.id === jobId ? { ...job, status: 'CLOSED' } : job));
      setSuccessMessage('Job closed successfully');
      setTimeout(() => setSuccessMessage(''), 5000);
    } catch (err) { alert('Failed to close job: ' + (err.response?.data?.message || 'Please try again')); }
  };

  const handleDeleteJob = async (jobId) => {
    if (!window.confirm('Are you sure you want to delete this job? This action cannot be undone and all applications will be lost.')) return;
    try {
      await jobAPI.deleteJob(jobId);
      setJobs(jobs.filter(job => job.id !== jobId));
      setSuccessMessage('Job deleted successfully');
      setTimeout(() => setSuccessMessage(''), 5000);
    } catch (err) { alert('Failed to delete job: ' + (err.response?.data?.message || 'Please try again')); }
  };

  const filteredJobs = jobs.filter(job => {
    const matchesSearch = job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         job.department?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         job.location?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === 'ALL' || job.status === filterStatus;
    return matchesSearch && matchesStatus;
  });

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="RECRUITER" />
        <div className="dashboard-content"><div className="loading-container"><div className="spinner-large"></div><p>Loading jobs...</p></div></div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <div className="page-header">
          <div><h1>Jobs</h1><p>Manage your job postings and track applications</p></div>
          <Link to="/recruiter/jobs/create" className="btn btn-primary"><Plus size={20} />Post New Job</Link>
        </div>
        {successMessage && <div className="success-alert"><CheckCircle size={20} />{successMessage}</div>}
        {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}
        <div className="jobs-filters">
          <div className="search-box">
            <Search size={20} />
            <input type="text" placeholder="Search jobs by title, department, or location..." value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)} className="search-input" />
          </div>
          <div className="filter-buttons">
            <button className={`filter-btn ${filterStatus === 'ALL' ? 'active' : ''}`} onClick={() => setFilterStatus('ALL')}>All Jobs ({jobs.length})</button>
            <button className={`filter-btn ${filterStatus === 'OPEN' ? 'active' : ''}`} onClick={() => setFilterStatus('OPEN')}>Open ({jobs.filter(j => j.status === 'OPEN').length})</button>
            <button className={`filter-btn ${filterStatus === 'CLOSED' ? 'active' : ''}`} onClick={() => setFilterStatus('CLOSED')}>Closed ({jobs.filter(j => j.status === 'CLOSED').length})</button>
          </div>
        </div>
        {filteredJobs.length === 0 ? (
          <div className="empty-state">
            <Briefcase size={64} strokeWidth={1.5} />
            <h3>No jobs found</h3>
            <p>{searchTerm || filterStatus !== 'ALL' ? 'Try adjusting your filters' : 'Start by posting your first job'}</p>
            {!searchTerm && filterStatus === 'ALL' && <Link to="/recruiter/jobs/create" className="btn btn-primary"><Plus size={20} />Post Your First Job</Link>}
          </div>
        ) : (
          <div className="jobs-grid">
            {filteredJobs.map(job => (
              <div key={job.id} className="job-card">
                <div className="job-card-header">
                  <div className="job-title-section">
                    <h3>{job.title}</h3>
                    <span className={`job-status-badge ${job.status.toLowerCase()}`}>{job.status}</span>
                  </div>
                  <div className="job-actions">
                    <Link to={`/recruiter/applicants?jobId=${job.id}`} className="icon-btn" title="View Applicants"><Eye size={18} /></Link>
                    <Link to={`/recruiter/jobs/${job.id}/edit`} className="icon-btn" title="Edit Job"><Edit size={18} /></Link>
                    {job.status === 'OPEN' && (
                      <button onClick={() => handleCloseJob(job.id)} className="icon-btn" title="Close Job"><XCircle size={18} /></button>
                    )}
                    <button onClick={() => handleDeleteJob(job.id)} className="icon-btn danger" title="Delete Job"><Trash2 size={18} /></button>
                  </div>
                </div>
                <div className="job-card-body">
                  <div className="job-meta">
                    {job.department && <div className="meta-item"><Briefcase size={16} /><span>{job.department}</span></div>}
                    {job.location && <div className="meta-item"><MapPin size={16} /><span>{job.location}</span></div>}
                    {job.salaryRange && <div className="meta-item"><DollarSign size={16} /><span>{job.salaryRange}</span></div>}
                  </div>
                  <p className="job-description">{job.description?.substring(0, 150)}{job.description?.length > 150 ? '...' : ''}</p>
                  {job.requiredSkills?.length > 0 && (
                    <div className="job-skills">
                      {job.requiredSkills.slice(0, 3).map((skill, i) => <span key={i} className="skill-tag">{skill}</span>)}
                      {job.requiredSkills.length > 3 && <span className="skill-tag more">+{job.requiredSkills.length - 3} more</span>}
                    </div>
                  )}
                </div>
                <div className="job-card-footer">
                  <div className="footer-item"><Users size={16} /><span>{job.applicantsCount || 0} applicants</span></div>
                  <div className="footer-item"><Calendar size={16} /><span>Posted {new Date(job.createdAt).toLocaleDateString()}</span></div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Jobs;