import { useState } from 'react';
import { X, Upload, AlertCircle, CheckCircle } from 'lucide-react';
import { applicationAPI } from '../../services/api';

const GuestApplyModal = ({ job, onClose, onSuccess }) => {
  const [formData, setFormData] = useState({ guestName: '', guestEmail: '', coverLetter: '' });
  const [resumeFile, setResumeFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.guestName.trim() || !formData.guestEmail.trim() || !resumeFile) {
      setError('All fields are required');
      return;
    }
    setLoading(true);
    try {
      await applicationAPI.apply(job.id, formData.coverLetter, resumeFile, formData.guestName, formData.guestEmail);
      setSuccess(true);
      setTimeout(() => {
        onSuccess?.();
        onClose();
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Application failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Apply for {job.title}</h3>
          <button onClick={onClose} className="modal-close"><X size={24} /></button>
        </div>
        {success ? (
          <div className="modal-body">
            <div className="success-alert"><CheckCircle size={20} /> Application submitted!</div>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="modal-body">
            {error && <div className="error-alert">{error}</div>}
            <div className="form-group">
              <label>Full Name *</label>
              <input name="guestName" value={formData.guestName} onChange={(e) => setFormData({ ...formData, guestName: e.target.value })} required />
            </div>
            <div className="form-group">
              <label>Email *</label>
              <input type="email" name="guestEmail" value={formData.guestEmail} onChange={(e) => setFormData({ ...formData, guestEmail: e.target.value })} required />
            </div>
            <div className="form-group">
              <label>Resume (PDF/DOCX) *</label>
              <div className="file-upload-area">
                <input type="file" accept=".pdf,.doc,.docx" onChange={(e) => setResumeFile(e.target.files[0])} required />
              </div>
            </div>
            <div className="form-group">
              <label>Cover Letter (Optional)</label>
              <textarea name="coverLetter" rows="4" value={formData.coverLetter} onChange={(e) => setFormData({ ...formData, coverLetter: e.target.value })} />
            </div>
            <div className="modal-footer">
              <button type="button" onClick={onClose} className="btn btn-outline">Cancel</button>
              <button type="submit" className="btn btn-primary" disabled={loading}>{loading ? 'Submitting...' : 'Apply'}</button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default GuestApplyModal;