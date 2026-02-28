import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { jobAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { Save, X, AlertCircle } from 'lucide-react';
import '../../styles/JobForm.css';

const EditJob = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    department: '',
    location: '',
    salaryRange: '',
    experienceRequired: '',
    requiredSkills: '',
    jobType: 'FULL_TIME',
    lastDateToApply: ''
  });

  useEffect(() => { fetchJob(); }, [id]);

  const fetchJob = async () => {
    try {
      const response = await jobAPI.getJobById(id);
      const job = response.data;
      setFormData({
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
    } catch (err) {
      console.error('Error fetching job:', err);
      setError('Failed to load job details');
    } finally { setLoading(false); }
  };

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!formData.title.trim()) { setError('Job title is required'); return; }
    if (!formData.description.trim()) { setError('Job description is required'); return; }
    setSubmitting(true);
    try {
      await jobAPI.updateJob(id, {
        ...formData,
        title: formData.title.trim(),
        description: formData.description.trim(),
        department: formData.department.trim() || null,
        location: formData.location.trim() || null,
        salaryRange: formData.salaryRange.trim() || null,
        experienceRequired: formData.experienceRequired.trim() || null,
        requiredSkills: formData.requiredSkills.trim() || null,
        lastDateToApply: formData.lastDateToApply || null
      });
      navigate('/recruiter/jobs', { state: { message: 'Job updated successfully!' } });
    } catch (err) {
      console.error('Error updating job:', err);
      setError(err.response?.data?.message || 'Failed to update job. Please try again.');
    } finally { setSubmitting(false); }
  };

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="RECRUITER" />
        <div className="dashboard-content">
          <div className="loading-container">
            <div className="spinner-large"></div>
            <p>Loading job details...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="RECRUITER" />
      <div className="dashboard-content">
        <div className="page-header">
          <div><h1>Edit Job</h1><p>Update job posting details</p></div>
          <button onClick={() => navigate('/recruiter/jobs')} className="btn btn-secondary">
            <X size={20} /> Cancel
          </button>
        </div>
        <div className="form-container">
          <form onSubmit={handleSubmit} className="job-form">
            {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

            <div className="form-group">
              <label htmlFor="title">Job Title <span className="required">*</span></label>
              <input id="title" type="text" name="title" className="input"
                placeholder="e.g. Senior Full Stack Developer" value={formData.title}
                onChange={handleChange} disabled={submitting} required />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="department">Department</label>
                <input id="department" type="text" name="department" className="input"
                  placeholder="e.g. Engineering" value={formData.department}
                  onChange={handleChange} disabled={submitting} />
              </div>
              <div className="form-group">
                <label htmlFor="location">Location</label>
                <input id="location" type="text" name="location" className="input"
                  placeholder="e.g. San Francisco, CA / Remote" value={formData.location}
                  onChange={handleChange} disabled={submitting} />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="jobType">Job Type</label>
                <select id="jobType" name="jobType" className="input select-input"
                  value={formData.jobType} onChange={handleChange} disabled={submitting}>
                  <option value="FULL_TIME">Full Time</option>
                  <option value="PART_TIME">Part Time</option>
                  <option value="CONTRACT">Contract</option>
                  <option value="INTERNSHIP">Internship</option>
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="experienceRequired">Experience Required</label>
                <input id="experienceRequired" type="text" name="experienceRequired" className="input"
                  placeholder="e.g. 3-5 years" value={formData.experienceRequired}
                  onChange={handleChange} disabled={submitting} />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="salaryRange">Salary Range</label>
                <input id="salaryRange" type="text" name="salaryRange" className="input"
                  placeholder="e.g. $100k - $150k" value={formData.salaryRange}
                  onChange={handleChange} disabled={submitting} />
              </div>
              <div className="form-group">
                <label htmlFor="lastDateToApply">Last Date to Apply</label>
                <input id="lastDateToApply" type="date" name="lastDateToApply" className="input"
                  value={formData.lastDateToApply} onChange={handleChange} disabled={submitting}
                  min={new Date().toISOString().split('T')[0]} />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="requiredSkills">Required Skills</label>
              <input id="requiredSkills" type="text" name="requiredSkills" className="input"
                placeholder="e.g. React, Node.js, AWS, Docker (comma separated)"
                value={formData.requiredSkills} onChange={handleChange} disabled={submitting} />
              <small className="help-text">Separate skills with commas</small>
            </div>

            <div className="form-group">
              <label htmlFor="description">Job Description <span className="required">*</span></label>
              <textarea id="description" name="description" className="input textarea" rows="12"
                placeholder="Describe the role, responsibilities, and requirements..."
                value={formData.description} onChange={handleChange} disabled={submitting} required />
            </div>

            <div className="form-actions">
              <button type="button" onClick={() => navigate('/recruiter/jobs')}
                className="btn btn-secondary" disabled={submitting}>Cancel</button>
              <button type="submit" className="btn btn-primary" disabled={submitting}>
                {submitting ? <><div className="spinner"></div> Updating...</> : <><Save size={20} /> Update Job</>}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditJob;