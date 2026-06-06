import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authAPI } from '../services/api';
import '../styles/Auth.css';
import ThemeToggle from '../components/common/ThemeToggle';
import { LogIn, User, Briefcase, Eye, EyeOff, AlertCircle } from 'lucide-react';

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [role, setRole] = useState('CANDIDATE');
  const [form, setForm] = useState({ email: '', password: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = role === 'RECRUITER'
        ? await authAPI.recruiterLogin(form)
        : await authAPI.candidateLogin(form);
      const { token, id, email, fullName, role: userRole } = res.data;
      login(token, { id, email, fullName, role: userRole });
      navigate(userRole === 'RECRUITER' ? '/recruiter/dashboard' : '/candidate/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card glass">
        <div className="auth-header">
          <Briefcase size={32} />
          <h1>Welcome Back</h1>
          <p>Sign in to continue</p>
          <div className="theme-toggle-placeholder"><ThemeToggle /></div>
        </div>
        <div className="role-toggle">
          <button className={`role-btn ${role === 'CANDIDATE' ? 'active' : ''}`} onClick={() => setRole('CANDIDATE')}><User size={20} /> Candidate</button>
          <button className={`role-btn ${role === 'RECRUITER' ? 'active' : ''}`} onClick={() => setRole('RECRUITER')}><Briefcase size={20} /> Recruiter</button>
        </div>
        <form onSubmit={handleSubmit} className="auth-form">
          {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}
          <div className="form-group">
            <label>Email</label>
            <input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Password</label>
            <div className="password-wrapper">
              <input type={showPassword ? 'text' : 'password'} value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
              <button type="button" onClick={() => setShowPassword(!showPassword)}>{showPassword ? <EyeOff size={20} /> : <Eye size={20} />}</button>
            </div>
          </div>
          <button type="submit" className="btn btn-primary btn-block" disabled={loading}>{loading ? 'Signing in...' : 'Sign In'}</button>
        </form>
        <div className="auth-footer">
          <p>Don't have an account? <Link to="/register">Sign up</Link></p>
        </div>
      </div>
    </div>
  );
};

export default Login;