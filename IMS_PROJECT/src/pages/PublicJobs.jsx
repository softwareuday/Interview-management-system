import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { jobAPI, applicationAPI } from '../services/api';
import GuestApplyModal from '../components/candidate/GuestApplyModal';
import AtsScannerModal from '../components/candidate/AtsScannerModal';
import { Link } from 'react-router-dom';
import { 
  Search, MapPin, DollarSign, Briefcase, Calendar, X, Send,
  CheckCircle, AlertCircle, Clock, Building, Filter, TrendingUp, LogIn
} from 'lucide-react';
import { JOB_TYPE, JOB_TYPE_LABELS } from '../constants';
import '../styles/BrowseJobs.css';

const PublicJobs = () => {
  const { isAuthenticated, user } = useAuth();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [locationFilter, setLocationFilter] = useState('');
  const [typeFilter, setTypeFilter] = useState('ALL');
  const [selectedJob, setSelectedJob] = useState(null);
  const [showJobModal, setShowJobModal] = useState(false);
  const [showGuestModal, setShowGuestModal] = useState(false);
  const [showAtsModal, setShowAtsModal] = useState(false);
  const [applying, setApplying] = useState(false);
  const [appliedJobs, setAppliedJobs] = useState(new Set());

  useEffect(() => {
    fetchJobs();
    if (isAuthenticated && user?.role === 'CANDIDATE') {
      fetchAppliedJobs();
    }
  }, [isAuthenticated]);

  const fetchJobs = async () => {
    try {
      const response = await jobAPI.browseJobs({ status: 'OPEN' });
      setJobs(response.data);
    } catch (err) {
      console.error('Error fetching jobs:', err);
      setError('Failed to load jobs');
    } finally {
      setLoading(false);
    }
  };

  const fetchAppliedJobs = async () => {
    try {
      const response = await applicationAPI.getCandidateApplications();
      const appliedJobIds = new Set(response.data.map(app => app.jobId));
      setAppliedJobs(appliedJobIds);
    } catch (err) {
      console.error('Error fetching applications:', err);
    }
  };

  const handleApplyClick = (job) => {
    setSelectedJob(job);
    if (isAuthenticated && user?.role === 'CANDIDATE') {
      handleApply(job.id);
    } else {
      setShowGuestModal(true);
    }
  };

  const handleApply = async (jobId) => {
    setApplying(true);
    try {
      await applicationAPI.apply(jobId, '', null, null, null);
      setAppliedJobs(new Set([...appliedJobs, jobId]));
      setShowJobModal(false);
      alert('Application submitted successfully!');
    } catch (err) {
      console.error('Error applying:', err);
      alert(err.response?.data?.message || 'Failed to apply. Please make sure you have uploaded your resume.');
    } finally {
      setApplying(false);
    }
  };

  const handleScanClick = (job) => {
    setSelectedJob(job);
    if (!isAuthenticated || user?.role !== 'CANDIDATE') {
      alert('Please login as a candidate to use the ATS scanner. Create an account and upload your resume first.');
      return;
    }
    setShowAtsModal(true);
  };

  const openJobModal = (job) => {
    setSelectedJob(job);
    setShowJobModal(true);
  };

  const filteredJobs = jobs.filter(job => {
    const matchesSearch = job.title?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         job.department?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         job.description?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesLocation = !locationFilter || job.location?.toLowerCase().includes(locationFilter.toLowerCase());
    const matchesType = typeFilter === 'ALL' || job.jobType === typeFilter;
    return matchesSearch && matchesLocation && matchesType;
  });

  if (loading) {
    return (
      <div className="public-layout">
        <div className="public-header">
          <div className="container">
            <div className="header-content">
              <Link to="/" className="logo">InterviewPortal</Link>
              <Link to="/login" className="btn btn-outline">Login / Register</Link>
            </div>
          </div>
        </div>
        <div className="loading-container"><div className="spinner-large"></div><p>Loading jobs...</p></div>
      </div>
    );
  }

  return (
    <div className="public-layout">
      <div className="public-header">
        <div className="container">
          <div className="header-content">
            <Link to="/" className="logo">InterviewPortal</Link>
            <Link to="/login" className="btn btn-outline">
              <LogIn size={18} /> Login / Register
            </Link>
          </div>
        </div>
      </div>

      <div className="container" style={{ paddingTop: '32px', paddingBottom: '64px' }}>
        <div className="page-header">
          <div>
            <h1>Browse Jobs</h1>
            <p>Explore opportunities and apply to your dream job</p>
          </div>
          <div className="header-stats">
            <div className="stat-chip"><Briefcase size={18} /><span>{filteredJobs.length} Jobs Available</span></div>
          </div>
        </div>

        {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

        <div className="jobs-filters">
          <div className="search-box">
            <Search size={20} />
            <input type="text" placeholder="Search by job title, department, or keywords..."
              value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} className="search-input" />
          </div>
          <div className="filter-row">
            <div className="filter-input">
              <MapPin size={16} />
              <input type="text" placeholder="Location" value={locationFilter}
                onChange={(e) => setLocationFilter(e.target.value)} className="location-input" />
            </div>
            <div className="type-filter-buttons">
              <button className={`type-btn ${typeFilter === 'ALL' ? 'active' : ''}`}
                onClick={() => setTypeFilter('ALL')}>All Types</button>
              {Object.keys(JOB_TYPE).map(type => (
                <button key={type} className={`type-btn ${typeFilter === type ? 'active' : ''}`}
                  onClick={() => setTypeFilter(type)}>{JOB_TYPE_LABELS[type]}</button>
              ))}
            </div>
          </div>
        </div>

        {filteredJobs.length === 0 ? (
          <div className="empty-state">
            <Briefcase size={64} strokeWidth={1.5} />
            <h3>No jobs found</h3>
            <p>{searchTerm || locationFilter || typeFilter !== 'ALL' ? 'Try adjusting your filters' : 'No jobs are currently available'}</p>
          </div>
        ) : (
          <div className="jobs-grid">
            {filteredJobs.map(job => (
              <div key={job.id} className="job-card" onClick={() => openJobModal(job)}>
                <div className="job-card-header">
                  <div className="company-logo"><Building size={24} /></div>
                  <div className="job-type-badge">{JOB_TYPE_LABELS[job.jobType] || 'Full Time'}</div>
                </div>
                <div className="job-card-body">
                  <h3 className="job-title">{job.title}</h3>
                  <div className="job-meta">
                    {job.department && <div className="meta-item"><Briefcase size={16} /><span>{job.department}</span></div>}
                    {job.location && <div className="meta-item"><MapPin size={16} /><span>{job.location}</span></div>}
                    {job.salaryRange && <div className="meta-item"><DollarSign size={16} /><span>{job.salaryRange}</span></div>}
                  </div>
                  <p className="job-description">
                    {job.description?.substring(0, 120)}{job.description?.length > 120 ? '...' : ''}
                  </p>
                  {job.requiredSkills?.length > 0 && (
                    <div className="job-skills">
                      {job.requiredSkills.slice(0, 3).map((skill, i) => <span key={i} className="skill-tag">{skill}</span>)}
                      {job.requiredSkills.length > 3 && <span className="skill-tag more">+{job.requiredSkills.length - 3}</span>}
                    </div>
                  )}
                </div>
                <div className="job-card-footer">
                  <div className="posted-date"><Clock size={14} /><span>Posted {new Date(job.createdAt).toLocaleDateString()}</span></div>
                  <div style={{ display: 'flex', gap: '8px' }}>
                    <button 
                      className="btn-scan"
                      onClick={(e) => { e.stopPropagation(); handleScanClick(job); }}
                      title="Check ATS score"
                    >
                      <TrendingUp size={16} /> Scan
                    </button>
                    {appliedJobs.has(job.id) ? (
                      <span className="applied-badge"><CheckCircle size={16} />Applied</span>
                    ) : (
                      <button className="apply-btn-small" onClick={(e) => { e.stopPropagation(); handleApplyClick(job); }}>
                        Apply Now
                      </button>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Job Details Modal */}
        {showJobModal && selectedJob && (
          <div className="modal-overlay" onClick={() => setShowJobModal(false)}>
            <div className="modal-content large" onClick={(e) => e.stopPropagation()}>
              <div className="modal-header">
                <div>
                  <h2>{selectedJob.title}</h2>
                  <p className="company-name">{selectedJob.companyName || 'Company'}</p>
                </div>
                <button onClick={() => setShowJobModal(false)} className="modal-close"><X size={24} /></button>
              </div>
              <div className="modal-body">
                <div className="job-overview">
                  <div className="overview-item"><Briefcase size={20} /><div><span className="label">Job Type</span><span className="value">{JOB_TYPE_LABELS[selectedJob.jobType] || 'Full Time'}</span></div></div>
                  {selectedJob.location && <div className="overview-item"><MapPin size={20} /><div><span className="label">Location</span><span className="value">{selectedJob.location}</span></div></div>}
                  {selectedJob.salaryRange && <div className="overview-item"><DollarSign size={20} /><div><span className="label">Salary Range</span><span className="value">{selectedJob.salaryRange}</span></div></div>}
                  {selectedJob.experienceRequired && <div className="overview-item"><Calendar size={20} /><div><span className="label">Experience</span><span className="value">{selectedJob.experienceRequired}</span></div></div>}
                </div>
                <div className="job-section">
                  <h3>Job Description</h3>
                  <p className="description-text">{selectedJob.description || 'No description provided.'}</p>
                </div>
                {selectedJob.requiredSkills?.length > 0 && (
                  <div className="job-section">
                    <h3>Required Skills</h3>
                    <div className="skills-list">
                      {selectedJob.requiredSkills.map((skill, i) => <span key={i} className="skill-badge">{skill}</span>)}
                    </div>
                  </div>
                )}
                {selectedJob.lastDateToApply && (
                  <div className="deadline-notice">
                    <Calendar size={18} />
                    <span>Application Deadline: {new Date(selectedJob.lastDateToApply).toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' })}</span>
                  </div>
                )}
              </div>
              <div className="modal-footer">
                <button onClick={() => setShowJobModal(false)} className="btn btn-secondary">Close</button>
                <button onClick={() => handleScanClick(selectedJob)} className="btn btn-outline">
                  <TrendingUp size={18} /> Check ATS Score
                </button>
                {appliedJobs.has(selectedJob.id) ? (
                  <button className="btn btn-success" disabled><CheckCircle size={20} />Already Applied</button>
                ) : (
                  <button onClick={() => handleApplyClick(selectedJob)} className="btn btn-primary" disabled={applying}>
                    {applying ? <><div className="spinner"></div>Applying...</> : <><Send size={20} />Apply Now</>}
                  </button>
                )}
              </div>
            </div>
          </div>
        )}

        {/* Guest Apply Modal */}
        {showGuestModal && selectedJob && (
          <GuestApplyModal
            job={selectedJob}
            onClose={() => setShowGuestModal(false)}
            onSuccess={() => {
              setAppliedJobs(new Set([...appliedJobs, selectedJob.id]));
            }}
          />
        )}

        {/* ATS Scanner Modal */}
        {showAtsModal && selectedJob && (
          <AtsScannerModal
            job={selectedJob}
            onClose={() => setShowAtsModal(false)}
            onApply={handleApplyClick}
          />
        )}
      </div>

      <footer className="public-footer">
        <div className="container">
          <p>&copy; 2026 InterviewPortal. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default PublicJobs;