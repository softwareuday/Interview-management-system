import { useState } from 'react';
import { X, Upload, AlertCircle, CheckCircle, FileText } from 'lucide-react';
import { applicationAPI } from '../../services/api';
import '../../styles/Modal.css';

const GuestApplyModal = ({ job, onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    guestName: '',
    guestEmail: '',
    coverLetter: ''
  });
  const [resumeFile, setResumeFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const allowedTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
      if (!allowedTypes.includes(file.type)) {
        setError('Please upload a PDF or Word document');
        return;
      }
      if (file.size > 10 * 1024 * 1024) {
        setError('File size must be less than 10MB');
        return;
      }
      setResumeFile(file);
      setError('');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.guestName.trim()) {
      setError('Please enter your full name');
      return;
    }
    if (!formData.guestEmail.trim()) {
      setError('Please enter your email');
      return;
    }
    if (!resumeFile) {
      setError('Please upload your resume');
      return;
    }
    setLoading(true);
    setError('');
    try {
      await applicationAPI.apply(
        job.id,
        formData.coverLetter,
        resumeFile,
        formData.guestName,
        formData.guestEmail
      );
      setSuccess(true);
      setTimeout(() => {
        onSuccess && onSuccess();
        onClose();
      }, 2000);
    } catch (err) {
      console.error('Apply error:', err);
      setError(err.response?.data?.message || 'Failed to submit application. Please try again.');
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
            <div className="success-alert">
              <CheckCircle size={20} />
              Application submitted successfully! You will be contacted via email.
            </div>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="modal-body">
            {error && (
              <div className="error-alert">
                <AlertCircle size={20} />
                {error}
              </div>
            )}
            
            <div className="form-group">
              <label>Full Name *</label>
              <input
                type="text"
                name="guestName"
                className="input"
                placeholder="Enter your full name"
                value={formData.guestName}
                onChange={handleChange}
                disabled={loading}
                required
              />
            </div>

            <div className="form-group">
              <label>Email Address *</label>
              <input
                type="email"
                name="guestEmail"
                className="input"
                placeholder="Enter your email"
                value={formData.guestEmail}
                onChange={handleChange}
                disabled={loading}
                required
              />
            </div>

            <div className="form-group">
              <label>Resume (PDF or Word) *</label>
              <div className="file-upload-area">
                <input
                  type="file"
                  id="resume"
                  accept=".pdf,.doc,.docx"
                  onChange={handleFileChange}
                  style={{ display: 'none' }}
                  disabled={loading}
                />
                <label htmlFor="resume" className="upload-label">
                  <Upload size={24} />
                  <span>{resumeFile ? resumeFile.name : 'Click to upload resume'}</span>
                </label>
                {resumeFile && (
                  <div className="file-info">
                    <FileText size={16} />
                    <span>{(resumeFile.size / 1024).toFixed(0)} KB</span>
                  </div>
                )}
              </div>
            </div>

            <div className="form-group">
              <label>Cover Letter (Optional)</label>
              <textarea
                name="coverLetter"
                className="input textarea"
                rows="4"
                placeholder="Tell us why you're a good fit for this position..."
                value={formData.coverLetter}
                onChange={handleChange}
                disabled={loading}
              />
            </div>

            <div className="modal-footer">
              <button type="button" onClick={onClose} className="btn btn-secondary" disabled={loading}>
                Cancel
              </button>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? <><div className="spinner"></div> Submitting...</> : 'Submit Application'}
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default GuestApplyModal;