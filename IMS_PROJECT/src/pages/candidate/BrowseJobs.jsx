import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { jobAPI, applicationAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import AtsScannerModal from '../../components/candidate/AtsScannerModal';
import { Search, MapPin, DollarSign, Briefcase, Calendar, Building, Clock, CheckCircle, AlertCircle, TrendingUp } from 'lucide-react';
import { JOB_TYPE_LABELS } from '../../constants';

const CandidateJobs = () => {
  const { isAuthenticated } = useAuth();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [locationFilter, setLocationFilter] = useState('');
  const [appliedJobs, setAppliedJobs] = useState(new Set());
  const [selectedJob, setSelectedJob] = useState(null);
  const [showAtsModal, setShowAtsModal] = useState(false);

  useEffect(() => {
    fetchJobs();
    if (isAuthenticated) fetchAppliedJobs();
  }, [isAuthenticated]);

  const fetchJobs = async () => {
    try {
      const res = await jobAPI.browseJobs({ status: 'OPEN' });
      setJobs(res.data);
    } finally { setLoading(false); }
  };

  const fetchAppliedJobs = async () => {
    try {
      const res = await applicationAPI.getCandidateApplications();
      setAppliedJobs(new Set(res.data.map(app => app.jobId)));
    } catch (err) {}
  };

  const handleApply = async (jobId) => {
    try {
      await applicationAPI.apply(jobId, '', null, null, null);
      setAppliedJobs(new Set([...appliedJobs, jobId]));
      alert('Application submitted!');
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to apply');
    }
  };

  const filteredJobs = jobs.filter(job => job.title.toLowerCase().includes(searchTerm.toLowerCase()) && (!locationFilter || job.location?.toLowerCase().includes(locationFilter.toLowerCase())));

  if (loading) return <div className="dashboard-layout"><Sidebar role="CANDIDATE" /><div className="dashboard-content">Loading...</div></div>;

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <h1>Browse Jobs</h1>
        <div className="jobs-filters">
          <div className="search-box"><Search size={20} /><input type="text" placeholder="Search..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} /></div>
          <div className="filter-input"><MapPin size={16} /><input type="text" placeholder="Location" value={locationFilter} onChange={(e) => setLocationFilter(e.target.value)} /></div>
        </div>
        <div className="jobs-grid">
          {filteredJobs.map(job => (
            <div key={job.id} className="job-card glass">
              <div className="job-card-header"><div className="company-logo"><Building size={24} /></div><div className="job-type-badge">{JOB_TYPE_LABELS[job.jobType]}</div></div>
              <h3>{job.title}</h3>
              <div className="job-meta">{job.location && <span><MapPin size={14} /> {job.location}</span>}{job.salaryRange && <span><DollarSign size={14} /> {job.salaryRange}</span>}</div>
              <p className="job-description">{job.description?.substring(0, 100)}...</p>
              <div className="job-card-footer">
                <button className="btn-scan" onClick={() => { setSelectedJob(job); setShowAtsModal(true); }}><TrendingUp size={16} /> Scan</button>
                {appliedJobs.has(job.id) ? <span className="applied-badge">Applied</span> : <button className="apply-btn-small" onClick={() => handleApply(job.id)}>Apply</button>}
              </div>
            </div>
          ))}
        </div>
        {showAtsModal && selectedJob && <AtsScannerModal job={selectedJob} onClose={() => setShowAtsModal(false)} onApply={handleApply} />}
      </div>
    </div>
  );
};

export default CandidateJobs;