import { useState, useEffect } from 'react';
import { X, TrendingUp, AlertCircle, CheckCircle, Award, FileText } from 'lucide-react';
import { atsAPI } from '../../services/api';
import '../../styles/Modal.css';

const AtsScannerModal = ({ job, onClose, onApply }) => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [result, setResult] = useState(null);

  useEffect(() => {
    scanJob();
  }, []);

  const scanJob = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await atsAPI.scan(job.id);
      setResult(response.data);
    } catch (err) {
      console.error('ATS scan error:', err);
      if (err.response?.status === 401) {
        setError('Please login to use the ATS scanner. Create an account and upload your resume first.');
      } else if (err.response?.status === 400) {
        setError(err.response.data?.message || 'Please upload your resume in your profile before scanning.');
      } else {
        setError('Failed to scan. Please try again later.');
      }
    } finally {
      setLoading(false);
    }
  };

  const getScoreColor = (score) => {
    if (score >= 70) return 'high';
    if (score >= 50) return 'medium';
    return 'low';
  };

  const getScoreMessage = (score) => {
    if (score >= 70) return 'Excellent match! You are highly recommended for this role.';
    if (score >= 50) return 'Good match. Consider updating your resume with missing skills.';
    return 'Low match. Review the job requirements and tailor your resume.';
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content large" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>ATS Score: {job.title}</h3>
          <button onClick={onClose} className="modal-close"><X size={24} /></button>
        </div>
        <div className="modal-body">
          {loading ? (
            <div className="loading-container">
              <div className="spinner-large"></div>
              <p>Analyzing your resume against job requirements...</p>
            </div>
          ) : error ? (
            <div className="error-alert">
              <AlertCircle size={20} />
              {error}
              {error.includes('login') && (
                <div style={{ marginTop: '16px' }}>
                  <button onClick={() => window.location.href = '/login'} className="btn btn-primary">
                    Login / Register
                  </button>
                </div>
              )}
            </div>
          ) : result && (
            <>
              <div className="ats-score-section">
                <div className="score-circle">
                  <div className={`score-value ${getScoreColor(result.atsScore)}`}>
                    {result.atsScore}%
                  </div>
                  <div className="score-label">Match Score</div>
                </div>
                <p className="score-message">{getScoreMessage(result.atsScore)}</p>
              </div>

              {result.matchedKeywords && result.matchedKeywords.length > 0 && (
                <div className="matched-section">
                  <h4><CheckCircle size={18} /> Matched Skills</h4>
                  <div className="skills-list">
                    {result.matchedKeywords.map((skill, i) => (
                      <span key={i} className="skill-badge matched">{skill}</span>
                    ))}
                  </div>
                </div>
              )}

              {result.missingKeywords && result.missingKeywords.length > 0 && (
                <div className="missing-section">
                  <h4><AlertCircle size={18} /> Missing Skills</h4>
                  <div className="skills-list">
                    {result.missingKeywords.slice(0, 10).map((skill, i) => (
                      <span key={i} className="skill-badge missing">{skill}</span>
                    ))}
                  </div>
                  <p className="tip">Consider adding these keywords to your resume to improve your match.</p>
                </div>
              )}

              <div className="modal-footer">
                <button onClick={onClose} className="btn btn-secondary">Close</button>
                <button onClick={() => onApply && onApply(job)} className="btn btn-primary">
                  Apply Now
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default AtsScannerModal;