import { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { applicationAPI, jobAPI, interviewAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import {
  Users, Search, Filter, FileText, Mail, Calendar, TrendingUp,
  CheckCircle, XCircle, Clock, Star, X, AlertCircle, Send
} from 'lucide-react';
import { APPLICATION_STATUS, STATUS_LABELS, INTERVIEW_MODE } from '../../constants';
import { API_BASE_URL } from '../../constants';
import '../../styles/Applicants.css';

const Applicants = () => {
  const [searchParams] = useSearchParams();
  const initialJobId = searchParams.get('jobId');

  const [applications, setApplications] = useState([]);
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [selectedJob, setSelectedJob] = useState(initialJobId || 'ALL');
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [selectedApplication, setSelectedApplication] = useState(null);
  const [showStatusModal, setShowStatusModal] = useState(false);
  const [showInterviewModal, setShowInterviewModal] = useState(false);
  const [newStatus, setNewStatus] = useState('');
  const [statusNotes, setStatusNotes] = useState('');
  const [updatingStatus, setUpdatingStatus] = useState(false);
  const [schedulingInterview, setSchedulingInterview] = useState(false);

  const [interviewData, setInterviewData] = useState({
    interviewDate: '',
    interviewTime: '',
    mode: INTERVIEW_MODE.VIDEO,
    meetingLink: '',
    remarks: ''
  });

  useEffect(() => { fetchData(); }, []);

  const fetchData = async () => {
    try {
      const [appsResponse, jobsResponse] = await Promise.all([
        applicationAPI.getAllRecruiterApplications(),
        jobAPI.getRecruiterJobs()
      ]);
      setApplications(appsResponse.data);
      setJobs(jobsResponse.data);
    } catch (err) {
      console.error('Error fetching data:', err);
      setError('Failed to load applicants');
    } finally { setLoading(false); }
  };

  const handleStatusUpdate = async () => {
    if (!newStatus || !selectedApplication) return;
    setUpdatingStatus(true);
    try {
      await applicationAPI.updateStatus(
        selectedApplication.applicationId,
        newStatus,
        statusNotes
      );
      setApplications(applications.map(app =>
        app.applicationId === selectedApplication.applicationId
          ? { ...app, status: newStatus }
          : app
      ));
      setShowStatusModal(false);
      setSelectedApplication(null);
      setNewStatus('');
      setStatusNotes('');
    } catch (err) {
      alert('Failed to update status: ' + (err.response?.data?.message || 'Please try again'));
    } finally { setUpdatingStatus(false); }
  };

  const handleScheduleInterview = async () => {
    if (!interviewData.interviewDate || !interviewData.interviewTime) {
      alert('Please fill in interview date and time');
      return;
    }
    setSchedulingInterview(true);
    try {
      await interviewAPI.scheduleInterview({
        candidateId: selectedApplication.candidateId,
        jobId: selectedApplication.jobId,
        position: selectedApplication.jobTitle,
        interviewDate: interviewData.interviewDate,
        interviewTime: interviewData.interviewTime,
        mode: interviewData.mode,
        meetingLink: interviewData.meetingLink,
        remarks: interviewData.remarks
      });
      setApplications(applications.map(app =>
        app.applicationId === selectedApplication.applicationId
          ? { ...app, status: APPLICATION_STATUS.INTERVIEW_SCHEDULED }
          : app
      ));
      setShowInterviewModal(false);
      setSelectedApplication(null);
      setInterviewData({
        interviewDate: '', interviewTime: '', mode: INTERVIEW_MODE.VIDEO,
        meetingLink: '', remarks: ''
      });
    } catch (err) {
      alert('Failed to schedule interview: ' + (err.response?.data?.message || 'Please try again'));
    } finally { setSchedulingInterview(false); }
  };

  const openStatusModal = (app) => {
    setSelectedApplication(app);
    setNewStatus(app.status);
    setShowStatusModal(true);
  };

  const openInterviewModal = (app) => {
    setSelectedApplication(app);
    setShowInterviewModal(true);
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

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="RECRUITER" />
        <div className="dashboard-content"><div className="loading-container"><div className="spinner-large"></div><p>Loading applicants...</p></div></div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <div className="page-header">
          <div><h1>Applicants</h1><p>Review and manage candidate applications</p></div>
        </div>
        {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

        <div className="applicants-filters">
          <div className="search-box">
            <Search size={20} />
            <input type="text" placeholder="Search by candidate name, email, or job title..."
              value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} className="search-input" />
          </div>
          <div className="filter-row">
            <div className="filter-group">
              <Filter size={16} />
              <select value={selectedJob} onChange={(e) => setSelectedJob(e.target.value)} className="filter-select">
                <option value="ALL">All Jobs ({applications.length})</option>
                {jobs.map(job => (
                  <option key={job.id} value={job.id}>
                    {job.title} ({applications.filter(a => a.jobId === job.id).length})
                  </option>
                ))}
              </select>
            </div>
            <div className="status-filter-buttons">
              <button className={`status-filter-btn ${statusFilter === 'ALL' ? 'active' : ''}`}
                onClick={() => setStatusFilter('ALL')}>All</button>
              {Object.keys(APPLICATION_STATUS).map(status => (
                <button key={status} className={`status-filter-btn ${statusFilter === status ? 'active' : ''}`}
                  onClick={() => setStatusFilter(status)}>{STATUS_LABELS[status]}</button>
              ))}
            </div>
          </div>
        </div>

        {filteredApplications.length === 0 ? (
          <div className="empty-state">
            <Users size={64} strokeWidth={1.5} />
            <h3>No applicants found</h3>
            <p>{searchTerm || statusFilter !== 'ALL' || selectedJob !== 'ALL'
              ? 'Try adjusting your filters'
              : 'Applications will appear here once candidates apply'}</p>
          </div>
        ) : (
          <div className="applicants-table-container">
            <table className="applicants-table">
              <thead>
                <tr>
                  <th>Candidate</th>
                  <th>Job Title</th>
                  <th>Applied Date</th>
                  <th>ATS Score</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredApplications.map(app => (
                  <tr key={app.applicationId}>
                    <td>
                      <div className="candidate-info">
                        <div className="candidate-avatar">
                          {app.candidateName?.charAt(0).toUpperCase() || '?'}
                        </div>
                        <div>
                          <div className="candidate-name">{app.candidateName}</div>
                          <div className="candidate-email">
                            <Mail size={14} /> {app.candidateEmail}
                          </div>
                        </div>
                      </div>
                    </td>
                    <td><div className="job-title-cell">{app.jobTitle}</div></td>
                    <td><div className="date-cell">{new Date(app.appliedAt).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}</div></td>
                    <td>
                      <div className="ats-score-cell">
                        {app.atsScore != null ? (
                          <div className={`ats-badge ${app.atsScore >= 70 ? 'high' : app.atsScore >= 50 ? 'medium' : 'low'}`}>
                            <TrendingUp size={14} /> {app.atsScore}%
                          </div>
                        ) : <span className="score-na">N/A</span>}
                      </div>
                    </td>
                    <td>
                      <span className={`status-badge ${getStatusColor(app.status)}`}>
                        {app.status === APPLICATION_STATUS.APPLIED && <FileText size={14} />}
                        {app.status === APPLICATION_STATUS.SHORTLISTED && <Star size={14} />}
                        {app.status === APPLICATION_STATUS.INTERVIEW_SCHEDULED && <Calendar size={14} />}
                        {app.status === APPLICATION_STATUS.SELECTED && <CheckCircle size={14} />}
                        {app.status === APPLICATION_STATUS.REJECTED && <XCircle size={14} />}
                        {STATUS_LABELS[app.status]}
                      </span>
                    </td>
                    <td>
                      <div className="action-buttons">
                        <button onClick={() => openStatusModal(app)} className="btn-action primary">Update Status</button>
                        <button onClick={() => openInterviewModal(app)} className="btn-action secondary" title="Schedule Interview">
                          <Calendar size={16} />
                        </button>
                        {app.resumeUrl && (
                          <a href={`${API_BASE_URL}${app.resumeUrl}`} target="_blank" rel="noopener noreferrer"
                            className="btn-action secondary" title="View Resume">
                            <FileText size={16} />
                          </a>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Status Update Modal */}
        {showStatusModal && (
          <div className="modal-overlay" onClick={() => setShowStatusModal(false)}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
              <div className="modal-header">
                <h3>Update Application Status</h3>
                <button onClick={() => setShowStatusModal(false)} className="modal-close"><X size={24} /></button>
              </div>
              <div className="modal-body">
                <div className="candidate-summary">
                  <div className="candidate-avatar large">
                    {selectedApplication?.candidateName?.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <strong>{selectedApplication?.candidateName}</strong>
                    <p>Applied for {selectedApplication?.jobTitle}</p>
                  </div>
                </div>
                <div className="form-group">
                  <label className="label">New Status</label>
                  <select value={newStatus} onChange={(e) => setNewStatus(e.target.value)} className="input select-input">
                    {Object.keys(APPLICATION_STATUS).map(status => (
                      <option key={status} value={status}>{STATUS_LABELS[status]}</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label className="label">Notes (Optional)</label>
                  <textarea value={statusNotes} onChange={(e) => setStatusNotes(e.target.value)}
                    className="input" rows="4" placeholder="Add any notes for the candidate..." />
                </div>
              </div>
              <div className="modal-footer">
                <button onClick={() => setShowStatusModal(false)} className="btn btn-secondary" disabled={updatingStatus}>Cancel</button>
                <button onClick={handleStatusUpdate} className="btn btn-primary" disabled={updatingStatus}>
                  {updatingStatus ? <><div className="spinner"></div> Updating...</> : <><Send size={18} /> Update Status</>}
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Schedule Interview Modal */}
        {showInterviewModal && (
          <div className="modal-overlay" onClick={() => setShowInterviewModal(false)}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
              <div className="modal-header">
                <h3>Schedule Interview</h3>
                <button onClick={() => setShowInterviewModal(false)} className="modal-close"><X size={24} /></button>
              </div>
              <div className="modal-body">
                <div className="candidate-summary">
                  <div className="candidate-avatar large">
                    {selectedApplication?.candidateName?.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <strong>{selectedApplication?.candidateName}</strong>
                    <p>{selectedApplication?.jobTitle}</p>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="label">Interview Date</label>
                    <input type="date" value={interviewData.interviewDate}
                      onChange={(e) => setInterviewData({...interviewData, interviewDate: e.target.value})}
                      className="input" min={new Date().toISOString().split('T')[0]} />
                  </div>
                  <div className="form-group">
                    <label className="label">Interview Time</label>
                    <input type="time" value={interviewData.interviewTime}
                      onChange={(e) => setInterviewData({...interviewData, interviewTime: e.target.value})}
                      className="input" />
                  </div>
                </div>
                <div className="form-group">
                  <label className="label">Interview Mode</label>
                  <select value={interviewData.mode}
                    onChange={(e) => setInterviewData({...interviewData, mode: e.target.value})}
                    className="input select-input">
                    <option value={INTERVIEW_MODE.VIDEO}>Video Call</option>
                    <option value={INTERVIEW_MODE.IN_PERSON}>In Person</option>
                    <option value={INTERVIEW_MODE.PHONE}>Phone Call</option>
                  </select>
                </div>
                {interviewData.mode === INTERVIEW_MODE.VIDEO && (
                  <div className="form-group">
                    <label className="label">Meeting Link</label>
                    <input type="url" value={interviewData.meetingLink}
                      onChange={(e) => setInterviewData({...interviewData, meetingLink: e.target.value})}
                      className="input" placeholder="https://meet.google.com/..." />
                  </div>
                )}
                <div className="form-group">
                  <label className="label">Remarks (Optional)</label>
                  <textarea value={interviewData.remarks}
                    onChange={(e) => setInterviewData({...interviewData, remarks: e.target.value})}
                    className="input" rows="3" placeholder="Any special instructions or notes..." />
                </div>
              </div>
              <div className="modal-footer">
                <button onClick={() => setShowInterviewModal(false)} className="btn btn-secondary" disabled={schedulingInterview}>Cancel</button>
                <button onClick={handleScheduleInterview} className="btn btn-primary" disabled={schedulingInterview}>
                  {schedulingInterview ? <><div className="spinner"></div> Scheduling...</> : <><Calendar size={18} /> Schedule Interview</>}
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Applicants;