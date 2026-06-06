import { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { applicationAPI, jobAPI, interviewAPI, atsAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import UpdateStatusModal from '../../components/recruiter/UpdateStatusModal';
import ScheduleInterviewModal from '../../components/recruiter/ScheduleInterviewModal';
import { Users, Search, Filter, FileText, Mail, Calendar, TrendingUp, CheckCircle, XCircle, Clock, Star, X, AlertCircle, Send, Eye } from 'lucide-react';
import { APPLICATION_STATUS, STATUS_LABELS } from '../../constants';
import { API_BASE_URL } from '../../constants';

const Applicants = () => {
  const [searchParams] = useSearchParams();
  const [applications, setApplications] = useState([]);
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedJob, setSelectedJob] = useState(searchParams.get('jobId') || 'ALL');
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [selectedApp, setSelectedApp] = useState(null);
  const [showStatusModal, setShowStatusModal] = useState(false);
  const [showInterviewModal, setShowInterviewModal] = useState(false);
  const [showScanModal, setShowScanModal] = useState(false);
  const [scanResult, setScanResult] = useState(null);
  const [scanning, setScanning] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [appsRes, jobsRes] = await Promise.all([
        applicationAPI.getAllRecruiterApplications(),
        jobAPI.getRecruiterJobs()
      ]);
      setApplications(appsRes.data);
      setJobs(jobsRes.data);
    } catch (err) {
      setError('Failed to load applicants');
    } finally {
      setLoading(false);
    }
  };

  const handleScan = async (app) => {
    setSelectedApp(app);
    setScanning(true);
    setShowScanModal(true);
    try {
      const res = await atsAPI.scanApplication(app.applicationId);
      setScanResult(res.data);
      // update atsScore in the list
      setApplications(applications.map(a => a.applicationId === app.applicationId ? { ...a, atsScore: res.data.atsScore } : a));
    } catch (err) {
      alert('Scan failed: ' + (err.response?.data?.message || 'Unknown error'));
      setShowScanModal(false);
    } finally {
      setScanning(false);
    }
  };

  const filteredApplications = applications.filter(app => {
    const matchesJob = selectedJob === 'ALL' || app.jobId === parseInt(selectedJob);
    const matchesSearch = (app.candidateName?.toLowerCase() || '').includes(searchTerm.toLowerCase()) ||
                         (app.candidateEmail?.toLowerCase() || '').includes(searchTerm.toLowerCase()) ||
                         (app.jobTitle?.toLowerCase() || '').includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || app.status === statusFilter;
    return matchesJob && matchesSearch && matchesStatus;
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

  if (loading) return <div className="dashboard-layout"><Sidebar role="RECRUITER" /><div className="dashboard-content">Loading applicants...</div></div>;

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <h1>Applicants</h1>
        {error && <div className="error-alert">{error}</div>}

        <div className="filters-row">
          <div className="search-box"><Search size={20} /><input type="text" placeholder="Search candidate, email, job..." value={searchTerm} onChange={e => setSearchTerm(e.target.value)} /></div>
          <select value={selectedJob} onChange={e => setSelectedJob(e.target.value)}>
            <option value="ALL">All Jobs ({applications.length})</option>
            {jobs.map(job => <option key={job.id} value={job.id}>{job.title} ({applications.filter(a => a.jobId === job.id).length})</option>)}
          </select>
          <div className="status-filter-buttons">
            <button className={`status-filter-btn ${statusFilter === 'ALL' ? 'active' : ''}`} onClick={() => setStatusFilter('ALL')}>All</button>
            {Object.keys(APPLICATION_STATUS).map(s => <button key={s} className={`status-filter-btn ${statusFilter === s ? 'active' : ''}`} onClick={() => setStatusFilter(s)}>{STATUS_LABELS[s]}</button>)}
          </div>
        </div>

        <div className="applicants-table-container">
          <table className="applicants-table">
            <thead><tr><th>Candidate</th><th>Job Title</th><th>Applied</th><th>ATS Score</th><th>Status</th><th>Actions</th></tr></thead>
            <tbody>
              {filteredApplications.map(app => (
                <tr key={app.applicationId}>
                  <td><div><strong>{app.candidateName}</strong><br /><small>{app.candidateEmail}</small></div></td>
                  <td>{app.jobTitle}</td>
                  <td>{new Date(app.appliedAt).toLocaleDateString()}</td>
                  <td>
                    {app.atsScore != null ? (
                      <span className={`score-badge ${app.atsScore >= 70 ? 'high' : app.atsScore >= 50 ? 'medium' : 'low'}`}>{app.atsScore}%</span>
                    ) : (
                      <button onClick={() => handleScan(app)} className="btn-scan-sm">Scan</button>
                    )}
                  </td>
                  <td><span className={`status-badge ${getStatusColor(app.status)}`}>{STATUS_LABELS[app.status]}</span></td>
                  <td className="actions">
                    <button onClick={() => { setSelectedApp(app); setShowStatusModal(true); }} className="btn-action">Status</button>
                    <button onClick={() => { setSelectedApp(app); setShowInterviewModal(true); }} className="btn-action">Interview</button>
                    {app.resumeUrl && <a href={`${API_BASE_URL}${app.resumeUrl}`} target="_blank" className="btn-action">Resume</a>}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {showStatusModal && selectedApp && <UpdateStatusModal application={selectedApp} onClose={() => setShowStatusModal(false)} onUpdate={fetchData} />}
        {showInterviewModal && selectedApp && <ScheduleInterviewModal application={selectedApp} onClose={() => setShowInterviewModal(false)} onSchedule={fetchData} />}
        {showScanModal && selectedApp && (
          <div className="modal-overlay" onClick={() => setShowScanModal(false)}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
              <div className="modal-header"><h3>ATS Score for {selectedApp.candidateName}</h3><button onClick={() => setShowScanModal(false)} className="modal-close">✕</button></div>
              <div className="modal-body">
                {scanning ? <div>Analyzing...</div> : scanResult && (
                  <>
                    <div className={`score-value ${scanResult.atsScore >= 70 ? 'high' : scanResult.atsScore >= 50 ? 'medium' : 'low'}`}>{scanResult.atsScore}%</div>
                    <p>{scanResult.recommendation}</p>
                    <div><strong>Matched Skills</strong><div>{scanResult.matchedKeywords?.join(', ') || 'None'}</div></div>
                    <div><strong>Missing Skills</strong><div>{scanResult.missingKeywords?.join(', ') || 'None'}</div></div>
                  </>
                )}
              </div>
              <div className="modal-footer"><button onClick={() => setShowScanModal(false)} className="btn btn-primary">Close</button></div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Applicants;