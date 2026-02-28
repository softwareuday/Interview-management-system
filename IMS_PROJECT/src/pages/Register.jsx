import { useState } from 'react';
import { useNavigate, Link, useSearchParams } from 'react-router-dom';
import { authAPI } from '../services/api';
import { UserPlus, User, Briefcase, Eye, EyeOff, AlertCircle } from 'lucide-react';
import '../styles/Auth.css';

const Register = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const initialRole = searchParams.get('role') === 'recruiter' ? 'RECRUITER' : 'CANDIDATE';
  const [role, setRole] = useState(initialRole);
  const [formData, setFormData] = useState({
    fullName: '', email: '', password: '', confirmPassword: '',
    phoneNumber: '', companyName: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [validationErrors, setValidationErrors] = useState({});

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
    if (validationErrors[e.target.name]) {
      setValidationErrors({ ...validationErrors, [e.target.name]: '' });
    }
  };

  const validateForm = () => {
    const errors = {};
    if (!formData.fullName.trim()) errors.fullName = 'Full name is required';
    else if (formData.fullName.trim().length < 3) errors.fullName = 'Full name must be at least 3 characters';

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email.trim()) errors.email = 'Email is required';
    else if (!emailRegex.test(formData.email)) errors.email = 'Please enter a valid email address';

    if (!formData.password) errors.password = 'Password is required';
    else if (formData.password.length < 6) errors.password = 'Password must be at least 6 characters';

    if (!formData.confirmPassword) errors.confirmPassword = 'Please confirm your password';
    else if (formData.password !== formData.confirmPassword) errors.confirmPassword = 'Passwords do not match';

    if (role === 'RECRUITER') {
      if (!formData.phoneNumber.trim()) errors.phoneNumber = 'Phone number is required';
      else if (formData.phoneNumber.trim().length < 10) errors.phoneNumber = 'Please enter a valid phone number';
      if (!formData.companyName.trim()) errors.companyName = 'Company name is required';
      else if (formData.companyName.trim().length < 2) errors.companyName = 'Company name must be at least 2 characters';
    }
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!validateForm()) {
      setError('Please fix the errors below');
      return;
    }
    setLoading(true);
    try {
      if (role === 'RECRUITER') {
        await authAPI.recruiterRegister({
          fullName: formData.fullName.trim(),
          email: formData.email.trim().toLowerCase(),
          password: formData.password,
          phoneNumber: formData.phoneNumber.trim(),
          companyName: formData.companyName.trim()
        });
      } else {
        await authAPI.candidateRegister({
          fullName: formData.fullName.trim(),
          email: formData.email.trim().toLowerCase(),
          password: formData.password
        });
      }
      navigate('/login', { state: { message: '🎉 Registration successful! Please login to continue.' } });
    } catch (err) {
      console.error('Registration error:', err);
      setError(err.response?.data?.message || 'Registration failed. Email may already be registered.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card register-card">
        <div className="auth-header">
          <div className="auth-logo"><Briefcase size={32} /></div>
          <h1>Create Account</h1>
          <p>Join us and start your journey today</p>
        </div>

        <div className="role-toggle">
          <button type="button" className={`role-btn ${role === 'CANDIDATE' ? 'active' : ''}`}
            onClick={() => { setRole('CANDIDATE'); setError(''); setValidationErrors({}); }}>
            <User size={20} /> Candidate
          </button>
          <button type="button" className={`role-btn ${role === 'RECRUITER' ? 'active' : ''}`}
            onClick={() => { setRole('RECRUITER'); setError(''); setValidationErrors({}); }}>
            <Briefcase size={20} /> Recruiter
          </button>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          {error && <div className="error-alert"><AlertCircle size={20} />{error}</div>}

          <div className="form-group">
            <label htmlFor="fullName">Full Name <span className="required">*</span></label>
            <input id="fullName" type="text" name="fullName"
              className={`input ${validationErrors.fullName ? 'input-error' : ''}`}
              placeholder="Enter your full name" value={formData.fullName}
              onChange={handleChange} disabled={loading} />
            {validationErrors.fullName && <span className="error-message">{validationErrors.fullName}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="email">Email Address <span className="required">*</span></label>
            <input id="email" type="email" name="email"
              className={`input ${validationErrors.email ? 'input-error' : ''}`}
              placeholder="Enter your email" value={formData.email}
              onChange={handleChange} autoComplete="email" disabled={loading} />
            {validationErrors.email && <span className="error-message">{validationErrors.email}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="password">Password <span className="required">*</span></label>
            <div className="password-input-wrapper">
              <input id="password" type={showPassword ? 'text' : 'password'} name="password"
                className={`input password-input ${validationErrors.password ? 'input-error' : ''}`}
                placeholder="Create a password (min 6 characters)" value={formData.password}
                onChange={handleChange} autoComplete="new-password" disabled={loading} />
              <button type="button" className="password-toggle"
                onClick={() => setShowPassword(!showPassword)} disabled={loading}>
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
            {validationErrors.password && <span className="error-message">{validationErrors.password}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password <span className="required">*</span></label>
            <div className="password-input-wrapper">
              <input id="confirmPassword" type={showConfirmPassword ? 'text' : 'password'} name="confirmPassword"
                className={`input password-input ${validationErrors.confirmPassword ? 'input-error' : ''}`}
                placeholder="Re-enter your password" value={formData.confirmPassword}
                onChange={handleChange} autoComplete="new-password" disabled={loading} />
              <button type="button" className="password-toggle"
                onClick={() => setShowConfirmPassword(!showConfirmPassword)} disabled={loading}>
                {showConfirmPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
            {validationErrors.confirmPassword && <span className="error-message">{validationErrors.confirmPassword}</span>}
          </div>

          {role === 'RECRUITER' && (
            <>
              <div className="form-group">
                <label htmlFor="phoneNumber">Phone Number <span className="required">*</span></label>
                <input id="phoneNumber" type="tel" name="phoneNumber"
                  className={`input ${validationErrors.phoneNumber ? 'input-error' : ''}`}
                  placeholder="Enter your phone number" value={formData.phoneNumber}
                  onChange={handleChange} disabled={loading} />
                {validationErrors.phoneNumber && <span className="error-message">{validationErrors.phoneNumber}</span>}
              </div>
              <div className="form-group">
                <label htmlFor="companyName">Company Name <span className="required">*</span></label>
                <input id="companyName" type="text" name="companyName"
                  className={`input ${validationErrors.companyName ? 'input-error' : ''}`}
                  placeholder="Enter your company name" value={formData.companyName}
                  onChange={handleChange} disabled={loading} />
                {validationErrors.companyName && <span className="error-message">{validationErrors.companyName}</span>}
              </div>
            </>
          )}

          <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
            {loading ? <><div className="spinner"></div> Creating account...</> : <><UserPlus size={20} /> Create Account</>}
          </button>
        </form>

        <div className="auth-footer">
          <p>Already have an account? <Link to="/login" className="auth-link">Sign in here</Link></p>
        </div>
      </div>
    </div>
  );
};

export default Register;