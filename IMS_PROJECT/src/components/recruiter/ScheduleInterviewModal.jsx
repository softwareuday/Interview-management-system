import { useState } from 'react';
import { X } from 'lucide-react';
import { INTERVIEW_MODE, INTERVIEW_MODE_LABELS } from '../../constants';
import { interviewAPI } from '../../services/api';

const ScheduleInterviewModal = ({ application, onClose, onSchedule }) => {
  const [form, setForm] = useState({
    interviewDate: '',
    interviewTime: '',
    mode: INTERVIEW_MODE.VIDEO,
    meetingLink: '',
    remarks: ''
  });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.interviewDate || !form.interviewTime) {
      alert('Date and time are required');
      return;
    }
    setLoading(true);
    try {
      await interviewAPI.scheduleInterview({
        candidateId: application.candidateId,
        jobId: application.jobId,
        position: application.jobTitle,
        interviewDate: form.interviewDate,
        interviewTime: form.interviewTime,
        mode: form.mode,
        meetingLink: form.meetingLink,
        remarks: form.remarks
      });
      onSchedule?.();
      onClose();
    } catch (err) {
      alert('Failed to schedule interview');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Schedule Interview – {application.candidateName}</h3>
          <button onClick={onClose} className="modal-close"><X size={24} /></button>
        </div>
        <form onSubmit={handleSubmit} className="modal-body">
          <div className="form-group">
            <label>Date *</label>
            <input type="date" value={form.interviewDate} onChange={(e) => setForm({ ...form, interviewDate: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Time *</label>
            <input type="time" value={form.interviewTime} onChange={(e) => setForm({ ...form, interviewTime: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Mode</label>
            <select value={form.mode} onChange={(e) => setForm({ ...form, mode: e.target.value })}>
              {Object.keys(INTERVIEW_MODE).map(m => <option key={m} value={m}>{INTERVIEW_MODE_LABELS[m]}</option>)}
            </select>
          </div>
          {form.mode === INTERVIEW_MODE.VIDEO && (
            <div className="form-group">
              <label>Meeting Link</label>
              <input type="url" value={form.meetingLink} onChange={(e) => setForm({ ...form, meetingLink: e.target.value })} placeholder="https://meet.google.com/..." />
            </div>
          )}
          <div className="form-group">
            <label>Remarks</label>
            <textarea value={form.remarks} onChange={(e) => setForm({ ...form, remarks: e.target.value })} rows="3" placeholder="Instructions for candidate..." />
          </div>
          <div className="modal-footer">
            <button type="button" onClick={onClose} className="btn btn-outline">Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={loading}>Schedule</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ScheduleInterviewModal;