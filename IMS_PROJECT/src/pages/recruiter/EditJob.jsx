import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { jobAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { Save, X, AlertCircle } from 'lucide-react';
import { JOB_TYPE, JOB_TYPE_LABELS } from '../../constants';

const EditJob = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    title: '', description: '', department: '', location: '', salaryRange: '',
    experienceRequired: '', requiredSkills: '', jobType: 'FULL_TIME', lastDateToApply: ''
  });

  useEffect(() => {
    jobAPI.getJobById(id).then(res => {
      const job = res.data;
      setForm({
        title: job.title || '',
        description: job.description || '',
        department: job.department || '',
        location: job.location || '',
        salaryRange: job.salaryRange || '',
        experienceRequired: job.experienceRequired || '',
        requiredSkills: job.requiredSkills?.join(', ') || '',
        jobType: job.jobType || 'FULL_TIME',
        lastDateToApply: job.lastDateToApply || ''
      });
    }).catch(() => setError('Failed to load job')).finally(() => setLoading(false));
  }, [id]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.title.trim() || !form.description.trim()) {
      setError('Title and description are required');
      return;
    }
    setSubmitting(true);
    try {
      await jobAPI.updateJob(id, {
        ...form,
        requiredSkills: form.requiredSkills.trim() || null,
        lastDateToApply: form.lastDateToApply || null
      });
      navigate('/recruiter/jobs', { state: { message: 'Job updated successfully!' } });
    } catch (err) {
      setError(err.response?.data?.message || 'Update failed');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="dashboard-layout"><Sidebar role="RECRUITER" /><div className="dashboard-content">Loading...</div></div>;

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <div className="dashboard-header">
          <h1>Edit Job</h1>
          <button onClick={() => navigate('/recruiter/jobs')} className="btn btn-outline"><X size={20} /> Cancel</button>
        </div>
        <form onSubmit={handleSubmit} className="job-form glass">
          {error && <div className="error-alert">{error}</div>}
          <div className="form-group">
            <label>Job Title *</label>
            <input name="title" value={form.title} onChange={handleChange} required />
          </div>
          <div className="form-row">
            <div className="form-group"><label>Department</label><input name="department" value={form.department} onChange={handleChange} /></div>
            <div className="form-group"><label>Location</label><input name="location" value={form.location} onChange={handleChange} /></div>
          </div>
          <div className="form-row">
            <div className="form-group"><label>Job Type</label><select name="jobType" value={form.jobType} onChange={handleChange}>
              {Object.keys(JOB_TYPE).map(t => <option key={t} value={t}>{JOB_TYPE_LABELS[t]}</option>)}
            </select></div>
            <div className="form-group"><label>Experience Required</label><input name="experienceRequired" value={form.experienceRequired} onChange={handleChange} /></div>
          </div>
          <div className="form-row">
            <div className="form-group"><label>Salary Range</label><input name="salaryRange" value={form.salaryRange} onChange={handleChange} /></div>
            <div className="form-group"><label>Last Date to Apply</label><input type="date" name="lastDateToApply" value={form.lastDateToApply} onChange={handleChange} /></div>
          </div>
          <div className="form-group">
            <label>Required Skills (comma separated)</label>
            <input name="requiredSkills" value={form.requiredSkills} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label>Job Description *</label>
            <textarea name="description" rows="8" value={form.description} onChange={handleChange} required />
          </div>
          <div className="form-actions">
            <button type="button" onClick={() => navigate('/recruiter/jobs')} className="btn btn-outline">Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={submitting}>{submitting ? 'Updating...' : 'Update Job'}</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditJob;