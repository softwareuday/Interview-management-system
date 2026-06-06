import { useState, useEffect } from 'react';
import { profileAPI, resumeAPI } from '../../services/api';
import Sidebar from '../../components/common/Sidebar';
import { User, Mail, Phone, MapPin, Linkedin, FileText, Upload, Save, AlertCircle, CheckCircle, Eye } from 'lucide-react';
import { API_BASE_URL } from '../../constants';

const Profile = () => {
  const [profile, setProfile] = useState({ fullName: '', email: '', phoneNumber: '', location: '', linkedinUrl: '', skills: '', resumeUrl: '' });
  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    profileAPI.getCandidateProfile().then(res => setProfile(res.data)).catch(console.error).finally(() => setLoading(false));
  }, []);

  const handleUpdate = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await profileAPI.updateCandidateProfile({ phoneNumber: profile.phoneNumber, location: profile.location, linkedinUrl: profile.linkedinUrl, skills: profile.skills });
      setSuccess('Profile updated');
    } catch (err) { setError('Update failed'); } finally { setSaving(false); }
  };

  const handleUpload = async () => {
    if (!selectedFile) return;
    setUploading(true);
    try {
      const res = await resumeAPI.upload(selectedFile);
      setProfile({ ...profile, resumeUrl: res.data.resumeUrl });
      setSuccess('Resume uploaded');
    } catch (err) { setError('Upload failed'); } finally { setUploading(false); }
  };

  if (loading) return <div className="dashboard-layout"><Sidebar role="CANDIDATE" /><div className="dashboard-content">Loading...</div></div>;

  return (
    <div className="dashboard-layout">
      <Sidebar role="CANDIDATE" />
      <div className="dashboard-content">
        <h1>My Profile</h1>
        {success && <div className="success-alert">{success}</div>}
        {error && <div className="error-alert">{error}</div>}
        <form onSubmit={handleUpdate}>
          <div><label>Full Name</label><input value={profile.fullName} disabled /></div>
          <div><label>Email</label><input value={profile.email} disabled /></div>
          <div><label>Phone</label><input value={profile.phoneNumber} onChange={e => setProfile({...profile, phoneNumber: e.target.value})} /></div>
          <div><label>Location</label><input value={profile.location} onChange={e => setProfile({...profile, location: e.target.value})} /></div>
          <div><label>LinkedIn</label><input value={profile.linkedinUrl} onChange={e => setProfile({...profile, linkedinUrl: e.target.value})} /></div>
          <div><label>Skills (comma separated)</label><textarea value={profile.skills} onChange={e => setProfile({...profile, skills: e.target.value})} /></div>
          <button type="submit" disabled={saving}>Save Changes</button>
        </form>
        <div>
          <h3>Resume</h3>
          {profile.resumeUrl && <a href={`${API_BASE_URL}${profile.resumeUrl}`} target="_blank">View Resume</a>}
          <input type="file" accept=".pdf,.doc,.docx" onChange={e => setSelectedFile(e.target.files[0])} />
          <button onClick={handleUpload} disabled={uploading}>Upload Resume</button>
        </div>
      </div>
    </div>
  );
};

export default Profile;