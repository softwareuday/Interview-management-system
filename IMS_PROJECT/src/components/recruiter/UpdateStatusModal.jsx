import { useState } from 'react';
import { X } from 'lucide-react';
import { APPLICATION_STATUS, STATUS_LABELS } from '../../constants';
import { applicationAPI } from '../../services/api';

const UpdateStatusModal = ({ application, onClose, onUpdate }) => {
  const [status, setStatus] = useState(application.status);
  const [notes, setNotes] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await applicationAPI.updateStatus(application.applicationId, status, notes);
      onUpdate?.();
      onClose();
    } catch (err) {
      alert('Failed to update status');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Update Status – {application.candidateName}</h3>
          <button onClick={onClose} className="modal-close"><X size={24} /></button>
        </div>
        <form onSubmit={handleSubmit} className="modal-body">
          <div className="form-group">
            <label>New Status</label>
            <select value={status} onChange={(e) => setStatus(e.target.value)}>
              {Object.keys(APPLICATION_STATUS).map(s => (
                <option key={s} value={s}>{STATUS_LABELS[s]}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>Notes (Optional)</label>
            <textarea value={notes} onChange={(e) => setNotes(e.target.value)} rows="3" placeholder="Add notes for the candidate..." />
          </div>
          <div className="modal-footer">
            <button type="button" onClick={onClose} className="btn btn-outline">Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={loading}>Update</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UpdateStatusModal;