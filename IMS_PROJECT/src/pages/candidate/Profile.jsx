import { useState, useEffect } from 'react';
import { profileAPI, resumeAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import {
  User, Mail, Phone, MapPin, Linkedin, FileText,
  Upload, Save, AlertCircle, CheckCircle, X, Eye
} from 'lucide-react';
import { API_BASE_URL } from '../../constants';
import '../../styles/Profile.css';

const Profile = () => {
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const [profileData, setProfileData] = useState({
    fullName: '', email: '', phoneNumber: '', location: '',
    linkedinUrl: '', skills: '', resumeUrl: ''
  });
  const [selectedFile, setSelectedFile] = useState(null);

  useEffect(() => { fetchProfile(); }, []);

  const fetchProfile = async () => {
    try {
      const response = await profileAPI.getCandidateProfile();
      setProfileData({
        fullName: response.data.fullName || '',
        email: response.data.email || '',
        phoneNumber: response.data.phoneNumber || '',
        location: response.data.location || '',
        linkedinUrl: response.data.linkedinUrl || '',
        skills: response.data.skills?.join(', ') || '',
        resumeUrl: response.data.resumeUrl || ''
      });
    } catch (err) {
      console.error('Error fetching profile:', err);
      setError('Failed to load profile');
    } finally { setLoading(false); }
  };

  const handleChange = (e) => {
    setProfileData({ ...profileData, [e.target.name]: e.target.value });
    setError(''); setSuccess('');
  };

  const handleFileSelect = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    const allowedTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (!allowedTypes.includes(file.type)) {
      setError('Please upload a PDF or Word document');
      return;
    }
    if (file.size > 10 * 1024 * 1024) {
      setError('File size must be less than 10MB');
      return;
    }
    setSelectedFile(file);
    setError('');
  };

  const handleResumeUpload = async () => {
    if (!selectedFile) { setError('Please select a file first'); return; }
    setUploading(true); setError('');
    try {
      const response = await resumeAPI.upload(selectedFile);
      setProfileData({ ...profileData, resumeUrl: response.data.resumeUrl });
      setSelectedFile(null);
      setSuccess('Resume uploaded successfully!');
      setTimeout(() => setSuccess(''), 5000);
    } catch (err) {
      console.error('Error uploading resume:', err);
      setError(err.response?.data?.message || 'Failed to upload resume. Please try again.');
    } finally { setUploading(false); }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    if (!profileData.fullName.trim()) {
      setError('Full name is required');
      return;
    }
    setSaving(true);
    try {
      await profileAPI.updateCandidateProfile({
        phoneNumber: profileData.phoneNumber.trim() || null,
        location: profileData.location.trim() || null,
        linkedinUrl: profileData.linkedinUrl.trim() || null,
        skills: profileData.skills.trim() || null
      });
      setSuccess('Profile updated successfully!');
      setTimeout(() => setSuccess(''), 5000);
    } catch (err) {
      console.error('Error updating profile:', err);
      setError(err.response?.data?.message || 'Failed to update profile. Please try again.');
    } finally { setSaving(false); }
  };

  if (loading) {
    return (
      <div className="dashboard-layout">
        <Sidebar role="CANDIDATE" />
        <div className="dashboard-content">
          <div className="loading-container">
            <div className="spinner-large"></div>
            <p>Loading profile...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <div className="page-header">
          <div><h1>My Profile</h1><p>Manage your personal information and resume</p></div>
        </div>

        {success && <div className="success-alert"><CheckCircle size={20} />{success}</div>}
        {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

        <div className="profile-container">
          {/* Personal Information */}
          <div className="profile-section">
            <div className="section-header">
              <User size={24} />
              <h2>Personal Information</h2>
            </div>
            <form onSubmit={handleSubmit} className="profile-form">
              <div className="form-group">
                <label>Full Name</label>
                <div className="input-with-icon">
                  <User size={18} />
                  <input type="text" name="fullName" className="input"
                    value={profileData.fullName} disabled />
                </div>
                <small className="help-text">Name cannot be changed</small>
              </div>
              <div className="form-group">
                <label>Email Address</label>
                <div className="input-with-icon">
                  <Mail size={18} />
                  <input type="email" name="email" className="input"
                    value={profileData.email} disabled />
                </div>
                <small className="help-text">Email cannot be changed</small>
              </div>
              <div className="form-group">
                <label>Phone Number</label>
                <div className="input-with-icon">
                  <Phone size={18} />
                  <input type="tel" name="phoneNumber" className="input"
                    placeholder="e.g. +1 (555) 123-4567"
                    value={profileData.phoneNumber} onChange={handleChange}
                    disabled={saving} />
                </div>
              </div>
              <div className="form-group">
                <label>Location</label>
                <div className="input-with-icon">
                  <MapPin size={18} />
                  <input type="text" name="location" className="input"
                    placeholder="e.g. San Francisco, CA"
                    value={profileData.location} onChange={handleChange}
                    disabled={saving} />
                </div>
              </div>
              <div className="form-group">
                <label>LinkedIn Profile</label>
                <div className="input-with-icon">
                  <Linkedin size={18} />
                  <input type="url" name="linkedinUrl" className="input"
                    placeholder="https://linkedin.com/in/yourprofile"
                    value={profileData.linkedinUrl} onChange={handleChange}
                    disabled={saving} />
                </div>
              </div>
              <div className="form-group">
                <label>Skills</label>
                <textarea name="skills" className="input textarea" rows="4"
                  placeholder="e.g. JavaScript, React, Node.js, Python (comma separated)"
                  value={profileData.skills} onChange={handleChange}
                  disabled={saving} />
                <small className="help-text">Separate skills with commas</small>
              </div>
              <button type="submit" className="btn btn-primary btn-block" disabled={saving}>
                {saving ? <><div className="spinner"></div> Saving...</> : <><Save size={20} /> Save Changes</>}
              </button>
            </form>
          </div>

          {/* Resume Section */}
          <div className="profile-section">
            <div className="section-header">
              <FileText size={24} />
              <h2>Resume</h2>
            </div>
            <div className="resume-upload">
              {profileData.resumeUrl ? (
                <div className="current-resume">
                  <div className="resume-icon"><FileText size={32} /></div>
                  <div className="resume-info">
                    <h4>Current Resume</h4>
                    <p>Your resume is uploaded and ready</p>
                  </div>
                  <div className="resume-actions">
                    <a href={`${API_BASE_URL}${profileData.resumeUrl}`}
                      target="_blank" rel="noopener noreferrer"
                      className="btn btn-secondary">
                      <Eye size={18} /> View Resume
                    </a>
                  </div>
                </div>
              ) : (
                <div className="no-resume">
                  <FileText size={48} strokeWidth={1.5} />
                  <h4>No resume uploaded</h4>
                  <p>Upload your resume to apply for jobs</p>
                </div>
              )}

              <div className="upload-section">
                <div className="upload-area">
                  <input type="file" id="resume-upload"
                    accept=".pdf,.doc,.docx"
                    onChange={handleFileSelect}
                    className="file-input" />
                  <label htmlFor="resume-upload" className="upload-label">
                    <Upload size={32} />
                    <span className="upload-text">
                      {selectedFile ? selectedFile.name : 'Choose a file or drag here'}
                    </span>
                    <span className="upload-hint">PDF, DOC, DOCX (Max 10MB)</span>
                  </label>
                </div>

                {selectedFile && (
                  <div className="selected-file">
                    <div className="file-info">
                      <FileText size={20} />
                      <div>
                        <p className="file-name">{selectedFile.name}</p>
                        <p className="file-size">
                          {(selectedFile.size / 1024 / 1024).toFixed(2)} MB
                        </p>
                      </div>
                    </div>
                    <button onClick={() => setSelectedFile(null)} className="btn-remove">
                      <X size={18} />
                    </button>
                  </div>
                )}

                <button onClick={handleResumeUpload}
                  className="btn btn-primary btn-block"
                  disabled={!selectedFile || uploading}>
                  {uploading ? <><div className="spinner"></div> Uploading...</> : <><Upload size={20} /> Upload Resume</>}
                </button>
              </div>

              <div className="resume-tips">
                <h4>💡 Resume Tips</h4>
                <ul>
                  <li>Use a clear, professional format</li>
                  <li>Include relevant keywords for better ATS scores</li>
                  <li>Keep it updated with your latest experience</li>
                  <li>Tailor it for the jobs you're applying to</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;