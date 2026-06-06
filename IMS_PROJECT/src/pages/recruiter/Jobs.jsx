import { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { jobAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { Plus, Search, Briefcase, MapPin, DollarSign, Users, Calendar, Edit, Trash2, XCircle, Eye, Filter, AlertCircle, CheckCircle } from 'lucide-react';
import { JOB_TYPE_LABELS } from '../../constants';

const Jobs = () => {
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
    }
  }, []);

  const fetchJobs = async () => {
    try {
      const res = await jobAPI.getRecruiterJobs();
      setJobs(res.data);
    } catch (err) {
      setError('Failed to load jobs');
    } finally {
      setLoading(false);
    }
  };

  const handleCloseJob = async (jobId) => {
    if (!window.confirm('Close this job? Candidates can no longer apply.')) return;
    try {
      await jobAPI.closeJob(jobId);
      setJobs(jobs.map(j => j.id === jobId ? { ...j, status: 'CLOSED' } : j));
      setSuccessMessage('Job closed');
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (err) {
      alert('Failed to close job');
    }
  };

  const handleDeleteJob = async (jobId) => {
    if (!window.confirm('Delete this job? This cannot be undone.')) return;
    try {
      await jobAPI.deleteJob(jobId);
      setJobs(jobs.filter(j => j.id !== jobId));
      setSuccessMessage('Job deleted');
    } catch (err) {
      alert('Failed to delete job');
    }
  };

  const filteredJobs = jobs.filter(job => {
    const matchesSearch = job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         (job.department?.toLowerCase() || '').includes(searchTerm.toLowerCase()) ||
                         (job.location?.toLowerCase() || '').includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === 'ALL' || job.status === filterStatus;
    return matchesSearch && matchesStatus;
  });

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="RECRUITER" />
        <div className="dashboard-content">Loading jobs...</div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <div className="dashboard-header">
          <div><h1>Jobs</h1><p>Manage your job postings</p></div>
          <Link to="/recruiter/jobs/create" className="btn btn-primary"><Plus size={20} /> Post New Job</Link>
        </div>
        {successMessage && <div className="success-alert">{successMessage}</div>}
        {error && <div className="error-alert">{error}</div>}

        <div className="jobs-filters">
          <div className="search-box">
            <Search size={20} />
            <input type="text" placeholder="Search jobs..." value={searchTerm} onChange={e => setSearchTerm(e.target.value)} />
          </div>
          <div className="filter-buttons">
            <button className={`filter-btn ${filterStatus === 'ALL' ? 'active' : ''}`} onClick={() => setFilterStatus('ALL')}>All ({jobs.length})</button>
            <button className={`filter-btn ${filterStatus === 'OPEN' ? 'active' : ''}`} onClick={() => setFilterStatus('OPEN')}>Open ({jobs.filter(j => j.status === 'OPEN').length})</button>
            <button className={`filter-btn ${filterStatus === 'CLOSED' ? 'active' : ''}`} onClick={() => setFilterStatus('CLOSED')}>Closed ({jobs.filter(j => j.status === 'CLOSED').length})</button>
          </div>
        </div>

        {filteredJobs.length === 0 ? (
          <div className="empty-state">No jobs found</div>
        ) : (
          <div className="jobs-grid">
            {filteredJobs.map(job => (
              <div key={job.id} className="job-card glass">
                <div className="job-card-header">
                  <div><h3>{job.title}</h3><span className={`job-status-badge ${job.status.toLowerCase()}`}>{job.status}</span></div>
                  <div className="job-actions">
                    <Link to={`/recruiter/applicants?jobId=${job.id}`} className="icon-btn"><Eye size={18} /></Link>
                    <Link to={`/recruiter/jobs/${job.id}/edit`} className="icon-btn"><Edit size={18} /></Link>
                    {job.status === 'OPEN' && <button onClick={() => handleCloseJob(job.id)} className="icon-btn"><XCircle size={18} /></button>}
                    <button onClick={() => handleDeleteJob(job.id)} className="icon-btn danger"><Trash2 size={18} /></button>
                  </div>
                </div>
                <div className="job-meta">
                  {job.department && <span><Briefcase size={16} /> {job.department}</span>}
                  {job.location && <span><MapPin size={16} /> {job.location}</span>}
                  {job.salaryRange && <span><DollarSign size={16} /> {job.salaryRange}</span>}
                </div>
                <p className="job-description">{job.description?.substring(0, 120)}...</p>
                {job.requiredSkills?.length > 0 && (
                  <div className="job-skills">
                    {job.requiredSkills.slice(0, 3).map(skill => <span key={skill} className="skill-tag">{skill}</span>)}
                    {job.requiredSkills.length > 3 && <span className="skill-tag">+{job.requiredSkills.length - 3}</span>}
                  </div>
                )}
                <div className="job-card-footer">
                  <span><Users size={16} /> {job.applicantsCount || 0} applicants</span>
                  <span><Calendar size={16} /> Posted {new Date(job.createdAt).toLocaleDateString()}</span>
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