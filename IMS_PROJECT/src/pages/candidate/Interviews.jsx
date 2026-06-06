import { useState, useEffect } from 'react';
import { interviewAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { Calendar, Clock, Video, MapPin, Phone, Briefcase, AlertCircle, CheckCircle, XCircle } from 'lucide-react';
import { INTERVIEW_MODE, INTERVIEW_MODE_LABELS, INTERVIEW_STATUS } from '../../constants';

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

  if (loading) return <div className="dashboard-layout"><Sidebar role="CANDIDATE" /><div className="dashboard-content">Loading...</div></div>;

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <h1>My Interviews</h1>
        {error && <div className="error-alert">{error}</div>}
        {upcomingInterviews.length > 0 && (
          <div>
            <h2>Upcoming Interviews</h2>
            {upcomingInterviews.map(iv => (
              <div key={iv.id} className="interview-card glass">
                <h3>{iv.position}</h3>
                <p><Calendar size={16} /> {new Date(iv.interviewDate).toLocaleDateString()} at {iv.interviewTime}</p>
                <p>{INTERVIEW_MODE_LABELS[iv.mode]}</p>
                {iv.meetingLink && <a href={iv.meetingLink} target="_blank">Join Meeting</a>}
              </div>
            ))}
          </div>
        )}
        {pastInterviews.length > 0 && (
          <div>
            <h2>Past Interviews</h2>
            {pastInterviews.map(iv => (
              <div key={iv.id} className="past-interview-card">
                <span>{iv.position} - {new Date(iv.interviewDate).toLocaleDateString()}</span>
                <span className={`status-badge ${iv.status.toLowerCase()}`}>{iv.status}</span>
              </div>
            ))}
          </div>
        )}
        {interviews.length === 0 && <div>No interviews scheduled.</div>}
      </div>
    </div>
  );
};

export default CandidateInterviews;