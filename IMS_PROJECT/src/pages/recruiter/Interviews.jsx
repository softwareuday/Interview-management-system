import { useState, useEffect } from 'react';
import { interviewAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { Calendar, Clock, Video, MapPin, Phone, Briefcase, XCircle, CheckCircle, AlertCircle, Search, Filter } from 'lucide-react';
import { INTERVIEW_MODE, INTERVIEW_MODE_LABELS, INTERVIEW_STATUS } from '../../constants';

const RecruiterInterviews = () => {
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('ALL');

  useEffect(() => { fetchInterviews(); }, []);

  const fetchInterviews = async () => {
    try {
      const res = await interviewAPI.getRecruiterInterviews();
      setInterviews(res.data);
    } catch (err) {
      setError('Failed to load interviews');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm('Cancel this interview?')) return;
    try {
      await interviewAPI.cancelInterview(id);
      setInterviews(interviews.map(i => i.id === id ? { ...i, status: INTERVIEW_STATUS.CANCELLED } : i));
    } catch (err) {
      alert('Failed to cancel');
    }
  };

  const filteredInterviews = interviews.filter(i => {
    const matchesSearch = (i.candidateName?.toLowerCase() || '').includes(searchTerm.toLowerCase()) ||
                         (i.position?.toLowerCase() || '').includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === 'ALL' || i.status === filterStatus;
    return matchesSearch && matchesStatus;
  });

  const grouped = filteredInterviews.reduce((groups, iv) => {
    const date = new Date(iv.interviewDate).toLocaleDateString();
    if (!groups[date]) groups[date] = [];
    groups[date].push(iv);
    return groups;
  }, {});

  if (loading) return <div className="dashboard-layout"><Sidebar role="RECRUITER" /><div className="dashboard-content">Loading...</div></div>;

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <h1>Interviews</h1>
        {error && <div className="error-alert">{error}</div>}
        <div className="filters-row">
          <div className="search-box"><Search size={20} /><input type="text" placeholder="Search candidate or position..." value={searchTerm} onChange={e => setSearchTerm(e.target.value)} /></div>
          <select value={filterStatus} onChange={e => setFilterStatus(e.target.value)}>
            <option value="ALL">All Status</option>
            <option value={INTERVIEW_STATUS.SCHEDULED}>Scheduled</option>
            <option value={INTERVIEW_STATUS.COMPLETED}>Completed</option>
            <option value={INTERVIEW_STATUS.CANCELLED}>Cancelled</option>
          </select>
        </div>

        {Object.keys(grouped).length === 0 ? (
          <div className="empty-state">No interviews found</div>
        ) : (
          Object.entries(grouped).sort().map(([date, ivs]) => (
            <div key={date}>
              <h3>{date}</h3>
              {ivs.map(iv => (
                <div key={iv.id} className="interview-card glass">
                  <div className="interview-header">
                    <div><strong>{iv.candidateName}</strong> – {iv.position}</div>
                    <span className={`status-badge ${iv.status.toLowerCase()}`}>{iv.status}</span>
                  </div>
                  <div><Calendar size={16} /> {iv.interviewDate} at {iv.interviewTime}</div>
                  <div>{INTERVIEW_MODE_LABELS[iv.mode]}</div>
                  {iv.meetingLink && <div><Video size={16} /> <a href={iv.meetingLink} target="_blank">Join Meeting</a></div>}
                  {iv.remarks && <div><em>{iv.remarks}</em></div>}
                  {iv.status === INTERVIEW_STATUS.SCHEDULED && (
                    <div className="interview-actions">
                      <button onClick={() => handleCancel(iv.id)} className="btn-danger">Cancel</button>
                      {iv.resumeUrl && <a href={iv.resumeUrl} target="_blank">View Resume</a>}
                    </div>
                  )}
                </div>
              ))}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default RecruiterInterviews;