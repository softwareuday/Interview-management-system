import { useState, useEffect } from 'react';
import { interviewAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { Calendar, Clock, Video, MapPin, Phone, Briefcase, AlertCircle, CheckCircle, XCircle } from 'lucide-react';
import { INTERVIEW_MODE, INTERVIEW_MODE_LABELS, INTERVIEW_STATUS } from '../../constants';
import '../../styles/CandidateInterviews.css';

const CandidateInterviews = () => {
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => { fetchInterviews(); }, []);

  const fetchInterviews = async () => {
    try {
      const response = await interviewAPI.getCandidateInterviews();
      setInterviews(response.data);
    } catch (err) {
      console.error('Error fetching interviews:', err);
      setError('Failed to load interviews');
    } finally { setLoading(false); }
  };

  const upcomingInterviews = interviews.filter(i => i.status === INTERVIEW_STATUS.SCHEDULED && new Date(i.interviewDate) >= new Date());
  const pastInterviews = interviews.filter(i => i.status !== INTERVIEW_STATUS.SCHEDULED || new Date(i.interviewDate) < new Date());

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

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="CANDIDATE" />
        <div className="dashboard-content"><div className="loading-container"><div className="spinner-large"></div><p>Loading interviews...</p></div></div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <div className="page-header">
          <div><h1>My Interviews</h1><p>View and prepare for your scheduled interviews</p></div>
          <div className="header-stats">
            <div className="stat-chip"><Calendar size={18} /><span>{upcomingInterviews.length} Upcoming</span></div>
            <div className="stat-chip"><Clock size={18} /><span>{upcomingInterviews.filter(i => isToday(i.interviewDate)).length} Today</span></div>
          </div>
        </div>
        {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

        {upcomingInterviews.length > 0 && (
          <div className="interviews-section">
            <h2 className="section-title">Upcoming Interviews</h2>
            <div className="interviews-grid">
              {upcomingInterviews.map(interview => (
                <div key={interview.id} className={`interview-card ${isToday(interview.interviewDate) ? 'today' : ''}`}>
                  {isToday(interview.interviewDate) && <div className="today-badge">Today</div>}
                  <div className="interview-header">
                    <div className="interview-icon"><Briefcase size={24} /></div>
                    {getStatusBadge(interview.status)}
                  </div>
                  <div className="interview-body">
                    <h3>{interview.position}</h3>
                    <div className="interview-details">
                      <div className="detail-item"><Calendar size={16} /><span>{new Date(interview.interviewDate).toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' })}</span></div>
                      <div className="detail-item"><Clock size={16} /><span>{new Date(`2000-01-01T${interview.interviewTime}`).toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true })}</span></div>
                      <div className="detail-item">{getModeIcon(interview.mode)}<span>{INTERVIEW_MODE_LABELS[interview.mode]}</span></div>
                    </div>
                    {interview.meetingLink && interview.mode === INTERVIEW_MODE.VIDEO && (
                      <div className="meeting-link"><Video size={16} /><a href={interview.meetingLink} target="_blank" rel="noopener noreferrer" className="link">Join Meeting</a></div>
                    )}
                    {interview.remarks && (
                      <div className="interview-remarks"><strong>Instructions:</strong><p>{interview.remarks}</p></div>
                    )}
                  </div>
                  <div className="interview-footer">
                    <button className="btn btn-primary btn-block">Prepare for Interview</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {pastInterviews.length > 0 && (
          <div className="interviews-section">
            <h2 className="section-title">Past Interviews</h2>
            <div className="past-interviews-list">
              {pastInterviews.map(interview => (
                <div key={interview.id} className="past-interview-card">
                  <div className="past-interview-content">
                    <div className="interview-icon small"><Briefcase size={20} /></div>
                    <div className="interview-info">
                      <h4>{interview.position}</h4>
                      <div className="interview-meta">
                        <span>{new Date(interview.interviewDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}</span>
                        <span>•</span>
                        <span>{INTERVIEW_MODE_LABELS[interview.mode]}</span>
                      </div>
                    </div>
                  </div>
                  {getStatusBadge(interview.status)}
                </div>
              ))}
            </div>
          </div>
        )}

        {interviews.length === 0 && (
          <div className="empty-state">
            <Calendar size={64} strokeWidth={1.5} />
            <h3>No interviews scheduled</h3>
            <p>Your scheduled interviews will appear here</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default CandidateInterviews;