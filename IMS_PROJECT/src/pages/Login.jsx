import { useState, useEffect } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authAPI } from '../services/api';
import { LogIn, User, Briefcase, Eye, EyeOff, AlertCircle } from 'lucide-react';
import '../styles/Auth.css';

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  const [role, setRole] = useState('CANDIDATE');
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (location.state?.message) {
      setSuccessMessage(location.state.message);
      setTimeout(() => setSuccessMessage(''), 5000);
    }
  }, [location]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!formData.email || !formData.password) {
      setError('Please fill in all fields');
      return;
    }
    setLoading(true);
    try {
      const response = role === 'RECRUITER'
        ? await authAPI.recruiterLogin(formData)
        : await authAPI.candidateLogin(formData);
      const { token, id, email, fullName, role: userRole } = response.data;
      login(token, { id, email, fullName, role: userRole });
      navigate(userRole === 'RECRUITER' ? '/recruiter/dashboard' : '/candidate/dashboard');
    } catch (err) {
      console.error('Login error:', err);
      setError(err.response?.data?.message || 'Invalid email or password. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <div className="auth-logo"><Briefcase size={32} /></div>
          <h1>Welcome Back</h1>
          <p>Sign in to continue to your account</p>
        </div>

        {successMessage && (
          <div className="success-alert"><AlertCircle size={20} />{successMessage}</div>
        )}

        <div className="role-toggle">
          <button type="button" className={`role-btn ${role === 'CANDIDATE' ? 'active' : ''}`}
            onClick={() => { setRole('CANDIDATE'); setError(''); }}>
            <User size={20} /> Candidate
          </button>
          <button type="button" className={`role-btn ${role === 'RECRUITER' ? 'active' : ''}`}
            onClick={() => { setRole('RECRUITER'); setError(''); }}>
            <Briefcase size={20} /> Recruiter
          </button>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

          <div className="form-group">
            <label htmlFor="email">Email Address</label>
            <input id="email" type="email" name="email" className="input"
              placeholder="Enter your email" value={formData.email}
              onChange={handleChange} autoComplete="email" disabled={loading} />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <div className="password-input-wrapper">
              <input id="password" type={showPassword ? 'text' : 'password'} name="password"
                className="input password-input" placeholder="Enter your password"
                value={formData.password} onChange={handleChange}
                autoComplete="current-password" disabled={loading} />
              <button type="button" className="password-toggle"
                onClick={() => setShowPassword(!showPassword)} disabled={loading}
                aria-label={showPassword ? 'Hide password' : 'Show password'}>
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
          </div>

          <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
            {loading ? <><div className="spinner"></div> Signing in...</> : <><LogIn size={20} /> Sign In</>}
          </button>
        </form>

        <div className="auth-footer">
          <p>Don't have an account? <Link to="/register" className="auth-link">Sign up here</Link></p>
        </div>

       
      </div>
    </div>
  );
};

export default Login;