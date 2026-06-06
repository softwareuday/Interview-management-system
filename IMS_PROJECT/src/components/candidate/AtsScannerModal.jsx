import { useState, useEffect } from 'react';
import { X, TrendingUp, CheckCircle, AlertCircle } from 'lucide-react';
import { atsAPI } from '../../services/api';

const AtsScannerModal = ({ job, onClose, onApply }) => {
  const [loading, setLoading] = useState(true);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    atsAPI.scan(job.id)
      .then(res => setResult(res.data))
      .catch(err => setError(err.response?.data?.message || 'Scan failed'))
      .finally(() => setLoading(false));
  }, [job.id]);

  const getScoreClass = (score) => {
    if (score >= 70) return 'high';
    if (score >= 50) return 'medium';
    return 'low';
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>ATS Score: {job.title}</h3>
          <button onClick={onClose} className="modal-close"><X size={24} /></button>
        </div>
        <div className="modal-body">
          {loading ? (
            <div className="loading">Analyzing...</div>
          ) : error ? (
            <div className="error-alert">{error}</div>
          ) : result && (
            <>
              <div className="score-circle">
                <div className={`score-value ${getScoreClass(result.atsScore)}`}>
                  {result.atsScore}%
                </div>
                <div className="score-label">Match Score</div>
              </div>
              {result.recommendation && <p>{result.recommendation}</p>}
              {result.matchedKeywords?.length > 0 && (
                <div className="matched-section">
                  <h4><CheckCircle size={18} /> Matched Skills</h4>
                  <div className="skills-list">
                    {result.matchedKeywords.map((skill, i) => <span key={i} className="skill-badge matched">{skill}</span>)}
                  </div>
                </div>
              )}
              {result.missingKeywords?.length > 0 && (
                <div className="missing-section">
                  <h4><AlertCircle size={18} /> Missing Skills</h4>
                  <div className="skills-list">
                    {result.missingKeywords.map((skill, i) => <span key={i} className="skill-badge missing">{skill}</span>)}
                  </div>
                </div>
              )}
              <div className="modal-footer">
                <button onClick={onClose} className="btn btn-outline">Close</button>
                <button onClick={() => onApply?.(job)} className="btn btn-primary">Apply Now</button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default AtsScannerModal;