import { useState } from 'react';
import { useNavigate, Link, useSearchParams } from 'react-router-dom';
import { authAPI } from '../services/api';
import '../styles/Auth.css';
import ThemeToggle from '../components/common/ThemeToggle';
import { UserPlus, User, Briefcase, Eye, EyeOff, AlertCircle } from 'lucide-react';

const Register = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const initialRole = searchParams.get('role') === 'recruiter' ? 'RECRUITER' : 'CANDIDATE';
  const [role, setRole] = useState(initialRole);
  const [form, setForm] = useState({ fullName: '', email: '', password: '', confirmPassword: '', phoneNumber: '', companyName: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    setLoading(true);
    try {
      if (role === 'RECRUITER') {
        await authAPI.recruiterRegister({
          fullName: form.fullName,
          email: form.email,
          password: form.password,
          phoneNumber: form.phoneNumber,
          companyName: form.companyName
        });
      } else {
        await authAPI.candidateRegister({
          fullName: form.fullName,
          email: form.email,
          password: form.password
        });
      }
      navigate('/login', { state: { message: 'Registration successful! Please login.' } });
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card glass">
        <div className="auth-header">
          <Briefcase size={32} />
          <h1>Create Account</h1>
          <p>Join us today</p>
          <div className="theme-toggle-placeholder"><ThemeToggle /></div>
        </div>
        <div className="role-toggle">
          <button className={`role-btn ${role === 'CANDIDATE' ? 'active' : ''}`} onClick={() => setRole('CANDIDATE')}><User size={20} /> Candidate</button>
          <button className={`role-btn ${role === 'RECRUITER' ? 'active' : ''}`} onClick={() => setRole('RECRUITER')}><Briefcase size={20} /> Recruiter</button>
        </div>
        <form onSubmit={handleSubmit} className="auth-form">
          {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}
          <div className="form-group">
            <label>Full Name *</label>
            <input value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Email *</label>
            <input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Password *</label>
            <div className="password-wrapper">
              <input type={showPassword ? 'text' : 'password'} value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
              <button type="button" onClick={() => setShowPassword(!showPassword)}>{showPassword ? <EyeOff size={20} /> : <Eye size={20} />}</button>
            </div>
          </div>
          <div className="form-group">
            <label>Confirm Password *</label>
            <div className="password-wrapper">
              <input type={showConfirm ? 'text' : 'password'} value={form.confirmPassword} onChange={(e) => setForm({ ...form, confirmPassword: e.target.value })} required />
              <button type="button" onClick={() => setShowConfirm(!showConfirm)}>{showConfirm ? <EyeOff size={20} /> : <Eye size={20} />}</button>
            </div>
          </div>
          {role === 'RECRUITER' && (
            <>
              <div className="form-group">
                <label>Phone Number *</label>
                <input value={form.phoneNumber} onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })} required />
              </div>
              <div className="form-group">
                <label>Company Name *</label>
                <input value={form.companyName} onChange={(e) => setForm({ ...form, companyName: e.target.value })} required />
              </div>
            </>
          )}
          <button type="submit" className="btn btn-primary btn-block" disabled={loading}>{loading ? 'Creating account...' : 'Create Account'}</button>
        </form>
        <div className="auth-footer">
          <p>Already have an account? <Link to="/login">Sign in</Link></p>
        </div>
      </div>
    </div>
  );
};

export default Register;