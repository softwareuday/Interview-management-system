import { useState, useEffect } from 'react';
import { interviewAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import {
  Calendar, Clock, Video, MapPin, Phone, User, Briefcase,
  XCircle, CheckCircle, AlertCircle, Filter, Search
} from 'lucide-react';
import { INTERVIEW_MODE, INTERVIEW_MODE_LABELS, INTERVIEW_STATUS } from '../../constants';
import { API_BASE_URL } from '../../constants';
import '../../styles/Interviews.css';

const Interviews = () => {
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('ALL');
  const [filterMode, setFilterMode] = useState('ALL');

  useEffect(() => { fetchInterviews(); }, []);

  const fetchInterviews = async () => {
    try {
      const response = await interviewAPI.getRecruiterInterviews();
      setInterviews(response.data);
    } catch (err) {
      console.error('Error fetching interviews:', err);
      setError('Failed to load interviews');
    } finally { setLoading(false); }
  };

  const handleCancelInterview = async (interviewId) => {
    if (!window.confirm('Are you sure you want to cancel this interview? The candidate will be notified.')) return;
    try {
      await interviewAPI.cancelInterview(interviewId);
      setInterviews(interviews.map(i => i.id === interviewId ? { ...i, status: INTERVIEW_STATUS.CANCELLED } : i));
    } catch (err) {
      alert('Failed to cancel interview: ' + (err.response?.data?.message || 'Please try again'));
    }
  };

  const filteredInterviews = interviews.filter(interview => {
    const matchesSearch = (interview.candidateName?.toLowerCase() || '').includes(searchTerm.toLowerCase()) ||
                         (interview.position?.toLowerCase() || '').includes(searchTerm.toLowerCase()) ||
                         (interview.candidateEmail?.toLowerCase() || '').includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === 'ALL' || interview.status === filterStatus;
    const matchesMode = filterMode === 'ALL' || interview.mode === filterMode;
    return matchesSearch && matchesStatus && matchesMode;
  });

  const groupedInterviews = filteredInterviews.reduce((groups, interview) => {
    const date = new Date(interview.interviewDate).toLocaleDateString('en-US', {
      year: 'numeric', month: 'long', day: 'numeric'
    });
    if (!groups[date]) groups[date] = [];
    groups[date].push(interview);
    return groups;
  }, {});

  const sortedDates = Object.keys(groupedInterviews).sort((a, b) => new Date(a) - new Date(b));

  const getModeIcon = (mode) => {
    switch (mode) {
      case INTERVIEW_MODE.VIDEO: return <Video size={18} />;
      case INTERVIEW_MODE.IN_PERSON: return <MapPin size={18} />;
      case INTERVIEW_MODE.PHONE: return <Phone size={18} />;
      default: return <Video size={18} />;
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case INTERVIEW_STATUS.SCHEDULED: return <span className="status-badge scheduled"><Clock size={14} /> Scheduled</span>;
      case INTERVIEW_STATUS.COMPLETED: return <span className="status-badge completed"><CheckCircle size={14} /> Completed</span>;
      case INTERVIEW_STATUS.CANCELLED: return <span className="status-badge cancelled"><XCircle size={14} /> Cancelled</span>;
      default: return <span className="status-badge scheduled">{status}</span>;
    }
  };

  const isToday = (date) => new Date(date).toDateString() === new Date().toDateString();
  const isPast = (date) => new Date(date) < new Date(new Date().setHours(0,0,0,0));

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="RECRUITER" />
        <div className="dashboard-content"><div className="loading-container"><div className="spinner-large"></div><p>Loading interviews...</p></div></div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <div className="page-header">
          <div><h1>Interviews</h1><p>Manage and track your scheduled interviews</p></div>
          <div className="header-stats">
            <div className="stat-chip"><Calendar size={18} /><span>{interviews.filter(i => i.status === INTERVIEW_STATUS.SCHEDULED).length} Scheduled</span></div>
            <div className="stat-chip"><Clock size={18} /><span>{interviews.filter(i => isToday(i.interviewDate) && i.status === INTERVIEW_STATUS.SCHEDULED).length} Today</span></div>
          </div>
        </div>
        {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

        <div className="interviews-filters">
          <div className="search-box">
            <Search size={20} />
            <input type="text" placeholder="Search by candidate name, position, or email..."
              value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} className="search-input" />
          </div>
          <div className="filter-row">
            <div className="filter-group">
              <Filter size={16} />
              <select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)} className="filter-select">
                <option value="ALL">All Status</option>
                <option value={INTERVIEW_STATUS.SCHEDULED}>Scheduled</option>
                <option value={INTERVIEW_STATUS.COMPLETED}>Completed</option>
                <option value={INTERVIEW_STATUS.CANCELLED}>Cancelled</option>
              </select>
            </div>
            <div className="filter-group">
              <Video size={16} />
              <select value={filterMode} onChange={(e) => setFilterMode(e.target.value)} className="filter-select">
                <option value="ALL">All Modes</option>
                <option value={INTERVIEW_MODE.VIDEO}>Video Call</option>
                <option value={INTERVIEW_MODE.IN_PERSON}>In Person</option>
                <option value={INTERVIEW_MODE.PHONE}>Phone Call</option>
              </select>
            </div>
          </div>
        </div>

        {filteredInterviews.length === 0 ? (
          <div className="empty-state">
            <Calendar size={64} strokeWidth={1.5} />
            <h3>No interviews found</h3>
            <p>{searchTerm || filterStatus !== 'ALL' || filterMode !== 'ALL'
              ? 'Try adjusting your filters'
              : 'Scheduled interviews will appear here'}</p>
          </div>
        ) : (
          <div className="interviews-timeline">
            {sortedDates.map(date => (
              <div key={date} className="date-group">
                <div className="date-header">
                  <h3>{date}</h3>
                  {isToday(groupedInterviews[date][0].interviewDate) && <span className="today-badge">Today</span>}
                  {isPast(groupedInterviews[date][0].interviewDate) && !isToday(groupedInterviews[date][0].interviewDate) && <span className="past-badge">Past</span>}
                </div>
                <div className="interviews-list">
                  {groupedInterviews[date].map(interview => (
                    <div key={interview.id} className={`interview-card ${interview.status.toLowerCase()}`}>
                      <div className="interview-time">
                        <Clock size={20} />
                        <span className="time">
                          {new Date(`2000-01-01T${interview.interviewTime}`).toLocaleTimeString('en-US', {
                            hour: 'numeric', minute: '2-digit', hour12: true
                          })}
                        </span>
                      </div>
                      <div className="interview-content">
                        <div className="interview-header">
                          <div className="candidate-section">
                            <div className="candidate-avatar">
                              {interview.candidateName?.charAt(0).toUpperCase()}
                            </div>
                            <div>
                              <h4>{interview.candidateName}</h4>
                              <p className="candidate-email">{interview.candidateEmail}</p>
                            </div>
                          </div>
                          {getStatusBadge(interview.status)}
                        </div>
                        <div className="interview-details">
                          <div className="detail-item"><Briefcase size={16} /><span>{interview.position}</span></div>
                          <div className="detail-item">{getModeIcon(interview.mode)}<span>{INTERVIEW_MODE_LABELS[interview.mode]}</span></div>
                        </div>
                        {interview.meetingLink && interview.mode === INTERVIEW_MODE.VIDEO && (
                          <div className="meeting-link">
                            <Video size={16} />
                            <a href={interview.meetingLink} target="_blank" rel="noopener noreferrer" className="link">Join Meeting</a>
                          </div>
                        )}
                        {interview.remarks && (
                          <div className="interview-remarks"><p>{interview.remarks}</p></div>
                        )}
                        {interview.status === INTERVIEW_STATUS.SCHEDULED && (
                          <div className="interview-actions">
                            {interview.resumeUrl && (
                              <a href={`${API_BASE_URL}${interview.resumeUrl}`} target="_blank" rel="noopener noreferrer"
                                className="btn btn-secondary btn-sm">View Resume</a>
                            )}
                            <button onClick={() => handleCancelInterview(interview.id)}
                              className="btn btn-danger btn-sm"><XCircle size={16} /> Cancel Interview</button>
                          </div>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Interviews;