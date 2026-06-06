import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { jobAPI, applicationAPI } from '../services/api';
import GuestApplyModal from '../components/candidate/GuestApplyModal';
import { Search, MapPin, DollarSign, Briefcase, Calendar, Building, Clock, CheckCircle, AlertCircle } from 'lucide-react';
import { JOB_TYPE_LABELS } from '../constants';
import ThemeToggle from '../components/common/ThemeToggle';

const PublicJobs = () => {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [locationFilter, setLocationFilter] = useState('');
  const [selectedJob, setSelectedJob] = useState(null);
  const [showGuestModal, setShowGuestModal] = useState(false);

  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      const res = await jobAPI.browseJobs({ status: 'OPEN' });
      setJobs(res.data);
    } catch (err) {
      setError('Failed to load jobs');
    } finally {
      setLoading(false);
    }
  };

  const filteredJobs = jobs.filter(job => {
    const matchTitle = job.title.toLowerCase().includes(searchTerm.toLowerCase());
    const matchLoc = !locationFilter || job.location?.toLowerCase().includes(locationFilter.toLowerCase());
    return matchTitle && matchLoc;
  });

  if (loading) return <div className="loading">Loading jobs...</div>;

  return (
    <div className="public-layout">
      <nav className="navbar glass">
        <div className="container">
          <div className="logo">InterviewPortal</div>
          <div className="nav-actions">
            <ThemeToggle />
            <Link to="/login" className="btn btn-outline">Login</Link>
            <Link to="/register" className="btn btn-primary">Register</Link>
          </div>
        </div>
      </nav>
      <div className="container">
        <h1>Browse Jobs</h1>
        <div className="jobs-filters">
          <div className="search-box">
            <Search size={20} />
            <input type="text" placeholder="Search by title..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
          </div>
          <div className="filter-input">
            <MapPin size={16} />
            <input type="text" placeholder="Location" value={locationFilter} onChange={(e) => setLocationFilter(e.target.value)} />
          </div>
        </div>
        {error && <div className="error-alert">{error}</div>}
        {filteredJobs.length === 0 ? (
          <div className="empty-state">No jobs found</div>
        ) : (
          <div className="jobs-grid">
            {filteredJobs.map(job => (
              <div key={job.id} className="job-card glass" onClick={() => { setSelectedJob(job); setShowGuestModal(true); }}>
                <div className="job-card-header">
                  <div className="company-logo"><Building size={24} /></div>
                  <div className="job-type-badge">{JOB_TYPE_LABELS[job.jobType] || 'Full Time'}</div>
                </div>
                <h3>{job.title}</h3>
                <div className="job-meta">
                  {job.location && <span><MapPin size={14} /> {job.location}</span>}
                  {job.salaryRange && <span><DollarSign size={14} /> {job.salaryRange}</span>}
                </div>
                <p className="job-description">{job.description?.substring(0, 100)}...</p>
                <div className="job-card-footer">
                  <span><Clock size={14} /> Posted {new Date(job.createdAt).toLocaleDateString()}</span>
                  <button className="apply-btn-small">Apply Now</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
      {showGuestModal && selectedJob && (
        <GuestApplyModal job={selectedJob} onClose={() => setShowGuestModal(false)} onSuccess={() => alert('Application submitted!')} />
      )}
    </div>
  );
};

export default PublicJobs;